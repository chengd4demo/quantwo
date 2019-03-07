var pageCurr;
layui.use(['table','form','layer','element','vip_tab','laydate'], function(){
	var form = layui.form,
	    table = layui.table,
		layer = layui.layer,
		vip_tab = layui.vip_tab,
		element = layui.element,
		laydate = layui.laydate,
		$ = layui.jquery;
	 
 	 //日期
     laydate.render({
        elem: '#date'
     });
	    
	 //跳转  
     table.on('tool(accountingTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'detail') {
			 vip_tab.add(parent.layer.element, "差异明细 | "+data.orderNumber, "/market/difference/index/"+data.id+"/"+data.orderNumber);
		 }
	 });
	    
	 //列表
	 tableIns=table.render({
		 elem: '#accountingList'
	            ,url:'/market/accounting/page'
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
							field: '',
							title: '商户订单号',
							width: 160
						}, {
							field: '',
							title: '支付订单号',
							width: 130
						}, {
							field: '',
							title: '支付完成时间',
							sort: true,
							width: 130
						},  {
							field: '',
							title: '支付类型',							
							width: 100,
						},{
							field: '',
							title: '对账类型',
							width: 130
						},{
							field: '',
							title: '金额',
							align: 'center',
							width: 80
						},{
							field: '',
							title: '对账状态',
							width: 150
						},{
							fixed: 'right',
							title: '操作',
							width: 150,
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
		 
});

//重新加载table
function load(obj){   
   tableIns.reload({
       where: obj.field
       , page: {
           curr: pageCurr //从当前页码开始
       }
   });
}