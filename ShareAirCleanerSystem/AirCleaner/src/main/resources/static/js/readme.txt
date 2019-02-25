js文件夹

//load加载
var index = layer.load(2, {time: 10*1000,content:'加载中',success: function (layero){
 layero.find('.layui-layer-content').css({
            'padding-left': '39px',
            'padding-top': '5px',
            'width': '100px'
        });
} }); 
//关闭
layer.close(index);  