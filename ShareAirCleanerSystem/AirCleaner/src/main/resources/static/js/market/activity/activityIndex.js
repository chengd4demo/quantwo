layui.use(['form','element', 'layedit', 'laydate'], function(){
	 var $ = layui.jquery
     , element = layui.element
     ,layer = layui.layer
     ,layedit = layui.layedit
     ,laydate = layui.laydate;
	  
	  //获取hash来切换选项卡，假设当前地址的hash为lay-id对应的值
	  var layid = location.hash.replace(/^#activity=/, '');
	  element.tabChange('activity', layid); //假设当前地址为：http://a.com#test1=222，那么选项卡会自动切换到“发送消息”这一项
	  
	  //监听Tab切换，以改变地址hash值
	  element.on('tab(activity)', function(elem){
	    location.hash = 'activity='+ this.getAttribute('lay-id');
	  });
	  
  	  //模拟点击
	  setTimeout(function() {
		 // IE
		 if(document.all) {
			 document.getElementById("clickMe").click();
		 }
		 // 其它浏览器
		 else {
			 var e = document.createEvent("MouseEvents");
			 e.initEvent("click", true, true);
			 document.getElementById("clickMe").dispatchEvent(e);
		 }
	  });
	 
});