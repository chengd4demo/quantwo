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
			document.getElementById('lableApplyId');
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

function load(obj) {
//使用次数
var myChartBar = echarts.init(document.getElementById('frequency'));
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
    tooltip: {
      trigger: 'axis',
    },
    dataset: {
        source: [
            ['product', 'service1', 'service2', 'service3'],
            ['10-1', 43.3, 85.8, 93.7],
            ['10-2', 83.1, 73.4, 55.1],
            ['10-3', 86.4, 65.2, 82.5],
            ['10-4', 72.4, 53.9, 39.1],
            ['10-5', 72.4, 53.9, 39.1],
            ['10-6', 72.4, 53.9, 39.1],
            ['10-7', 72.4, 53.9, 39.1]
        ]
    },
    xAxis: {
        axisLine: {show:false},
        axisTick: {show:false},
        splitLine: {
            show: true,
            lineStyle:{
                type:'dashed'
            }
        }
    },
    yAxis: {
        type: 'category',
        axisLine:{
            lineStyle:{
                color:'rgba(191, 191, 191, 191)'
            }
        },
        axisLabel:{
            color:'rgba(84, 84, 84)'
        }
    },
    series: [
        {type: 'bar',barWidth:7},
        {type: 'bar',barWidth:7},
        {type: 'bar',barWidth:7}
    ]
  };
myChartBar.setOption(optionBar);
//使用率 
var myChartPie = echarts.init(document.getElementById('utilizationRate'));
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
        data: ['869300033638580','869300033639364','869300033639570','866289030052379','866289030086393','869300033631726','869300033638424']
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
            data:[
                {value:"400", name:'869300033638580'},
                {value:335, name:'869300033639364'},
                {value:310, name:'869300033639570'},
                {value:234, name:'866289030052379'},
                {value:135, name:'866289030086393'},
                {value:1548, name:'869300033631726'},
                {value:34, name:'869300033638424'}
            ],
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
