function getJudgeBean() {
    
}
function initJudgeRuleDg() {
    $("#dgJudgeRule").datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//宽度自适应
        url:"/WF/judge/findJudgeRule",
        queryParams: {
            type:"",
            no:"",
            alias:""
        },
        loader : function(param, success, error) {
            $.ajax({
                type : 'post',
                url : '/WF/judge/findJudgeRule',
                dataType : 'json',
                contentType : 'application/json;charset=utf-8', // 设置请求头信息
                data : JSON.stringify(param),
                success : function(result) {
                    success(result);
                }
            });
        },
        columns:[[
            {field:'no',title: '规则编码',align: 'center',width:10},
            {field:'alias',title: '别名',align: 'center',width:10},
            {field:'type',title: '类型',align: 'center',width:10,
                formatter:function(val,rec){
                    if (val==1)
                        return "表达式";
                    else if (val==2)
                        return "Bean";
                }},
            {field:'beanId',title: 'BeanId',align: 'center',width:10,
                formatter: function (value) {
                    return "<span title='" + value + "'>" + value + "</span>"}},
            {field:'expression',title: '表达式',align: 'center',width:10,
                formatter: function (value) {
                    return "<span title='" + value + "'>" + value + "</span>"}},
            {field:'context',title: '备注',align: 'center',width:10},
            {field:'creator',title: '创建者',align: 'center',width:10},
            {field:'isTest',title: '检验状态',align: 'center',width:10,
                formatter:function(val,rec){
                    if (val==1)
                        return "已检验";
                    return "未检验";
                }},

            {field:'action',title: '操作',align: 'center',width:20,
                formatter:function(val,rec){
                    var str="<input type='button' value='详情' onclick='gotoJudgeDetail(\""+rec.no+"\")'/>";
                    str+="<input type='button' value='启用规则' onclick='addJudge(\""+rec.no+"\")'/>";
                    if (rec.type==1)
                        str+="<input type='button' value='检验表达式' onclick='checkJudgeExpression(\""+rec.no+"\")'/>";
                    return str;
                }},
        ]]
    })
}

function initDgJudgeRuleNode(nodeId) {
    $("#dgJudgeRuleNode").datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        queryParams: {
            nodeId: nodeId
        },
        loader : function(param, success, error) {
            $.ajax({
                type : 'post',
                url : '/WF/judge/findNodeRule',
                dataType : 'json',
                contentType : 'application/json;charset=utf-8', // 设置请求头信息
                data : JSON.stringify(param),
                success : function(result) {
                    success(result);
                }
            });
        },
        columns:[[
            {field:'no',title: '规则配置编码',align: 'center',width:100},
            {field:'nodeId',title: '所属节点',align: 'center',width:100},
            {field:'nextNodeId',title: '流向节点',align: 'center',width:100},
            {field:'ruleNo',title: '规则编码',align: 'center',width:100},
            {field:'action',title: '操作',align: 'center',width:100,
                formatter:function(val,rec){
                    var str="<input type='button' value='规则详情' onclick='gotoJudgeDetail(\""+rec.ruleNo+"\")'/>";
                    var str="<input type='button' value='删除' onclick='deleteNodeRule(\""+rec.no+"\")'/>";
                    return str;
                }},
        ]]
    })
}

function gotoJudgeDetail(No) {
    var enName = "BP.Judge.JudgeRule";
    var url = "../../../Comm/En.htm?EnName=" + enName + "&PKVal=" + No;
    OpenEasyUiDialogExt(url,"决策规则", 800, 450, false);
}
function queryJudge() {
    var queryParams = $('#dgJudgeRule').datagrid('options').queryParams;
    queryParams.no=$("#judgeNo").val().trim();
    queryParams.type=$("#typeJudge").val().trim();
    queryParams.alias=$("#judgeAlias").val().trim();
    $("#dgJudgeRule").datagrid('reload');
}
function addJudge(no) {
    var nodeId=GetQueryString("nodeId");
    var url = "../../../Admin/AttrNode/RunModel/NextNodeList.html?nodeId=" + nodeId + "&ruleNo=" + no;
    OpenEasyUiDialogExt(url,"启用规则", 800, 450, true);
}
function checkJudgeExpression(no) {
    $.ajax({
        url: "/WF/judge/testExpression",
        type: 'POST',
        data: {
            no:no
        },
        success: function (data) {
            if (data.success){
                alert("检验通过！");
                $("#dgJudgeRule").datagrid('reload');
            } else
                alert("检验未通过，请检查表达式");

        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
function deleteNodeRule(no) {
    $.ajax({
        url: "/WF/judge/deleteNodeRule",
        type: 'post',
        data: {
            no:no
        },
        success: function (data) {
            $("#dgJudgeRuleNode").datagrid('reload');
        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}

function initDgNextNode() {
    var nodeId=GetQueryString("nodeId");
    var ruleNo=GetQueryString("ruleNo");
    $("#dgNextNode").datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url: "/WF/flow/getAfterNodes",
        queryParams: {
            nodeId: nodeId
        },
        columns:[[
            {field:'NodeID',title: '节点编码',align: 'center',width:100},
            {field:'Name',title: '节点名',align: 'center',width:100},
            {field:'action',title: '操作',align: 'center',width:100,
                formatter:function(val,rec){
                    var str="<input type='button' value='应用规则' onclick='addNodeRule(\""+rec.NodeID+"\")'/>";
                    return str;
                }},
        ]]
    })
}

function addNodeRule(nextNodeId) {
    var ruleNo=GetQueryString("ruleNo");
    var nodeId=GetQueryString("nodeId");
    var nodeRule={
        nodeId:nodeId,
        ruleNo:ruleNo,
        nextNodeId:nextNodeId
    };
    $.ajax({
        url: "/WF/judge/addNodeRule",
        type: 'POST',
        dataType: 'json',
        contentType:'application/json',
        data: JSON.stringify(nodeRule),
        success: function (data) {
            if (data.success)
                alert("启用成功！");
            else
                alert(data.message);

        },
        error: function (data) {
            console.log("error"+data);
        }
    });
}
