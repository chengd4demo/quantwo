var pageCurr;
layui.use(['table','element','form'], function(){
	var form = layui.form,
		table = layui.table,
		element = layui.element,
		$ = layui.jquery;
	 		 
		//表单提交
	    form.on('submit(demo-1)', function(data) {
			// 询问框
			layer.confirm('确认账单无误？', {
				title : "提示",
				icon : 3,
				btn : [ '确认', '取消' ]
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

function load(obj){
   //重新加载table
   tableIns.reload({
       where: obj.field
       , page: {
           curr: pageCurr //从当前页码开始
       }
   });
}