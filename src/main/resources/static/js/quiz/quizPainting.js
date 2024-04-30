$(document).on('change', '#attach', function(){
    var fileName = $('#attach')[0].files[0].name;
    if($.fnIsEmpty(fileName)) return false;
    $(this).next('span').text(fileName);
});

$(document).on('click', '.menu-link', function(){
    alert('시간날 때 추가함');
});

$(document).on('click', '#insertPainting', function(){
    console.log('aaa');
});