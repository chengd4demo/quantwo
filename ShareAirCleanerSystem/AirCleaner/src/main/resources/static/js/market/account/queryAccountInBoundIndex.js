var pageCurr;
layui.use(['table', 'form', 'layer', 'vip_table','laydate'], function(){
	 var table = layui.table
		,form = layui.form
	    ,layer = layui.layer
	    ,vipTable = layui.vip_table
	    ,laydate = layui.laydate
	    ,$ = layui.jquery;
	//日期
	    laydate.render({
	        elem: '#date'
	    });
	 tableIns=table.render({
		 elem: '#accountInBoundList'
	            ,url:'/market/accountInBound/page'
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
							field: 'code',
							title: '用户编号',
							width: 185
						}, {
							field: 'name',
							title: '用户名称',
							width: 200
						}, {
							field: 'type',
							title: '用户类型',
							align: 'center',
							width: 90
						}, {
							field: 'amount',
							title: '金额(元)',
							align: 'center',
							width: 95
						}, {
							field: 'weixin',
							title: '微信号',
							width: 260
						}, {
							field: 'inBoundTime',
							title: '入账时间',
							sort: true,
							width: 170
						}, {
							field: 'amount',
							title: '支付金额(元)',
							align: 'center',
							width: 135
						}, {
							field: 'state',
							title: '状态',
							templet: '#stateTag',
							align: 'center',
							width: 100
						},{
							fixed: 'right',
							title: '操作',
							width: 80,
							align: 'center',
							toolbar: '#barOption'
							}
						]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
			 numbers=count;
		}
	 });
	
	//监听搜索框
	form.on('submit(searchSubmit)', function(data){
		//重新加载table
		load(data);
		return false;
	});
	
	 table.on('tool(accountInboundTable)',function(obj){
		var data = obj.data,layEvent = obj.event;
		if(layEvent === 'inBoundAffirm') {
				layer.confirm('确定入账？', function(index) {
					//请求
					$.ajax({
						url:"/market/accountInBound/affirm/"+data.id,
						async:false,
						success:function(data){
							if(data.code==200){
								layer.msg('入账成功！',{time:500},function(){
									layer.close(index);
									obj.del();
									$(".layui-laypage-btn").click();
			            		 });
							}else {
								layer.msg(data.msg);
								layer.close(index);
							}
						}
					});
				});
			}
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