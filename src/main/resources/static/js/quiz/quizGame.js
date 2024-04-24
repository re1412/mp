$(document).on('keyup','input[name="gameAnswer"]',function(key){
    if( key.keyCode == 13 ) {
        const input = $(this);
        if( $.fnIsEmpty(input.val()) ) return false;
        input.prop('disabled', true);

        let inputAnswer = CryptoJS.SHA256(input.val());
        let answer = input.closest('.quiz-form').find('input[name="encryptedAnswer"]').val();

        if( inputAnswer != answer) {
            input.prop('disabled', false);
            input.css('border', 'solid 2px red');
            input.val('');
            input.focus();
            return false;
        }

        const insertData = {
            gameId: input.siblings('input[name="gameId"]').val(),
            answer: input.val(),
            userId: $('#userId').val()
        };
        console.log(insertData);
        $.ajax({
            type : "POST",
            url : "/quiz/rank",
            contentType: "application/json",
            dataType  : "json",
            data : JSON.stringify(insertData)
        })
        .done(function(data) {
            input.closest('.quiz-form').find('span[name="solver"]').text(data.solver);
            input.closest('.quiz-form').find('span[name="plainAnswer"]').text(data.answer);
            $('#userPoint').text( Number($('#userPoint').text()) + Number(data.point) );
            input.css('border', 'solid 2px #98A81B');
            input.css('display', 'none');
        })
        .fail(function(data) {
            alert(data.resultMsg);
            input.prop('disabled', false);
        });
    }
});

$(document).on('focusout','input[name="gameAnswer"]',function(){
    $(this).css('border', 'solid 2px #98A81B');
});