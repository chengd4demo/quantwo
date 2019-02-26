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
		if(data.value == 'ZD' || data.value == 'DL') {
			getAgentsList(data.value)
			document.getElementById('isIdText').style.display='none';
			document.getElementById('isIdSelect').style.display='block';
			document.getElementById('textNameId').removeAttribute('lay-verify')
			document.getElementById('agentId').setAttribute('lay-verify','required')
		} else {
			document.getElementById('isIdText').style.display='block';
			document.getElementById('isIdSelect').style.display='none';
			document.getElementById('agentId').removeAttribute('lay-verify')
			document.getElementById('textNameId').setAttribute('lay-verify','required')
		}
		form.render(); 	
	});
	form.on('submit(demo1)',function(data){
		var type = data.field.type
		if(type == 'ZD' || type == 'DL') {
			var selectAgent = document.getElementById('agentId')
			var index = selectAgent.selectedIndex
			var agentName = selectAgent.options[index]
			if(agentName.value){
				data.field.name = agentName.text
			} else {
				data.field.name = ''
			}
			
		} else {
			data.field.agentId = ''
		}
		alert(JSON.stringify(data.field))
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
	function getAgentsList(type){
		//请求
		$.ajax({
			url:"/market/agent/list?type="+type,
			async:false,
			success:function(data){
				$('#agentId').empty()
				var agent,t='<option value="">请选择公司名称/客户</option>';
				if(data != undefined && data.length != 0) {
					for ( var i = 0; i <data.length; i++){
						agent = data[i];
						t+='<option value="'+agent.id+'">'+agent.name+'</option>'
					}
					$('#agentId').append(t);
				}
			}
		});
	}
	function removeFree() {
		document.getElementById('shouhou1').style.display='none';
		document.getElementById('freeId').removeAttribute('lay-verify')
	}
});