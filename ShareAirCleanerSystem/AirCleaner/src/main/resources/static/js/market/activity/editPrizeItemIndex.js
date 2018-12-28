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
	form.verify({
		name:function(value){
			if (value.length == 0) {
				return '请输入奖项名称';
			} else if (value.length > 20) {
				return '奖项名称不能大于20个字符';
			}
		}
		,probability:function(value){
			if (value.length == 0) {
				return '请输入中奖几率';
			} else if (100<max && n>0) {
				return '0~100';
			}
		}
		,remarks:function(value){
			if (value.length > 200) {
				return '备注信息不能大于200个字符'
			}
		}
	});
	<!-- 图片上传 -->
	var uploadInst = upload.render({
	    elem: '#test1'
	    ,url: '/upload/'
	    ,before: function(obj){
	      //预读本地文件示例，不支持ie8
	      obj.preview(function(index, file, result){
	        $('#demo1').attr('src', result); //图片链接（base64）
	      });
	    }
	    ,done: function(res){
	      //如果上传失败
	      if(res.code > 0){
	        return layer.msg('上传失败');
	      }
	      //上传成功
	    }
	    ,error: function(){
	      //演示失败状态，并实现重传
	      var demoText = $('#demoText');
	      demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
	      demoText.find('.demo-reload').on('click', function(){
	        uploadInst.upload();
	      });
	    }
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