(function () {
    window.nc = window.nc || {};
    window.nc.Msg = function () {
        var that = this;
        that.init && that.init.apply(that, arguments);
    };
    $.extend(window.nc.Msg.prototype, {
        init: fInit,
        initLayer: fInitLayer,
        initPopup: fInitPopup,
        initAction: fInitAction,

        error: fError,
        close: fClose
    });

    function fInit(oConf) {
        var that = this;
        that.initLayer(oConf);
        that.initPopup(oConf);
        that.initAction(oConf);
    }

    function fInitLayer(oConf) {
        var that = this;
        that.layer = $('<div class="pop-layer"></div>');
        $(document.body).append(that.layer);
    }

    function fInitPopup(oConf) {
        var that = this;
        var oPopup = that.popup = $([
            '<div class="pop-box">',
                '<div class="pop-head">',
                    '<span class="pop-tit">发送私信</span>',
                    '<span class="icon-close js-close"></span>',
                '</div>',
                '<div class="pop-content">',
                    '<div class="pop-form-wrap">',
                        '<div class="your-question">',
                            '<div class="remark-head">发给：</div>',
                            '<div class="your-question-input">',
                                '<textarea rows="1" class="js-name" title="在这里输入问题" placeholder="姓名" autocomplete="off"></textarea>',
                            '</div>',
                        '</div>',
                        '<div class="your-question">',
                            '<div class="remark-head">内容：</div>',
                            '<div class="remark-container">',
                                '<div class="remark-wrap">',
                                    '<div class="editor-field-wrap">',
                                        '<div class="editor-field-element js-content" contenteditable="true">',
                                            // '<p><span class="remark-tips">私信内容</span></p>',
                                        '</div>',
                                    '</div>',
                                '</div>',
                            '</div>',
                        '</div>',
                        '<div class="pop-submit-wrap">',
                            '<span class="pop-tag-err js-error" style="display:none"></span>',
                            '<a href="javascript:void(0);" name="cancel" class="btn-cancel js-cancel">取消</a>',
                            '<a href="javascript:void(0);" name="addq" class="btn-submit js-submit">发布</a>',
                        '</div>',
                    '</div>',
                '</div>',
            '</div>'].join(''));
        $(document.body).append(oPopup);
        // 居中
        var oWin = $(window);
        var nWidth = oPopup.outerWidth();
        var nHeight = oPopup.outerHeight();
        var nWinWidth = oWin.width();
        var nWinHeight = oWin.height();
        oPopup.css('left', Math.max(0, (nWinWidth - nWidth) / 2))
            .css('top', Math.max(0, (nWinHeight - nHeight) / 2) + oWin.scrollTop());
    }

    function fInitAction() {
        var that = this;
        var oEl = that.popup;
        var bLockSend = false;
        var oNameIpt = oEl.find('.js-name');
        var oContentIpt = oEl.find('.js-content');
        oEl.on('click', '.js-cancel', function () {
            that.close();
        });
        oEl.on('click', '.js-close', function () {
            that.close();
        });
        oEl.on('click', '.js-submit', function () {
            var sName = $.trim(oNameIpt.val());
            var sContent = $.trim(oContentIpt.html());
            if (!sName) {
                return that.error('姓名不能为空');
            }
            if (!sContent) {
                return that.error('内容不能为空');
            }
            if (bLockSend) {
                return;
            }
            bLockSend = true;
            nc.util.ajax({
                url: '/addMessage',
                data: {content: sContent, toName: sName},
                call: function () {
                    alert('发送成功');
                    that.close();
                },
                error: function (oResult) {
                    that.error(oResult.msg);
                },
                always: function () {
                    bLockSend = false;
                }
            });
        });
        $.each([oNameIpt, oContentIpt], function (_, oIpt) {
            oIpt.on('focus', function () {
                that.error('');
            });
        });
    }

    function fError(sContent) {
        var that = this;
        var oEl = that.popup;
        var oErrorEl = oEl.find('.js-error');
        sContent = $.trim(sContent);
        oErrorEl.html(sContent)[sContent ? 'show' : 'hide']();
    }

    function fClose() {
        var that = this;
        that.layer.remove();
        that.popup.remove();
    }
})();