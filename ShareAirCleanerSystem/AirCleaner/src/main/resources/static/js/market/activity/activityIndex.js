 layui.use(['form','element', 'layedit', 'laydate'], function () {
        var $ = layui.jquery
                , element = layui.element
                ,layer = layui.layer
                ,layedit = layui.layedit
                ,laydate = layui.laydate;
		$("#table1").load("../../../market/activity/set/index");
	$("#table2").load("../../../market/prizeItem/index");
		$("#table3").load("../../../market/prize/index");
		$("#table4").load("../../../market/queryWinning/index");
    });