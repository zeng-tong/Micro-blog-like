(function () {
    window.nc = window.nc || {};
    window.nc.Comment = function () {
        var that = this;
        that.init && that.init.apply(that, arguments);
    };
    $.extend(window.nc.Comment.prototype, {
        init: fInit,
        initSubmit: fInitSubmit,
        initAction: fInitAction,

        toggle: fToggle,
        open: fOpen,
        close: fClose,

        refreshList: fRefreshList,

        _getList: _fGetList,
        _renderCount: _fRenderCount,
        _renderList: _fRenderList,
        _renderNext: _fRenderNext
    });

    function fInit(oDv) {
        var that = this;
        that.el = {
            container: oDv,
            target: oDv.find('.js-comment'),
            reply: oDv.find('.js-reply'),
            textarea: oDv.find('textarea'),
            submit: oDv.find('.js-submit'),
            list: oDv.find('.js-list')
        };
        that.id = $.trim(oDv.attr('data-wb-id'));
        that.count = +oDv.attr('data-comment-count') || 0;
        // 一些操作
        that.initSubmit();
        that.initAction();
    }

    function fInitSubmit() {
        var that = this;
        var oElMap = that.el;
        var bLock = false;
        oElMap.submit.on('click', function () {
            var sContent = $.trim(oElMap.textarea.val());
            if (!sContent) {
                return alert('内容不能为空');
            }
            if (bLock) {
                return;
            }
            bLock = true;
            nc.util.ajax({
                url: '/addComment',
                data: {content: sContent, entityType: 1, entityId: that.id},
                call: function () {
                    oElMap.textarea.val('');
                    that.count++;
                    that.refreshList();
                    that._renderCount();
                    alert('操作成功');
                },
                error: function (oResult) {
                    alert(oResult.msg);
                },
                always: function () {
                    bLock = false;
                }
            });
        });
    }

    function fInitAction() {
        var that = this;
        var oDv = that.el.container;
        oDv.on('click', '.js-next', function (oEvent) {
            that._getList({
                offset: that.offset,
                clear: false
            });
        });
        oDv.on('click', '.js-cmt-like', function (oEvent) {
            var oEl = $(oEvent.currentTarget);
            var oDv = oEl.closest('.js-cmt-item');
            var bLike = oDv.attr('data-liked') === '1';
            var sId = $.trim(oDv.attr('data-id'));
            if (oEl.attr('data-lock') === '1') {
                return;
            }
            oEl.attr('data-lock', 1);
            nc.util.ajax({
                url: bLike ? '/dislike' : '/like',
                data: {entityType: 2, entityId: sId},
                call: function (oResult) {
                    oDv.attr('data-liked', bLike ? 0 : 1);
                    oEl.html('<i class="ico-like"></i>' + (bLike ? '赞' : '已赞') + '(' + oResult.count + ')');
                },
                error: function (oResult) {
                    alert(oResult.msg);
                },
                always: function () {
                    oEl.removeAttr('data-lock');
                }
            });
        });
    }

    function fToggle() {
        var that = this;
        that[that.el.reply.css('display') === 'none' ? 'open' : 'close']();
    }

    function fOpen() {
        var that = this;
        var oElMap = that.el;
        oElMap.reply.show();
        that.refreshList();
    }

    function fClose() {
        var that = this;
        var oElMap = that.el;
        oElMap.reply.hide();
    }

    function fRefreshList() {
        var that = this;
        var oElMap = that.el;
        // 清空当前的数据
        oElMap.list.html('');
        that.offset = 0;
        // 重新请求数据
        that._getList({
            offset: 0,
            clear: true
        });
    }

    function _fGetList(oConf) {
        var that = this;
        var nCount = 10;
        var nOffset = oConf.offset || 0;
        nc.util.ajax({
            url: '/listComments',
            data: {entityType: 1, entityId: that.id, offset: nOffset, count: nCount},
            call: function (oResult) {
                var aItem = oResult.comments || [];
                var bHasNext = aItem.length >= nCount;
                that.offset = nOffset + aItem.length;
                that._renderList(aItem, oConf.clear);
                that._renderNext(bHasNext);
            },
            error: function (oResult) {
                alert(oResult.msg);
            }
        });
    }

    function _fRenderCount() {
        var that = this;
        that.el.target.html('<i class="ico-reply"></i>' + that.count);
    }

    function _fRenderList(aItem, bClear) {
        var that = this;
        var oElMap = that.el;
        var sHtml = '';
        $.each(aItem, function (_, oItem) {
            var sLink = '/profile/' + oItem.userid;
            sHtml += [
                '<li class="clearfix js-cmt-item" data-liked="' + (oItem.liked ? 1 : 0) + '" data-id="' + oItem.cid + '">',
                    '<a href="' + sLink + '" class="answer-head" target="_blank">',
                        '<img src="' + oItem.userhead + '">',
                    '</a>',
                    '<div class="reply-content">',
                        '<div class="reply-person"><a href="' + sLink + '" target="_blank">' + oItem.username + '</a></div>',
                        '<p>' + oItem.content + '</p>',
                    '</div>',
                    '<div class="answer-legend reply-info">',
                        '<a href="javascript:void(0);" class="js-cmt-like"><i class="ico-like"></i>' + (oItem.liked ? '已赞' : '赞') + '(' + oItem.likeCount + ')</a>',
                    '</div>',
                '</li>'].join('');
        });
        oElMap.list[bClear ? 'html' : 'append'](sHtml);
    }

    function _fRenderNext(bHasNext) {
        var that = this;
        var oElMap = that.el;
        oElMap.reply.find('.js-next').remove();
        if (!bHasNext) {
            return;
        }
        oElMap.reply.append('<a class="more-reply loaded-more js-next"><span>更多评论</span><i class="arrow"></i></a>');
    }
})();