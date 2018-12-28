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
		 elem: '#weiXinNotityList'
	            ,url:'/market/weiXinNotity/page'
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
						field: 'outTradeNo',
						title: '商户订单号',
						width: 260
					}, {
						field: 'deviceInfo',
						title: '设备编码',
						width: 160
					}, {
						field: 'transactionId',
						title: '支付订单号',
						width: 260
					}, {
						field: 'timeEnd',
						title: '支付完成时间',
						sort: true,
						width: 180
					}, {
						field: 'bankType',
						title: '支付类型',
						width: 120 							
					}, {
						field: 'cashFee',
						title: '订单金额',
						align: 'center',
						width: 120
					}, {
						field: 'state',
						title: '财务状态',
						templet: '#stateTpl',							
						width: 120 							
					}, {
						field: 'returnCode',
						title: '业务结果',							
						width: 120
					}, {
						field: 'errCode',
						title: '错误代码',
						width: 120
					}, {
						field: 'returnMsg',
						title: '错误信息',
						width: 120 							
					}
	            ]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
		}
	 });

	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 layui.jquery('#resetbtn .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
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