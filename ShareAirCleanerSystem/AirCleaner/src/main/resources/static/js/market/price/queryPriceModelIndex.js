var pageCurr;
	layui.use(['table','element','vip_tab'], function(){
		var form = layui.form,table = layui.table,
		layer = layui.layer,vip_tab = layui.vip_tab,
		$ = layui.jquery;
		 tableIns=table.render({
			 elem: '#dateTable'
		            ,url:'/market/price/page'
		        	,method: 'GET' //默认：get请求
		            ,height: 315
		            ,page: true,
		            request: {
		                pageName: 'page' //页码的参数名称，默认：page
		                ,limitName: 'limit' //每页数据量的参数名，默认：limit
		            }
		            ,cols: [[
		                 {
								field: 'name',
								title: '模型名称',
								sort: true,
								align: 'center',
								width: 185
							}, {
								field: 'description',
								title: '模型描述',
								sort: true,
								align: 'center',
								width: 150
							}, {
								field: 'prices',
								title: '当前价格',
								width: 430
							}, {
								field: 'createTime',
								title: '添加时间',
								sort: true,
								width: 170
							}, {
								fixed: 'right',
								title: '操作',
								width: 185,
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
				 		,content: '../../../market/price/model/edit'
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
					 if(layEvent === 'detail') {
						 vip_tab.add(parent.layer.element,"指定模型价格 | "+data.name, "/market/priceValue/index/"+data.id+"/"+data.name);
						} else if(layEvent === 'edit') {
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
						 		,content: '../../../market/price/model/edit?id='+data.id
						 	});
						} else if(layEvent === 'del') {
							layer.confirm('确定删除', function(index) {
								//请求
								$.ajax({
									url:"/market/price/model/del/"+data.id,
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
				 
	});
	
	
	/**
	*查询 
	*/
	layui.use(['form'], function(){
		var form = layui.form ,layer = layui.layer;
		//TODO 数据校验
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
