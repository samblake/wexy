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

function go(form, button, editor) {

    var regex = /\{(.+?)}/g;
    var template = form.action;
    var match = regex.exec(template);
    while (match != null) {
        var name = match[1];
        var value = $(form).find('input[name=' + name + ']').val();
        template = template.replace(match[0], value);
        match = regex.exec(template);
    }

    $(button).addClass('is-loading');

    fetch(template, {
            method: form.method,
            headers: new Headers({
                "X-Requested-With": "Wexy"
            })
        })
        .then(function(data) {
            // TODO handle other types
            return data.json();
        })
        .then(function (json) {
            $(button).removeClass('is-loading');
            var jsonString = JSON.stringify(json, null, 4)
            editor.setValue(jsonString);
        })
        .catch(function (error) {
            console.log('Request failed', error);
            $(button).removeClass('is-loading');
            editor.setValue('');

            bulmaToast.toast({
                message: "Request failed",
                type: "is-danger",
                animate: { in: 'fadeIn', out: 'fadeOut' }
            });
        });

}

function makeEditor(element) {
    var editor = editify(element);
    editor.setOptions({
        autoScrollEditorIntoView: true,
        maxLines: 10
    });

    var input = $(element).next();
    editor.getSession().on('change', function() {
        input.val(editor.getSession().getValue());
    });
}

function makeViewer(element) {
    var editor = editify(element);
    editor.setOptions({
        autoScrollEditorIntoView: true,
        maxLines: 10,
        readOnly: true,
        highlightActiveLine: false,
        highlightGutterLine: false
    });
}

function editify(element) {
    var editor = ace.edit(element.id);
    editor.setTheme("ace/theme/chrome");
    editor.session.setMode("ace/mode/json")
    editor.setShowPrintMargin(false);
    editor.renderer.setScrollMargin(10, 10);

    element.editor = editor;
    return editor;
}