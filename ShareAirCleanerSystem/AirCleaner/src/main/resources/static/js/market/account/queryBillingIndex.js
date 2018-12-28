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
		 elem: '#billingList'
			 	,id:"id"
	            ,url:'/market/billing/page'
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
							field: 'billingId',
							title: '订单号',
							width: 265
						}, {
							field: 'transactionId',
							title: '交易流水号',
							width: 260
						}, {
							field: 'machNo',
							title: '设备编号',
							align: 'center',
							width: 155
						}, {
							field: 'unitPrice',
							title: '单价（元）',
							align: 'center',
							width: 105
						}, {
							field: 'costTime',
							title: '消费时长（分钟）',
							align: 'center',
							width: 145
						}, {
							field: 'discountStr',
							title: '折扣',
							align: 'center',
							width: 90
						}, {
							field: 'amount',
							title: '支付金额（元）',
							align: 'center',
							width: 135
						}, {
							field: 'state',
							title: '订单状态',
							templet: '#billingTpl',
							align: 'center',
							sort: true,
							width: 100
						},{
							field:'createTimeStr',
							title:'消费时间',
							sort:true,
							width:170
						},{
							field:'errorCode',
							title:'错误编码',
							sort:true,
							width:180
						},{
							field:'errorMsg',
							title:'错误信息',
							sort:true,
							width:180
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