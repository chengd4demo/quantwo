layui.use(['form', 'layedit', 'laydate', 'layer'], function() {
	var form = layui.form,$=layui.jquery,
		layer = layui.layer;
	form.verify({
		name:function(value){
			if (value.length == 0) {
				return '请输入菜单名称';
			} else if (value.length > 20) {
				return '菜单名称不能大于20个字符';
			}
		}
	});
	form.on('submit(addIcon)',function(data){
		parent.parent.layer.open({
		  type: 2,
		  title: '图标',
		  skin: 'layui-layer-rim', //加上边框
		  area: ['1024px', '600px'], //宽高
		  content: '/system/menu/icon?windowName='+window.name
		});
	    return false;
	});
	form.on('submit(demo1)', function(data) {
		// 询问框
		layer.confirm('是否提交保存？', {
			title : "提示",
			icon : 3,
			btn : [ '是', '否' ]
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