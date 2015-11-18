~
function() {
    var site, siteUrl;
    var weiboAppKey;
    site = '搜狐快站';
    siteUrl = 'http://www.kuaizhan.com/';
    weiboAppKey = '992243007';
    function popWindow(targetLink, winStyle) {
        window.open(targetLink, '_blank', winStyle);
    }
    function prepareData(extraData) {
        var title, shortTitle, desc, url, pic, sel, content, data;
        title = document.title;
        shortTitle = encodeURIComponent('给大家分享一个不错的应用，“' + title + '”，欢迎下载安装。');
        desc = encodeURIComponent(title);
        url = encodeURIComponent(location.href);
        pic = encodeURIComponent("http://" + location.host + $('.QR-code').find('img').attr('src') + "?v=" + Date.now());
        sel = getSelectionContent();
        content = encodeURIComponent(sel.substr(0, 280));
        data = {
            title: title,
            shortTitle: shortTitle,
            desc: desc,
            url: url,
            pic: pic,
            sel: sel,
            content: content
        };
        $.extend(data, extraData);

        return data;
    }

    function getSelectionContent() {
        var content;

        if (window.getSelection) {
            content = '' + window.getSelection();
        } else if (document.getSelection) {
            content = '' + document.getSelection();
        } else {
            content = '' + document.selection.createRange().text;
        }

        return content;
    }

    function shareTo(witch, extraData) {
        var data;
        data = extraData;
        switch (witch) {
            case 'weibo':
                shareToWeibo(data);
                break;
            case 'qq':
                shareToQQ(data);
                break;
            case 'tweibo':
                shareToTweibo(data);
                break;

            default:
                break;
        }
    }

    function shareToWeibo(data) {
        var width, height, left, top, winStyle, targetLink;

        width = 440;
        height = 430;
        left = (screen.width - width) / 2;
        top = (screen.height - height) / 2;
        winStyle = ["toolbar=0,status=0,resizable=1,width=", width, ",height=", height, ",left=", left, ",top=", top].join('');

        targetLink = [
            'http://service.t.sina.com.cn/share/share.php?',
            'appkey=', weiboAppKey,
            '&title=', data.shortTitle,
            '&url=', data.url,
            '&pic=', data.pic
        ].join('');

        popWindow(targetLink, winStyle);
    }

    function shareToQQ(data) {
        var width, height, left, top, winStyle, targetLink;
        width = 700;
        height = 680;
        left = (screen.width - width) / 2;
        top = (screen.height - height) / 2;
        winStyle = ["toolbar=0,resizable=1,scrollbars=yes,status=1,width=", width, ",height=", height, ",left=", left, ",top=", top].join('');

        targetLink = [
            'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?',
            'url=', data.url,
            '&title=', data.shortTitle,
            '&site=', data.site,
            '&pics=', data.pic
            // '&desc=', data.desc,
            // '&summary=', data.desc
        ].join('');

        popWindow(targetLink, winStyle);
    }

    function shareToTweibo(data) {
        var width, height, left, top, winStyle, targetLink;
        width = 440;
        height = 430;
        left = (screen.width - width) / 2;
        top = (screen.height - height) / 2;
        winStyle = ["toolbar=0,resizable=1,scrollbars=yes,status=1,width=", width, ",height=", height, ",left=", left, ",top=", top].join('');

        targetLink = [
            'http://share.v.t.qq.com/index.php?c=share&a=index&',
            'url=', data.url,
            '&title=', data.shortTitle,
            '&site=', data.site,
            '&pic=', data.pic
            // '&desc=', data.desc,
            // '&summary=', data.desc
        ].join('');

        popWindow(targetLink, winStyle);
    }
    ~
    function bindEvent() {
        var $share = $('.share');
        $share.on('click', '[data-action]', function(e) {
						e.preventDefault();
            switch ($(this).attr('data-action')) {
                case 'share-weibo':
                    shareTo('weibo', prepareData());
                    break;
                case 'share-qzone':
                    shareTo('qq', prepareData());
                    break;
                case 'share-tweibo':
                    shareTo('tweibo', prepareData());
                    break;
                default:
                    break;
            }
        });
    }();
}();
