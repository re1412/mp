$(document).on('click', '#quizManagement', function(){
    $.ajax({
        type : "GET",
        url : "/quiz/new"
    })
    .done(function(data) {
        $('.content').replaceWith(data);
    })
});

$(document).on('click','#insertButton',function(){
    const button = $(this);
    button.prop('disabled', true);

    var insertData = $('#insertData').serializeObject();
    let insertFlag = false;
    for(let i = 0; i < insertData.gameId.length; i++){
        if( insertData.gameId[i] == '0' && insertData.question[i] != '' && insertData.answer[i] != '' ) {
            insertFlag = true;
            break;
        }
    }
    if( insertFlag == false ) return false;

    $.ajax({
        type : "POST",
        url : "/quiz/new",
        contentType: "application/json",
        dataType  : "json",
        data : JSON.stringify(insertData)
    })
    .done(function(data) {
        alert(data.resultMsg);
        location.href="/quiz";
    })
    .fail(function(data) {
        alert(data.resultMsg);
        button.prop('disabled', false);
    });
});

$(document).on('click','#addButton',function(){
    const userId = $('#userId').val();
    $('tbody[name="quizData"]').append('<tr>'+
        '<td class="write-form-td"><input type="text" name="question" class="add-input" /></td>'+
        '<td class="write-form-td"><input type="text" name="answer" class="add-input" /></td>'+
        '<td class="write-form-td"><input type="text" name="hint" class="add-input" /></td>'+
        '<td class="write-form-td">'+
            '<input name="userId" type="hidden" value="' + userId + '"/>'+
            '<input name="gameId" type="hidden" value="0"/>'+
        '</td>'+
    '</tr>');
});

$(document).on('click','button[name="updateButton"]',function(){
    const button = $(this);
    if( $.fnIsEmptyMsg(button.closest('tr').find('input[name="question"]').val(), 'Question') ) return false;
    if( $.fnIsEmptyMsg(button.closest('tr').find('input[name="answer"]').val(), 'Answer') ) return false;

    button.prop('disabled', true);
    const insertData = {
        question: button.closest('tr').find('input[name="question"]').val(),
        answer: button.closest('tr').find('input[name="answer"]').val(),
        hint: button.closest('tr').find('input[name="hint"]').val(),
        hintId: button.closest('tr').find('input[name="hintId"]').val(),
        gameId: button.siblings('input[name="gameId"]').val()
    };

    $.ajax({
        type : "PATCH",
        url : "/quiz/new",
        contentType: "application/json",
        dataType  : "json",
        data : JSON.stringify(insertData)
    })
    .done(function(data) {
        if( data.resultCode == '1' ){
            button.closest('tr').find('input[name="hintId"]').val(data.resultHintId);
        }
        alert(data.resultMsg);
        button.prop('disabled', false); // AJAX 호출 완료 후 버튼 활성화
    })
    .fail(function(data) {
        alert(data.resultMsg);
        button.prop('disabled', false); // AJAX 호출 완료 후 버튼 활성화
    });
});

$(document).on('click','button[name="deleteButton"]',function(){
    const button = $(this);
    button.prop('disabled', true);
    const gameId = button.siblings('input[name="gameId"]').val();

    let warning = confirm("삭제하시겠습니까?");
    if(warning == true){
        button.prop('disabled', true);
        $.ajax({
            type : "DELETE",
            url : "/quiz/new",
            contentType: "application/json",
            data: JSON.stringify(gameId),
        })
        .done(function(data) {
            button.closest('tr').remove();
        })
        .fail(function(data) {
            alert(data.resultMsg);
            button.prop('disabled', false); // AJAX 호출 완료 후 버튼 활성화
        })
    }
});