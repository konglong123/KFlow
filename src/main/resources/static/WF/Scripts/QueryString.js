//获取QueryString的数组 
function getQueryString() {
    var result = location.search.match(new RegExp("[\?\&][^\?\&]+=[^\?\&]+", "g"));
    for (var i = 0; i < result.length; i++) {
        result[i] = result[i].substring(1);
    }
    return result;
}
//根据QueryString参数名称获取值 
function getQueryStringByName(name) {
    var result = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    if (result == null || result.length < 1) {
        return "";
    }
    return result[1];
}
//根据QueryString参数索引获取值 
function getQueryStringByIndex(index) {
    if (index == null) {
        return "";
    }
    var queryStringList = getQueryString();
    if (index >= queryStringList.length) {
        return "";
    }
    var result = queryStringList[index];
    var startIndex = result.indexOf("=") + 1;
    result = result.substring(startIndex);
    return result;
}
//Other
function GetQueryString(name) {

    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");

    var r = window.location.search.substr(1).match(reg);

    if (r != null) return decodeURI(r[2]); return null;

}

//修改URL参数值
function replaceParamVal(url, paramName, replaceWith) {
    var re = eval('/(' + paramName + '=)([^&]*)/gi');
    var nUrl = url.replace(re, paramName + '=' + replaceWith);
    return nUrl;
}

//获取cookie中cname的值，获取后删除cookie
function getCookie(cname)
{
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0) {
            document.cookie = "copyFlow=; expires=Thu, 01 Jan 1970 00:00:00 GMT";//删除cookie
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

//封装消息提示代码，message为提示内容
function messageShow(message) {
    $.messager.show({
        title:'提示',
        msg:message,
        timeout:250,
        showType:'fade'
    });
}



