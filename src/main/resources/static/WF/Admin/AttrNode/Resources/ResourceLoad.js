function initResourceKindGant(resourceNo) {
    $.ajax({
        url: "/WF/resource/getResourceKindGant",
        type: 'POST',
        data: {
            resourceNo: resourceNo
        },
        success: function (resData) {
            map = Highcharts.map;
            series = resData.series.map(function (car, i) {
                var data = car.deals.map(function (deal) {
                    debugger
                    return {
                        id: deal.id,
                        start: deal.start,
                        end: deal.end,
                        y: i
                    };
                });
                debugger
                return {
                    name: car.model,
                    data: data,
                    current: car.deals[car.current]
                };
            });
            Highcharts.ganttChart('containerKind', {
                series: series,
                title: {
                    text: 'Car Rental Schedule'
                },
                scrollbar: {
                    enabled: true
                },
                tooltip: {
                    pointFormat: '<span>Rented To: {point.rentedTo}</span><br/><span>From: {point.start:%e. %b}</span><br/><span>To: {point.end:%e. %b}</span>'
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
            resourceNo: resourceNo
        },
        success: function (data) {
            debugger
            Highcharts.ganttChart('containerKind', {
                title: {
                    text: '资源甘特图'
                },
                yAxis: {
                    uniqueNames: true
                },
                credits: {
                    enabled: false
                },
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
                rangeSelector: {
                    enabled: true,
                    selected: 0
                },
                series: [
                    {
                        name: "资源实例甘特图",
                        data: data,
                        events: {
                            click: function (e) {
                                var id = e.point.options.id;
                                initResourceItemGant(id);
                            }
                        }
                    }
                ],
                tooltip: {
                    pointFormatter: function () {
                        var point = this,
                            format = '%Y-%m-%d %H',
                            options = point.options,
                            completed = options.completed,
                            amount = isObject(completed) ? completed.amount : completed,
                            status = ((amount || 0) * 100) + '%',
                            lines;
                        lines = [{
                            value: point.name,
                            style: 'font-weight: bold;'
                        }, {
                            title: 'Start',
                            value: dateFormat(format, point.start)
                        }, {
                            visible: !options.milestone,
                            title: 'End',
                            value: dateFormat(format, point.end)
                        }, {
                            title: 'Owner',
                            value: options.owner || 'unassigned'
                        }];
                        return reduce(lines, function (str, line) {
                            var s = '',
                                style = (
                                    defined(line.style) ? line.style : 'font-size: 0.8em;'
                                );
                            if (line.visible !== false) {
                                s = (
                                    '<span style="' + style + '">' +
                                    (defined(line.title) ? line.title + ': ' : '') +
                                    (defined(line.value) ? line.value : '') +
                                    '</span><br/>'
                                );
                            }
                            return str + s;
                        }, '');
                    }
                },
            });
        },
        error: function (data) {
            console.log("error" + data);
        }
    })
}


