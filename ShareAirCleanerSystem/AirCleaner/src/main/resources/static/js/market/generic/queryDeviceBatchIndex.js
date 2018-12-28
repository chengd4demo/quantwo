var pageCurr;
layui.use(['table','element','vip_tab'], function(){
	var form = layui.form,table = layui.table,
		layer = layui.layer,vip_tab = layui.vip_tab,
		element = layui.element,$ = layui.jquery;
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
		 elem: '#deviceBatchList'
			 	,id:"id"
	            ,url:'/market/device/batch/page'
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
							field: 'investorName',
							title: '所属投资商',
							width: 185
						}, {
							field: 'batchNo',
							title: '批次编号',
							sort: true,
							width: 150
						}, {
							field: 'batchName',
							title: '批次名称',
							width: 180
						}, {
							field: 'createTime',
							title: '添加时间',
							sort: true,
							width: 170
						}, {
							field: 'remarks',
							title: '备注',
							width: 180
						}, {
							fixed: 'right',
							title: '操作',
							width: 255,
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
		 		,content: '../../../market/device/batch/edit'
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
	 table.on('tool(deviceBatchTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'detail') {
			 vip_tab.add(parent.layer.element, "设备信息维护 | "+data.batchName, "/market/devices/index/"+data.id+"/"+data.batchName);
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
			 		,content: '../../../market/device/batch/edit?id='+data.id
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/market/device/batch/"+data.id,
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
