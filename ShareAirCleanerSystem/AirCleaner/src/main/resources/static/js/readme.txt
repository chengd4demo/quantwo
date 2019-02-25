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

//计算方法调用时间
long startTime=System.currentTimeMillis();
float excTime=(float)(startTime-startTime)/1000;
System.out.println("findCashApplyCount方法开始执行时间："+excTime+"s");

long endTime=System.currentTimeMillis();
excTime=(float)(endTime-startTime)/1000;
System.out.println("findCashApplyCount方法耗时执行时间："+excTime+"s");