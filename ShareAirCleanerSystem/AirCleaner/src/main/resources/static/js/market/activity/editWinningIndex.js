var pageCurr;
layui.use(['form', 'layedit', 'laydate', 'layer'], function(){
    var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate
            ,$=layui.jquery;
    form.verify({
    	logisticsNum:function(value){
			if (value.length == 0) {
				return '请输入物流编号';
			} else if (value.length > 40) {
				return '物流编号不能大于40个字符';
			}
		}
	});
    
  //按键取消监听
    form.on('submit(closeWinning)', function(data){
    	index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
    	});
  //按键确认监听
    form.on('submit(submitWinning)',function(data){
		//询问框
		layer.confirm('是否发货？',{
		  title:"提示",
		  icon:3,
		  btn: ['是','否'] //按钮
		}, function(){
			$.ajax({
                url: 'save',
                type: "POST",
                data:data.field,
                success:function (data) {
                	if(data.code==200){
                		layer.msg('发货成功！', {
              			  icon: 1,
              			  time: 1000 
              			}, function(){
              				 index = parent.layer.getFrameIndex(window.name);
              				 parent.layer.close(index);
 	                		 	 parent.location.reload();

              			});
                	} else {
                		layer.msg('发货失败！');
                	}
                }
              })
		}, function(){
		  layer.close();
		});
		return false;
	});

    
});