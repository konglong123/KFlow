function initNlpModels() {
    $('#dgNlpModels').datagrid({
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        pageSize: 10,
        pageList:[10,25,50,100],
        nowrap:false,//数据多行显示
        fitColumns:true,//表头与数据对齐
        url:"/WF/NLPModel/getNlpModelList",
        queryParams: {
            id:"",
            name:"",
            trainFile:"",
            modelFile:"",
            type:0
        },
        columns:[[
            {field:'id',title: '模型编码',align: 'center',width:10},
            {field:'name',title: '模型名',align: 'center',width:10},
            {field:'modelType',title: '类型',align: 'center',width:10,
                formatter:function (val) {
                    if (val==1)
                        return "word2vec";
                    else if (val==2)
                        return "分词器";
                }},
            {field:'correctRate',title: '正确率',align: 'center',width:10},
            {field:'modelFile',title: '模型文件',align: 'center',width:20},
            {field:'trainFile',title: '训练数据',align: 'center',width:20},
            {field:'compressRate',title: '特征压缩率',align: 'center',width:10},
            {field:'action',title: '操作',align: 'center',width:20,
                formatter:function(val,rec){
                    var str="<input type='button' value='启用'  onclick='loadModel(\""+rec.id+"\")'/>";
                    str+="<input type='button' value='详情'  onclick='gotoModelDetail(\""+rec.id+"\")'/>";
                    return str;
                }},

        ]]
    });
}
function queryNlpModels() {
    var queryParams = $('#dgNlpModels').datagrid('options').queryParams;
    queryParams.id = $("#nlpModelIdQuery").val().trim();
    queryParams.name =$("#nameQuery").val().trim();
    queryParams.trainFile =$("#trainFileQuery").val().trim();
    queryParams.modelFile =$("#modelFileQuery").val().trim();
    queryParams.type =$("#typeQuery").val().trim();
    $("#dgNlpModels").datagrid('reload');
}

function trainModel() {
    var url="/WF/WF/Admin/Nlp/ModelTrain.html?"
    window.parent.addTab("模型训练", url);
}

function learnOnLine() {
    var url="/WF/WF/Admin/Nlp/SystemModelInfo.html?"
    OpenEasyUiDialogExt(url,"在线学习", 800, 450, true);
}
function gotoModelDetail(id) {
    var url="/WF/WF/Comm/HighChart.html?modelId="+id;
    OpenEasyUiDialogExt(url,"在线学习", 800, 450, true);;
}

function startTrain() {
    var trainFile=$("#trainFileTemp").val().trim();
    if (trainFile==""){
        alert("请上传训练数据！");
        return;
    }
    if ($("#modelFileTrain").val().trim()==""){
        alert("请指定模型文件名！");
        return;
    }

    var tempUrl="/WF/NLPModel";
    var type=$("#typeTrain").val();
    if (type==2)
        tempUrl+="/trainSegmentModel";
    else
        tempUrl+="/trainWord2vec";

    var nlpModel={
        name:$("#nameTrain").val().trim(),
        numThreads:$("#numThread").val().trim(),
        compressRate:$("#compressRate").val().trim(),
        iterations:$("#iterations").val().trim(),
        modelType:type,
        modelFile:$("#modelFileTrain").val().trim(),
        testFile:$("#testFileTemp").val().trim(),
        trainFile:$("#trainFileTemp").val().trim(),
    };
    $.ajax({
        url: tempUrl,
        type: 'POST',
        dataType: 'json',
        contentType:'application/json',
        data: JSON.stringify(nlpModel),
        success: function (data) {
            showTrainProgress(data.progress);
        },
        error:function (data) {
            alert("训练失败");
        }
    });
}

function resetText() {
    $("#learnSentence").val("");
    $("#segmentSentence").val("");
}
function fileUpload() {
    if ($("#fileTrain").val().trim()==""){
        alert("缺乏训练数据！");
        return;
    }
    var formData = new FormData();
    formData.append('file', $('#fileTrain')[0].files[0]);
    $.ajax({
        url: '/WF/file/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false
    }).done(function(res) {
        $("#trainFileTemp").val(res);
    }).fail(function(res) {
        messageShow("上传失败！");
    });

    if ($("#fileTest").val().trim()!="") {
        var formData = new FormData();
        formData.append('file', $('#fileTest')[0].files[0]);
        $.ajax({
            url: '/WF/file/upload',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false
        }).done(function(res) {
            $("#testFileTemp").val(res);
        }).fail(function(res) {
            messageShow("上传失败！");
        });
    }
    messageShow("上传成功！");
}
function showTrainProgress(context) {
    var chart = Highcharts.chart('container', {
        chart: {
            type: 'line'
        },
        title: {
            text: '正确率'
        },
        plotOptions: {
            series: {
                label: {
                    connectorAllowed: false
                },
                pointStart: 1
            }
        },
        credits: {
            enabled: false//除去highcharts.com
        },
        xAxis:{
            title: {
                text: '迭代次数'
            }
        },
        yAxis: {
            title: {
                text: '正确率'
            }
        },
        series: [{
            name: '正确率曲线',
            data: []
        }]
    });
    chart.series[0].update({
        data:context
    });

}

function learnModel() {
    var sentence=$("#learnSentence").val().trim();
    $.ajax({
        url: "/WF/NLPModel/learnWord",
        type: 'POST',
        dataType: 'json',
        data: {
            sentence:sentence
        },
        success: function (data) {
            messageShow("模型更新成功！");
        },
        error:function (data) {
            alert("训练失败");
        }
    });
}
function saveModel(type) {
    debugger
    $.ajax({
        url: "/WF/NLPModel/saveNLPModel",
        type: 'POST',
        dataType: 'json',
        data: {
            modelType:type
        },
        success: function (data) {
            messageShow("模型持久化成功!");
        },
        error:function (data) {
            alert("保存失败！");
        }
    });
}

function loadModel(id) {
    $.ajax({
        url: "/WF/NLPModel/loadModel",
        type: 'POST',
        dataType: 'json',
        data: {
            modelId:id
        },
        success: function (data) {
            messageShow("启用成功！");
        },
        error:function (data) {
            alert("启用失败！");
        }
    });
}
function segmentSentence() {
    var sentence=$("#segmentSentence").val().trim();
    $.ajax({
        url: "/WF/NLPModel/segmentSentence",
        type: 'POST',
        dataType: 'json',
        data: {
            sentence:sentence
        },
        success: function (data) {
            $("#learnSentence").val(data);
        },
        error:function (data) {
            alert("分词失败！");
        }
    });

}

function initSystemModels() {
    $.ajax({
        url: "/WF/NLPModel/getSysNlpModel",
        type: 'POST',
        dataType: 'json',
        data: {},
        success: function (data) {
            $("#systemSegment").val(data.segment);
            $("#systemWord2vec").val(data.word2vec);
        },
        error:function (data) {
            messageShow("获取系统模型失败！");
        }
    });
}

function getModelTrainData(id) {
    $.ajax({
        url: "/WF/NLPModel/getModelTrainData",
        type: 'POST',
        dataType: 'json',
        data: {
            modelId:id
        },
        success: function (data) {
            var context=JSON.parse(data.progress);
            showTrainProgress(context);
        },
        error:function (data) {
            messageShow("获取系统模型失败！");
        }
    });
}
