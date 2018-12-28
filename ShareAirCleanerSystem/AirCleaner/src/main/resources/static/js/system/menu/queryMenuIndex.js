var pageCurr;
layui.use(['table', 'form', 'layer', 'vip_table'], function(){
	 var table = layui.table
		,form = layui.form
	    ,layer = layui.layer
	    ,vipTable = layui.vip_table
	    ,$ = layui.jquery;
	 tableIns=table.render({
		 elem: '#menuList'
			 	,id:"id"
	            ,url:'/system/menu/page'
	        	,method: 'GET' //默认：get请求
	            ,height: vipTable.getFullHeight()
	            ,page: true
	            ,limits: [20, 30, 50, 100, 200]
	            ,limit:20
	            ,request: {
	                pageName: 'page' //页码的参数名称，默认：page
	                ,limitName: 'limit' //每页数据量的参数名，默认：limit
	            }
	            ,cols: [[
	                 {
							field: 'name',
							title: '名称',
							width: 300
						}, {
							field: 'url',
							title: 'URL',
							width: 622
						}, {
							fixed: 'right',
							title: '操作',
							width: 200,
							align: 'center',
							toolbar: '#barOption'
						} 
	            ]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
			 numbers=count;
		}
	 });
	 var active = {
		 add: function(){
		 	layer.open({
		 		 type: 2
		 		,title: '新增 '//显示标题栏
		 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
		 		,skin: 'layui-layer-rim' //加上边框
		 		,area: ['570px', '410px'] //宽高
		 		,maxmin: true
		 		,shade: 0.8
		 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		 		,btnAlign: 'c'
		 		,moveType: 1 //拖拽模式，0或者1
		 		,content: '../../../system/menu/edit'
		 	});
	 	 }
	 };
	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 table.on('tool(menuTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'addChildrenMenu') {
				layer.open({
			 		 type: 2
			 		,title: '新增 '//显示标题栏
			 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
			 		,skin: 'layui-layer-rim' //加上边框
			 		,area: ['570px', '410px'] //宽高
			 		,maxmin: true
			 		,shade: 0.8
			 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			 		,btnAlign: 'c'
			 		,moveType: 1 //拖拽模式，0或者1
			 		,content: '../../../system/menu/edit?parentMenuId='+data.id
			 	});
			} else if(layEvent === 'edit') {
				layer.open({
			 		 type: 2
			 		,title: '编辑 '//显示标题栏
			 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
			 		,skin: 'layui-layer-rim' //加上边框
			 		,area: ['570px', '410px'] //宽高
			 		,maxmin: true
			 		,shade: 0.8
			 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			 		,btnAlign: 'c'
			 		,moveType: 1 //拖拽模式，0或者1
			 		,content: '../../../system/menu/edit?id='+data.id
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/system/menu/"+data.id,
						async:false,
						success:function(data){
							if(data.code==200){
								layer.msg('删除成功！',{time:500},function(){
									layer.close(index);
									obj.del();
									$(".layui-laypage-btn").click();
			            		 });
							}else {
								layer.msg('删除失败！');
								layer.close(index);
							}
						}
					});
				});
			}
	 });
	//监听搜索框
	form.on('submit(searchSubmit)', function(data){
		//重新加载table
		load(data);
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