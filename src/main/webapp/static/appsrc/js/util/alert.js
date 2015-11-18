define(
['util/doT',
'text!template/alert.html!strip'],

function(doT, alertTpl) {
    var isShow;
    var alertTplFn;
    var isCenter;

    var scrollHandler,
        scrollTimeOut = 100;

    var $window;

    $window = $(window);

    isShow = false;
    alertTplFn = doT.template(alertTpl);


    return {
        show: function(option, confirmCb, cancelCb) {
            var alertEl;

            isCenter = option.isCenter;

            alertEl = $(alertTplFn({
                msg: option.msg || '确定要删除吗' ,
                InnerYesBtn: option.InnerYesBtn || '删除' ,
                InnerNoBtn: option.InnerNoBtn || '取消' ,
                confirmWidth:option.confirmWidth
            }));

            alertEl.appendTo($('body'));

            if (isCenter) {
                alertEl.css({
                    'position': 'fixed',
                    'margin-left': (-alertEl.outerWidth() / 2),
                    'margin-top': (-alertEl.outerHeight() / 2),
                    'top': '50%',
                    'left': '50%'
                });
            } else {
                alertEl.css({
                    'position': 'absolute',
                    'margin-left': (-alertEl.outerWidth() / 2),
                    'margin-top': -alertEl.outerHeight(),
                    'top': option.top,
                    'left': option.left
                });
            }

            alertEl.show();

            alertEl.on('click', '#yesBtn', function() {
                confirmCb();
                alertEl.remove();

                if (isCenter) {
                    $window.off('scroll.alert');
                }
            });
            alertEl.on('click', '#noBtn', cancelCb || function() {
                alertEl.remove();
                if (isCenter) {
                    $window.off('scroll.alert');
                }
            });
        }
    };
});
