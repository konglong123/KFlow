var rows = [];
$(function(){
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
            abstracts: ""
        },
        columns:[[
            {field:'no',title: '资源编号',align: 'center',width:10},
            {field:'name',title: '资源名',align: 'center',width:10},
            {field:'kind',title: '类别',align: 'center',width:10},
            {field:'deptId',title: '所属单位编号',align: 'center',width:10},
            {field:'score',title: '推荐值',align: 'center',width:10},
            {field:'action',title: '操作',align: 'center',width:50,
                formatter:function(val,rec){
                    var str="<input type='button' value='使用情况' onclick='resourceLoad(\""+rec.id+"\")'/>";
                    str+="<input type='button' value='预定' onclick='bookResource(\""+rec.id+"\")'/>";
                    str+="<input type='button' value='性能详情' onclick='gotoResourceDetail(\""+rec.id+"\")'/>";
                    return str;
                }},



        ]]
    });
});
function bookResource(id) {
    var url="WF/WF/Comm/EnOnly.htm?EnName=BP.WF.Template.FrmNodeComponent&PKVal="+id;
    window.parent.addTab(id,url);
}

function gotoResourceDetail(pkVal) {
    var enName = "BP.WF.Port.Resource";
    var url = "En.htm?EnName=" + enName + "&PKVal=" + pkVal;
    OpenEasyUiDialogExt(url, this.document.title + ' : 详细', 800, 450, true);
}
function queryResources() {
    var queryParams = $('#dgSearchResource').datagrid('options').queryParams;
    queryParams.abstracts = $("#resourcesText").val();
    $("#dgSearchResource").datagrid('reload');
}
function resetText() {
    $("#resourcesText").val("");
}
function resourceLoad(id) {
    var url="/WF/WF/Admin/AttrNode/Resources/ResourceLoad.htm?FK_Flow="+id+"&UserNo=admin&SID=&Flow_V=1";
    window.parent.addTab(id,"资源负载", url);
}

