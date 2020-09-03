
function initDgNodeTasks() {
    $('#dgNodeTasks').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/nodeTask/getNodeTasks",
        queryParams: {
            nodeTaskNo:"",
            workId:"",
            flowNo:"",
            status:""
        },
        columns:[[
            {field:'no',title: '任务编码',align: 'center',width:10},
            {field:'workId',title: '工作编码',align: 'center',width:10},
            {field:'flowId',title: '流程编码',align: 'center',width:10},
            {field:'nodeId',title: '节点编码',align: 'center',width:10},
            {field:'nodeName',title: '节点名',align: 'center',width:10},
            {field:'totalTime',title: '总工作量',align: 'center',width:10},
            {field:'useTime',title: '已完成工作量',align: 'center',width:15},
            {field:'preNodeTask',title: '前置任务',align: 'center',width:10},
            {field:'nextNodeTask',title: '后置任务',align: 'center',width:10},
            {field:'status',title: '状态',align: 'center',width:10,
                formatter:function (val,rec) {
                    return getNodeTaskStatus(val);
                }},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='详细' id='btnToDetail' onclick='gotoNodeTaskDetail(\""+rec.no+"\")'/>";
                    if (rec.isReady==1||rec.isReady==2)
                        str+="<input type='button' value='执行' id='btnDo' onclick='doNodeTask(\""+rec.no+"\")'/>";
                    return str;
                }},



        ]]
    });
}
function getNodeTaskStatus(val) {
    switch (val) {
        case 20:
            return "未准备";
        case 1:
            return '可以开始';
        case 2:
            return '已经开始';
        case 3:
            return '已经完成';
        case 4:
            return "<font color=red>逾期开始</font>"
        case 5:
            return "<font color=#cd853f>警告开始</font>"
        case 6:
            return "<font color=green>正常</font>"
        case 7:
            return "<font color=red>逾期结束</font>"
        case 8:
            return "<font color=#cd853f>警告结束</font>"
        case 9:
            return "计划完成"
    }
}
function gotoNodeTaskDetail(pkVal) {
    var enName = "BP.Task.NodeTask";
    var url = "/WF/WF/Comm/En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url,"任务详情", 800, 450, false);
}
//开始任务，(将任务状态设定成已经开始)
function startNodeTask(no) {
    $.ajax({
        url:"/WF/nodeTask/startNodeTask",
        data:{
            no:no
        },
        error: function (data) {
            console.log(data);
        }
    })
}

//执行任务，每次执行前（检查该任务是否存在）
function doNodeTask(no) {
    $.ajax({
        url:"/WF/nodeTask/checkNodeTaskIsFinish",
        data:{
            no:no
        },
        success:function(data){
            if (!data) {
                alert("请刷新任务列表!");
                $("#dgNodeTasks").datagrid('reload');
            }else {
                var url="/WF/WF/Task/NodeTaskDetail.html?NodeTaskNo="+no;
                var self = window.open(url);
            }
        },
        error: function (data) {
            console.log(data);
        }
    })

}

//执行节点任务
function doNodeTaskDetail(no) {
    var dataUrl="/WF/WF/MyFlowGener.htm?NodeTaskNo="+no;
    $.ajax({
        url: "/WF/nodeTask/getNodeTaskByNo",
        type: 'GET',
        data:{
            no:no
        },
        success:function (nodeTask) {
            dataUrl+="&FK_Flow="+nodeTask.flowId;
            dataUrl+="&WorkID="+nodeTask.workId;
            dataUrl+="&FK_Node="+nodeTask.nodeId;
            dataUrl+="&FID="+nodeTask.flowId;
            window.location.href = dataUrl;
        }
    });

}
//type=1更新自己的任务状态，type=2更新所有任务状态（）
function updateNodeTaskStatus(type) {
    $.ajax({
        url: "/WF/nodeTask/updateTasksStatus",
        type: 'POST',
        dataType: 'json',
        contentType:'application/json',
        data:JSON.stringify(type),
        success:function (data) {
            alert("更新成功！")
            if (type==1)
                $('#dgNodeTasks').datagrid('reload');
            else if (type==2)
                $('#dgNodeTasksAll').datagrid('reload');
        },
        error:function (date) {
            alert("更新失败！"+date);
        }
    });
}

