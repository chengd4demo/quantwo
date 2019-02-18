//设备使用统计
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
		var timeStartDom = '<div class="layui-form-item"><label class="layui-form-label" style="word-break: keep-all;">开始时间:</label><div class="layui-input-block"><input type="text" id="date1" class="layui-input" name="startTime" lay-verify="required" placeholder="请选择时间"></div></div>';
		$('#timeStartDom').append(timeStartDom);
		var timeEndDom = '<div class="layui-form-item"><label class="layui-form-label" style="word-break: keep-all;">结束时间:</label><div class="layui-input-block"><input type="text" id="date2" class="layui-input" name="endTime" lay-verify="required" placeholder="请选择时间"></div></div>';
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
		var data1 = document.getElementById('date1').value
		var date2 = document.getElementById('date2').value
		var traderId = document.getElementById('traderId')
		var index = traderId.selectedIndex
		traderId = traderId.options[index].value
		var type = document.getElementById('type')
		index = type.selectedIndex
		type = type.options[index].value
		if(count(data1,date2,traderId,type)){
			getData(data.field)
		}
		return false;
	});

});

/**
 * 获取展示数据
 * @param req
 * @returns
 */
function getData(req) {
	 $.ajax({
		 type:"POST",
		 async:false,
		 url:"../../../market/report/applay/query", 
		 contentType: 'application/json',
		 data : JSON.stringify(req),
		 success:function(data){
			 if (data) {
				 load(data);
			 }
		 },
		 error: function(errorMsg){
			 alert("图表请求数据失败!");
		 }
	 });
}

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

function load(obj) {

//使用次数
var myChartBar = echarts.init(document.getElementById('frequency'));
//使用率 
var myChartPie = echarts.init(document.getElementById('utilizationRate'));
if(obj.respBar == null) {
	myChartBar.clear();
	myChartPie.clear();
	alert('无相关数据')
	return
}
var optionBar = {
	    title:{
	        text: '使用次数',
	         textStyle:{
	            fontWeight:'normal',
	            fontFamily:'serif',
	            fontSize:20
	         },
	           x: 'center',
	    },
	    legend: {
	    	type: 'scroll',
	        icon: "rect",
	        itemWidth: 9,  // 设置宽度
	        itemHeight: 9, // 设置高度
	        itemGap: 10, // 设置间距
	        x: 'center', // 'center' | 'left' | {number},     
	        y: 'bottom', // 'center' | 'bottom' | {number}
	    },
		color: ['RGB(179,231,167)'],
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : obj.respBar.date,
	            axisLabel:{
	            	 formatter:function(value) {
	 	            	return value.substr(5)
	 	            }
	            },
	            axisTick: {
	                alignWithLabel: true
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'使用次数',
	            type:'bar',
	            barWidth: '25',
	            data:obj.respBar.series_data,
		        itemStyle: {
					normal: {
						label: {
							show: true, //开启显示
							position: 'top', //在上方显示
							textStyle: { //数值样式
								color: 'RGB(145,199,174)',
								fontSize: 12
							}
						}
					}
				}
	        }
	    ]
	};
myChartBar.setOption(optionBar);
function getSeries(devices) {
	var serie = {type: 'bar',barWidth:7}
	var seriesArray = null;
	if(devices == null) {
		return new Array(0);
	} else {
		seriesArray = new Array(devices.length);
	}
	for(var i in devices) {
		seriesArray.push(serie)
	}
	return seriesArray;
}
var optionPie = {
    title : {
        text: '使用率',
        textStyle:{
            fontWeight:'normal',
            fontFamily:'serif',
            fontSize:20
         },
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {d}%",
    },
    legend: {
        type: 'scroll',
        icon: "circle",
        itemWidth: 9,  // 设置宽度
        itemHeight: 9, // 设置高度
        itemGap: 10, // 设置间距
        x: 'center', // 'center' | 'left' | {number},     
        y: 'bottom', // 'center' | 'bottom' | {number}
        data: obj.respPie.legend_date
    },
    series : [
          {
            name: '使用率',
            type: 'pie',
            radius : '50%',
            center: ['50%', '50%'],
            label: {
                normal: {
                    show: true,
                    formatter: '{b}: {d}%'
                }
            },
            data:obj.respPie.series_date,
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
  }
  myChartPie.setOption(optionPie);
}
