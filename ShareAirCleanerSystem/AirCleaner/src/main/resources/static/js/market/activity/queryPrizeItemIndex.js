var pageCurr; 
	/*奖项设置*/
	layui.use(['table', 'form', 'layer','laydate'], function(){
		var table = layui.table
		,form = layui.form
	    ,layer = layui.layer
	    ,laydate = layui.laydate
	    ,$ = layui.jquery;
		//日期
	    laydate.render({
	        elem: '#date'
	    });
		 tablePrizeItemIns=table.render({
			 elem: '#prizeItemList'
		            ,url:'/market/prizeItem/page'
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
							field: 'name',
							title: '奖项名称',
							width: 180
						}, {
							field: 'picturePath',
							title: '奖项图片',
							width: 180
						}, {
							field: 'probabilityLable',
							title: '中奖几率',
							align: 'center',
							width: 120
						}, {
							field: 'remarks',
							title: '备注', 								
							width: 280
						}, { 								
							fixed: 'right',
							title: '操作',
							width: 300,
							align: 'center',
							toolbar: '#prizeItemOption'
						}

		            ]]
	            ,  done: function(res, curr, count){
	   			 pageCurr=curr;
			}	 		   
		 });
	
		 var active = {
		 	prizeItemadd: function(){
			 	layer.open({
			 		 type: 2
			 		,title: '新增 '//显示标题栏
			 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
			 		,skin: 'layui-layer-rim' //加上边框
			 		,area: ['60%', '90%'] //宽高
			 		,maxmin: true
			 		,shade: 0.8
			 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			 		,btnAlign: 'c'
			 		,moveType: 1 //拖拽模式，0或者1
			 		,content: '../../../market/prizeItem/edit'
			 	});
		 	 }
		 };
		 layui.jquery('#prizeItemaddbtn .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
		 });
		 layui.jquery('#resetbtn .layui-btn').on('click', function(){
			 	var othis = $(this), method = othis.data('method');
			 	active[method] ? active[method].call(this, othis) : '';
		 });
		 setTimeout(function(){			 	
			 table.on('tool(prizeItemTable)',function(obj){
				 var data = obj.data,layEvent = obj.event;
				if(layEvent === 'edit') {
						layer.open({
					 		 type: 2
					 		,title: '编辑 '//显示标题栏
					 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
					 		,skin: 'layui-layer-rim' //加上边框
					 		,area: ['60%', '90%'] //宽高
					 		,maxmin: true
					 		,shade: 0.8
					 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
					 		,btnAlign: 'c'
					 		,moveType: 1 //拖拽模式，0或者1
					 		,content: '../../../market/prizeItem/edit?id='+data.id
					 	});
					} else if(layEvent === 'del') {
						layer.confirm('确定删除', function(index) {
							//请求
							$.ajax({
								url:"/market/prizeItem/"+data.id,
								async:false,
								success:function(data){
									if(data.code==200){
										layer.msg('删除成功！',{time:500},function(){
											layer.close(index);
											obj.del();
											$(".layui-laypage-btn").click();
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
		 
		 },900);
		 
		//监听搜索框
		setTimeout(function(){
				 form.on('submit(searchPrizeItemSubmit)', function(data){
						//重新加载table
						load(data);
						return false;
					});
			 },1000);
		});
		function load(obj){
		   //重新加载table
			tablePrizeItemIns.reload({
		       where: obj.field
		       , page: {
		           curr: pageCurr //从当前页码开始
		       }
		   });
		}