function initDgNodeGroups(flowNo) {
    $('#dgNodeGroups').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/flow/getNodeGroups",
        onClickRow: function (index, row) {
            initDgNodeGroupItems(row.No);
        },
        queryParams: {
            flowNo: flowNo
        },
        columns:[[
            {field:'No',title: '分组编码',align: 'center',width:10},
            {field:'flow_no',title: '流程编码',align: 'center',width:10},
            {field:'flow_name',title: '流程名',align: 'center',width:15},
            {field:'type',title: '分类',align: 'center',width:10,
                formatter:function (val,rec) {
                    var str="";
                    if (val==1)
                        str="可调换分组";
                    else if (val==2)
                        str="模块分组";
                    else
                        str="错误模块";
                    return str;
                }},
            {field:'inNodeNo',title: '入口节点编码',align: 'center',width:10},
            {field:'outNodeNo',title: '出口节点编码',align: 'center',width:10},
            {field:'nodeNum',title: '节点数',align: 'center',width:10},
            {field:'sumTime',title: '总工时(/h)',align: 'center',width:10},
            {field:'abstracts',title: '摘要',align: 'center',width:30},
            {field:'operation',title: '操作',align: 'center',width:30,
                formatter:function(val,rec){
                    var str="<input type='button' value='删除分组' onclick='deleteNodeGroup(\""+rec.No+"\")'/>";
                    str+="<input type='button' value='可视化查看' onclick='gotoNodeGroupView(\""+rec.No+"\")'/>";
                    str+="<input type='button' value='详情' onclick='gotoNodeGroupDetail(\""+rec.No+"\")'/>";
                    return str;}},
        ]]
    });
}
function initDgNodeGroupItems(groupNo) {
    $('#dgNodeGroupItems').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/flow/getNodeGroupItems",
        queryParams: {
            groupNo: groupNo
        },
        columns:[[
            {field:'No',title: '编号',align: 'center',width:10},
            {field:'node_no',title: '节点编号',align: 'center',width:15},
            {field:'node_name',title: '节点名',align: 'center',width:20},
            {field:'group_no',title: '分组编码',align: 'center',width:15},
            {field:'action',title: '操作',align: 'center',width:15,
                formatter:function(val,rec){
                    var str="<input type='button' value='删除' onclick='deleteGroupItem(\""+rec.No+"\")'/>";
                    return str;
                }},
        ]]
    });
}
function deleteNodeGroup(groupNo) {
    $.ajax({
        url: "/WF/flow/delNodeGroup",
        type: 'POST',
        data: {
            groupNo:groupNo
        },
        success: function (data) {
            $('#dgNodeGroups').datagrid('reload');
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
function gotoNodeGroupDetail(pkVal) {
    var enName = "BP.NodeGroup.NodeGroup";
    var url = "/WF/WF/Comm/En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url,"节点分组详情", 800, 450, false);
}
function gotoNodeGroupView(nodeGroupNo,flowNo){
    if (flowNo==null||flowNo=="")
        flowNo=GetQueryString("FK_Flow");
    var url="/WF/WF/Admin/CCBPMDesigner/Designer.htm?FK_Flow="+flowNo+"&NodeGroupNo="+nodeGroupNo+"&type=3";
    window.parent.addTab(flowNo+"_"+nodeGroupNo, "分组"+nodeGroupNo, url);
}
function addNodeGroup() {
    var flowNo=GetQueryString("FK_Flow");
    $.ajax({
        url: "/WF/flow/addNodeGroup",
        type: 'POST',
        data: {
            flowNo:flowNo
        },
        success: function (data) {
            $('#dgNodeGroups').datagrid('reload');
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
function initDgNodes(flowNo,nodeNo) {
    $('#dgNodes').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/flow/getNodesByFlowNo",
        queryParams: {
            flowNo: flowNo,
            nodeNo:nodeNo
        },
        columns:[[
            {field:'NodeID',title: '节点编码',align: 'center',width:10},
            {field:'Name',title: '节点名',align: 'center',width:20},
            {field:'FK_Flow',title: '流程编码',align: 'center',width:10},
            {field:'operation',title: '操作',align: 'center',width:40,
                formatter:function(val,rec){
                    var str="<input type='button' value='添加到分组' onclick='addToNodeGroup(\""+rec.NodeID+"\",\""+rec.Name+"\")'/>";
                    str+="<input type='button' value='详情' onclick='gotoNodeDetail(\""+rec.NodeID+"\")'/>";
                    return str;}},
        ]]
    });
}

function addToNodeGroup(nodeNo,nodeName) {
    var row = $('#dgNodeGroups').datagrid('getSelected');
    if (row==null){
        alert("请选中分组！");
        return;
    }
    $.ajax({
        url: "/WF/flow/addNodeGroupItem",
        type: 'POST',
        data: {
            nodeNo:nodeNo,
            groupNo:row.No,
            nodeName:nodeName
        },
        success: function (data) {
            alert(data.msg);
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}

function gotoNodeDetail(pkVal) {
    var enName = "BP.WF.Template.NodeExt";
    var url = "/WF/WF/Comm/En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url,"节点分组详情", 800, 450, false);
}

function deleteGroupItem(itemNo) {
    $.ajax({
        url: "/WF/flow/delNodeGroupItem",
        type: 'POST',
        data: {
            itemNo:itemNo
        },
        success: function (data) {
            $('#dgNodeGroupItems').datagrid('reload');
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}

function queryNode() {
    var queryParams = $('#dgNodes').datagrid('options').queryParams;
    queryParams.flowNo = GetQueryString("FK_Flow");
    queryParams.nodeNo=$("#nodeNoQuery").val();
    $("#dgNodes").datagrid('reload');

}

function initGroupInfo() {
    $.ajax({
        url: "/WF/composeGroup/initNodeGroup",
        type: 'POST',
        success: function (data) {
            alert("初始化结束！");
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
function groupManage() {
    url = "../../Admin/CCBPMDesigner/NodeGroupManage.html?";
    window.parent.addTab("nodeGroup", "节点分组管理", url);
}