var pageCurr;
layui.use(['form', 'layedit', 'laydate', 'layer'], function(){
    var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate
            ,$=layui.jquery;
    
  //按键取消监听
    form.on('submit(closeCustomer)', function(data){
    	index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
    	});
    
});