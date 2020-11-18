
//全局变量
function WinOpen(url) {
    window.open(url);
}

//流程属性.
function FlowProperty() {
    url = "../../Comm/En.htm?EnName=BP.WF.Template.FlowExt&PKVal=" + flowNo + "&Lang=CH";

    //OpenEasyUiDialogExt(url, "流程属性", 900, 500, false);
    window.parent.addTab(flowNo, "流程属性" + flowNo, url);

    //  WinOpen(url);
    //    OpenEasyUiDialog(url, "eudlgframe", '流程属性', 1000, 550, "icon-property", true, null, null, null, function () {
    //        //window.location.href = window.location.href;
    //    });
}

//报表设计.
function FlowRpt() {

    if (window.confirm('该功能，我们将要取消,仅供内部开发人员使用.') == false)
        return;

    //  alert('该功能，我们将要取消.');
    // return;

    var flowId = Number(flowNo);
    flowId = String(flowId);
    url = "../RptDfine/Default.htm?FK_Flow=" + flowNo + "&FK_MapData=ND" + flowId + "MyRpt";

    //OpenEasyUiDialogExt(url, "报表设计", 900, 500, false);
    window.parent.addTab(flowNo + "_BBSJ", "报表设计" + flowNo, url);
}

//检查流程.
function FlowCheck() {

    var flowId = Number(flowNo);
    flowId = String(flowId);
    url = "../AttrFlow/CheckFlow.htm?FK_Flow=" + flowNo + "&FK_MapData=ND" + flowId + "MyRpt";
    // WinOpen(url);
    OpenEasyUiDialog(url, "FlowCheck" + flowNo, "检查流程" + flowNo, 600, 500, "icon - library", false);
    //window.parent.addTab(flowNo + "_JCLC", "检查流程" + flowNo, url);
}

//运行流程
function FlowRun() {

    //执行流程检查.
    var flow = new Entity("BP.WF.Flow", flowNo);
    flow.DoMethodReturnString("ClearCash");

    var url = "../TestFlow.htm?FK_Flow=" + flowNo + "&Lang=CH";
    //WinOpen(url);
    window.parent.addTab(flowNo + "_YXLH", "运行流程" + flowNo, url);
}
//运行流程
function FlowRunAdmin() {

    //执行流程检查.
    var flow = new Entity("BP.WF.Flow", flowNo);
    flow.DoMethodReturnString("ClearCash");

    //var url = "../TestFlow.htm?FK_Flow=" + flowNo + "&Lang=CH";
    var webUser = new WebUser();
    var url = "../TestFlow.htm?DoType=TestFlow_ReturnToUser&DoWhat=StartClassic&UserNo=" + webUser.No + "&FK_Flow=" + flowNo;
    //  var url = "../../MyFlow.htm?FK_Flow=" + flowNo + "&Lang=CH";
    WinOpen(url);
    //window.parent.addTab(flowNo + "_YXLH", "运行流程" + flowNo, url);
}

//旧版本.
function OldVer() {
    var url = "Designer2016.htm?FK_Flow=" + flowNo + "&Lang=CH&&Flow_V=1";
    window.location.href = url;
}

function Help() {
    var msg = "<ul>";
    msg += "<li>KFLow流程管理平台！</li>";
    msg += "<li>交流方式：微信:17888821571 QQ:2332832734</li>";
    msg += "<li>地址:北航新主楼A515</li>";
    msg += "</ul>";
    mAlert(msg, 20000);
}

/***********************  节点信息. ******************************************/

//节点属性
function NodeAttr(nodeID) {

    //var url = "../../Comm/RefFunc/EnV2.htm?EnName=BP.WF.Template.NodeExt&NodeID=" + nodeID + "&Lang=CH";
    var url = "../../Comm/En.htm?EnName=BP.WF.Template.NodeExt&NodeID=" + nodeID + "&Lang=CH";
    var html = "";

    //var html = "<a href=\"javascript:OpenEasyUiDialogExt('" + url + "','';\" >主页</a> - ";
    window.parent.addTab(nodeID, "节点属性" + nodeID, url);
    //OpenEasyUiDialogExt(url, html+"属性", 900, 500, false);
}
//节点属性
function NodeAttrOld(nodeID) {
    var url = "../../Comm/En.htm?EnName=BP.WF.Template.NodeExt&NodeID=" + nodeID + "&Lang=CH";
    window.parent.addTab(nodeID, "节点属性" + nodeID, url);
    //OpenEasyUiDialogExt(url, "节点属性", 800, 500, false);
}

