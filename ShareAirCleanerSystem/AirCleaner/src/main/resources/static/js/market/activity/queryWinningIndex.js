var pageCurr;            
	//中奖名单
layui.use(['table', 'form', 'layer','laydate'], function(){
		var table = layui.table
		,form = layui.form
	    ,layer = layui.layer
	    ,laydate = layui.laydate
	    ,table = layui.table
	    ,$ = layui.jquery;
		//日期
		laydate.render({
	        elem: '#date'
	    });
	tableWinningIns=table.render({
		 elem: '#winningList'
	            ,url:'/market/queryWinning/page'
	        	,method: 'GET' //默认：get请求
	            ,page: true
	            ,limits: [20, 30, 50, 100, 200]
	            ,limit:20
	            ,request: {
	                pageName: 'page' //页码的参数名称，默认：page
	                ,limitName: 'limit' //每页数据量的参数名，默认：limit
	            }
	            ,cols: [[
	                 {
							field: 'awardsName',
							title: '奖项名称',
							width: 120
						}, {
							field: 'prizeName',
							title: '奖品名称',
							width: 120
						}, {
							field: 'phone',
							title: '手机号码',
							width: 120
						}, {
							field: 'customerName',
							title: '姓名',
							width: 180
						}, {
							field: 'address',
							title: '地址',
							
							width: 120 							
						}, {
							field: 'state',
							title: '是否兑奖',
							templet: '#stateTag',
							align: 'center',
							width: 120 							
						}, {
							field: 'prizeTime',
							title: '兑奖时间',
							sort: true,
							width: 120
						}, {
							field: 'winningTime',
							title: '中奖时间',
							sort: true,
							width: 120
						}, {
							field: 'trader',
							title: '所属商家',
							width: 120 							
						}, {
							field: 'logisticsNum',
							title: '物流号',
							width: 120 							
						}, {
							fixed: 'right',
							title: '操作',
							width: 150,
							align: 'center',
							toolbar: '#winningOption'
						}
	            ]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
		}
	      
	 });
	setTimeout(function(){
  	//新弹窗
	table.on('tool(winningTable)',function(obj){
		var data = obj.data,layEvent = obj.event;
		if(layEvent === 'shipments') {
			layer.open({
		 		 type: 2
		 		,title: '发货'//显示标题栏
		 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
		 		,skin: 'layui-layer-rim' //加上边框
		 		,area: ['60%', '90%'] //宽高
		 		,maxmin: true
		 		,shade: 0.8
		 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		 		,btnAlign: 'c'
		 		,moveType: 1 //拖拽模式，0或者1
		 		,content: '../../../market/queryWinning/edit?id='+data.id
		 	});
		}
	 });
	},800);
	//监听搜索框
	setTimeout(function(){
		form.on('submit(searchWinningBtn)', function(data){
			//重新加载table
			load(data);
			return false;
		});
		form.on('submit(winingReset)',function(data){
			document.getElementById("searchWinningForm").reset();
			//重新加载table
			$(".layui-laypage-btn").click();
			return false;
		})
	},1000);
});

function load(obj){
   //重新加载table
	tableWinningIns.reload({
       where: obj.field
       , page: {
           curr: pageCurr //从当前页码开始
       }
   });
}