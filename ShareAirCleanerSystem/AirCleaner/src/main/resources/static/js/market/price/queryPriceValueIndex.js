var pageCurr;
	layui.use(['table','element'], function(){
		var form = layui.form,table = layui.table,
		layer = layui.layer,
		$ = layui.jquery,
		priceModelId = $("#modelId").val();
		priceModelName = $("#modelName").text();
		 tableIns=table.render({
			 elem: '#dateTable'
		            ,url:'/market/priceValue/page?priceModelId='+priceModelId
		        	,method: 'GET' //默认：get请求
		            ,height: 315
		            ,page: true,
		            request: {
		                pageName: 'page' //页码的参数名称，默认：page
		                ,limitName: 'limit' //每页数据量的参数名，默认：limit
		            }
		            ,cols: [[
		                 {
								field: 'costTime',
								title: '时长(分钟)',
								sort: true,
								align: 'center',
								width: 110
							}, {
								field: 'value',
								title: '单价(元)',
								sort: true,
								align: 'center',
								width: 90
							}, {
								field: 'discount',
								title: '优惠折扣(%)',
								sort: true,
								align: 'center',
								width: 100
							}, {
								field: 'realValue',
								title: '实际价格',
								sort: true,
								width: 80
							},  {
								field: 'activeStartTime',
								title: '优惠开始时间',
								sort: true,
								width: 170
							}, {
								field: 'activeEndTime',
								title: '优惠结束时间',
								sort: true,
								width: 170
							},{
								fixed: 'right',
								title: '操作',
								width: 398,
								align: 'center',
								toolbar: '#barOption'
							} 
		            ]]
			,  done: function(res, curr, count){
				pageCurr=curr;
			}
		 });
		 //新增窗口绑定事件
		 var active = {
				 add: function(){
				 	layer.open({
				 		type: 2
				 		,title: '新增 '//显示标题栏
				 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
				 		,skin: 'layui-layer-rim' //加上边框
				 		,area: ['570px', '410px'] //宽高
				 		,maxmin: true
				 		,shade: 0.8
				 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
				 		,btnAlign: 'c'
				 		,moveType: 1 //拖拽模式，0或者1
				 		,content: '../../../priceValue/bath/edit?priceModelId='+priceModelId+'&priceModelName='+priceModelName
				 		,success: function(layero){
				 			var btn = layero.find('.layui-layer-btn');
				 			btn.find('.layui-layer-btn0').attr({href: '',target: '_blank'});
				 			}
				 		});
				 	}
			 };
				 layui.jquery('#addbtn .layui-btn').on('click', function(){
				 	var othis = $(this), method = othis.data('method');
				 	active[method] ? active[method].call(this, othis) : '';
				 });
				 
				 table.on('tool(formElements)',function(obj){
					 var data = obj.data,layEvent = obj.event;
					  if(layEvent === 'edit') {
							layer.open({
						 		 type: 2
						 		,title: '编辑 '//显示标题栏
						 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
						 		,skin: 'layui-layer-rim' //加上边框
						 		,area: ['570px', '410px'] //宽高
						 		,maxmin: true
						 		,shade: 0.8
						 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
						 		,btnAlign: 'c'
						 		,moveType: 1 //拖拽模式，0或者1
						 		,content: '../../../priceValue/bath/edit?id='+data.id+'&priceModelName='+priceModelName+"&priceModelId="+priceModelId
						 	});
						} else if(layEvent === 'del') {
							layer.confirm('确定删除', function(index) {
								//请求
								$.ajax({
									url:"/market/priceValue/"+data.id,
									async:false,
									success:function(data){
										if(data.code==200){
											layer.msg('删除成功！',{time:500},function(){
												layer.close(index);
												obj.del();
						            		 });
										}else {
											layer.msg('删除失败！');
											layer.close(index);
										}
									}
								});
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
