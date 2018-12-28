var pageCurr;
var numbers;
layui.use(['table', 'form', 'layer', 'vip_table'], function(){
	 var table = layui.table
     , layer = layui.layer
     , vipTable = layui.vip_table
     , $ = layui.jquery;
	 tableIns=table.render({
		 elem: '#userList'
			 	,id:"id"
	            ,url:'/security/user/page'
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
							field: 'username',
							title: '账号',
							width: 185
						}, {
							field: 'trueName',
							title: '真实姓名',
							width: 150
						}, {
							field: 'status',
							title: '状态',
							width: 180,
							templet: '#statusTpl'
						}, {
							field: 'lastLoginTime',
							title: '最后登录时间',
							sort: true,
							width: 170
						}, {
							field: 'createTime',
							title: '创建时间',
							sort: true,
							width: 180
						}, {
							fixed: 'right',
							title: '操作',
							width: 255,
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
		 		,content: '../../../security/user/edit'
		 	});
	 	 }
	 };
	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 table.on('tool(userTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'udapteStatus') {
			//请求
			$.ajax({
				url:"/security/user/update/"+data.id,
				async:false,
				success:function(data){
					if(data.code==200){
						layer.msg('操作成功！',{time:500},function(){
							$(".layui-laypage-btn").click();
	            		 });
					}else {
						layer.msg('操作失败！');
					}
				}
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
			 		,content: '../../../security/user/edit?id='+data.id
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/security/user/"+data.id,
						async:false,
						success:function(data){
							if(data.code==200){
								layer.msg('删除成功！',{time:500},function(){
									layer.close(index);
									obj.del();
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
});




/**
 *查询 
 */
layui.use(['form'], function(){
	var form = layui.form ,layer = layui.layer;
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
