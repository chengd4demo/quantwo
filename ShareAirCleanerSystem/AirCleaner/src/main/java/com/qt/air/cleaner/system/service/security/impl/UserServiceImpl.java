package com.qt.air.cleaner.system.service.security.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.qt.air.cleaner.common.constants.Constants;
import com.qt.air.cleaner.common.exception.BusinessException;
import com.qt.air.cleaner.common.util.salt.Digests;
import com.qt.air.cleaner.config.pk.FactoryAboutKey;
import com.qt.air.cleaner.config.pk.TableEnum;
import com.qt.air.cleaner.config.shiro.vo.Principal;
import com.qt.air.cleaner.system.domain.security.Role;
import com.qt.air.cleaner.system.domain.security.User;
import com.qt.air.cleaner.system.domain.security.UserRole;
import com.qt.air.cleaner.system.repository.security.RoleRepository;
import com.qt.air.cleaner.system.repository.security.UserRepository;
import com.qt.air.cleaner.system.repository.security.UserRoleRepository;
import com.qt.air.cleaner.system.service.security.UserService;
import com.qt.air.cleaner.vo.common.ErrorCodeEnum;
import com.qt.air.cleaner.vo.security.UserView;
import com.singalrain.framework.util.Encodes;

@Service
public class UserServiceImpl implements UserService {
	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;
    
    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }
    
    @Transactional
	@Override
	public void addUser(User user, Role role) {
		if (user == null || role == null) {
            throw new BusinessException("user.registr.error", "注册信息错误");
        }

        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            throw new BusinessException("user.registr.error", "注册信息错误");
        }

        if (StringUtils.isBlank(role.getId())) {
            throw new BusinessException("user.registr.error", "用户未指定所属角色");
        }

        Role r = roleRepository.findOne(role.getId());
        if (r == null) {
            throw new BusinessException("user.registr.error", "用户未指定所属组织或角色");
        }
        
        User u = userRepository.findByUsernameAndRemovedAndStatus(user.getUsername(),false,Constants.STATUS_VALID);
        if(u!=null){
            throw new BusinessException("user.registr.error", "用户账号已经存在,username="+user.getUsername());
        }

        entryptPassword(user);
        user.setStatus(Constants.STATUS_VALID);
        user.setCreateTime(Calendar.getInstance().getTime());
        user.setId(FactoryAboutKey.getPK(TableEnum.T_SYS_USER));
        userRepository.save(user);

        UserRole ur = new UserRole();
        ur.setRoleId(r.getId());
        ur.setUserId(user.getId());
        ur.setId(FactoryAboutKey.getPK(TableEnum.T_SYS_USER_ROLE));
        userRoleRepository.save(ur);

	}

	@Override
	public void updatePassword(User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("## update User password.");
        }
        User u = userRepository.findOne(user.getId());
        u.setPassword(user.getPassword());
        entryptPassword(u);
        u.setModifyTime(Calendar.getInstance().getTime());
        userRepository.saveAndFlush(u);

	}

	@Override
	public User findUserByName(String username) {
		 try {
	            return userRepository.findByUsernameAndRemovedAndStatus(username,false,Constants.STATUS_VALID);
	        } catch (Exception e) {
	            logger.error("# 根据账号查询用户报错 , username={}", username);
	            throw new BusinessException("1001", "查询用户失败");
	        }
	}
	
	@Transactional
	@Override
	public void updateUserLastLoginTime(User user) {
		User u = userRepository.findOne(user.getId());
        if (u != null) {
            user = new User();
            user.setLastLoginTime(Calendar.getInstance().getTime());
            user.setId(u.getId());
            userRepository.saveAndFlush(user);
        }
	}

	@Override
	public List<User> findUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> findEmp(String shopId, String empName) {
		return null;
	}

	/**
	 * 用户信息查询
	 * 
	 * @param params
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<User> findAllUser(UserView params, Pageable pageable) {
		Specification<User> specification = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> conditions = new ArrayList<>();
				String userName = params.getUsername();
				if (StringUtils.isNotBlank(userName)) { // 账号
					Predicate p1 = cb.like(root.get("username"), "%" + userName + "%");
					conditions.add(p1);
				}
				String trueName = params.getTrueName();
				if (StringUtils.isNotBlank(trueName)) { // 真是姓名
					Predicate p5 = cb.like(root.get("trueName"), "%" + trueName + "%");
					conditions.add(p5);
				}
				Predicate p2 = cb.notEqual(root.get("username"), "admin");
				conditions.add(p2);
				Predicate p9 = cb.equal(root.get("removed"), false);
				conditions.add(p9);
				Predicate[] p = new Predicate[conditions.size()];
				return cb.and(conditions.toArray(p));
			}
		};
		return userRepository.findAll(specification, pageable);
	}

	@Override
	public User findById(String id) {
		return userRepository.findByIdAndRemoved(id, false);
	}

	/**
	 * 用户信息新增或修改
	 * 
	 * @param userView
	 * @return
	 */
	@Override
	@Transactional
	public void saveOrUpdate(UserView userView) {
		if(userView != null) {
			User user = userRepository.findByUsernameAndRemoved(userView.getUsername(),false);
			if(user != null) {
				throw new BusinessException(ErrorCodeEnum.ES_3001.getErrorCode(), ErrorCodeEnum.ES_3001.getMessage());
			}
			UserRole userRole = null;
			Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			String id = userView.getId();
			String roleId = userView.getRoleId();
			Date nowDate = Calendar.getInstance().getTime();
			if(StringUtils.isNotBlank(id)) {
				user = userRepository.findByIdAndRemoved(id, false);
				user.setTrueName(userView.getTrueName());
				if(StringUtils.isNotBlank(userView.getPassward())) {
					user.setPassword(userView.getPassward());
					entryptPassword(user);
				}
				user.setLastOperateTime(nowDate);
				user.setLastOperator(principal.getUser().getUsername());
			} else {
				user = new User();
				BeanUtils.copyProperties(userView, user);
				user.setPassword(Constants.STREXC_ESEDE);
				entryptPassword(user);
				user.setCreateTime(nowDate);
				user.setCreater(principal.getUser().getUsername());
			}
			user = userRepository.saveAndFlush(user);
			if(StringUtils.isNotBlank(roleId)) {
				Role role = roleRepository.findByIdAndRemoved(roleId, false);
				if(role != null) {
					userRole = new UserRole();
					userRole.setUserId(user.getId());
					userRole.setRoleId(role.getId());
					userRole.setCreateTime(nowDate);
					userRole.setCreater(principal.getUser().getUsername());
				}
				userRoleRepository.saveAndFlush(userRole);
			}
		} else {
			throw new BusinessException(ErrorCodeEnum.ES_1001.getErrorCode(), ErrorCodeEnum.ES_1001.getMessage());
		}
	}
	
	/**
	 * 用户信息删除
	 * 
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(String id) {
		User user = userRepository.findByIdAndRemoved(id, false);
		user.setRemoved(Boolean.TRUE);
		userRepository.saveAndFlush(user);
		List<UserRole> userRoleList = userRoleRepository.findByUserId(user.getId());
		if (!CollectionUtils.isEmpty(userRoleList)) {
			userRoleRepository.deleteInBatch(userRoleList);
		}
	}
	
	/**
	 * 根据用户id获取用户角色
	 * @param userId
	 * @return
	 */
	@Override
	public String findByUserRoleId(String userId) {
		List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
		String roleId=null;
		if(!CollectionUtils.isEmpty(userRoles)){
			roleId = userRoles.get(0).getRoleId();
		}
		return roleId;
	}
	
	/**
	 * 启用或禁用
	 * 
	 * @param userId
	 * @return
	 */
	public void updateSstatus(String userId){
		User user = userRepository.findByIdAndRemoved(userId, false);
		if(user != null){
			if (user.getStatus() == Constants.STATUS_VALID){
				user.setStatus(Constants.STATUS_INVALID);
			} else {
				user.setStatus(Constants.STATUS_VALID);
			}
			userRepository.saveAndFlush(user);
		}
		
	}

}
