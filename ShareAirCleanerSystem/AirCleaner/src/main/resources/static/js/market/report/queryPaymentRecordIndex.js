//支付统计
var pageCurr;
layui.use(['form', 'layer', 'vip_table', 'laydate' ],function() {
	var table = layui.table, form = layui.form, layer = layui.layer, vipTable = layui.vip_table, laydate = layui.laydate, $ = layui.jquery;
	laydate.render({
		elem : '#date1',
	});
	laydate.render({
		elem : '#date2',
	});
	form.on('select(type)', function(data){
		var type = data.value
		$('#timeStartDom').empty();
		$('#timeEndDom').empty();
		var timeStartDom = '<div class="layui-form-item"><label class="layui-form-label" style="word-break: keep-all;">开始时间:</label><div class="layui-input-block"><input type="text" id="date1" class="layui-input" name="timeStart" lay-verify="required" placeholder="请选择时间"></div></div>';
		$('#timeStartDom').append(timeStartDom);
		var timeEndDom = '<div class="layui-form-item"><label class="layui-form-label" style="word-break: keep-all;">结束时间:</label><div class="layui-input-block"><input type="text" id="date2" class="layui-input" name="timeEnd" lay-verify="required" placeholder="请选择时间"></div></div>';
		$('#timeEndDom').append(timeEndDom)
		form.render(); 
		if ("day" == data.value){
			laydate.render({
				elem : '#date1',
				type: 'date'
			});
			laydate.render({
				elem : '#date2',
				type: 'date'
			});
		} else if("week" == type) {
			laydate.render({
				elem : '#date1',
				type: 'date'
			});
			laydate.render({
				elem : '#date2',
				type: 'date'
			});
		} else if("month" == type) {
			laydate.render({
				elem : '#date1',
				type: 'month'
			});
			laydate.render({
				elem : '#date2',
				type: 'month'
			});
		} else if("year" == type) {
			
			laydate.render({
				elem : '#date1',
				type: 'year'
			});
			laydate.render({
				elem : '#date2',
				type: 'year'
			});
		}
	})
	//监听搜索框
	form.on('submit(searchSubmit)', function(data) {
		//重新加载table
		console.log(data);
		console.log(document.getElementById('date1').value)
		console.log(document.getElementById('date2').value)
		var data1 = document.getElementById('date1').value
		var date2 = document.getElementById('date2').value
		var traderId = document.getElementById('traderId')
		var index = traderId.selectedIndex
		traderId = traderId.options[index].value
		var type = document.getElementById('type')
		index = type.selectedIndex
		type = type.options[index].value
		if(count(data1,date2,traderId,type)){
			load(data);
			document.getElementById('lablePamentId');
		}
		return false;
	});

});

/**
 * 计算两个日期相差月份
 * 
 * @param startDate
 * @param endStart
 * @returns
 */
function getIntervalMonth(startDate,endDate){
	var startMonth = startDate.getMonth();
	var endMonth = endDate.getMonth();
	var intervalMonth = (endDate.getFullYear()*12+endMonth)-(startDate.getFullYear()*12+startMonth);
	return intervalMonth
}

/**
 * 计算两个日期之间的周数
 * 
 * @param startDate
 * @param endDate
 * @returns
 */
function getWeeksBetw(startDate,endDate) {
	var dt1 = startDate.getTime();
	var dt2 = endDate.getTime();
	return parseInt(Math.abs(dt2 - dt1) / 1000 / 60 / 60 / 24 / 7);
}

/**
 * 计算两个日期相差多少年
 * @param startDate
 * @param endDate
 * @returns
 */
function getDateYearSub(startDate,endDate) {
	 var day = 24 * 60 * 60 *1000; 
	 startDate = new Date(startDate.getTime() - day);
	//获得各自的年、月、日
     var sY  = startDate.getFullYear();     
     var eY  = endDate.getFullYear();
     if(eY > sY) {
         return eY - sY;
     }
}

/**
 * 选择时间验证
 * 
 * @param timeStart
 * @param timeEnd
 * @param triderId
 * @param type
 * @returns
 */
