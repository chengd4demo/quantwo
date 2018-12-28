layui.use(['form','upload', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form
	,$=layui.jquery
	,layer = layui.layer
	,upload = layui.upload
	,laydate = layui.laydate;
	laydate.render({
	    elem: '#startTime'
	  });
	laydate.render({
	    elem: '#endTime'
	  });
	//日期
    laydate.render({
        elem: '#date'
    });

	form.verify({
		prizeName:function(value){
			if (value.length > 20) {
				return '奖品名称不能大于20个字符';
			}
		}
	});
	form.verify({
		address:function(value){
			if (value.length > 20) {
				return '使用地址不能大于20个字符';
			}
		}
	});
	//按键取消监听
    form.on('submit(closeAudit)', function(data){
    	index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
    });
    
  //监听指定开关
    form.on('switch(prizeCategory)', function(data){
      layer.msg('开关checked：'+ (this.checked ? 'true' : 'false'), {
        offset: '6px'
      });
      layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
    });
    
    //按键取消监听
    form.on('radio(prizeCategory)', function(data){
    	if (data.value==3) {
    		$("#reason-1").hide();
    		$("#reason-2").hide();
    		$("#reason-3").show();
    		$("#reason-3").append('<input id="prizeTypeId" type="hidden" name="prizeType">');
    		$("#prizeTypeId").val("2");
    	} else {
    		$("div #prizeTypeId").remove();
    		$("#reason-1").show();
    		$("#reason-2").show();
    		$("#reason-3").hide();
    	}
    	form.render();
    });
  	<!-- 表单提交 -->	
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
                		  //parent.location.reload();
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