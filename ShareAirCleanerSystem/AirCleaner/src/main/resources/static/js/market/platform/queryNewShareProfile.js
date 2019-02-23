var pageCurr;
layui.config({
    base: '../../../frame/echarts/',
})
layui.use(['treetable','form','table','laypage', 'layer'],function(){
	var data=[{"id":1,"pid":"0","title":"成都圈兔公司"}];
	var o = layui.$,treetable = layui.treetable;
    var form = layui.form,layer = layui.layer;
    var laypage = layui.laypage,
    layer = layui.layer,
    table = layui.table,layer = layui.layer,
    vipTable = layui.vip_table,
	$ = layui.jquery;
    var req = {page:1,limit:20};
    var pageCount = 0
    function load(parame) {
    	  $.ajax({
    			 type:"POST",
    			 async:false,
    			 url:"../../../market/platform/query?page="+parame.page+"&limit="+parame.limit+"", 
    			 contentType: 'application/json',
    			 data : JSON.stringify(parame),
    			 success:function(res){
    				 if (res) {
    					data = res.data
    					pageCount = res.pageCount
    				 }
    			 },
    			 error: function(errorMsg){
    				return new Array();
    			 }
    		 });
    }
    load(req)
    tableIns=treetable.render({
        elem: '#test-tree-table',
        data: data,
        field: 'title',
        is_checkbox: true,
        checked:[1,2,3,4],
        cols: [
            {
                field: 'title',
                title: '上级',
                width: '30%',
                template: function(item){
                    if(item.level == 1){
                        return '<span style="color:red;">'+item.title+'</span>';
                    }
                    if(item.level == 2){
                        return '<span style="color:green;">'+item.title+'</span>';
                    }
                    return item.title;
                }
            },
            {
                field: 'name',
                title: '名称',
                width: '20%'
            },
            {
                field: 'type',
                title: '类型',
                width: '20%',
                template:function(item) {
                	if(item.type =='TR') {
                		return '商家';
                	} else if(item.type =='IR') {
                		return '投资商';
                	} else if(item.type =='CY') {
                		return '公司';
                	} else if(item.type =='SR') {
                		return '促销员';
                	} else if(item.type =='ZD') {
                		return '区域总代';
                	} else if(item.type =='DL') {
                		return '代理';
                	} else {
                		return '其它'
                	}
                }
                
            },
            {
                field: 'scale',
                title: '分润比例',
                width: '10%',
                template:function(item) {
                	return item.scale + '%'
                }
            },
            {
                field: 'actions',
                title: '操作',
                width: '30%',
                template: function(item){
                    var tem = [];
                    //if(item.pid == 0){
                        tem.push('<a class="layui-icon layui-btn layui-btn-mini layui-btn-normal" lay-filter="add" lay-event="detail">&#xe654;</a>');
                    //}   
                    tem.push('<a class="layui-btn layui-btn-mini layui-btn-normal" lay-filter="edit">编辑</a>');                  
                    //if(item.pid > 0){
                        tem.push('<a class="layui-btn layui-btn-mini layui-btn-danger" lay-filter="del">删除</a>');
                    //}  
                    return tem.join(' <font></font> ')
                },
            },
        ]
    });
        
    o('.up-all').click(function(){
        treetable.all('up');
    })

    o('.down-all').click(function(){
        treetable.all('down');
    })

    o('.get-checked').click(function(){
        console.dir(treetable.all('checked'));
    })

    form.on('switch(status)',function(data){
        layer.msg('监听状态操作');
        console.dir(data);
    })
    
    //分页
    laypage.render({
        elem: 'demo7'
       ,count: pageCount
       ,limit:20
       ,first: '首页'
       ,last: '尾页'
       ,prev: '<em>←</em>'
       ,next: '<em>→</em>'
       ,jump: function(obj, first){
               start = 1 + obj.limit * (obj.curr-1);
               end = obj.curr*obj.limit;
               if(!first){
            	   queryDeviceUsedData(dateType,traderIds,start,end);
               }
           }
     });
    
    //新增
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
	 		,content: '../../../market/platform/edit'
	 		,success: function(layero){
	 			var btn = layero.find('.layui-layer-btn');
	 			btn.find('.layui-layer-btn0').attr({href: '',target: '_blank'});
	 			}
	 		});
	 	}
	  };
    
    //添加
    treetable.on('treetable(add)',function(data){
        layer.open({
	 		type: 2
	 		,title: '添加 '//显示标题栏
	 		,closeBtn: 1 //显示关闭按钮 属性0，1，2
	 		,skin: 'layui-layer-rim' //加上边框
	 		,area: ['570px', '410px'] //宽高
	 		,maxmin: true
	 		,shade: 0.8
	 		,id: 'LAY_layuipro' //设定一个id，防止重复弹出
	 		,btnAlign: 'c'
	 		,moveType: 1 //拖拽模式，0或者1
	 		,content: '../../../market/platform/edit'
	 		,success: function(layero){
	 			var btn = layero.find('.layui-layer-btn');
	 			btn.find('.layui-layer-btn0').attr({href: '',target: '_blank'});
	 		}
	 	});
    })
    
    //编辑
    treetable.on('treetable(edit)',function(data){
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
	 		,content: '../../../market/platform/edit'
	 		,success: function(layero){
	 			var btn = layero.find('.layui-layer-btn');
	 			btn.find('.layui-layer-btn0').attr({href: '',target: '_blank'});
	 		}
	 	});
    })
    
    //删除
    treetable.on('treetable(del)',function(data){
        layer.confirm('确定删除', function(index) {
			//请求
			$.ajax({
				url:"/market/platform/"+data.id,
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
    })
    
    //新增按钮监听
	 layui.jquery('#addbtn .layui-btn').on('click', function(){
	 	var othis = $(this), method = othis.data('method');
	 	active[method] ? active[method].call(this, othis) : '';
	 });
	 
	//监听搜索框
	form.on('submit(searchSubmit)', function(data){
		//重新加载table
		load(data);
		return false;
	});
	 	 	    
})