//表单方案
function NodeFrmSln(nodeID) {
    //表单方案.
    var url = "../AttrNode/FrmSln/Default.htm?FK_Node=" + nodeID;

    window.parent.addTab(nodeID + "_JDFA", "表单方案" + nodeID, url);
    // OpenEasyUiDialogExt(url, "表单方案", 800, 500, false);
}

//表单方案
function CondDir(fromNodeID) {

    var flowNo = GetQueryString("FK_Flow");

    var targetId = fromNodeID;

    var url = "../Cond/ConditionLine.htm?FK_Flow=" + flowNo + "&FK_MainNode=" + fromNodeID + "&FK_Node=" + fromNodeID + "&ToNodeID=" + targetId + "&CondType=2&Lang=CH&t=" + new Date().getTime();
    $("#LineModal").hide();
    $(".modal-backdrop").hide();
    OpenEasyUiDialog(url, flowNo + fromNodeID + "DIRECTION" + targetId, "设置方向条件" + fromNodeID + "->" + targetId, 880, 500, "icon-property", true, null, null, null, function () {

    });
}



//设计表单
function NodeFrmD(nodeID) {

    var node = new Entity("BP.WF.Node", nodeID);
    if (node.FormType == 1) {
        NodeFrmFree(nodeID);
        return;
    }

    NodeFrmFool(nodeID);
}

function NodeFrmFool(nodeID) {
    //傻瓜表单.
    var url = "../FoolFormDesigner/Designer.htm?FK_MapData=ND" + nodeID + "&IsFirst=1&FK_Flow=" + flowNo + "&FK_Node=" + nodeID;
    //WinOpen(url);
    window.parent.addTab(nodeID + "_Fool", "设计表单" + nodeID, url);
}

function NodeFrmFree(nodeID) {

    //自由表单.
    var url = "../CCFormDesigner/FormDesigner.htm?FK_MapData=ND" + nodeID + "&FK_Flow=" + flowNo + "&FK_Node=" + nodeID;
    window.parent.addTab(nodeID + "_Free", "设计表单" + nodeID, url);
    ///CCFormDesigner/FormDesigner.htm?FK_Node=9502&FK_MapData=ND9502&FK_Flow=095&UserNo=admin&SID=c3466cb7-edbe-4cdc-92df-674482182d01
    //WinOpen(url);
}
function nodeResource(nodeID) {
    var url = "../AttrNode/Resources/Resources.htm?FK_Node=" + nodeID;
    window.parent.addTab(nodeID +"_JDZY", "节点资源" + nodeID, url);
}

//接受人规则.
function NodeAccepterRole(nodeID) {
    //接受人规则.
    var url = "../AttrNode/AccepterRole/Default.htm?FK_MapData=ND" + nodeID + "&FK_Flow=" + flowNo + "&FK_Node=" + nodeID;
    window.parent.addTab(nodeID + "_JSGZ", "接受人规则" + nodeID, url);
    //OpenEasyUiDialogExt(url, "接受人规则", 800, 500, false);
}

function Reload() {
    window.location.href = window.location.href;
}


//打开.
function OpenEasyUiDialogExt(url, title, w, h, isReload) {

    OpenEasyUiDialog(url, "eudlgframe", title, w, h, "icon-property", true, null, null, null, function () {
        if (isReload == true) {
            window.location.href = window.location.href;
        }
    });
}

