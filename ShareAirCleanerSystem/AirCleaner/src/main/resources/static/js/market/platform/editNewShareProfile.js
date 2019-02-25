layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form,$=layui.jquery,layer = layui.layer;
	
	form.verify({
		scale: [/^([1-9]\d?|99)$/, "请输入1~99的整数"]	
	   ,free:[/[1-9]\d*.\d*|0\.\d*[1-9]\d*/,"请输入0.01~1的正确数字范围"]
	   ,name:function(value){
		   if (value.length == 0) {
				return '请输入名称';
			} else if (value.length > 20) {
				return '名称不能大于20个字符';
			}
	   }
    });
	form.on('select(type)',function(data){
		if(data.value == 'CY') {
			data.elem.disabled = true
			document.getElementById('pid').style.display='none';
			removeFree();
		} 
		if (data.value == 'IR') {
			document.getElementById('shouhou1').style.display='block';
			document.getElementById('freeId').setAttribute('lay-verify','free')
		} else {
			removeFree();
		}
		form.render(); 	
	})
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
//	function init(){
//		var level = document.getElementById('level').value
//		if(level=='0') {
//			document.getElementById('pid').style.display='block';
//			document.getElementById('select1').style.disabled = false;
//		}
//	}
	
	function removeFree() {
		document.getElementById('shouhou1').style.display='none';
		document.getElementById('freeId').removeAttribute('lay-verify')
	}
});