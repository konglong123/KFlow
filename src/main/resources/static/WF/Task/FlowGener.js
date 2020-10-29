function initDgGenerFlows() {
    $('#dgGenerFlows').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/generFlow/getGenerFlowList",
        queryParams: {
            generFlowNo:"",
            workId:"",
            flowNo:"",
            workGroupId:"",
            parentWorkId:"",
            creator:"",
            yn:""
        },
        columns:[[
            {field:'no',title: '实例编码',align: 'center',width:10},
            {field:'workId',title: '工作编码',align: 'center',width:10},
            {field:'flowId',title: '流程编码',align: 'center',width:10},
            {field:'workGroupId',title: '工作组',align: 'center',width:10},
            {field:'parentWorkId',title: '父工作',align: 'center',width:10},
            {field:'useTime',title: '已完成工作量',align: 'center',width:10},
            {field:'totalTime',title: '总工作量',align: 'center',width:10},
            {field:'creator',title: '发起人',align: 'center',width:10},
            {field:'activatedNodes',title: '激活节点',align: 'center',width:20,
            formatter:function (val) { return "<span title='" + val + "'>" + val + "</span>" }},
            {field:'status',title: '状态',align: 'center',width:10,
                formatter:function (val,rec) {
                    if(val==1)
                        return "<font color=green>运行中</font>";
                    else if (val==3)
                        return "准备";
                    else if (val==2)
                        return "完成";
                }},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='详细' id='btnToDetail' onclick='gotoGenerFlowDetail(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='甘特图' id='btnShowProgress' onclick='showProgress(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='流程图' id='btnShowProgressFlow' onclick='showProgressFlow(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='下发指令' id='btnShowProgressFlow' onclick='doManage(\""+rec.no+"\")'/>";
                    return str;
                }},



        ]]
    });
}
//初始化甘特图
function initGantt(generFlowNo) {

    var gantData;
    $.ajax({
        url: "/WF/nodeTask/getNodeTaskGantData",
        type: 'POST',
        data: {
            generFlowNo: generFlowNo,
            depth: 5
        },
        success: function (data) {
            gantData=data;

            dateFormat = Highcharts.dateFormat,
                defined = Highcharts.defined,
                isObject = Highcharts.isObject,
                reduce = Highcharts.reduce;

            Highcharts.ganttChart('container', {
                title: {
                    text: '流程实例甘特图'
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
                series:[
                    {
                        name:"任务甘特图",
                        data:gantData.series[0].data,
                        events:{
                            click: function(e) {
                                var id=e.point.options.id;
                                var taskNo=id.split("-")[0];
                                gotoNodeTaskDetail(taskNo);
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
        }
    });


}

function showProgress(no) {
    var url="/WF/WF/Task/FlowGenerDetail.html?GenerFlowNo="+no;
    var self = window.open(url);
}

function showProgressFlow(no) {
    var url="/WF/WF/Admin/CCBPMDesigner/Designer.htm?GenerFlowNo="+no+"&type=2";
    var self = window.open(url);
}
//下发监管命令
function doManage(no) {
    var url = "/WF/WF/Task/Manage.html?GenerFlowNo=" + no;
    OpenEasyUiDialogExt(url,"下发监管指令", 800, 450, false);
}

function queryGenerFlowByCondition() {
    var generFlowNo=$("#generFlowNoQuery").val().trim();
    var workId=$("#workIdQuery").val().trim();
    var flowNo=$("#flowNoQuery").val().trim();
    var workGroupId=$("#workGroupIdQuery").val().trim();
    var parentWorkId=$("#parentWorkIdQuery").val().trim();
    var creator=$("#creatorQuery").val().trim();
    var yn=$("#ynQuery").val().trim();
    var queryParams = $('#dgGenerFlows').datagrid('options').queryParams;
    queryParams.generFlowNo = generFlowNo;
    queryParams.workId = workId;
    queryParams.flowNo = flowNo;
    queryParams.workGroupId = workGroupId;
    queryParams.parentWorkId = parentWorkId;
    queryParams.creator = creator;
    queryParams.yn = yn;
    $("#dgGenerFlows").datagrid('reload');

    initGenerFlow(queryParams);

}

function gotoGenerFlowDetail(no) {
    var enName = "BP.Task.FlowGener";
    var url = "/WF/WF/Comm/En.htm?EnName=" + enName + "&PKVal=" + no;
    OpenEasyUiDialogExt(url,"流程实例", 800, 450, false);
}

function getActiveNodes() {
    var generFlow=new Entity("BP.Task.FlowGener");
    var no=GetQueryString("GenerFlowNo");
    generFlow.No=no;
    generFlow.RetrieveFromDBSources();
    flowNo=generFlow.flow_id;
    var activeNodeStr=generFlow.activated_nodes;
    activeNodes=activeNodeStr.split(",");
    return activeNodes;
}

function getGenerInfoAll(con) {
    var toUrl='/WF/generFlow/getDataForGeners';
    var data;
    $.ajax({
        url: toUrl,
        type: 'POST',
        dataType: 'json',
        async:false,
        contentType:'application/json',
        data: JSON.stringify(con),
        success: function (dataTemp) {
            data= dataTemp;
        }
    });
    return data;
}

function getGenerInfoForOne(generFlowNo) {
    var toUrl='/WF/generFlow/getDataForOneGener';
    var  con={
        no:generFlowNo
    };
    var data;
    $.ajax({
        url: toUrl,
        type: 'POST',
        dataType: 'json',
        async:false,
        contentType:'application/json',
        data: JSON.stringify(con),
        success: function (dataTemp) {
            data= dataTemp;
        }
    });
    return data;
}

function initGenerFlow(con) {
    var dom = document.getElementById("generFlowShow");
    var myChart = echarts.init(dom);
    var dataChart={
        pieData:[],
        barDataAll:[],
        barDataUse:[],
        lineData:[],
        xAxis:[]
    };

    dataTemp=getGenerInfoAll(con);
    dataChart.xAxis=dataTemp.xAxis;
    dataChart.lineData=dataTemp.lineData;
    dataChart.barDataAll=dataTemp.barDataAll;
    dataChart.barDataUse=dataTemp.barDataUse;

    option = {
        title: {
            text: '流程实例统计图',
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
        legend: {
            x: '40%',
            y: '68%',
            data: ['总工时', '已用工时','完成进度']
        },
        grid: [
            {x: '5%', y: '15%', width: '50%', height: '48%'},//折线图位置控制
        ],
        xAxis: [
            {
                type: 'category',
                name:'流程实例',
                data: dataChart.xAxis,
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '工时',
                interval: 50,
                axisLabel: {
                    formatter: '{value} h'
                }
            },
            {
                type: 'value',
                name: '完成进度',
                interval: 0.05,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '任务状态分布',
                type: 'pie',
                radius: [30, 100],
                center: ['80%', '40%'],
                roseType: 'area',
                tooltip: {
                    trigger: 'item',
                    formatter: " <br/>{b} : {c} ({d}%)"
                },
                data: dataChart.pieData
            },
            {
                name: '总工时',
                type: 'bar',
                data: dataChart.barDataAll
            },
            {
                name: '已用工时',
                type: 'bar',
                data: dataChart.barDataUse
            },
            {
                name: '完成进度',
                type: 'line',
                yAxisIndex: 1,
                data: dataChart.lineData
            },
        ]
    };
    myChart.setOption(option);
    myChart.on("click",function (param) {
        //饼图联动
        if (param.seriesType=='bar'){
            debugger
            var generNo=param.name;
            var data =getGenerInfoForOne(generNo);
            option.series[0].data=data.pieData;
            myChart.setOption(option);
        };
    });
}