//创建节点
function createNode(flowNo,_canvas) {
    //获取坐标
    var mLeft = $("#jqContextMenu").css("left").replace('px', '');
    var mTop = $("#jqContextMenu").css("top").replace('px', '');


    //创建一个节点
    var hander = new HttpHandler("BP.WF.HttpHandler.WF_Admin_CCBPMDesigner2018");
    hander.AddPara("X", mLeft);
    hander.AddPara("Y", mTop);
    hander.AddPara("FK_Flow", flowNo);

    var data = hander.DoMethodReturnString("CreateNode");
    if (data.indexOf('err@') == 0) {
        alert(data);
        return;
    }

    //添加节点样式与坐标
    data = JSON.parse(data);
    var strs = "";
    strs += "{'id':'" + data.NodeID + "',";
    strs += "'flow_id':'" + flowNo + "',";
    strs += "'process_name':'" + data.Name + "',";
    strs += "'process_to':0,";
    strs += "'icon':'icon-ok',";
    strs += "'style':'width:auto;height:41px;line-height:41px;color:#0e76a8;left:" + mLeft + "px;top:" + mTop + "px;'";
    strs += "}";
    strs = eval("(" + strs + ")");

    if (_canvas.addProcess(strs) == false) //添加
    {
        alert("添加失败");

    }
}

//复制节点时，将选中信息同步到复制tab页中,
function updateCopyTab(nodeId,directionId,labelId) {
    url = "../../Comm/En.htm?EnName=BP.WF.Template.FlowExt&PKVal=" + flowNo + "&Lang=CH";

    //OpenEasyUiDialogExt(url, "流程属性", 900, 500, false);
    window.parent.addTab(flowNo, "流程属性" + flowNo, url);
}

//粘贴节点
function pasteNode(flowNo,_canvas,nodeIds) {
    //获取坐标
    var mLeft = $("#jqContextMenu").css("left").replace('px', '');
    var mTop = $("#jqContextMenu").css("top").replace('px', '');
    //创建一个节点
    var hander = new HttpHandler("BP.WF.HttpHandler.WF_Admin_CCBPMDesigner2018");
    hander.AddPara("X", mLeft);
    hander.AddPara("Y", mTop);
    hander.AddPara("nodeIds",nodeIds);
    hander.AddPara("FK_Flow", flowNo);

    var data = hander.DoMethodReturnString("pasteNode");
    if (data.indexOf('err@') == 0) {
        alert(data);
        return;
    }

    //添加节点样式与坐标
    data = JSON.parse(data);
    for (var index in data){
        var node=data[index];
        var strs = "";
        strs += "{'id':'" + node.NodeId + "',";
        strs += "'flow_id':'" + flowNo + "',";
        strs += "'process_name':'" + node.Name + "',";
        strs += "'process_to':0,";
        strs += "'icon':'icon-ok',";
        strs += "'style':'width:auto;height:41px;line-height:41px;color:#0e76a8;left:" + node.X + "px;top:" + node.Y + "px;'";
        strs += "}";
        strs = eval("(" + strs + ")");

        if (_canvas.addProcess(strs) == false) //添加
        {
            alert("添加失败");
            return;
        }
    }
}

//复制流程（批量可选择性复制流程）
function FlowCopy() {
    url = "../../Admin/CCBPMDesigner/copyNodes.html?PKVal=" + flowNo;
    window.parent.addTab(flowNo, "复制流程" + flowNo, url);
}

//查询流程
function searchFlow(){
    url = "./../CCBPMDesigner/SearchFlow.htm?Lang=CH";
    window.parent.addTab(flowNo, "语义查询", url);
}

//划分片段，两种片段（1，可调换顺序，2可作为模块独立检索）
function groupNode() {
    url = "../../Admin/CCBPMDesigner/GroupNode.html?FK_Flow=" + flowNo;
    window.parent.addTab(flowNo, "划分模块" + flowNo, url);
}

function composeGroup() {
    url = "../../Admin/CCBPMDesigner/ComposeGroup.html?FK_Flow=" + flowNo;
    window.parent.addTab(flowNo, "智能推荐" + flowNo, url);
}

function autoLayout() {
    var flowNo=GetQueryString("FK_Flow");
    $.ajax({
        url: "/WF/composeGroup/autoLayout",
        type: 'POST',
        data: {
            flowNo:flowNo
        },
        success: function (data) {
            Reload();
        },
        error: function (data) {
            alert("暂不支持该结构流程布局，自动布局失败！");
        }
    });
}

