$(function () {

    jQuery.getScript(basePath + "/WF/Admin/Admin.js")
        .done(function () {
            /* 耶，没有问题，这里可以干点什么 */
            // alert('ok');
        })
        .fail(function () {
            /* 靠，马上执行挽救操作 */
            //alert('err');
        });
});


var optionKey = 0;
function InitBar(key) {

    optionKey = key;

    var nodeID = GetQueryString("FK_Node");
    var str = nodeID.substr(nodeID.length - 2);
    var isSatrtNode = false;
    if (str == "01")
        isSatrtNode = true;

    // var html = "<div style='background-color:Silver' > 请选择访问规则: ";
    var html = "<div style='padding:5px' >访问规则: ";

    html += "<select id='changBar' onchange='changeOption()'>";
    html += "<option value=" + DeliveryWay.NoSelect + " >&nbsp;&nbsp;&nbsp;&nbsp;不启用选择器</option>";
    html += "<option value=" + DeliveryWay.ByBindEmp + " >&nbsp;&nbsp;&nbsp;&nbsp;按节点绑定的人员计算</option>";
    html += "<option value=" + DeliveryWay.ByStation + ">&nbsp;&nbsp;&nbsp;&nbsp;按照岗位智能计算</option>";
    html += "<option value=" + DeliveryWay.ByDept + " >&nbsp;&nbsp;&nbsp;&nbsp;按节点绑定的部门计算</option>";

    html += "</select >";

    html += "<input  id='Btn_Save' type=button onclick='Save()' value='保存' />";
    html += "</div>";

    document.getElementById("bar").innerHTML = html;

    $("#changBar option[value='" + optionKey + "']").attr("selected", "selected");


}

function OldVer() {

    var nodeID = GetQueryString("FK_Node");
    var flowNo = GetQueryString("FK_Flow");

    var url = '../NodeAccepterRole.aspx?FK_Flow=' + flowNo + '&FK_Node=' + nodeID;
    window.location.href = url;
}
function Help() {

    var url = "";
    switch (optionKey) {
        case DeliveryWay.ByStation:
            url = 'http://bbs.ccflow.org/showtopic-131376.aspx';
            break;
        case DeliveryWay.ByDept:
            url = 'http://bbs.ccflow.org/showtopic-131376.aspx';
            break;
        default:
            url = "http://ccbpm.mydoc.io/?v=5404&t=17906";
            break;
    }

    window.open(url);
}

//通用的设置岗位的方法。for admin.

function OpenDot2DotStations() {

    var nodeID = GetQueryString("FK_Node");

    var url = "../../../Comm/RefFunc/Dot2Dot.htm?EnName=BP.WF.Template.NodeSheet&Dot2DotEnsName=BP.WF.Template.NodeStations";
    url += "&AttrOfOneInMM=FK_Node&AttrOfMInMM=FK_Station&EnsOfM=BP.WF.Port.Stations";
    url += "&DefaultGroupAttrKey=FK_StationType&NodeID=" + nodeID + "&PKVal=" + nodeID;

    OpenEasyUiDialogExt(url, '设置岗位', 800, 500, true);
}

function changeOption() {
    var nodeID = GetQueryString("FK_Node");
    var obj = document.getElementById("changBar");
    var sele = obj.options;
    var index = obj.selectedIndex;
    var optionKey = 0;
    if (index > 0) {
        optionKey = sele[index].value
    }
    debugger
    var roleName = "";
    switch (parseInt(optionKey)) {
        case DeliveryWay.ByStation:
            roleName = "0.ByStation.htm";
            break;
        case DeliveryWay.ByDept:
            roleName = "1.ByDept.htm";
            break;
        case DeliveryWay.BySQL:
            roleName = "2.BySQL.htm";
            break;
        case DeliveryWay.ByBindEmp:
            roleName = "3.ByBindEmp.htm";
            break;
        case DeliveryWay.BySelected:
            roleName = "4.BySelected.htm";
            break;
        case DeliveryWay.ByPreviousNodeFormEmpsField:
            roleName = "5.ByPreviousNodeFormEmpsField.htm";
            break;
        case DeliveryWay.ByPreviousNodeEmp:
            roleName = "6.ByPreviousNodeEmp.htm";
            break;
        case DeliveryWay.ByStarter:
            roleName = "7.ByStarter.htm";
            break;
        case DeliveryWay.BySpecNodeEmp:
            roleName = "8.BySpecNodeEmp.htm";
            break;
        case DeliveryWay.ByDeptAndStation:
            roleName = "9.ByDeptAndStation.htm";
            break;
        case DeliveryWay.ByStationAndEmpDept:
            roleName = "10.ByStationAndEmpDept.htm";
            break;
        case DeliveryWay.BySpecNodeEmpStation:
            roleName = "11.BySpecNodeEmpStation.htm";
            break;
        case DeliveryWay.BySQLAsSubThreadEmpsAndData:
            roleName = "12.BySQLAsSubThreadEmpsAndData.htm";
            break;
        case DeliveryWay.ByDtlAsSubThreadEmps:
            roleName = "13.ByDtlAsSubThreadEmps.htm";
            break;
        case DeliveryWay.ByStationOnly:
            roleName = "14.ByStationOnly.htm";
            break;
        case DeliveryWay.ByFEE:
            roleName = "15.ByFEEp.htm";
            break;
        case DeliveryWay.BySetDeptAsSubthread:
            roleName = "16.BySetDeptAsSubthread.htm";
            break;
        case DeliveryWay.BySQLTemplate:
            roleName = "17.BySQLTemplate.htm";
            break;
        case DeliveryWay.ByFromEmpToEmp:
            roleName = "18.ByFromEmpToEmp.htm";
            break;
        case DeliveryWay.ByStationForPrj:
            roleName = "20.ByStationForPrj.htm";
            break;
        case DeliveryWay.BySelectedForPrj:
            roleName = "21.BySelectedForPrj.htm";
            break;
        case DeliveryWay.ByCCFlowBPM:
            roleName = "100.ByCCFlowBPM.htm";
            break;
        default:
            roleName = "0.ByStation.htm";
            break;
    }

    // alert(roleName);

    window.location.href = roleName + "?FK_Node=" + nodeID;
}
function SaveAndClose() {
    Save();
    window.close();
}

//打开窗体.
function OpenEasyUiDialogExt(url, title, w, h, isReload) {

    OpenEasyUiDialog(url, "eudlgframe", title, w, h, "icon-property", true, null, null, null, function () {
        if (isReload == true) {
            window.location.href = window.location.href;
        }
    });
}

//高级设置.
function AdvSetting() {

    var nodeID = GetQueryString("FK_Node");
    var url = "AdvSetting.htm?FK_Node=" + nodeID + "&M=" + Math.random();
    OpenEasyUiDialogExt(url, "高级设置", 600, 500, false);
}