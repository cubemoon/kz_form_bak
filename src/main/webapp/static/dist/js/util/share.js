define([],function(){function e(e,t){window.open(e,"_blank",t)}function t(e,t){var t;switch(e){case"weibo":o(t);break;case"renren":i(t);break;case"qq":r(t);break;case"sohu":shareToSohu(t);break;case"douban":c(t);break;case"qwb":n(t)}}function n(t){var n,o,i,r,c,s;n=440,o=430,i=(screen.width-n)/2,r=(screen.height-o)/2,c=["toolbar=0,resizable=1,scrollbars=yes,status=1,width=",n,",height=",o,",left=",i,",top=",r].join(""),s=["http://share.v.t.qq.com/index.php?c=share&a=index&","url=",encodeURIComponent(t.url),"&title=",encodeURIComponent(t.title),"&pic=",encodeURIComponent(t.pic)].join(""),e(s,c)}function o(t){var n,o,i,r,c,a;n=440,o=430,i=(screen.width-n)/2,r=(screen.height-o)/2,c=["toolbar=0,status=0,resizable=1,width=",n,",height=",o,",left=",i,",top=",r].join(""),a=["http://service.t.sina.com.cn/share/share.php?","appkey=",s,"&title=",encodeURIComponent(t.title),"&url=",encodeURIComponent(t.url),"&pic=",encodeURIComponent(t.pic)].join(""),e(a,c)}function i(t){var n,o,i,r,c,s;n=540,o=480,i=(screen.width-n)/2,r=(screen.height-o)/2,c=["toolbar=0,status=0,resizable=1,width=",n,",height=",o,",left=",i,",top=",r].join(""),s=["http://widget.renren.com/dialog/share?","resourceUrl=",encodeURIComponent(t.url),"&title=",encodeURIComponent(t.title),"&pic=",encodeURIComponent(t.pic)].join(""),e(s,c)}function r(t){var n,o,i,r,c,s;n=540,o=480,i=(screen.width-n)/2,r=(screen.height-o)/2,c=["toolbar=0,resizable=1,scrollbars=yes,status=1,width=",n,",height=",o,",left=",i,",top=",r].join(""),s=["http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?","url=",encodeURIComponent(t.url),"&title=",encodeURIComponent(t.title),"&pics=",encodeURIComponent(t.pic)].join(""),e(s,c)}function c(e){!function(e,t,n,o,i){var r=window.getSelection,c=e.getSelection,s=e.selection,a=(r?r():c?c():s?s.createRange().text:"","http://www.douban.com/share/service/?href="+n(e.url)+"%26name="+n(e.title)),h=function(){window.open(a,"douban","toolbar=0,resizable=1,scrollbars=yes,status=1,width="+o+",height="+i+",left="+(t.width-o)/2+",top="+(t.height-i)/2)||(location.href=a)};setTimeout(h,10)}(e,screen,encodeURIComponent,500,360)}var s;return s="992243007",{shareTo:t}});