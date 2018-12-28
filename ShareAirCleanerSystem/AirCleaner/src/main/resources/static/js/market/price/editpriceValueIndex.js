layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form,$=layui.jquery,layer = layui.layer,laydate = layui.laydate;
	laydate.render({
	    elem: '#startTime'
	  });
	laydate.render({
	    elem: '#endTime'
	  });
	  //自定义验证规则
    form.verify({
    	costTime: [/^[1-9]\d*$/, "请输入一个大于0的整数"]
	    ,value: [/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,"请输入正确的金额"]
	    ,discount: [/^([1-9]\d?|99)$/, "请输入1至99的整数"]	    
    });
    
	//表单提交
	form.on('submit(demo1)',function(data){
		//询问框
		layer.confirm('是否提交保存？',{
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