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
    	var obj = treetable.all('checked');
    	if (checkParent(obj)) {
    		var id = obj.data[0].id;
    		parent.parent.layui.$.find('iframe')[2].contentWindow.layui.$.find('iframe')[0].contentWindow.document.getElementById('distributionRatioId').value = id
    		var index = parent.parent.parent.layer.getFrameIndex(window.name);
    		var showRatioValue = getShowRatioValue(obj.data)
    		parent.parent.layui.$.find('iframe')[2].contentWindow.layui.$.find('iframe')[0].contentWindow.document.getElementById('ratioId').value = showRatioValue
    		parent.parent.parent.layer.close(index)
    	} 
//        console.dir(treetable.all('checked'));
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
    treetable.on('tree(del)',function(data){
        layer.msg('删除操作');
        layer.confirm('确定删除', function(index) {
        	console.dir(data);
			//请求
			$.ajax({
				url:"/market/platform/"+data.item.id,
				async:false,
				success:function(data){
					if(data.code==200){
						layer.msg('删除成功！',{time:500},function(){
							layer.close(index);
							return false;
	            		 });
					}else {
						layer.msg(data.msg);
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
	function checkParent(obj){
    	var pidArray = new Array();
    	if (obj.data.length == 0) {
    		 layer.msg('请选择分润比例');
    		 return false;
    	} else {
    		var data = obj.data;
    		$.each(data,function(){
    			if(this.pid == '0')
    			pidArray.push(this.pid);
    		})
    		return checkPidMultiple(pidArray);
    	}
    }
    
    function checkPidMultiple(parame){
    	if(parame.length >1) {
    		 layer.msg('最多只能选择1条数类型为【公司】的数据');
    		 return false;
    	} else if(parame.length == 0) {
    		 layer.msg('至少选择1条数类型为【公司】的数据');
    		 return false;
    	}
    	return true;
    }
    function research(){
    	load(req);
		tableIns.data.push(research())
		treetable.render(tableIns)
    }
    
    function getShowRatioValue(parame) {
    	console.log(parame)
    	//公司：30%，代理：10%，投资商：30% (耗材费：0.09/h)，商家：50%
    	var result='',cyName='',dlName='',irName='',zdName='',trName='',srName='',otherName=''
    	$.each(parame,function(){
    		if(this.type =='CY') {
        		cyName = this.title;
        		cyName += ':' + this.scale + '% '
        	} else if(this.type =='ZD') {
        		zdName = this.title;
        		zdName += ':' + this.scale + '% '
        	} else if(this.type =='DL') {
        		dlName = this.title;
        		dlName += ':' + this.scale + '% '
        	}  else if(this.type =='IR') {
        		irName = this.title;
        		irName += ':' + this.scale + '% '
        	} else if(this.type =='TR') {
    			trName = this.title;
    			trName += ':' + this.scale + '% '
        	} else if(this.type =='SR') {
        		srName = this.title;
        		srName += ':' + this.scale + '% '
        	} else {
        		otherName = this.title;
        		otherName + ':' + this.scale + '%'
        	}
		})
		if(cyName != undefined) {
			result += cyName
		}
    	if(zdName != undefined) {
			result += zdName
		}
    	if(dlName != undefined) {
			result += dlName
		}
    	if(irName != undefined) {
			result += irName
		}
    	if(trName != undefined) {
			result += trName
		}
    	if(srName != undefined) {
			result += srName
		}
		console.log(result)
		return result;
    }
	 	 	    
})

