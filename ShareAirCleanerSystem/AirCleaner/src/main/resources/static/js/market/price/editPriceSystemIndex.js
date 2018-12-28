var pageCurr;
layui.use(['form', 'layedit', 'laydate', 'layer'], function(){
    var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate
            ,$=layui.jquery;
    form.verify({
		name:function(value){
			if (value.length == 0) {
				return '请输入价格体系名称';
			} else if (value.length > 20) {
				return '价格体系名称不能大于20个字符';
			}
		},
		description:function(value){
			if (description.length > 200) {
				return '描述信息不能大于200个字符'
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