var pageCurr;
layui.use('table', function(){
	var form = layui.form,table = layui.table,
		layer = layui.layer,vipTable = layui.vip_table,
		$ = layui.jquery,deviceBatchId = $("#hiddenDeviceBatchId").val();
	 tableIns=table.render({
		 		elem: '#deviceList'
		 		,id:'tableReload'
	            ,url:'/market/devices/page?deviceBatchId='+deviceBatchId
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
	            	 {checkbox: true, sort: true, fixed: true, space: true},
	                 {
							field: 'machNo',
							title: '设备编码',
							width: 155
						}, {
							field: 'deviceSequence',
							title: '序列号',
							width: 155
						}, {
							field: 'deviceBatchName',
							title: '所属批次',
							width: 120
						}, {
							field: 'setupTime',
							title: '安装时间',
							sort: true,
							width: 120
						}, {
							field: 'setupAddress',
							title: '安装地址',
							width: 180
						}, {
							field: 'investorName',
							title: '投资商',
							width: 120
						}, {
							field: 'traderName',
							title: '商家',
							width: 120 							
						}, {
							field: 'salerName',
							title: '促销员',
							width: 120 							
						}, {
							field: 'createTime',
							title: '添加时间',
							sort: true,
							width: 165 							
						}, {
							fixed: 'right',
							title: '操作',
							width: 150,
							align: 'center',
							toolbar: '#barOption'
						}
	            ]]
		,  done: function(res, curr, count){
			 pageCurr=curr;
		}
	 });
	//单击行勾选checkbox事件
	 $(document).on("click",".layui-table-body table.layui-table tbody tr", function () {
	     var index = $(this).attr('data-index');
	     var tableBox = $(this).parents('.layui-table');
	     //存在固定列
	     if (tableBox.find(".layui-table-fixed.layui-table-fixed-l").length>0) {
	         tableDiv = tableBox.find(".layui-table-fixed.layui-table-fixed-l");
	     } else {
	         tableDiv = tableBox.find("tbody");
	     }
	     var checkCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-checkbox div.layui-form-checkbox I");
	     if (checkCell.length>0) {
	         checkCell.click();
	     }
	 });

	 $(document).on("click", "td div.laytable-cell-checkbox div.layui-form-checkbox", function (e) {
	     e.stopPropagation();
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
		 		,content: '../../../devices/device/edit?deviceBatchId='+deviceBatchId
		 		,success: function(layero){
		 			var btn = layero.find('.layui-layer-btn');
		 			btn.find('.layui-layer-btn0').attr({href: '',target: '_blank'});
		 			}
		 		});
		 	},
		 binding: function() {
			  var checkStatus = table.checkStatus('tableReload')
		      ,data = checkStatus.data;
		      if(data.length == 0) {
		    	  layer.msg("请选择需要绑定的数据", {
						 time: 1000
						,icon : 7
						,shade :0.3
						,anim: 5
					});
		      } else if(data.length>1) {
		    	  layer.msg("您选择了多条数据", {
						 time: 1000
						,icon : 7
						,shade :0.3
						,anim: 5
					});
		      } else {
		    	  var id = data[0].id;
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
		 	 		,content: '../../../devices/device/edit?id='+id+"&deviceBatchId="+deviceBatchId
		 	 	});
		      }
		 },
		 upload: function() {
			//页面层
			 layer.open({
				   type: 1,
				   title:'批量导入',
				   skin: 'layui-layer-rim', //加上边框
				   area: ['420px', '240px'], //宽高
				   shade: 0.8,
				   id: 'LAY_layuipro',
				   moveType: 1,
				   content: '<div class="upload"> ' +
								   '<div class="layui-upload" style="margin-top:5em">' +
									   '<button type="button" class="layui-btn layui-btn-normal" id="test8">选择文件</button>' +
									   '&nbsp;<button type="button" class="layui-btn" id="test9">开始导入</button>' +
								   '</div>' +
						     '</div>',
			       success: function(layero){
			    	   layui.use(['upload'], function() {
			    			upload = layui.upload;
			    			upload.render({
			    			    elem: '#test8'
			    			    ,url: '../../../devices/batchImport/'+deviceBatchId
			    			    ,size: 10240
			    			    ,auto: false
			    			    ,multiple: true
			    			    ,bindAction: '#test9'
			    			    ,accept:'file'
			    			    ,exts:'xls|xlsx|csv'
			    			    ,done: function(data){
			    			    	if (data.code == 200) {
			    						layer.msg('保存成功！', {
			    							icon : 1,
			    							time : 1000
			    						}, function() {
			    							layer.closeAll();
			    							$(".layui-laypage-btn").click();
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
			    			});
			    		});
			       }
		    });
		 }
	 };
	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 layui.jquery('#binding .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
	 });
	 layui.jquery('#uploadExcel .layui-btn').on('click', function(){
		 	var othis = $(this), method = othis.data('method');
		 	active[method] ? active[method].call(this, othis) : '';
	 });
	 table.on('tool(deviceTable)',function(obj){
		 var data = obj.data,layEvent = obj.event;
		 if(layEvent === 'qrcode') {
				 layer.open({
			 		 type: 2
			 		,title: '查看二维码信息 [' + data.machNo + ']'//显示标题栏
			 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
			 		,skin: 'layui-layer-rim' //加上边框
			 		,area: ['570px', '410px'] //宽高
			 		,maxmin: true
			 		,shade: 0.8
			 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			 		,btnAlign: 'c'
			 		,moveType: 1 //拖拽模式，0或者1
			 		,content: '../../../devices/view/qrcode?machNo='+data.machNo+'&deviceSequence='+data.deviceSequence
			 	});
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
			 		,content: '../../../devices/device/edit?id='+data.id+"&deviceBatchId="+deviceBatchId
			 	});
			} else if(layEvent === 'del') {
				layer.confirm('确定删除', function(index) {
					//请求
					$.ajax({
						url:"/market/devices/device"+data.id,
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
