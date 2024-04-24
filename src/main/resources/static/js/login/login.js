// ID 저장 스크립트
$(function() {
    fnInit();
});

function frm_check(){
    saveId();
}

function fnInit(){
    var cookieid = getCookie("username");
    if(cookieid !=""){
        $("input:checkbox[id='keep']").prop("checked", true);
        $('#username').val(cookieid);
    }
}

function saveId() {
    var expdate = new Date();
    if ($("#keep").is(":checked")){
        expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30);
        setCookie("username", $("#username").val(), expdate);
    }else{
       expdate.setTime(expdate.getTime() - 1000 * 3600 * 24 * 30);
       setCookie("username", $("#username").val(), expdate);
    }
}

function setCookie(name, value, expiredays) {
    var todayDate = new Date();
    todayDate.setTime(todayDate.getTime() + 0);
    if(todayDate > expiredays){
        document.cookie = name + "=" + escape(value) + "; path=/; expires=" + expiredays + ";";
    }else if(todayDate < expiredays){
        todayDate.setDate(todayDate.getDate() + expiredays);
        document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";";
    }
}

function getCookie(Name) {
    var search = Name + "=";

    if (document.cookie.length > 0) { // 쿠키가 설정되어 있다면
        offset = document.cookie.indexOf(search);
        if (offset != -1) { // 쿠키가 존재하면
            offset += search.length;
            // set index of beginning of value
            end = document.cookie.indexOf(";", offset);
            // 쿠키 값의 마지막 위치 인덱스 번호 설정
            if (end == -1)
                end = document.cookie.length;

            return unescape(document.cookie.substring(offset, end));
        }
    }
    return "";
}

$(document).on('keyup', '#joinId', function(key) {
    if( $('#joinId').val().length > 4 && $('#joinId').val().length < 21 ){
        $('#joinId').css('border-bottom','1px solid #7ac142');
        $('#joinIdRule').css('display','none');
        $('#joinIdFlag').val('true');
    } else {
        $('#joinId').css('border-bottom','1px solid #EF6161');
        $('#joinIdRule').css('display','block');
        $('#joinIdFlag').val('false');
    }

    const regExp = /[^0-9a-zA-Z]/g;
    if( regExp.test($('#joinId').val()) ) {
        $('#joinId').css('border-bottom','1px solid #EF6161');
        $('#joinIdRule').css('display','block');
        $('#joinIdFlag').val('false');
    }
});

$(document).on('blur', '#joinId', function(key) {
    $.ajax({
        type : "GET",
        url : "/join/confirm",
        data : { loginId : $("#joinId").val() },
    })
    .done(function(response) {
        if( response.result == false ){
            $('#joinIdDuplicate').css('display', 'block');
            $('#joinId').css('border-bottom','1px solid #EF6161');
            $('#joinIdFlag').val('false');
        } else {
            $('#joinIdDuplicate').css('display', 'none');
        }
    });
});

$(document).on('keyup', '#joinName', function(key) {
    if( $('#joinName').val().length > 1 && $('#joinId').val().length < 17 ){
        $('#joinName').css('border-bottom','1px solid #7ac142');
        $('#joinNameRule').css('display','none');
        $('#joinNameFlag').val('true');
    } else {
        $('#joinName').css('border-bottom','1px solid #EF6161');
        $('#joinNameRule').css('display','block');
        $('#joinNameFlag').val('false');
    }
});

$(document).on('keyup', '#joinPassword', function(key) {
    if( $('#joinPassword').val().length > 5 && $('#joinId').val().length < 17 ){
        $('#joinPassword').css('border-bottom','1px solid #7ac142');
        $('#joinPasswordRule').css('display','none');
        $('#joinPasswordFlag').val('true');
    } else {
        $('#joinPassword').css('border-bottom','1px solid #EF6161');
        $('#joinPasswordRule').css('display','block');
        $('#joinPasswordFlag').val('false');
    }
});

$(document).on('keyup', '#joinPasswordConfirm', function(key) {
    if( $('#joinPassword').val() == $('#joinPasswordConfirm').val() && $('#joinPasswordConfirm').val() != '' ){
        $('#joinPasswordConfirm').css('border-bottom','1px solid #7ac142');
        $('#joinPasswordConfirmRule').css('display','none');
        $('#joinPasswordConfirmFlag').val('true');
    } else {
        $('#joinPasswordConfirm').css('border-bottom','1px solid #EF6161');
        $('#joinPasswordConfirmRule').css('display','block');
        $('#joinPasswordConfirmFlag').val('false');
    }
    if( key.keyCode == 13 ) {
        $('#joinButton').click();
    }
});

$(document).on('click', '#joinButton', function() {
    var token = $("input[name='_csrf']").val();
    var header = $("input[name='_csrf_header']").val();

    let idValid = $('#joinIdFlag').val();
    let nameValid = $('#joinNameFlag').val();
    let passwordValid = $('#joinPasswordFlag').val();
    let passwordConfirmValid = $('#joinPasswordConfirmFlag').val();

    if( idValid && nameValid && passwordValid && passwordConfirmValid == 'true') {
        $('#joinButton').attr('disabled');
        $.ajax({
            type : "POST",
            url : "/join",
            async : false,
            data : {
                loginId : $("#joinId").val(),
                password : $("#joinPasswordConfirm").val(),
                name : $("#joinName").val()
            },
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            }
        })
        .done(function(response) {
            $('#joinForm').css('display', 'none');
            $('#joinCompleteForm').css('display', 'block');
            $('#joinId').val('');
            $('#joinName').val('');
            $('#joinIdFlag').val('false');
            $('#joinNameFlag').val('false');
            $('#joinPasswordFlag').val('false');
            $('#joinPasswordConfirmFlag').val('false');
        })
        .fail(function(xhr, status, error) {
            $('#joinConfirmRule').css('display', 'block');
            $('#joinButton').removeAttr('disabled');
        });
    } else {
        $('#joinConfirmRule').css('display', 'block');
    }

});