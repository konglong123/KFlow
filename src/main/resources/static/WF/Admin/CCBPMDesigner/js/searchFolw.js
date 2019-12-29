var rows = [];
$(function(){
    $('#dg').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/feign/getWF",
        queryParams: {
            abstracts: ""
        },
        columns:[[
            {field:'action',title: '操作',align: 'center',width:10,
                formatter:function(val,rec){
                    return "<input type='button' value='查看' onclick='gotoWorkflow(\""+rec.mysqlId+"\",\""+rec.name+"\")'/>";
                }},
            {field:'mysqlId',title: '流程编号',align: 'center',width:10},
            {field:'name',title: '流程名',align: 'center',width:10},
            {field:'abstracts',title: '功能性描述',align: 'center',width:10},
            {field:'score',title: '相似度分值',align: 'center',width:10},
            {field:'sensitiveHash',title: '局部敏感哈希',align: 'center',width:10},
            {field:'updateTime',title: '更新时间',align: 'center',width:10}
        ]]  
    });
});
function gotoWorkflow(id,name) {
    var url="/WF/WF/Admin/CCBPMDesigner/Designer.htm?FK_Flow="+id+"&UserNo=admin&SID=&Flow_V=1";
    window.parent.addTab(id, id+"."+name, url);
}
function queryWorkflow() {
    var queryParams = $('#dg').datagrid('options').queryParams;
    queryParams.abstracts = $("#abstractsText").val();
    $("#dg").datagrid('reload');
}
function resetText() {
    $("#abstractsText").val("");
}
function getWorkflowData() {
    var abstracts=$("#abstractsText").val();
    if (abstracts==null||abstracts=="")
        return;
    var result;
    $.ajax({
        type: 'post',
        async: false,
        url: "feign/getWF",
        dataType: 'json',
        data:{
            "abstracts": abstracts
        },
        success: function (data) {
            result=data;
        },
        error: function () {
            alert("请求数据出错！");
        }
    });


    for(var i=1; i<=80; i++){
        var amount = Math.floor(Math.random()*1000);
        var price = Math.floor(Math.random()*1000);
        rows.push({
            workflowNo: 'Inv No '+i,
            workflowName: 'Name '+i,
            abstracts: amount,
            score: price,
            updateTime: $.fn.datebox.defaults.formatter(new Date())
        });
    }
    return rows;
}


function iconAction() {

}
function parseDate() {

}