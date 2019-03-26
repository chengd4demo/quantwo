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
		 elem: '#accountOutBoundList'
	            ,url:'/market/accountOutBound/page'
	        	,method: 'GET' //默认：get请求
	            ,height: 315
	            ,page: true
	            ,limits: [20, 30, 50, 100, 200]
	            ,limit:20
	            ,where:{method:'record'}
	            ,request: {
	                pageName: 'page' //页码的参数名称，默认：page
	                ,limitName: 'limit' //每页数据量的参数名，默认：limit
	            }
	            ,cols: [[
	                 {
							field: 'name',
							title: '用户名称',
							width: 120
						}, {
							field: 'phoneNumber',
							title: '手机号码',
							width: 120
						}, {
							field: 'nickName',
							title: '微信昵称',
							width: 120
						}, {
							field: 'timeEnd',
							title: '支付完成时间',
							templet: '#stateTime',
							sort: true,
							width: 180
						}, {
							field: 'type',
							title: '用户类型',
							templet: '#typeTag',
							width: 120 							
						}, {
							field: 'types',
							title: '出账类型',
							templet: '#typesTag',
							width: 120 							
						}, {
							field: 'cashMode',
							title: '支付类型',
							width: 120
						}, {
							field: 'amount',
							title: '金额',
							align: 'center',
							width: 120
						}, {
							field: 'outBoundStatus',
							title: '状态',
							templet: '#stateTpl',
							width: 120 							
						}, {
							field: 'returnCode',
							title: '业务结果',
							templet: '#stateTpls',
							width: 120
						}, {
							field: 'errCode',
							title: '错误代码',
							width: 120
						}, {
							field: 'errorMsg',
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