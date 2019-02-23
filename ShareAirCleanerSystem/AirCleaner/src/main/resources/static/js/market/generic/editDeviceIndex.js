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
    	setupAddress: function(value){
	    	if (value.length==0) {
	    		return "请输入安装地址";
	    	} else if(value.length >50) {
	    		return "安装地址不能大于50个字符";
	    	}
	    }
	    ,machNo: function(value){
	    	if(!/^\d+(\.\d{0,15})?$/.test(value)){
	    		 return "设备编码不合法,只能使用数字组合";
	    	}else if (value.length>0 && value.length>30) {
	    		return "设备编码不能大于15个字符";
	    	}
	    }
    });
    
    //添加
    form.on('submit(addFenRun)',function(data){
		parent.parent.layer.open({
		  type: 2,
		  title: '分润',
		  skin: 'layui-layer-rim', //加上边框
		  area: ['1024px', '520px'], //宽高
		  content: '../../../market/devices/add'
		});
	    return false;
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