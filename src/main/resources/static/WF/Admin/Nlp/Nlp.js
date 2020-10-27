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
                    str+="<input type='button' value='删除'  onclick='deleteModel(\""+rec.id+"\")'/>";
                    if (rec.modelType==2)
                        str+="<input type='button' value='训练过程'  onclick='gotoModelDetail(\""+rec.id+"\")'/>";
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

function trainSegmentModel() {
    var url="/WF/WF/Admin/Nlp/ModelTrain.html?"
    window.parent.addTab("模型训练", url);
}

function trainWord2vecModel() {
    var url="/WF/WF/Admin/Nlp/ModelTrainWord2.html?"
    window.parent.addTab("模型训练", url);
}
function deleteModel(id) {
    $.ajax({
        url: "/WF/NLPModel/deletedModel",
        type: 'POST',
        dataType: 'json',
        data: {
            modelId:id
        },
        success: function (data) {
            messageShow("删除成功！");
            queryNlpModels();
        },
        error:function (data) {
            alert("删除失败！"+data);
        }
    });
}

function learnOnLine() {
    var url="/WF/WF/Admin/Nlp/SegmentModelLearn.html?"
    OpenEasyUiDialogExt(url,"在线学习", 800, 450, false);
}
function gotoModelDetail(id) {
    var url="/WF/WF/Comm/HighChart.html?modelId="+id;
    OpenEasyUiDialogExt(url,"准确度曲线", 800, 450, false);
}

function startTrain(type) {
    var trainFile=$("#trainFileTemp"+type).val().trim();
    if (trainFile==""){
        alert("请上传训练数据！");
        return;
    }
    if ($("#modelFileTrain"+type).val().trim()==""){
        alert("请指定模型文件名！");
        return;
    }

    var tempUrl="/WF/NLPModel";
    if (type==2)
        tempUrl+="/trainSegmentModel";
    else
        tempUrl+="/trainWord2vec";

    var nlpModel={
        name:$("#nameTrain"+type).val().trim(),
        numThreads:$("#numThread"+type).val().trim(),
        iterations:$("#iterations"+type).val().trim(),
        modelType:type,
        modelFile:$("#modelFileTrain"+type).val().trim(),
        testFile:$("#testFileTemp"+type).val().trim(),
        trainFile:$("#trainFileTemp"+type).val().trim(),
    };
    if (type==2){
        nlpModel.compressRate=$("#compressRate"+type).val().trim();
    }else if (type==1){
        nlpModel.neuralNetworkType=$("#neuralNetworkType").val().trim();
        nlpModel.learningRate=$("#learningRate").val().trim();
        nlpModel.dimensions=$("#dimensions").val().trim();
        nlpModel.negativeSamples=$("#negativeSamples").val().trim();
        nlpModel.layerSize=$("#layerSize").val().trim();
        nlpModel.isSoftmax=$("#isSoftmax").val().trim();
    }
    $.ajax({
        url: tempUrl,
        type: 'POST',
        dataType: 'json',
        contentType:'application/json',
        data: JSON.stringify(nlpModel),
        success: function (data) {
            if (type==2)
                showTrainProgress(data.progress);
            else
                alert("训练成功！");
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
function fileUpload(type) {
    if ($("#fileTrain"+type).val().trim()==""){
        alert("缺乏训练数据！");
        return;
    }

    var formData = new FormData();
    formData.append('file', $('#fileTrain'+type)[0].files[0]);
    $.ajax({
        url: '/WF/file/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false
    }).done(function(res) {
        $("#trainFileTemp"+type).val(res);
    }).fail(function(res) {
        messageShow("上传失败！");
    });

    if ($("#fileTest"+type).val().trim()!="") {
        var formData = new FormData();
        formData.append('file', $('#fileTest'+type)[0].files[0]);
        $.ajax({
            url: '/WF/file/upload',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false
        }).done(function(res) {
            $("#testFileTemp"+type).val(res);
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
    //启用后需要，更新
    initSystemModels();
    location.reload();
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
