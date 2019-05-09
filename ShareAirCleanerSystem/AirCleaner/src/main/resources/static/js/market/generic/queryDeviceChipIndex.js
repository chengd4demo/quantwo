var pageCurr;
layui.use(['table','element','vip_tab'], function(){
	var form = layui.form,table = layui.table,
		layer = layui.layer,vip_tab = layui.vip_tab,
		element = layui.element,$ = layui.jquery;
	 function getInvestorList(){
		//请求
		$.ajax({
			url:"/market/device/chip/investor/list",
			async:false,
			success:function(data){
				var investor,t='';
				if(data != undefined && data.length != 0) {
					for ( var i = 0; i <data.length; i++){
						investor = data[i];
						t+='<option value="'+investor.id+'">'+investor.name+'</option>'
					}
					$('#investor').append(t);
				}
			}
		});
	 }
	 function getTraderList(){
		//请求
		$.ajax({
			url:"/market/device/chip/trader/list",
			async:false,
			success:function(data){
				var trader,t='';
				if(data != undefined && data.length != 0) {
					for ( var i = 0; i <data.length; i++){
						trader = data[i];
						t+='<option value="'+trader.id+'">'+trader.name+'</option>'
					}
					$('#trader').append(t);
				}
			}
		});
	 }
	 function getSalerList(){
		//请求
		$.ajax({
			url:"/market/device/chip/saler/list",
			async:false,
			success:function(data){
				var saler,t='';
				if(data != undefined && data.length != 0) {
					for ( var i = 0; i <data.length; i++){
						saler = data[i];
						t+='<option value="'+saler.id+'">'+saler.name+'</option>'
					}
					$('#saler').append(t);
				}
			}
		});
	 }
	 getSalerList();
	 getTraderList();
	 getInvestorList();
	 form.render('select');
	 tableIns=table.render({
 		 elem: '#deviceChipList'
         ,url:'/market/device/chip/page'
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
				 field: 'machno',
				 title: '设备编码',
				 width: 155
             }, {
				 field: 'devicesequence',
				 title: '序列号',
				 width: 155
             }, {
				 field: 'batchname',
				 title: '所属批次',
				 width: 120
             }, {
				 field: 'setuptime',
				 title: '安装时间',
				 sort: true,
				 width: 120
             }, {
				 field: 'setupaddress',
				 title: '安装地址',
				 width: 180
             }, {
				 field: 'legalperson',
				 title: '投资商',
				 width: 120
             }, {
				 field: 'tradername',
				 title: '商家',
				 width: 120 							
             }, {
				 field: 'salername',
				 title: '促销员',
				 width: 120 							
             }, {
				 field: 'renascencetime',
				 title: '滤芯总生命时长(单位：小时)',
				 align: 'center',
				 width: 210 							
             }, {
				 field: 'employtime',
				 title: '已使用时长(单位：小时)',
				 align: 'center',
				 sort: true,
				 width: 200 							
             }, {
				 field: 'surplustime',
				 title: '剩余时长(单位：小时)',
				 align: 'center',
				 sort: true,
				 width: 200 							
             },{
				fixed: 'right',
				title: '操作',
				width: 80,
				align: 'center',
				toolbar: '#barOption'
			 }
         ]]
		 ,done: function(res, curr, count){
			 pageCurr=curr;
		 }
	 });
	 //恢复操作
	 table.on('tool(deviceChipTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'recovery') {
			 var lock=false;//默认未锁定
				layer.confirm('确定恢复滤芯时长？', function(index) {
					if (!lock){
						lock=true;//锁定
						//请求
						$.ajax({
							url:"/market/device/chip/updateChangeTime/"+ data.machno,
							async:false,
							success:function(data){
								if(data.code==200){
									layer.msg('恢复成功！',{time:500},function(){
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
					}
				});
			} 
	 });
	 
	 //查询
	 layui.use(['form'], function(){
		var form = layui.form ,layer = layui.layer;
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
});

