layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form
		,$=layui.jquery
		,layer = layui.layer,
		domIndex = 1,
	     htmlDom = ''; 
	form.verify({
		scale:[/^([1-9]\d?|99)$/, "请输入1至99的整数"]
		,free:[/^([1-9]\d?|99)$/, "请输入1至99的整数"]
	});
	//监听提交
	  form.on('submit(demo1)', function(data){
		  var types  = document.getElementsByName("type");
		  //如果新增选择相同的分润对象，提示错误
		  if(isRepeat(typeArrays(types))) {
			  layer.msg("您选择的分润对象重复,请从新选择", {
					time : 1000,
					icon : 5,
					shade : 0.3,
					anim : 5
				}); 
			  return false;
		  }
		  
		  var scales  = document.getElementsByName("scale");
		  var free = $('#free').val();
		  var shareProfile;
		  var shareProfileArray = new Array(types.length);
		  for (var i = 0, j = types.length; i < j; i++){
			  shareProfile = new ShareProfit(null,types[i].value,scales[i].value,free);
			  shareProfileArray[i] = shareProfile;
		  }
		  	//询问框
			layer.confirm('是否提交保存？', {
				title : "提示",
				icon : 3,
				btn : [ '是', '否' ]
			// 按钮
			}, function() {
				$.ajax({
					url : 'edit/save',
					contentType:"application/json",
					type : "POST",
					data : JSON.stringify(shareProfileArray),
					dataType:"json",
					success : function(data) {
						if (data.code == 200) {
							layer.msg('保存成功！', {
								icon : 1,
								time : 1000
							}, function() {
								index = parent.layer.getFrameIndex(window.name);
								parent.layer.close(index);
								window.location.reload();
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
	
	form.on('submit(add)',function(data){
		$("#content").append('<div id="domIndex_'+domIndex+'"><div class="layui-form-item">'
				+ '		<div class="layui-inline">'
				+ '		<label class="layui-form-label"><span style="color:red">*</span>分润对象：</label>'
				+ '		<div class="layui-input-inline">'
				+ '			<select name="type" lay-verify="required" lay-search="">'
				+ '				<option value="">请选择分润对象</option>'
				+'              <option value="CY">公司</option>'
				+ '				<option value="IR">投资商</option>'
				+ '				<option value="TR">商家</option>'
				+ '				<option value="SR">促销员</option>'
				+ '				<option value="CR">会员</option>'
				+ '			</select>'
				+ '		</div>'
				+ '	</div>'
				+ '	<div class="layui-inline">'
				+ '		<label class="layui-form-label"><span style="color:red">*</span>分润比例</label>'
				+ '		<div class="layui-input-inline">'
				+ '			<input type="text" name="scale" autocomplete="off" lay-verify="required|scale"  class="layui-input">'
				+ '		</div>'
				+ '	</div>'
				+ '	<div class="layui-inline">'
				+ '		<button class="layui-btn   layui-btn-normal layui-btn-sm" lay-submit="" lay-filter="add" title="新增">'
				+ '		<i class="fa fa-plus-square-o   " style=""></i>+&nbsp;新增</button>'
				+ '	</div>'
				+ '	<div class="layui-inline">'
				+ '		<button id="'+domIndex+'" onclick="'+'onboundEvent(this.id)'+'" class="layui-btn   layui-btn-danger layui-btn-sm" type="button" data-method="saler"  title="删除">'
				+ '		<i class="fa fa-plus-square   " style=""></i>-&nbsp;删除</button>'
				+ '	</div></div></div>');
		domIndex++;
		form.render();
		return false;
	});
});

function onboundEvent(id){
	document.getElementById('domIndex_'+id).innerHTML = "";
}

function ShareProfit(id,type,scale,free){
	this.id = null;
	this.type = type;
	this.scale = parseFloat(scale);
	this.free = parseFloat(free);
}
function isRepeat(arr) {
    var hash = {};
    for(var i in arr) {
        if(hash[arr[i]]) {
            return true;
        }
        // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
        hash[arr[i]] = true;
    }
    return false;
}

function typeArrays(v){
	var count = v.length;
	var vArrays = new Array(count);
	for(var i=0;i<count;i++){
		vArrays[i] = v[i].value;
	}
	return vArrays;
}
