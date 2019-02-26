layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form
		,$=layui.jquery
		,laydate = layui.laydate
		,layer = layui.layer;
	//日期
    laydate.render({
        elem: '#date'
    });
    //自定义验证规则
    form.verify({
	    name: function(value) {
	    	if (value.length==0) {
	    		return "请输入公司名称";
	    	} else if(value.length >20) {
	    		return "公司名称不能大于20个字符";
	    	}
	    }
	    ,address: function(value){
	    	if (value.length==0) {
	    		return "请输入公司地址";
	    	} else if(value.length >50) {
	    		return "公司地址不能大于50个字符";
	    	}
	    }
	    ,legalPerson: function(value){
	    	if (value.length==0) {
	    		return "请输入联系人";
	    	} else if(value.length >10) {
	    		return "联系人不能大于10个字符";
	    	}
	    }
	    ,phoneNumber: [/^1(3|4|5|7|8)\d{9}$/, "请输入正确的手机号"]
	    ,emails: function(value) {
	    	if(value.length != 0) {
	    		if (value.length>30) {
	    			return "邮箱不能大于30个字符";
	    		} else if(! /^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/.test(value)){
	    			return "邮箱格式不正确";
	    		}
	    	}
	    }
	    ,weixin: function(value){
	    	if (value.length>0 && value.length>30) {
	    		return "微信号不能大于30个字符";
	    	}
	    }
	    ,socialCreditCode: function(value){
	    	if(!/^[0-9a-zA-Z]+$/.test(value)){
	    		return "统一社会信用码不合法,只能使用字母数字组合";
	    	}else if(value.length == 0){
	    		return "请输入统一社会征信码";
	    	}else if (value.length<15 ){
	    		return "统一社会信用码不能小于15个字符";
	    	}else if(value.length>18) {
	    		return "统一社会信用码不能大于18个字符";
	    	}
	    }
    });
    
    //表单提交
    form.on('submit(demo1)', function(data) {
		// 询问框
		layer.confirm('是否提交保存？', {
			title : "提示",
			icon : 3,
			btn : [ '是', '否' ]
		// 按钮
		}, function() {
			$.ajax({
				url : 'edit/save',
				type : "POST",
				data : data.field,
				success : function(data) {
					if (data.code == 200) {
						layer.msg('保存成功！', {
							icon : 1,
							time : 1000
						}, function() {
							index = parent.layer.getFrameIndex(window.name);
							parent.layer.close(index);
							parent.location.reload();

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
			})
		}, function() {
			layer.close();
		});
		return false;
	});
	
});