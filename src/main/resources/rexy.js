$(function() {
    var tabButtons = $(".tabs li");
    var tabs = $("#tabs>div");

    tabButtons.on('click', function(event) {
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

function go(form, template) {

    var regex = /\{(.+?)}/g;
    var match = regex.exec(template);
    while (match != null) {
        var name = match[1];
        var value = $(form).find('input[name=' + name + ']').val();
        template = template.replace(match[0], value);
        match = regex.exec(template);
    }

    fetch(template, {
            method: form.method,
            headers: new Headers({
                "X-Requested-With": "Wexy"
            })
        })
        .then(function (data) {
            console.log('Request succeeded with JSON response', data);
        })
        .catch(function (error) {
            console.log('Request failed', error);
        });

}