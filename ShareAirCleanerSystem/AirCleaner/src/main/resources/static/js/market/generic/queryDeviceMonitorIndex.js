/*layui.use(['tree'], function(){
	var $ = layui.jquery
    layui.tree({
        elem: '#demo' //传入元素选择器
        ,target:'bodyFrame'
        ,nodes: [{ //节点
            name: '设备监控'
            ,spread:true
            ,children: [{
                name: '频率使用率监控',
                href:'/welcome'
            },{
                name: '设备滤芯监控',
                href:'/welcome'
            }]
        }]
        ,click:function(item) {
        	var name = item.name
        	console.log(name)
        	$("#name").text(name)
        }
    });
});*/
layui.use(['tree', 'layer'], function(){
	  var layer = layui.layer
	  ,$ = layui.jquery; 
	  
	  layui.tree({
	    elem: '#demo' //指定元素
	    ,target: 'bodyFrame' //是否新选项卡打开（比如节点返回href才有效）
	    ,click: function(item){ //点击节点回调
	      layer.msg('当前节名称：'+ item.name + '<br>全部参数：'+ JSON.stringify(item));
	      console.log(item);	      
	    }
	  ,nodes: [{ //节点
        name: '设备监控'
        ,id:'node1'
        ,href:'/welcome'
        ,spread:true
        ,children: [{
            name: '频率使用率监控',
            id:'node2',
            href:'/market/devices/addRate'
        },{
            name: '设备滤芯监控',
            id:'node3',
            href:'/market/devices/addChip'
        }]
    }]
	  
	});
}); 