function initResourceKindGant(resourceNo) {
    $.ajax({
        url: "/WF/resource/getResourceKindGant",
        type: 'POST',
        data: {
            resourceNo: resourceNo
        },
        success: function (resData) {
            map = Highcharts.map;
            dateFormat = Highcharts.dateFormat,
            series = resData.series.map(function (car, i) {
                var data = car.deals.map(function (deal) {
                    return {
                        id: deal.id,
                        start: deal.start,
                        end: deal.end,
                        resId:deal.resId,
                        y: i
                    };
                });
                return {
                    name: car.model,
                    data: data,
                    current: car.deals[car.current],
                    events:{
                        click: function(e) {
                            var taskId=e.point.options.id;
                            var resId=e.point.options.resId;
                            initResourceItemGant(resId);
                            initResourceLoadHistory(resId);
                        }
                    }
                };
            });
            Highcharts.ganttChart('containerKind', {
                series: series,
                title: {
                    text: '资源甘特图（资源实例角度）'
                },
                //导航栏
                navigator: {
                    enabled: true,
                    series: {
                        type: 'gantt',
                        pointPlacement: 0.5,
                        pointPadding: 0.25
                    },
                    yAxis: {
                        min: 0,
                        max: 3,
                        reversed: true,
                        categories: []
                    }
                },
                scrollbar: {
                    enabled: true
                },
                credits: {
                    enabled: false
                },
                rangeSelector: {
                    enabled: true,
                    selected: 0
                },
                tooltip: {
                    pointFormatter: function (){
                        var point = this;
                        format = '%Y-%m-%d %H';
                        var start=dateFormat(format, point.start);
                        var end=dateFormat(format, point.end);
                        return '<span>节点任务:'+ point.id+'</span><br/><span>开始时间:'+ start+'</span><br/><span>终止时间:'+end+'</span>'
                    }
                },
                xAxis: {
                    currentDateIndicator: true
                },
                yAxis: {
                    type: 'category',
                    grid: {
                        columns: [{
                            title: {
                                text: '资源实例'
                            },
                            categories: map(series, function (s) {
                                return s.name;
                            })
                        }]
                    }
                }

            });
        },
        error: function (data) {
            console.log("error" + data);
        }
    })
}
function initResourceItemGant(resourceId) {
    $.ajax({
        url: "/WF/resource/getResourceItemGant",
        type: 'POST',
        data: {
            resourceNo: resourceId
        },
        success: function (resData) {
            map = Highcharts.map;
            dateFormat = Highcharts.dateFormat;
            series = resData.series.map(function (car, i) {
                var data = car.deals.map(function (deal) {
                    return {
                        id: deal.id,
                        start: deal.start,
                        end: deal.end,
                        y: i
                    };
                });
                return {
                    name: car.model,
                    data: data,
                    current: car.deals[car.current],
                    events:{
                        click: function(e) {
                            var id=e.point.options.nTaskId;
                            gotoNodeTaskDetail(id);
                        }
                    }
                };
            });
            Highcharts.ganttChart('containerItem', {
                series: series,
                title: {
                    text: '资源甘特图（项目维度）'
                },
                //导航栏
                navigator: {
                    enabled: true,
                    series: {
                        type: 'gantt',
                        pointPlacement: 0.5,
                        pointPadding: 0.25
                    },
                    yAxis: {
                        min: 0,
                        max: 3,
                        reversed: true,
                        categories: []
                    }
                },
                scrollbar: {
                    enabled: true
                },
                credits: {
                    enabled: false
                },
                rangeSelector: {
                    enabled: true,
                    selected: 0
                },
                tooltip: {
                    pointFormatter: function (){
                        var point = this;
                        format = '%Y-%m-%d %H';
                        var start=dateFormat(format, point.start);
                        var end=dateFormat(format, point.end);
                        return '<span>节点任务:'+ point.id+'</span><br/><span>开始时间:'+ start+'</span><br/><span>终止时间:'+end+'</span>'
                    }
                },
                xAxis: {
                    currentDateIndicator: true
                },
                yAxis: {
                    type: 'category',
                    grid: {
                        columns: [{
                            title: {
                                text: '项目编码'
                            },
                            categories: map(series, function (s) {
                                return s.name;
                            })
                        }]
                    }
                }

            });
        },
        error: function (data) {
            console.log("error" + data);
        }
    })
}

function initResourceLoadAve(resourceNo) {
    $.ajax({
        url: "/WF/resource/getResourceLoadAve",
        type: 'POST',
        data: {
            resourceNo: resourceNo
        },
        success: function (data) {
            var dom = document.getElementById("containerAve");
            var aveChart = echarts.init(dom);

            option = {
                title: {
                    text: '平均负载',
                    left: 'center'
                },

                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                xAxis: [
                    {
                        type: 'category',
                        name:'资源实例',
                        data: data.dataAxis,
                        axisPointer: {
                            type: 'shadow'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '负载率',
                        interval: 50,
                        axisLabel: {
                            formatter: '{value} ‰'
                        }
                    }
                ],
                series: [
                    {
                        type: 'bar',
                        data: data.data
                    }
                ]
            };
            aveChart.setOption(option);
            aveChart.on("click",function (param) {
                var resId=param.name;
                initResourceLoadHistory(resId);
            });
        }
    });

}
function initResourceLoadHistory(resourceId) {
    $.ajax({
        url: "/WF/resource/getResourceLoadDaily",
        type: 'POST',
        data: {
            resourceId: resourceId
        },
        success: function (data) {
            var dom = document.getElementById("containerLoadHistory");
            var historyChart = echarts.init(dom);
            var option = {
                title: {
                    text: '每日负载/‰',
                    left: 10
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: false
                        },
                        saveAsImage: {
                            pixelRatio: 2
                        }
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                grid: {
                    bottom: 90
                },
                dataZoom: [{
                    type: 'inside'
                }, {
                    type: 'slider'
                }],
                xAxis: {
                    data: data.categoryData,
                    silent: false,
                    splitLine: {
                        show: false
                    },
                    splitArea: {
                        show: false
                    }
                },
                yAxis: {
                    splitArea: {
                        show: false
                    }
                },
                series: [{
                    type: 'bar',
                    data: data.valueData,
                    // Set `large` for large data amount
                    large: true
                }]
            };
            historyChart.setOption(option);
        }
    });

}

