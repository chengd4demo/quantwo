var res = false;
layui.use(['form', 'layedit', 'laydate'], function(){
		  var form = layui.form
		  ,layer = layui.layer
		  ,layedit = layui.layedit
		  ,$ = layui.jquery
		  ,laydate = layui.laydate;//监听指定开关
		  setTimeout(function(){
			  form.render('checkbox');
			  var initChecked = $("#switchId").attr("checked");
			  checkedStatus(initChecked);
			   form.on('switch(switchTest)', function(data){
				   res = doOpen(this.checked);
				   checkedStatus(this.checked);
			    });
		   },200);
		   function checkedStatus(checked) {
			  if(checked) {
				   $("#openId").text('跑马灯：开启');
			   } else {
				   $("#openId").text('跑马灯：关闭');
			   }
		  }
		   function doOpen(val) {
			   $.ajax({
	                url: '/market/activity/set/open?on=' + val,
	                type: "GET",
	                success:function (data) {
	                	if(data.code==200){
	                	} else {
	                		layer.msg(data.msg, {
								 time: 1000
								,icon : 5
								,shade :0.3
								,anim: 5
							},function(){
								res = false;
							});
	                	}
	                }
	              });
		  }
		});