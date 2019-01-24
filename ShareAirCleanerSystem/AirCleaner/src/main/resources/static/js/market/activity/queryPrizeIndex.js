var pageCurr; 
	/*奖品设置*/
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
		 tableIns=table.render({
			 elem: '#prizeList'
		            ,url:'/market/prize/page'
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
							field: 'prizeItemConfigName',
							title: '奖项名称',
							width: 120
						}, {
							field: 'prizeName',
							title: '奖品名称',
							width: 120
						}, {
							field: 'prizeNumber',
							title: '奖品数量',
							align: 'center',
							width: 120
						}, {
							field: 'prizeCategory',
							title: '奖品品类',
							templet: '#prizeCategoryTpl',
							width: 120
						}, {
							field: 'traderName',
							title: '所属商家',
							width: 180 
						}, {
							field: 'state',
							title: '是否激活',
							align: 'center',
							templet: '#stateTpl',
							width: 120
						}, {
							field: 'prizeType',
							title: '奖品类型',
							templet: '#prizeTypeTpl',
							width: 120 							
						}, {
							field: 'denomination',
							title: '面额',
							align: 'center',
							width: 120
						}, {
							field: 'effectiveTime',
							title: '有效期',
							sort: true,
							width: 120
						}, {
							field: 'address',
							title: '使用地址',
							width: 180 							
						}, {
							fixed: 'right',
							title: '操作',
							width: 180,
							align: 'center',
							toolbar: '#barOption1'
						}

		            ]]
	            ,  done: function(res, curr, count){
	   			 pageCurr=curr;
			}	 		   
		 });
		 var active = {
			 add: function(){
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
			 		,content: '../../../market/prize/edit'
			 		,end: function(){
			 			$(".layui-laypage-btn").click();
			 		}
			 	});
		 	 }
		 };
		 layui.jquery('#addbtn .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
		 });
		 layui.jquery('#resetbtn .layui-btn').on('click', function(){
			 	var othis = $(this), method = othis.data('method');
			 	active[method] ? active[method].call(this, othis) : '';
		 });
		 table.on('tool(prizeTable)',function(obj){
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
				 		,content: '../../../market/prize/edit?id='+data.id
				 	});
				} else if(layEvent === 'del') {
					layer.confirm('确定删除', function(index) {
						//请求
						$.ajax({
							url:"/market/prize/"+data.id,
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
				} else if(layEvent === 'prizeAffirm') {	 //奖品激活
					layer.confirm('确定激活？', function(index) {
						var parame = '6';
						if(data.state == '6') {
							parame = '7';
						} else {
							parame = '6';
						}
						//请求
						$.ajax({
							url:"../../../market/prize/affirm/"+data.id+"?state=" + parame,
							async:false,
							success:function(data){
								if(data.code==200){
									layer.msg(parame == '7' ? '激活成功': '取消激活成功',{time:500},function(){
										layer.close(index);
										obj.del();
										$(".layui-laypage-btn").click();
				            		 });
								}else {
									layer.msg(data.msg, {
	 	   								time : 1000,
	 	   								icon : 5,
	 	   								shade : 0.3,
	 	   								anim : 5
	 	   							});
								}
							}
						});
					});
				}
		 });
		 
		//监听搜索框
		setTimeout(function(){
			form.on('submit(searchPrizeSubmit)', function(data){
				//重新加载table
				load(data);
				return false;
			});
			form.on('submit(bnt-reset)',function(data){
				document.getElementById("searcPrizeConfighForm").reset();
				//重新加载table
				$(".layui-laypage-btn").click();
				return false;
			})
		},600);
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