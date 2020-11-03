var projectTreeChart;
var match = "@";
function initProjectTree(elementId,projectTreeNo) {
    var dom = document.getElementById(elementId);
    projectTreeChart = echarts.init(dom);
    option = null;
    projectTreeChart.showLoading();
    $.get('http://localhost:8081/WF/Project/getProjectTreeData?projectNo='+projectTreeNo, function (data) {
        projectTreeChart.hideLoading();
        /*echarts.util.each(data.children, function (datum, index) {
            index % 2 === 0 && (datum.collapsed = true);
        });*/
        projectTreeChart.on('dblclick', function (params) {
            var generNo=params.data.generNo;
            var url;
            if (generNo!=null&&generNo!='')
                url="/WF/WF/Admin/CCBPMDesigner/Designer.htm?GenerFlowNo="+generNo+"&type=2";
            else {
                var id = params.data.value;
                url = "/WF/WF/Admin/CCBPMDesigner/Designer.htm?FK_Flow=" + id + "&UserNo=admin&SID=&Flow_V=1";
            }
            window.open(url);
        });
        projectTreeChart.setOption(option = {
            tooltip: {
                trigger: 'item',
                triggerOn: 'mousemove'
            },
            toolbox:{
                feature:{
                    saveAsImage:{}
                }
            },
            series: [
                {
                    type: 'tree',
                    roam: true,
                    data: [data],

                    top: '1%',
                    left: '1%',
                    bottom: '1%',
                    right: '1%',

                    symbolSize: 8,
                    initialTreeDepth: 3,

                    label: {
                        position: 'left',
                        verticalAlign: 'middle',
                        align: 'right',
                        fontSize: 16,
                        formatter: function (param) {
                            if (param.name.match(match)) {
                                return '{a|' + param.name + '}'
                            } else {
                                if (param.data.flag==1) {
                                    return '{b|' + param.name + '}'
                                }
                                return param.name;
                            }

                        },
                        rich: {
                            a: {
                                color: 'red',
                                lineHeight: 12
                            },
                            b:{
                                color: 'blue',
                                lineHeight: 12
                            }
                        }
                    },

                    leaves: {
                        label: {
                            position: 'right',
                            verticalAlign: 'middle',
                            align: 'left'
                        }
                    },

                    expandAndCollapse: true,
                    animationDuration: 550,
                    animationDurationUpdate: 750
                }
            ]
        });
    });
}

function queryProjectNode() {
    match=$("#projectNodeQuery").val();
    var option = projectTreeChart.getOption();
    //option.series[0].data = data;
    projectTreeChart.setOption(option);
}