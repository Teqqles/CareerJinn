$(document).ready(function() {
    $(".text").focus(function(src) {
        if ($(this).val() == $(this)[0].title) {
            $(this).removeClass("defaultTextActive");
            $(this).val("");
        }
    });

    $(".text").blur(function() {
        if ($(this).val() == "") {
            $(this).addClass("defaultTextActive");
            $(this).val($(this)[0].title);
        }
    });

    $(".text").blur();
});

