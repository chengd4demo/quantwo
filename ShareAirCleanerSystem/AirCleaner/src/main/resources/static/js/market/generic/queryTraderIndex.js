var pageCurr;
layui.use('table', function(){
	var form = layui.form,table = layui.table,
		layer = layui.layer,vipTable = layui.vip_table,
		$ = layui.jquery;
	 tableIns=table.render({
		 elem: '#traderList'
	            ,url:'/market/traders/page'
	        	,method: 'GET' //默认：get请求
	            ,height: 315
	            ,page: true
	            ,limits: [20, 30, 50, 100, 200]
	            ,limit:20
	            ,request: {
	                pageName: 'page' //页码的参数名称，默认：page
	                ,limitName: 'limit' //每页数据量的参数名，默认：limit
	            }
	            ,cols: [[
	                 {
							field: 'socialCreditCode',
							title: '统一信用码',
							width: 120
						}, {
							field: 'name',
							title: '商户名称',
							width: 180
						}, {
							field: 'address',
							title: '商户地址',
							width: 120
						}, {
							field: 'legalPerson',
							title: '负责人',
							width: 180
						}, {
							field: 'phoneNumber',
							title: '联系电话',
							width: 120
						}, {
							field: 'weixin',
							title: '微信',
							width: 120 							
						}, {
							field: 'email',
							title: '邮箱',
							width: 120 							
						}, {
							field: 'joinTime',
							title: '成立时间',
							width: 120 							
						}, {
							fixed: 'right',
							title: '操作',
							width: 150,
							align: 'center',
							toolbar: '#barOption'
						}
	            ]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
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
	 		,content: '../../../market/traders/trader/edit'
	 		,success: function(layero){
	 			var btn = layero.find('.layui-layer-btn');
	 			btn.find('.layui-layer-btn0').attr({href: '',target: '_blank'});
	 			}
	 		});
	 	}
 };
	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 layui.jquery('#resetbtn .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
	 });
	 table.on('tool(traderTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'detail') {
				layer.msg('查看操作');
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
			 		,content: '../../../market/traders/trader/edit?id='+data.id
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/market/traders/"+data.id,
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
