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
	    
	 //本地交易数据
	 tableIns=table.render({
		 elem: '#localTransactionList'
	            ,url:'/market/difference/page'
	        	,method: 'GET' //默认：get请求
	            ,height: 150
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
							title: '用户ID',
							width: 160
						}, {
							field: '',
							title: '支付订单号',
							width: 160
						}, {
							field: '',
							title: '支付完成时间',
							sort: true,
							width: 160
						},  {
							field: '',
							title: '支付类型',							
							width: 130,
						},{
							field: '',
							title: '对账类型',
							width: 130
						},{
							field: '',
							title: '金额',
							align: 'center',
							width: 100
						},{
							field: '',
							title: '对账状态',
							width: 150
						}
					]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
			 numbers=count;
		}
	 });
	 //渠道交易数据
	 tableIns=table.render({
		 elem: '#channelTradingList'
	            ,url:'/market/difference/page'
	        	,method: 'GET' //默认：get请求
	            ,height: 150
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
							title: '用户ID',
							width: 160
						}, {
							field: '',
							title: '支付订单号',
							width: 160
						}, {
							field: '',
							title: '支付完成时间',
							sort: true,
							width: 160
						},  {
							field: '',
							title: '支付类型',							
							width: 130,
						},{
							field: '',
							title: '对账类型',
							width: 130
						},{
							field: '',
							title: '金额',
							align: 'center',
							width: 100
						},{
							field: '',
							title: '对账状态',
							width: 150
						}
					]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
			 numbers=count;
		}
	 });
	 
	//表单提交
    form.on('submit(demo-1)', function(data) {
		// 询问框
		layer.confirm('确认账单无误？', {
			title : "提示",
			icon : 3,
			btn : [ '确认', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				url : 'edit/save',
				type : "POST",
				data : data.field,
				success : function(data) {
					if (data.code == 200) {
						layer.msg('保存成功！', {
							icon : 1,
							time : 1000
						}, function() {
							index = parent.layer.getFrameIndex(window.name);
							parent.layer.close(index);
							parent.location.reload();

						});
					} else {
						layer.msg(data.msg, {
							time : 1000,
							icon : 5,
							shade : 0.3,
							anim : 5
						});
					}
				}
			})
		}, function() {
			layer.close();
		});
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