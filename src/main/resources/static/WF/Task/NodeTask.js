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
            isReady:""
        },
        columns:[[
            {field:'no',title: '任务编码',align: 'center',width:10},
            {field:'flow_task_id',title: '流程实例编码',align: 'center',width:10},
            {field:'flow_id',title: '流程编码',align: 'center',width:10},
            {field:'node_id',title: '当前节点',align: 'center',width:10},
            {field:'total_time',title: '总工作量',align: 'center',width:10},
            {field:'use_time',title: '已完成',align: 'center',width:10},
            {field:'status',title: '状态',align: 'center',width:10,
                formatter:function (val,rec) {
                    return getNodeTaskStatus(val);
                }},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='详细' id='btnToDetail' onclick='gotoNodeTaskDetail(\""+rec.no+"\")'/>";
                    if (rec.is_ready==1||rec.is_ready==2)
                        str+="<input type='button' value='执行' id='btnDo' onclick='doNodeTask(\""+rec.no+"\")'/>";
                    return str;
                }},



        ]]
    });
}
function getNodeTaskStatus(val) {
    switch (val) {
        case 0:
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
    debugger
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
            dataUrl+="&WorkID="+nodeTask.flow_task_id;
            dataUrl+="&FK_Node="+nodeTask.node_id;
            dataUrl+="&FID="+nodeTask.flow_id;
            debugger
            window.location.href = dataUrl;
        }
    });

}