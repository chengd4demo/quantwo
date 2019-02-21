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
		 elem: '#customerList'
	            ,url:'/market/customer/page'
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
							field: 'nickName',
							title: '微信昵称',
							width: 185
						}, {
							field: 'phoneNumber',
							title: '手机号码',
							width: 130
						}, {
							field: 'name',
							title: '姓名',
							width: 100
						},  {
							field: '',
							title: '年龄',							
							width: 100,
							templet:'#age'
						},{
							field: 'sex',
							title: '性别',
							align: 'center',
							templet: '#sexTpl',
							width: 60
						},{
							field: 'joinTime',
							title: '注册时间',
							sort: true,
							align: 'center',
							width: 180
						},{
							field: 'address',
							title: '地区',
							align: 'center',
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
	
	 table.on('tool(customerTable)',function(obj){
		var data = obj.data,layEvent = obj.event;
		if(layEvent === 'detail') {
			layer.open({
		 		 type: 2
		 		,title: '用户详情 '//显示标题栏
		 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
		 		,skin: 'layui-layer-rim' //加上边框
		 		,area: ['570px', '410px'] //宽高
		 		,maxmin: true
		 		,shade: 0.8
		 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		 		,btnAlign: 'c'
		 		,moveType: 1 //拖拽模式，0或者1
		 		,content: '../../../market/customer/edit?id='+data.id
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