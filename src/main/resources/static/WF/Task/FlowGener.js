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
                    else
                        return "已完成";
                }},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='详细' id='btnToDetail' onclick='gotoGenerFlowDetail(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='甘特图' id='btnShowProgress' onclick='showProgress(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='流程图' id='btnShowProgressFlow' onclick='showProgressFlow(\""+rec.no+"\")'/>";
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
                series: gantData.series,
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
    var url="/WF/WF/Admin/CCBPMDesigner/Designer.htm?NodetaskNo="+no+"&type=2";
    var self = window.open(url);
}

function getGenerFlowGantData(generFlowNo,depth) {

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
}

function gotoGenerFlowDetail(no) {
    var enName = "BP.Task.FlowGener";
    var url = "/WF/WF/Comm/En.htm?EnName=" + enName + "&PKVal=" + no;
    OpenEasyUiDialogExt(url,"流程实例", 800, 450, true);
}