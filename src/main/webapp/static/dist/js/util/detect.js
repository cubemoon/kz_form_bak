!function(e){function i(e){var i=this.os={},o={},a=e.match(/WebKit\/([\d.]+)/),t=e.match(/(Android)\s+([\d.]+)/),r=e.match(/(iPad).*OS\s([\d_]+)/),c=e.match(/Windows Phone/i),h=!r&&e.match(/(iPhone\sOS)\s([\d_]+)/),n=e.match(/(webOS|hpwOS)[\s\/]([\d.]+)/),s=n&&e.match(/TouchPad/),d=e.match(/Kindle\/([\d.]+)/),m=e.match(/Silk\/([\d._]+)/),l=e.match(/(BlackBerry).*Version\/([\d.]+)/),b=e.match(/(BB10).*Version\/([\d.]+)/),v=e.match(/(RIM\sTablet\sOS)\s([\d.]+)/),p=e.match(/PlayBook/),k=e.match(/Chrome\/([\d.]+)/)||e.match(/CriOS\/([\d.]+)/),w=e.match(/Firefox\/([\d.]+)/);(o.webkit=!!a)&&(o.version=a[1]),t&&(i.android=!0,i.version=t[2]),c&&(i.wp=!0),h&&(i.ios=i.iphone=!0,i.version=h[2].replace(/_/g,".")),r&&(i.ios=i.ipad=!0,i.version=r[2].replace(/_/g,".")),n&&(i.webos=!0,i.version=n[2]),s&&(i.touchpad=!0),l&&(i.blackberry=!0,i.version=l[2]),b&&(i.bb10=!0,i.version=b[2]),v&&(i.rimtabletos=!0,i.version=v[2]),p&&(o.playbook=!0),d&&(i.kindle=!0,i.version=d[1]),m&&(o.silk=!0,o.version=m[1]),!m&&i.android&&e.match(/Kindle Fire/)&&(o.silk=!0),k&&(o.chrome=!0,o.version=k[1]),w&&(o.firefox=!0,o.version=w[1]),i.tablet=!!(r||p||t&&!e.match(/Mobile/)||w&&e.match(/Tablet/)),i.phone=!(i.tablet||!(t||h||n||l||b||k&&e.match(/Android/)||k&&e.match(/CriOS\/([\d.]+)/)||w&&e.match(/Mobile/))),i.wep=i.phone||i.wp}i.call(e,navigator.userAgent),e.__detect=i}($);