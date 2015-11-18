define([], function() {

    var weiboAppKey;

    weiboAppKey = '992243007';

    function popWindow(targetLink, winStyle) {
        window.open(targetLink, '_blank', winStyle);
    }

    // function prepareData(extraData) {
    //     var title, shortTitle, desc, url, pic, sel, content, data;

    //     title = document.title;
    //     shortTitle = encodeURIComponent(title);
    //     desc = title;
    //     url = encodeURIComponent(location.href);
    //     pic = [];
    //     sel = getSelectionContent();
    //     content = encodeURIComponent(sel.substr(0, 280));

    //     data = {
    //         title: title,
    //         shortTitle: shortTitle,
    //         desc: desc,
    //         url: url,
    //         pic: pic,
    //         sel: sel,
    //         content: content
    //     };

    //     $.extend(data, extraData);

    //     return data;
    // }

    // function getSelectionContent() {
    //     var content;

    //     if (window.getSelection) {
    //         content = '' + window.getSelection();
    //     } else if (document.getSelection) {
    //         content = '' + document.getSelection();
    //     } else {
    //         content = '' + document.selection.createRange().text;
    //     }

    //     return content;
    // }

    function shareTo(witch, data) {
        var data;


        switch (witch) {
            case 'weibo':
                shareToWeibo(data);
                break;

            case 'renren':
                shareToRenren(data);
                break;

            case 'qq':
                shareToQQ(data);
                break;
            case 'sohu':
                shareToSohu(data);
                break;
            case 'douban':
                shareToDouban(data);
                break;
                case 'qwb':
                shareToTweibo(data);
                break;
            default:
                break;
        }
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
            'url=', encodeURIComponent(data.url),
            '&title=', encodeURIComponent(data.title),
            '&pic=', encodeURIComponent(data.pic)
            // '&desc=', data.desc,
            // '&summary=', data.desc
        ].join('');

        popWindow(targetLink, winStyle);
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
            '&title=', encodeURIComponent(data.title),
            '&url=', encodeURIComponent(data.url),
            '&pic=', encodeURIComponent(data.pic)
        ].join('');

        popWindow(targetLink, winStyle);
    }


    function shareToRenren(data) {
        var width, height, left, top, winStyle, targetLink;

        width = 540;
        height = 480;
        left = (screen.width - width) / 2;
        top = (screen.height - height) / 2;
        winStyle = ["toolbar=0,status=0,resizable=1,width=", width, ",height=", height, ",left=", left, ",top=", top].join('');

        targetLink = [
            'http://widget.renren.com/dialog/share?',
            'resourceUrl=', encodeURIComponent(data.url),
            '&title=', encodeURIComponent(data.title),
            '&pic=', encodeURIComponent(data.pic)
        ].join('');

        popWindow(targetLink, winStyle);
    }

    function shareToQQ(data) {
        var width, height, left, top, winStyle, targetLink;

        width = 540;
        height = 480;
        left = (screen.width - width) / 2;
        top = (screen.height - height) / 2;
        winStyle = ["toolbar=0,resizable=1,scrollbars=yes,status=1,width=", width, ",height=", height, ",left=", left, ",top=", top].join('');

        targetLink = [
            'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?',
            'url=', encodeURIComponent(data.url),
            '&title=', encodeURIComponent(data.title),
            '&pics=', encodeURIComponent(data.pic)
        ].join('');

        popWindow(targetLink, winStyle);
    }

    // function shareToSohu(data) {
    //     console.log(data);
    //     var width = 660;
    //     var height = 470;
    //     var left = (screen.width - width) / 2;
    //     var top = (screen.height - height) / 2;
    //     var winStyle = ["toolbar=0,status=0,resizable=1,width=", width, ",height=", height, ",left=", left, ",top=", top].join('');

    //     var params = ['url=', data.url, '&title=', encodeURIComponent(data.title), '&content=utf8', '&pic=', data.pic].join('');
    //     var link = "http://t.sohu.com/third/post.jsp?";
    //     var targetLink = [link, params].join('');
    //     popWindow(targetLink, winStyle);
    // }

    function shareToDouban(data) {
        (function(d, sc, e, w, h) {
            var s1 = window.getSelection,
            s2 = d.getSelection,
            s3 = d.selection,
            s = s1 ? s1() : s2 ? s2() : s3 ? s3.createRange().text : '',
            r = 'http://www.douban.com/share/service/?href=' + e(d.url) + '%26name=' + e(d.title),
            x = function() {
                if (!window.open(r, 'douban', 'toolbar=0,resizable=1,scrollbars=yes,status=1,width=' + w + ',height=' + h + ',left=' + (sc.width - w) / 2 + ',top=' + (sc.height - h) / 2)) location.href = r
            };
            setTimeout(x, 10);
        })(data, screen, encodeURIComponent, 500, 360);
    }


    return {
        shareTo: shareTo
    };
});