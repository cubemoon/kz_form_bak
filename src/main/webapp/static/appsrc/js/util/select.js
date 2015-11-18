define([], function() {
    var module = {
        selectAction: function(item, callBack) {
            var optHide;
            item
                .on('mouseenter', '.f-select', function() {
                    clearTimeout(optHide);
                    item.find('.f-option-list').hide();
                    $(this).find('.f-option-list').show();
                })
                .on('mouseleave', '.f-select', function() {
                    var opt = $(this).find('.f-option-list');
                    optHide = setTimeout(function() {
                        opt.hide()
                    }, 1000);
                })
                .on('click', '[data-action=option]', function() {
                    var $emText = $(this).closest('.f-select').find('em');
                    var $options = $(this).closest('.f-option-list');
                    var thisVal = $(this).html();
                    var type = $(this).attr('data-type');
                    var id = $(this).attr('data-id');
                    var name = $(this).attr('option-name');
                    var pageId = $(this).attr('page-id');
                    var textType = $(this).attr('text-type');
                    var required = $(this).attr('data-required');
                    var selectType = $(this).attr('select-type');
                    $options.hide();
                    $emText.html(thisVal);
                    if (type) {
                        $emText.attr('data-type', type);
                    }
                    if (id) {
                        $emText.attr('data-id', id);
                    }
                    if (name) {
                        $emText.attr('option-name', name);
                    }
                    if(pageId){
                        $emText.attr('page-id', pageId);
                    }
                    if(textType){
                        $emText.attr('text-type', textType);
                    }
                    if(required){
                        $emText.attr('data-required', required);
                    }
                    if(selectType){
                        $emText.attr('select-type', selectType);
                    }
                    if (callBack) {
                        callBack(this);
                    }
                })
        }

    };

    return module;
});