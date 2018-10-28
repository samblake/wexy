$(function() {
    var tabButtons = $(".tabs li");
    var tabs = $("#tabs>div");
    $(".tabs li").on('click', function(event) {
        var activeIndex = $(this).index();
        tabButtons.each(function(index, value, array) {
            if (index === activeIndex) {
                $(value).addClass("is-active");
            }
            else {
                $(value).removeClass("is-active");
            }
        });
        tabs.each(function(index, value, array) {
            if (index === activeIndex) {
                $(value).removeClass("is-hidden");
            }
            else {
                $(value).addClass("is-hidden");
            }
        });
    });
});