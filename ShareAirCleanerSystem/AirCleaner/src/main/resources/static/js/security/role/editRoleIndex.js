layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form,$=layui.jquery,layer = layui.layer;
	form.verify({
		name:function(value){
			if (value.length == 0) {
				return '请输入角色名称';
			} else if (value.length > 20) {
				return '角色名称不能大于20个字符';
			}
		},
		remarks:function(value){
			if (value.length > 200) {
				return '备注信息不能大于200个字符'
			}
		}
	});
	form.on('submit(demo1)',function(data){
		//询问框
		layer.confirm('是否提交保存？',{
		  title:"提示",
		  icon:3,
		  btn: ['是','否'] //按钮
		}, function(){
			$.ajax({
                url: 'edit/save',
                type: "POST",
                data:data.field,
                success:function (data) {
                	if(data.code==200){
            		  layer.msg('保存成功！',{time:500},function(){
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