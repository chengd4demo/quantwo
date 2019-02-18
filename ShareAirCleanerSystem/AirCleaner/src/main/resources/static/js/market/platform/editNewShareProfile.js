var pageCurr;
layui.config({
    base: '../../../frame/echarts/',
})
layui.use(['treetable','form','table','laypage', 'layer'],function(){
    var data=[{"id":1,"pid":0,"title":"成都圈兔公司"},{"id":2,"pid":0,"title":"1-2"},{"id":3,"pid":0,"title":"1-3"},{"id":4,"pid":1,"title":"郑州分公司"},{"id":5,"pid":1,"title":"重庆分公司"},{"id":6,"pid":2,"title":"1-2-1"},{"id":7,"pid":2,"title":"1-2-3"},{"id":8,"pid":3,"title":"1-3-1"},{"id":9,"pid":3,"title":"1-3-2"},{"id":10,"pid":4,"title":"郑州客务中心"},{"id":11,"pid":4,"title":"1-1-1-2"}];
    var o = layui.$,treetable = layui.treetable;
    var form = layui.form,layer = layui.layer;
    var laypage = layui.laypage,
    layer = layui.layer,
    table = layui.table,layer = layui.layer,
    vipTable = layui.vip_table,
		$ = layui.jquery;
    tableIns=treetable.render({
        elem: '#test-tree-table',
        data: data,
        field: 'title',
        is_checkbox: true,
        checked:[1,2,3,4],
        
        /*url:''
    	,method: 'GET' //默认：get请求

        ,page: true
        ,limits: [20, 30, 50, 100, 200]
        ,limit:20
        ,request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'limit' //每页数据量的参数名，默认：limit
        },*/
        
        /*icon_val: {
            open: "&#xe619;",
            close: "&#xe61a;"
        },
        space: 4,*/
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
            /*{
                title: '状态',
                width: '20%',
                template: function(item){
                    return '<input type="checkbox" lay-skin="switch" lay-filter="status" lay-text="开启|关闭">';
                }
            },*/
            {
                field: 'type',
                title: '类型',
                width: '20%',
            },
            {
                field: 'distributionRatio',
                title: '分润比例',
                width: '10%',
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
        /*,done: function(res, curr, count){
			pageCurr=curr;
		}*/
    });

    treetable.on('treetable(add)',function(data){
        layer.msg('添加下级');
        console.dir(data);
    })

    treetable.on('treetable(edit)',function(data){
        layer.msg('编辑操作');
        console.dir(data);
    })
    treetable.on('treetable(del)',function(data){
        layer.msg('删除操作');
        console.dir(data);
    })

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
      ,count: data.length
      ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
      /*,jump: function(obj){
    	//渲染
          document.getElementById('test-tree-table').innerHTML = function(){
            var arr = []
            ,thisData = data.concat().splice(obj.curr*obj.limit - obj.limit, obj.limit);
            layui.each(thisData, function(index, item){
              arr.push('<li>'+ item +'</li>');
            });
            return arr.join('');
          }();
        console.log(obj)
      }*/
    });
})

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