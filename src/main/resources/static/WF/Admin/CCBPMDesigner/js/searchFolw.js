var rows = [];
$(function(){
    $('#dg').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        url:"/WF/feign/getWF",
        queryParams: {
            abstracts: "国家"
        }
    });
});

function queryWorkflow() {
    var abstracts=$.trim($("#abstractsText").val());
    if (abstracts==null||abstracts==""){
        alert("请输入流程的功能性描述！");
        return;
    }
    $('#dg').datagrid({loadFilter:pagerFilter}).datagrid('loadData', getWorkflowData());

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