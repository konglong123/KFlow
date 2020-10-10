var rows = [];
$(function(){
    var nodeId=GetQueryString("FK_Node");
    var nodeName=GetQueryString("NodeName");
    initDgResourcePlan(nodeId);
    initDgSearchResource(nodeName);

});
function initDgSearchResource(abstracts) {
    $('#dgSearchResource').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/feign/getResources",
        queryParams: {
            abstracts: abstracts,
            resourceNo:""
        },
        columns:[[
            {field:'no',title: '资源编号',align: 'center',width:10},
            {field:'name',title: '资源名',align: 'center',width:10},
            {field:'kind',title: '类别',align: 'center',width:10,
            formatter:function (val,rec) {
                if (val==1)
                    return '人力';
                else if (val==2)
                    return '设备';
                else if (val==3)
                    return '环境';
                else if (val==4)
                    return '知识';
            }},
            {field:'deptId',title: '所属单位编号',align: 'center',width:10},
            {field:'score',title: '推荐值',align: 'center',width:10},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='资源每日负载' onclick='resourceLoad(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='资源甘特图' onclick='resourceGant(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='预定' onclick='bookResource(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='性能详情' onclick='gotoResourceDetail(\""+rec.no+"\")'/>";
                    return str;
                }},



        ]]
    });
}
function initDgNodeResource(nodeId,planId) {
    $('#dgNodeResource').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/resource/getNodeResources",
        queryParams: {
            nodeId: nodeId,
            planId:planId
        },
        columns:[[
            {field:'resource_no',title: '资源编号',align: 'center',width:10},
            {field:'book_start',title: '预定开始时间',align: 'center',width:15},
            {field:'book_end',title: '预定结束时间',align: 'center',width:15},
            {field:'use_time',title: '预占用时间(h)',align: 'center',width:15},
            {field:'is_plan',title: '是否计划',align: 'center',width:10,
                formatter:function(val,rec){
                    if (val==1){
                        return "已计划";
                    }else {
                        return "未计划";
                    }
                }},
            {field:'plan_start',title: '计划开始时间',align: 'center',width:15},
            {field:'plan_end',title: '计划结束时间',align: 'center',width:15},
            {field:'start_time',title: '实际开始时间',align: 'center',width:15},
            {field:'end_time',title: '实际结束时间',align: 'center',width:15},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='资源负载' onclick='resourceLoad(\""+rec.resource_no+"\")'/>";
                    str+="<input type='button' value='性能详情' onclick='gotoResourceDetail(\""+rec.resource_no+"\")'/>";
                    str+="<input type='button' value='放弃资源' onclick='deleteResource(\""+rec.No+"\")'/>";
                    return str;
                }},
        ]]
    });
}
function bookResource(no) {
    var row = $('#dgNodeResourcePlans').datagrid('getSelected');
    if (row==null){
        alert("请选中资源方案！");
        return;
    }
    var nodeId=GetQueryString("FK_Node");
    var url="../../../Admin/AttrNode/Resources/ResourceBook.html?resourceNo="+no+"&nodeId="+nodeId+"&planId="+row.No ;
    OpenEasyUiDialogExt(url,"资源预定", 400, 350, true);
}

function gotoResourceDetail(pkVal) {
    var enName = "BP.Resource.Resource";
    var url = "../../../Comm/En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url,"资源性能", 800, 450, false);
}
function queryResources() {
    //(后台代码中)优先采用编码查询
    var queryParams = $('#dgSearchResource').datagrid('options').queryParams;
    queryParams.abstracts = $("#resourcesText").val();
    queryParams.resourceNo=$("#resourceNoQuery").val();
    $("#dgSearchResource").datagrid('reload');
}
function resetText() {
    $("#resourcesText").val("");
    $("#resourceNoQuery").val("");
    $("#resourceNameQuery").val("");
}
function resourceLoad(no) {
    var url="../../../Admin/AttrNode/Resources/ResourceLoad.html?resourceNo="+no;
    OpenEasyUiDialogExt(url,"资源负载", 1200, 700, false);
}

function resourceGant(no) {
    var url="../../../Admin/AttrNode/Resources/ResourceLoadGant.html?resourceNo="+no;
    OpenEasyUiDialogExt(url,"资源负载", 1200, 700, false);
}
function deleteResource(no) {
    $.ajax({
        url: "/WF/resource/deleteResourceTask",
        type: 'POST',
        data: {
            no:no
        },
        success: function (data) {
            $('#dgNodeResource').datagrid('reload');
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
function initDgResourcePlan(nodeId) {
    $('#dgNodeResourcePlans').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/resource/getNodeResourcePlans",
        onClickRow: function (index, row) {
            initDgNodeResource(row.nodeId,row.No);
        },
        queryParams: {
            nodeId: nodeId
        },
        columns:[[
            {field:'No',title: '资源方案编码',align: 'center',width:10},
            {field:'nodeId',title: '节点编码',align: 'center',width:10},
            {field:'priority',title: '优先级',align: 'center',width:10},
            {field:'detail',title: '备注',align: 'center',width:10},
            {field:'creator',title: '创建人',align: 'center',width:10},
            {field:'operation',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='删除方案' onclick='deleteResourcePlan(\""+rec.No+"\")'/>";
                    str+="<input type='button' value='详情' onclick='gotoResourcePlanDetail(\""+rec.No+"\")'/>";
                    return str;}},
            ]]
    });
}

function addResourcePlan() {

    var nodeId=GetQueryString("FK_Node");
    $.ajax({
        url: "/WF/resource/addResourcePlan",
        type: 'POST',
        data: {
            nodeId:nodeId
        },
        success: function (data) {
            $('#dgNodeResourcePlans').datagrid('reload');
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
function deleteResourcePlan(no) {
    $.ajax({
        url: "/WF/resource/delResourcePlan",
        type: 'POST',
        data: {
            no:no
        },
        success: function (data) {
            alert("删除成功！");
            $('#dgNodeResourcePlans').datagrid('reload');

        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}

function gotoResourcePlanDetail(pkVal) {
    var enName = "BP.Resource.ResourcePlan";
    var url = "../../../Comm/En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url,"节点资源方案", 800, 450, false);
}