function count(timeStart,timeEnd,triderId,type){
	if(timeStart.length==0 || timeEnd.length == 0) {
		alert('请选择时间范围')
		return false
	}
    var date1=new Date(timeStart.replace(/-/g, ","));
    var date2=new Date(timeEnd.replace(/-/g, ","));
    var date=0;
    if (type.length==0 || type=='day' ){
    	date = (date2.getTime()-date1.getTime())/(1000*60*60*24);/*不用考虑闰年否*/
    }  else if(type=='month') {
    	date = getIntervalMonth(date1,date2)
    } else if(type =='week') {
    	date = getWeeksBetw(date1,date2);
    } else if (type=='year') {
    	date = getDateYearSub(date1,date2)
    }
    if(date1 > date2){
    	alert('开始时间不能大于结束时间');
    	return false
    } else if (date>7 && (type=='day' || type.length==0)){
    	alert('选择日期按【天】,时间范围不能大于7天');
    	return false
    } else if(date>7 && (type=='month' ||  type.length==0)){
    	alert('选择日期按【月】,时间范围不能大于7个月');
    	return false
    }  else if(date>7 && (type=='week'  || type.length==0)){
    	alert('选择日期按【周】,时间范围不能大于7周');
    	return false
    }  else if(date>7 && (type=='year'  || type.length==0)){
    	alert('选择日期按【年】,时间范围不能大于7年');
    	return false
    } else {
    	return true
    }
}
//支付金额
function load(obj) {
	var myChart = echarts.init(document.getElementById('columnar'));
	var option = {
	    tooltip : {
	        trigger: 'axis',
	        //透明层显示超出方法1
	        position:function(p){
	        	return [p[0] + 10, p[1] - 350];
	        },
	        //透明层显示超出方法2
	        /*position: function (point, params, dom, rect, size) {
	        	  // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
	        	  // 提示框位置
	        	  var x = 0; // x坐标位置
	        	  var y = 0; // y坐标位置
	        	 
	        	  // 当前鼠标位置
	        	  var pointX = point[0];
	        	  var pointY = point[1];
	        	 
	        	  // 外层div大小
	        	  // var viewWidth = size.viewSize[0];
	        	  // var viewHeight = size.viewSize[1];
	        	 
	        	  // 提示框大小
	        	  var boxWidth = size.contentSize[0];
	        	  var boxHeight = size.contentSize[1];
	        	 
	        	  // boxWidth > pointX 说明鼠标左边放不下提示框
	        	  if (boxWidth > pointX) {
	        	    x = 5;
	        	  } else { // 左边放的下
	        	    x = pointX - boxWidth;
	        	  }
	        	 
	        	  // boxHeight > pointY 说明鼠标上边放不下提示框
	        	  if (boxHeight > pointY) {
	        	    y = 5;
	        	  } else { // 上边放得下
	        	    y = pointY - boxHeight;
	        	  }
	        	 
	        	  return [x, y];
	        	},*/
     
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	    },
	    title:{
			 text : '支付金额',
	         textStyle:{
	            fontWeight:'bolder',
	            fontFamily:'serif',
	            fontSize:20
	           
	         },
	         text:'支付金额',
	          x: 'center', 
	    },
	    legend: {
	       type: 'scroll',
	       x: 'center', // 'center' | 'left' | {number},     
	       y: 'bottom', // 'center' | 'bottom' | {number}
	       data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎','百度','谷歌','必应','其他','其他1','其他2','其他3','其他4','其他5','其他6','其他7','其他8','其他9','其他10','其他11']
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            axisLine:{
	                lineStyle:{
	                    color:'rgba(191, 191, 191, 191)'
	                }
	            },
	             axisLabel:{
	                color:'rgba(84, 84, 84)'
	            },
	            type : 'category',
	            data : ['周一','周二','周三','周四','周五','周六','周日']
	        }
	    ],
	    yAxis : [
	        {
	            axisLine: {show:false},
	            axisTick: {show:false},
	            splitLine: {
	                show: true,
	                lineStyle:{
	                    type:'dashed'
	                }
	            },
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'直接访问',
	            type:'bar',
	             stack: '搜索引擎',
	            data:[320, 332, 301, 334, 390, 330, 320,320, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'邮件营销',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[120, 132, 101, 134, 90, 230, 210, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'联盟广告',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[220, 182, 191, 234, 290, 330, 310, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'视频广告',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[150, 232, 201, 154, 190, 330, 410, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'搜索引擎',
	            type:'bar',
	             stack: '搜索引擎',
	            data:[862, 1018, 964, 1026, 1679, 1600, 1570, 332, 301, 334, 390, 330, 320],
	            markLine : {
	                lineStyle: {
	                    normal: {
	                        type: 'dashed'
	                    }
	                }
	            }
	        },
	        {
	            name:'百度',
	            type:'bar',
	            barWidth : 30,
	            stack: '搜索引擎',
	            data:[620, 732, 701, 734, 1090, 1130, 1120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'谷歌',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[120, 132, 101, 134, 290, 230, 220, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'必应',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[60, 72, 71, 74, 190, 130, 110, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他1',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他2',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他3',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他4',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他5',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他6',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他7',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他8',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他9',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他10',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        },
	        {
	            name:'其他11',
	            type:'bar',
	            stack: '搜索引擎',
	            data:[62, 82, 91, 84, 109, 110, 120, 332, 301, 334, 390, 330, 320]
	        }
	    ]
	}; 
	myChart.setOption(option);
}
