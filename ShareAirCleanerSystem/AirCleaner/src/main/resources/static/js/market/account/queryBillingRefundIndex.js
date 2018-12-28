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
		 elem: '#billingRefundList'
	            ,url:'/market/billingRefund/page'
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
							title: '用户姓名',
							width: 185
						}, {
							field: 'phoneNum',
							title: '手机号码',
							width: 200
						}, {
							field: 'nickName',
							title: '微信昵称',
							width: 200
						},  {
							field: 'createTime',
							title: '申请退款时间',
							sort: true,
							width: 170
						},{
							field: 'remakes',
							title: '备注',
							width: 60
						},{
							field: 'totalFee',
							title: '金额(元)',
							align: 'center',
							width: 135
						},{
							field: 'refundStatus',
							title: '状态',
							align: 'center',
							templet: '#refundStatusTpl',
							width: 90
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
	
	 table.on('tool(billingRefundTable)',function(obj){
		var data = obj.data,layEvent = obj.event;
		if(layEvent === 'billingRefundAudit') {
			layer.open({
		 		 type: 2
		 		,title: '审核 '//显示标题栏
		 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
		 		,skin: 'layui-layer-rim' //加上边框
		 		,area: ['570px', '410px'] //宽高
		 		,maxmin: true
		 		,shade: 0.8
		 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		 		,btnAlign: 'c'
		 		,moveType: 1 //拖拽模式，0或者1
		 		,content: '../../../market/billingRefund/editAudit?id='+data.id
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