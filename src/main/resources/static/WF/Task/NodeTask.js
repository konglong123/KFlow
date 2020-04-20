
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
            {field:'nodeId',title: '当前节点',align: 'center',width:10},
            {field:'totalTime',title: '总工作量',align: 'center',width:10},
            {field:'useTime',title: '已完成工作量',align: 'center',width:10},
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
        case -1:
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
    }
}
function gotoNodeTaskDetail(pkVal) {
    var enName = "BP.Task.NodeTask";
    var url = "/WF/WF/Comm/En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url,"任务详情", 800, 450, true);
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
//执行任务，每次执行前（）
function doNodeTask(no) {
    var url="/WF/WF/Task/NodeTaskDetail.html?NodeTaskNo="+no;
    var self = window.open(url);
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
        success:function (data) {
            var nodeTask=JSON.parse(data);
            dataUrl+="&FK_Flow="+nodeTask.flow_id;
            dataUrl+="&WorkID="+nodeTask.work_id;
            dataUrl+="&FK_Node="+nodeTask.node_id;
            dataUrl+="&FID="+nodeTask.flow_id;
            window.location.href = dataUrl;
        }
    });

}

function updateNodeTaskStatus() {
    $.ajax({
        url: "/WF/nodeTask/updateTasksStatus",
        type: 'GET',
        data:{
        },
        success:function (data) {
            alert("更新成功！")
            $('#dgNodeTasks').datagrid('reload');
        },
        error:function (date) {
            alert("更新失败！"+date);
        }
    });
}

function queryNodeTaskByCondition() {
    var nodeTaskNo=$("#nodeTaskNoQuery").val().trim();
    var workId=$("#workIdQuery").val().trim();
    var flowNo=$("#flowNoQuery").val().trim();
    var status=$("#statusQuery").val().trim();
    var queryParams = $('#dgNodeTasks').datagrid('options').queryParams;
    queryParams.nodeTaskNo = nodeTaskNo;
    queryParams.workId = workId;
    queryParams.flowNo = flowNo;
    queryParams.status = status;
    $("#dgNodeTasks").datagrid('reload');
}