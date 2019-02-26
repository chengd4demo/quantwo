var pageCurr;
layui.use(['table', 'form', 'layer', 'vip_table'], function(){
	 var table = layui.table
		,form = layui.form
	    ,layer = layui.layer
	    ,vipTable = layui.vip_table
	    ,$ = layui.jquery;
	function getInvestorList(){
		//请求
		$.ajax({
			url:"/market/investor/list",
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
	 getInvestorList();
	 form.render('select');
	 tableIns=table.render({
		 elem: '#priceSystemList'
	            ,url:'/market/priceSystems/page'
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
							field: 'name',
							title: '价格体系名称',
							width: 120
						}, {
							field: 'description',
							title: '描述',
							width: 180
						}, {
							field: 'state',
							title: '是否激活',
							templet: '#stateTag',
							align: 'center',
							width: 120
						}, {
							field: 'priceModelName',
							title: '价格模型名称',
							width: 180
						}, {
							field: 'createTime',
							title: '添加时间',
							sort: true,
							width: 180
						}, {
							field: 'creater',
							title: '添加人',
							width: 180 							
						}, {
							fixed: 'right',
							title: '操作',
							width: 180,
							align: 'center',
							toolbar: '#barOption'
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
	 		,area: ['570px', '410px'] //宽高
	 		,maxmin: true
	 		,shade: 0.8
	 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
	 		,btnAlign: 'c'
	 		,moveType: 1 //拖拽模式，0或者1
	 		,content: '../../../market/priceSystems/edit'
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
	 layui.jquery('#resetbtn .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
	 });
	 table.on('tool(priceSystemTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'detail') {
				layer.msg('查看操作');
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
			 		,content: '../../../market/priceSystems/edit?id='+data.id
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/market/priceSystems/"+data.id,
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
			}else if(layEvent === 'priceSystemAffirm') {	 //价格体系激活
				layer.confirm('确定激活？', function(index) {
					var parame = '0';
					if(data.state == '0') {
						parame = '1';
					} else {
						parame = '0';
					}
					//请求
					$.ajax({
						url:"../../../market/priceSystems/affirm/"+data.id+"?state=" + parame,
						async:false,
						success:function(data){
							if(data.code==200){
								layer.msg(parame == '1' ? '激活成功': '取消激活成功',{time:500},function(){
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
});


/**
 *查询 
 */
layui.use(['form'], function(){
	var form = layui.form ,layer = layui.layer;
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
