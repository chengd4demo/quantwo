var pageCurr;
layui.use(['table','element','vip_tab'], function(){
	var form = layui.form,table = layui.table,
		layer = layui.layer,vip_tab = layui.vip_tab,
		$ = layui.jquery;
	 tableIns=table.render({
		 elem: '#roleList'
			 	,id:"id"
	            ,url:'/security/role/page'
	        	,method: 'GET' //默认：get请求
	            ,height: 315
	            ,page: true
	            ,limits: [20, 30, 50, 100, 200]
	            ,limit:20
	            ,request: {
	                pageName: 'page' //页码的参数名称，默认：page
	                ,limitName: 'limit' //每页数据量的参数名，默认：limit
	            }
	            ,cols: [[
	                 {title: '序号',type:'numbers',width: 60,align: 'center'}, 
	                 {
							field: 'name',
							title: '角色名称',
							width: 185
						}, {
							field: 'remark',
							title: '描述',
							width: 300
						}, {
							field: 'creater',
							title: '创建用户',
							width: 150
						}, {
							field: 'createTime',
							title: '创建时间',
							sort: true,
							width: 170
						}, {
							fixed: 'right',
							title: '操作',
							width: 255,
							align: 'center',
							toolbar: '#barOption'
						} 
	            ]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
		}
	 });
	 var active = {
		 add: function(){
		 	layer.open({
		 		 type: 2
		 		,title: '新增 '//显示标题栏
		 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
		 		,skin: 'layui-layer-rim' //加上边框
		 		,area: ['570px', '410px'] //宽高
		 		,maxmin: true
		 		,shade: 0.8
		 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		 		,btnAlign: 'c'
		 		,moveType: 1 //拖拽模式，0或者1
		 		,content: '../../../security/role/edit'
		 	});
	 	 }
	 };
	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 table.on('tool(roleTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'detail') {
			 vip_tab.add(parent.layer.element, "设备信息维护", "http://www.baidu.com/"+data.id)
			} else if(layEvent === 'edit') {
				layer.open({
			 		 type: 2
			 		,title: '编辑 '//显示标题栏
			 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
			 		,skin: 'layui-layer-rim' //加上边框
			 		,area: ['570px', '410px'] //宽高
			 		,maxmin: true
			 		,shade: 0.8
			 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			 		,btnAlign: 'c'
			 		,moveType: 1 //拖拽模式，0或者1
			 		,content: '../../../security/role/edit?id='+data.id
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/security/role/"+data.id,
						async:false,
						success:function(data){
							if(data.code==200){
								layer.msg('删除成功！',{time:500},function(){
									layer.close(index);
									obj.del();
									$(".layui-laypage-btn").click();
			            		 });
							}else {
								layer.msg('删除失败！');
								layer.close(index);
							}
						}
					});
				});
			} else if(layEvent === 'authorization') {
				layer.open({
					 type: 1
				     ,title:'角色授权'
				     ,skin: 'layui-layer-rim' //加上边框
				     ,area: ['350px', '420px'] //宽高
				     ,shade: 0.8
				     ,id: 'LAY_layuipro'
				     ,moveType: 1
			 		 ,content:
			 		 '<div class="eleTree ele1" lay-filter="data1"></div>'+
			 		 '<div class="layui-input-block" style="margin-left:150px">'+
			 		 '<button id = "submitId" class="layui-btn layui-btn-normal layui-btn-sm" type="button" title="提交">'+
			 		 '提交</button></div>'
			 		 ,success: function(layero){
			 			layui.config({
			 	            base: "../../frame/layui/lay/mymodules/"
			 	        }).use(['jquery','eleTree'], function(){
			 	            var $ = layui.jquery;
			 	            var eleTree = layui.eleTree;
			 	            var layer = layui.layer;
			 	            eleTree.render({
			 	                elem: '.ele1',
			 	                url: "../../../security/role/menue?roleId="+data.id,
			 	                showCheckbox: true,
			 	                contextmenuList: ["copy","add","edit","remove"],
			 	                drag: true,
			 	                accordion: true
			 	            });
			 	            $(".layui-btn").on("click",function() {
			 	            	var dataArrays = eleTree.checkedData(".ele1");
			 	            	var len = dataArrays.length;
			 	            	if (len==0) {
			 	            		layer.msg('请选择授权模块', {
										time : 1000,
										icon : 5,
										shade : 0.3,
										anim : 5
									});
			 	            	}
			 	            	var treeData;
			 	            	var rolePermissionArray = new Array(dataArrays.length);
			 	            	for (var i=0;i<dataArrays.length;i++) {
			 	            		treeData = dataArrays[i];
			 	            		if(treeData != undefined) {
			 	            			var rolePermission = new RolePermission(data.id,treeData.id);
			 	            			rolePermissionArray[i] = rolePermission;
			 	            		}
			 	            	}
			 	            	//询问框
			 	            	layer.confirm('是否提交保存？', {
			 	            		title : "提示",
			 	            		icon : 3,
			 	            		btn : [ '是', '否' ]
			 	            	// 按钮
			 	            	}, function() {
			 	            		$.ajax({
				 	   					url : 'authorization?roleId='+data.id,
				 	   					contentType:"application/json",
				 	   					type : "POST",
				 	   					data : JSON.stringify(rolePermissionArray),
				 	   					dataType:"json",
				 	   					success : function(data) {
				 	   						if (data.code == 200) {
				 	   							layer.msg('保存成功！', {
				 	   								icon : 1,
				 	   								time : 1000
				 	   							}, function() {
				 	   								index = parent.layer.getFrameIndex(window.name);
				 	   								parent.layer.close(index);
				 	   								window.location.reload();
				 	   							});
				 	   						} else {
				 	   							layer.msg(data.msg, {
				 	   								time : 1000,
				 	   								icon : 5,
				 	   								shade : 0.3,
				 	   								anim : 5
				 	   							});
				 	   						}
				 	   					}
				 	   				});
			 	            	}, function() {
			 	            	     layer.close();
			 	            	});
			 	        });
		 	        });
		 		}
		 	});
		}
 	});
});




/**
 *查询 
 */
layui.use(['form'], function(){
	var form = layui.form ,layer = layui.layer;
	//监听搜索框
	form.on('submit(searchSubmit)', function(data){
		//重新加载table
		load(data);
		return false;
	});
});
function load(obj){
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function RolePermission(roleId,permissionId){
	this.roleId = roleId;
	this.permissionId = permissionId;
}
