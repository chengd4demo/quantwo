layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form,$=layui.jquery,layer = layui.layer;
	form.verify({
		username:function(value){
			if (username.length == 0) {
				return '请输入账号';
			} else if (value.length > 20) {
				return RegExp("^[a-zA-Z0-9_]+$").test(e) ?  /(^\_)|(\__)|(\_+$)/.test(e) ? "用户名首尾不能出现下划线'_'": /^\d+\d+\d$/.test(e) ? "用户名不能全为数字" : void 0 : "用户名不能有特殊字符";
			}
		},
		trueName:function(value){
			if (value.length == 0) {
				return '请输真实姓名';
			} else if (value.length > 10) {
				return '真实姓不能大于200个字符'
			}
		},
		passward:function(value) {
			if(value.length !=0) {
				if(value.length<6 || value.length>12) {
					return "密码必须6到12位";
				}
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
                		layer.msg(data.msg, {
							 time: 1000
							,icon : 5
							,shade :0.3
							,anim: 5
						});
                	}
                }
              })
		}, function(){
		  layer.close();
		});
		return false;
	});
});