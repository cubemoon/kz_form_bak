(function(g, $) {

    var modal_mask = $("<div class=\"modal-backdrop\"></div>"),
            loading = $("#js-modal-loading"),
            loading_timeout = 0,
            tpl_toast = '<div class="ui-toast">\
                            <div class="inner">\
                                <div class="msg">{{msg}}</div>\
                                <a href="javascript:;" title="关闭" class="font-ico close">&#xe010;</a>\
                            </div>\
                        </div>',
            tpl_lite_pop = "<div class=\"ui-pop ui-pop-lite\">\
                                <p></p>\
                                <div class=\"btn-con\">\
                                    <button class=\"confirm\" data-action=\"confirm\">确定</button>\
                                    <button class=\"btn button\" data-dismiss=\"modal\">取消</button>\
                                </div>\
                            </div>",
            tpl_lite_info = "<div class=\"ui-pop ui-pop-lite ui-pop-info\">\
                                <p></p>\
                                <div class=\"btn-con\">\
                                    <button class=\"confirm\" data-dismiss=\"modal\">知道了</button>\
                                </div>\
                            </div>",
            tpl_alert = "<div class=\"ui-pop ui-pop-lite ui-pop-info\">\
                                <p></p>\
                                <div class=\"btn-con\">\
                                    <button class=\"confirm\" data-dismiss=\"modal\">知道了</button>\
                                </div>\
                            </div>",
            ui =
            {
                _load: function(elt, onSuccess, onError) {
                    try {
                        document.getElementsByTagName("head")[0].appendChild(elt);
                        if (elt.addEventListener) {
                            elt.addEventListener("load", onSuccess, false);
                        } else if (elt.attachEvent) {
                            elt.attachEvent("onreadystatechange", function() {
                                if (elt.readyState == 4
                                        || elt.readyState == 'complete'
                                        || elt.readyState == 'loaded') {
                                    onSuccess.call();
                                }
                            });
                        }
                    } catch (e) {
                        onError.call(e);
                    }
                },
                /**
                 * 
                 * @param {type} opt
                 * {
                 *      id
                 *      url
                 *      success
                 *      error
                 * }
                 * @returns {undefined}
                 */
                loadCSS: function(opt) {
                    if (opt.id && document.getElementById(opt.id)) {
                        opt.success.call();
                        return;
                    }
                    var css = document.createElement('link');
                    css.id = opt.id;
                    css.href = opt.url;
                    css.rel = "stylesheet";
                    css.type = "text/css";
                    this._load(css, opt.success, opt.error);
                },
                /**
                 * 
                 * @param {type} opt
                 * {
                 *      id
                 *      url
                 *      success
                 *      error
                 * }
                 * @returns {undefined}
                 */
                loadJS: function(opt) {
                    if (opt.id && document.getElementById(opt.id)) {
                        opt.success.call();
                        return;
                    }
                    var script = document.createElement('script');
                    script.src = opt.url;
                    script.type = "text/javascript";
                    this._load(script, opt.success, opt.error);
                },
                /**
                 * 显示toast框
                 * @param {type} msg
                 * @param {function} callback
                 * @returns {undefined}
                 */
                toast: function(msg, callback) {
                    var tpl = tpl_toast.replace("{{msg}}", msg),
                            t = $(tpl),
                            bd = $("body");
                    bd.append(t);
                    t.on("click", "a.close", function() {
                        t.remove();
                        callback.call();
                    });
                },
                /**
                 * 信息警示小弹窗
                 * @param {type} opt
                 * {
                 *  msg
                 * }
                 * @returns {undefined}
                 */
                alert: function(opt) {
                    if (ui.alert_to) {
                        g.clearTimeout(ui.alert_to);
                    }
                    if (!ui.pop_alert) {
                        ui.pop_alert = $(tpl_alert).modal({
                            show: false,
                            backdrop: true,
                            center: true
                        });
                        $("body").append(ui.pop_alert);
                    }
										ui.pop_alert.one("hidden", function() {
											if (ui.alert_to) {
													g.clearTimeout(ui.alert_to);
											}
											if (opt.callback) {
													opt.callback.call();
											}
										});
                    ui.pop_alert.find("p").html(opt.msg);
                    ui.pop_alert.modal("show");
                    if (false === opt.autoHide) {
                        ui.alert_to = g.setTimeout(function() {
                            ui.pop_alert.modal("hide");
                            ui.alert_to = null;
                        }, 5000);
                    }
                },
                /**
                 * 信息提示小弹窗
                 * @param {type} opt
                 * {
                 *  msg
                 *  sourceElt
                 *  callback
                 * }
                 * @returns {undefined}
                 */
                info: function(opt) {
                    if (ui.infoto) {
                        g.clearTimeout(ui.infoto);
                    }
                    if (!ui.liteinfo) {
                        ui.liteinfo = $(tpl_lite_info).modal({
                            show: false,
                            backdrop: false,
                            center: false
                        });
                        $("body").append(ui.liteinfo);
                    }
                    var s = opt.sourceElt, offset = s.offset();
                    ui.liteinfo.find("p").html(opt.msg).end().css({
                        left: offset.left,
                        top: offset.top
                    }).modal("show");
                    //如果存在callback则不执行倒计时关闭
                    if (!opt.callback) {
                        ui.infoto = g.setTimeout(function() {
                            ui.liteinfo.modal("hide");
                            ui.infoto = null;
                        }, 3000);
                    } else {
                        ui.liteinfo.one("hide", function() {
                            opt.callback.call(ui.liteinfo);
                        });
                    }
                },
                /**
                 * 轻量级确认小窗 
                 * @param {type} opt
                 * {
                 *  msg
                 *  sourceElt
                 *  confirm
                 * }
                 * @returns {undefined}
                 */
                confirm: function(opt) {
                    if (!ui.liteconfirm) {
                        ui.liteconfirm = $(tpl_lite_pop).modal({
                            show: false,
                            center: false
                        });
                        $("body").append(ui.liteconfirm);
                    }
                    var s = opt.sourceElt, offset = s.offset();
                    ui.liteconfirm.find("p").html(opt.msg).end().css({
                        left: offset.left,
                        top: offset.top
                    }).one("click.action.modal", function(e, n) {
                        if (n == "confirm") {
                            if (opt.confirm) {
                                opt.confirm.call(this);
                            }
                        }
                        $(this).modal("hide");
                    }).one("hide", function() {
                        if (opt.hide)
                            opt.hide.call(this);
                    }).modal("show");
                },
                replaceCSS: function(id, css) {
                    var elt = $("#" + id);
                    if (elt.length == 0) {
                        elt = $("<link rel=\"stylesheet\" type=\"text/css\" href=\"/_.gif\">").attr("id", id);
                        $("head").append(elt);
                    }
                    elt.attr("href", css);
                },
                showModal: function(parent) {
                    parent.append(modal_mask)
                    modal_mask.show();
                },
                hideModal: function() {
                    modal_mask.hide();
                },
                //模态的loading
                modalLoading: function(opt) {
                    if (!opt)
                        opt = {};
                    if (!opt.parent) {
                        opt.parent = $("body");
                        loading.css({
                            position: 'fixed'
                        });
                        modal_mask.css({
                            position: 'fixed'
                        });
                    } else {
                        //相对于父级元素定位
                        opt.parent.css({
                            position: 'relative'
                        });
                        loading.css({
                            position: 'absolute'
                        });
                        modal_mask.css({
                            position: 'absolute'
                        });
                    }
                    opt.parent.append(loading);

                    //定时设置
                    if (opt.timeout) {
                        loading_timeout = g.setTimeout(function() {
                            ui.showModal(opt.parent);
                            loading.show();
                            loading_timeout = null;
                        }, opt.timeout);
                    } else {
                        ui.showModal(opt.parent);
                        loading.show();
                    }
                },
                hideModalLoading: function() {
                    if (loading_timeout) {
                        g.clearTimeout(loading_timeout);
                    }
                    ui.hideModal();
                    loading.hide();
                },
                ajax_form: function(form, onSuccess) {
                    $.ajax({
                        url: form.attr("action"),
                        type: form.attr("method"),
                        dataType: "json",
                        data: form.serialize(),
                        beforeSend: function() {
                            ui.modalLoading({
                                timeout: 1000
                            });
                        },
                        complete: function() {
                            ui.hideModalLoading();
                        },
                        success: function(data) {
                            onSuccess.call(this, data);
                        },
                        error: function(data) {
                        }
                    });
                }

            };

    if (!g.SOHUZ)
        g.SOHUZ = {};
    if (!g.SOHUZ.myui)
        g.SOHUZ.myui = ui;
})(this, jQuery);