function queryNodeTaskByCondition() {
    var queryParams = $('#dgNodeTasks').datagrid('options').queryParams;
    queryParams.nodeTaskNo = $("#nodeTaskNoQuery").val().trim();
    queryParams.workId = $("#workIdQuery").val().trim();
    queryParams.flowNo = $("#flowNoQuery").val().trim();
    queryParams.status = $("#statusQuery").val().trim();
    $("#dgNodeTasks").datagrid('reload');

    initGantMyTask();
}

function queryNodeTaskAllByCondition() {
    var queryParams = $('#dgNodeTasksAll').datagrid('options').queryParams;
    queryParams.nodeTaskNo = $("#nodeTaskNoQueryAll").val().trim();
    queryParams.workId = $("#workIdQueryAll").val().trim();
    queryParams.flowNo = $("#flowNoQueryAll").val().trim();
    queryParams.status = $("#statusQueryAll").val().trim();
    queryParams.executor=$("#executorQueryAll").val().trim();
    $("#dgNodeTasksAll").datagrid('reload');
}

function initDgNodeTasksAll() {
    $('#dgNodeTasksAll').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/nodeTask/getNodeTasks",
        queryParams: {
            nodeTaskNo:"",
            workId:"",
            flowNo:"",
            status:"",
            executor:""
        },
        columns:[[
            {field:'no',title: '任务编码',align: 'center',width:10},
            {field:'workId',title: '工作编码',align: 'center',width:10},
            {field:'flowId',title: '流程编码',align: 'center',width:10},
            {field:'nodeId',title: '节点编码',align: 'center',width:10},
            {field:'totalTime',title: '总工作量',align: 'center',width:10},
            {field:'useTime',title: '已完成工作量',align: 'center',width:15},
            {field:'preNodeTask',title: '前置任务',align: 'center',width:10},
            {field:'nextNodeTask',title: '后置任务',align: 'center',width:10},
            {field:'executor',title: '执行人',align: 'center',width:10},
            {field:'status',title: '状态',align: 'center',width:10,
                formatter:function (val,rec) {
                    return getNodeTaskStatus(val);
                }},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='详细' id='btnToDetail' onclick='gotoNodeTaskDetail(\""+rec.no+"\")'/>";
                    return str;
                }},


        ]]
    });
}
function initDgTaskMessage() {
    var workId=GetQueryString("workID");
    var nodeId=GetQueryString("nodeID");
    $('#dgTaskMessage').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/nodeTask/getTaskMessage",
        queryParams: {
            nodeId:nodeId,
            workId:workId,
        },
        columns:[[
            {field:'MyPK',title: '消息编码',align: 'center',width:10},
            {field:'WorkID',title: '工作编码',align: 'center',width:10},
            {field:'Returner',title: '消息来源',align: 'center',width:10},
            {field:'BeiZhu',title: '消息内容',align: 'center',width:10,
                formatter:function (val) { return "<span title='" + val + "'>" + val + "</span>" }},
            {field:'Type',title: '消息类型',align: 'center',width:10,
                formatter:function (val,rec) {
                    if (val==1)
                        return "<font color=red>任务退回</font>";
                    else if (val==2)
                        return "<font color=black>监管命令</font>";
                }},
            {field:'RDT',title: '创建时间',align: 'center',width:10},
            {field:'ReturnNode',title: '回退节点',align: 'center',width:10},


        ]]
    });
}

function initGantMyTask() {

        var gantData;
        var con={
            No: $("#nodeTaskNoQuery").val().trim(),
            workId :$("#workIdQuery").val().trim(),
            flowId:$("#flowNoQuery").val().trim(),
            status : $("#statusQuery").val().trim(),
        };
        $.ajax({
            url: "/WF/nodeTask/getMyTaskGantData",
            type: 'POST',
            dataType: 'json',
            contentType:'application/json',
            data: JSON.stringify(con),
            success: function (data) {
                gantData=data;

                dateFormat = Highcharts.dateFormat,
                    defined = Highcharts.defined,
                    isObject = Highcharts.isObject,
                    reduce = Highcharts.reduce;

                Highcharts.ganttChart('container', {
                    title: {
                        text: '任务甘特图'
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

//查询每个状态下任务统计信息
function getTaskInfoForStatus() {
    var  con={
    };
    var data;
    $.ajax({
        url: "/WF/nodeTask/getTaskInfoForStatus",
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

//查询单个状态下任务统计信息
function getTaskInfoOneStatus(status) {
    if (status==null)
        status=0;
    var  con={
        status:status
    };
    var data;
    $.ajax({
        url: "/WF/nodeTask/getTaskInfoOneStatus",
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