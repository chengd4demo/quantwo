//扫码统计
var pageCurr;
layui
		.use(
				[ 'table', 'form', 'layer', 'vip_table', 'laydate' ],
				function() {
					var table = layui.table, form = layui.form, layer = layui.layer, vipTable = layui.vip_table, laydate = layui.laydate, $ = layui.jquery;
					//日期
					laydate.render({
						elem : '#date'
					});

				});

/**
 *查询 
 */
layui.use([ 'form' ], function() {
	var form = layui.form, layer = layui.layer;
	//监听搜索框
	form.on('submit(searchSubmit)', function(data) {
		//重新加载table
		document.getElementById('lableSweeptId').style.display='block';
		load(data);
		return false;
	});
});
function load(obj) {
	var myChart = echarts.init(document.getElementById('chartmain'));
	//扫码量
	var option = {
		title : {
			text : '扫码量',
			textStyle : {
				fontWeight : 'normal',
				fontFamily : 'serif',
				fontSize : 20,
				lineHeight : 50
			},
			x : '700',
		},
		backgroundColor : '#fff',
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			icon : "line",
			itemWidth : 9, // 设置宽度
			itemHeight : 9, // 设置高度
			itemGap : 10, // 设置间距
			x : 'center', // 'center' | 'left' | {number},     
			y : 'bottom', // 'center' | 'bottom' | {number}
			data : [ 'service01', 'service02', 'service03', 'service04',
					'service05' ]
		},
		grid : {
			left : '3%',
			right : '4%',
			bottom : '10%',
			containLabel : true
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			}
		},
		xAxis : {
			type : 'category',
			boundaryGap : false,
			data : [ '2018-08-01', '2018-08-02', '2018-08-03', '2018-08-04',
					'2018-08-05', '2018-08-06', '2018-08-07' ]
		},
		yAxis : {
			type : 'value',
			axisLine : {
				show : false
			},
			axisTick : {
				show : false
			},
			splitLine : {
				show : true,
				lineStyle : {
					type : 'dashed'
				}
			}

		},
		series : [ {
			name : 'service01',
			type : 'line',
			stack : '总量',
			data : [ 120, 132, 101, 134, 90, 230, 210 ]
		}, {
			name : 'service02',
			type : 'line',
			stack : '总量',
			data : [ 220, 182, 191, 234, 290, 330, 310 ]
		}, {
			name : 'service03',
			type : 'line',
			stack : '总量',
			data : [ 150, 232, 201, 154, 190, 330, 410 ]
		}, {
			name : 'service04',
			type : 'line',
			stack : '总量',
			data : [ 320, 332, 301, 334, 390, 330, 320 ]
		}, {
			name : 'service05',
			type : 'line',
			stack : '总量',
			data : [ 820, 932, 901, 934, 1290, 1330, 1320 ]
		} ]
	};

	myChart.setOption(option);
}