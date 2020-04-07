$(function(){
    var chart = Highcharts.chart('container',{
        chart: {
            type: 'xrange'
        },
        title: {
            text: '资源负载图'
        },
        scrollbar:{
            enabled:true //是否产生滚动条
        },
        animation: true,
        xAxis: {
            title: {
                text: '每日工作分布'
            },
            min:0,
            max:24,
            tickInterval:1
        },
        yAxis: {
            title: {
                text: '日期'
            },
            reversed: true,
            categories: []
        },
        tooltip: {
            valueSuffix: '时'
        },
        credits: {
            enabled: false//除去highcharts.com
        },
        series: [
            {
                name: '已预定',
                borderColor: 'blue',
                pointWidth: 20,
                data: []
            },
            {
                name: '计划中',
                borderColor: 'yellow',
                pointWidth: 20,
                data: []
            },
            {
                name: '计划完成',
                borderColor: 'red',
                pointWidth: 20,
                data: [],
            }]
    });
    $('#btnQueryLoad').click(function () {
        var startTime=$('#startTime').datebox('getValue');
        var endTime=$('#endTime').datebox('getValue');
        if (startTime==null||startTime==""||endTime==null||endTime==""){
            return;
        }
        var resourceNo=GetQueryString("resourceNo");
        $.ajax({
            url: "/WF/resource/getResourceLoad",
            type: 'POST',
            data: {
                startTime:startTime,
                endTime:endTime,
                resourceNo:resourceNo
            },
            success: function (data) {
                chart.yAxis[0].update({
                    categories: data.categories
                });
                chart.series[0].update({
                    data:data.bookPlan
                });
                chart.series[1].update({
                    data:data.book
                });
                chart.series[2].update({
                    data:data.finishPlan
                });
            },
            error: function (data) {
                console.log("error"+data);
            }
        });
    });
});


