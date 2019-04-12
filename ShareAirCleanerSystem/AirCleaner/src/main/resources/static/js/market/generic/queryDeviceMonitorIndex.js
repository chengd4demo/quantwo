layui.use(['tree'], function(){
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
    });
});