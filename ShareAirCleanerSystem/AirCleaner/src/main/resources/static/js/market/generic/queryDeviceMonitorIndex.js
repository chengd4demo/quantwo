layui.use(['tree','element'], function(){
	var $ = layui.jquery
    layui.tree({
        elem: '#demo' //传入元素选择器
        ,target:'bodyFrame'
        ,nodes: [{ //节点
            name: '设备监控'
            ,spread:true
            ,children: [{
                name: '频率使用率监控',
                href:'/market/device/rate/index'
            },{
                name: '设备滤芯监控',
                href:'/market/device/chip/index'
            }]
        }]
        ,click:function(item) {
        	var name = item.name;
        	//console.log(name)
        	$("#name").text(name);
        }
    });
});
