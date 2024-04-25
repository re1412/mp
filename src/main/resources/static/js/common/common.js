$(document).ready(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });

});

$.fn.serializeObject = function() {
    "use strict"
    var result = {};
    var extend = function(i, element) {
        var node = result[element.name];
        if ("undefined" !== typeof node && node !== null) {
            if ($.isArray(node)) {
                node.push(element.value);
            } else {
                result[element.name] = [node]
                result[element.name].push(element.value);
            }
        } else {
            result[element.name] = [element.value];
        }
    }

    $.each(this.serializeArray(), extend);
    return result;
}

$.fnIsEmpty = function(str){
    var newStr = str;
    if(newStr == null || newStr == "" || newStr == undefined || newStr == "undefined"){
        return true;
    }
    return false;
}

$.fnIsEmptyMsg = function(str, name){
    var newStr = str;
    if(newStr == null || newStr == "" || newStr == undefined || newStr == "undefined"){
        alert(name+'을(를) 확인해주세요.');
        return true;
    }
    return false;
}

function encryptValue(value) {
    // 여기에 암호화 로직을 구현합니다.
    // 예를 들어, sha256 알고리즘을 사용한다면 CryptoJS 라이브러리를 사용할 수 있습니다.
    var encrypted = CryptoJS.SHA256(value).toString(CryptoJS.enc.Hex);
    return encrypted;
}