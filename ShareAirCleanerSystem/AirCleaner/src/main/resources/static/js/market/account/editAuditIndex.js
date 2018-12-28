layui.use(['form', 'layedit','layer'], function() {
	var form = layui.form
		,$=layui.jquery
		,layer = layui.layer;
	//日期
    //自定义验证规则
    form.verify({   	
		remarks:function(value){
			if (value.length > 200) {
				return '备注信息不能大于200个字符'
			}
		}
    });
    
    //按键取消监听
    form.on('submit(closeAudit)', function(data){
    	index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
    	});
  //按键取消监听
    form.on('radio(refundStatus)', function(data){
    	if (data.value==1) {
    		$("#reason").hide();
    	} else {
    		$("#reason").show();
    	}
    	form.render();
    });
    //表单提交
    form.on('submit(submitAudit)', function(data) {
    	if ($('#refundStatus input[name="refundStatus"]:checked ').val() == 2 && $("#reason textarea").val().trim().length==0) {
			layer.msg('请输入驳回原因', {
				 time: 1000
				,icon : 5
				,shade :0.3
				,anim: 5
			});
			return false;
		}
		// 询问框
		layer.confirm('是否提交保存？', {
			title : "提示",
			icon : 3,
			btn : [ '是', '否' ]
		// 按钮
		}, function() {
			$.ajax({
				url : "refund",
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