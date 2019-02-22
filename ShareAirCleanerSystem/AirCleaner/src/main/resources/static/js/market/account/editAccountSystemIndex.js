layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form,$=layui.jquery,layer = layui.layer;
	form.verify({		
		reason:function(value){
			if (value.length > 200) {
				return '所输入信息不能大于200个字符'
			}
		}
	});
	//按键取消监听
    form.on('submit(closeAudit)', function(data){
    	index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
    });
    
    //按键取消监听
    form.on('radio(state)', function(data){
    	if (data.value==2) {
    		$("#reason").hide();
    	} else {
    		$("#reason").show();
    	}
    	form.render();
    });
    //表单提交
	form.on('submit(demo1)',function(data){
		if ($('#state input[name="state"]:checked ').val() == 3 && $("#reason textarea").val().trim().length==0) {
			layer.msg('请输入驳回原因', {
				 time: 1000
				,icon : 5
				,shade :0.3
				,anim: 5
			});
			return false;
		}
		//询问框
		layer.confirm('是否提交保存？',{
		  title:"提示",
		  icon:3,
		  btn: ['是','否'] //按钮
		}, function(){
			$.ajax({
                url: 'edit/examine',
                type: "POST",
                data:data.field,
                success:function (data) {
                	if(data.code==200){
                		layer.msg('保存成功！', {
              			  icon: 1,
              			  time: 1000 
              			}, function(){
              				 index = parent.layer.getFrameIndex(window.name);
              				 parent.layer.close(index);
 	                		 	 parent.location.reload();

              			});
                	} else {
                		layer.msg('保存失败！');
                	}
                }
              })
		}, function(){
		  layer.close();
		});
		return false;
	});
});