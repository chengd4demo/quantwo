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
		 elem: '#accountSystemList'
	            ,url:'/market/accountOutBound/page'
	        	,method: 'GET' //默认：get请求
	            ,height: 315
	            ,page: true
	            ,limits: [20, 30, 50, 100, 200]
	            ,limit:20
	            ,where:{method:'audit'}
	            ,request: {
	                pageName: 'page' //页码的参数名称，默认：page
	                ,limitName: 'limit' //每页数据量的参数名，默认：limit
	            }
	            ,cols: [[
	            	{
						field: 'name',
						title: '申请人',
						width: 120
					}, {
						field: 'phoneNumber',
						title: '手机号',
						width: 120
					}, {
						field: 'weixin',
						title: '微信号',
						width: 120
					}, {
						field: 'timeEnd',
						title: '申请时间',
						sort: true,
						width: 180						
					}, {
						field: 'amount',
						title: '金额',
						align: 'center',
						width: 120
					}, {
						field: 'rejectReason',
						title: '审核信息',
						width: 180
					}, {
						field: 'state',
						title: '状态',
						templet: '#stateTpl',
						width: 120 													
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
		}
	 });
	 
	 table.on('tool(accountSystemTable)',function(obj){
			var data = obj.data,layEvent = obj.event;
			if(layEvent === 'accountSystemAudit') {
				layer.open({
			 		 type: 2
			 		,title: '审核 '//显示标题栏
			 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
			 		,skin: 'layui-layer-rim' //加上边框
			 		,area: ['570px', '370px'] //宽高
			 		,maxmin: true
			 		,shade: 0.8
			 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			 		,btnAlign: 'c'
			 		,moveType: 1 //拖拽模式，0或者1
			 		,content: '../../../market/accountOutBound/audit/edit?id='+data.id
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