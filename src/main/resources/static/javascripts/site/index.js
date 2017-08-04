$(function () {
    var oPage = {
        init: fInit,
        initPublish: fInitPublish,
        initItemAction: fInitItemAction,

        clickFollow: fClickFollow,
        clickComment: fClickComment,
        clickLike: fClickLike
    };

    oPage.init();

    function fInit() {
        var that = this;
        that.comment = {};
        // 发布
        that.initPublish();
        // 关注
        that.initItemAction();
    }

    function fInitPublish() {
        var that = this;
        var oDv = $('.js-publish');
        var oTextarea = oDv.find('.js-content');
        var oImageDv = oDv.find('.js-image');
        var oSubmit = oDv.find('.js-submit');
        var aImage = [];

        // 图片上传
        nc.upload.file({
            el: $('.js-file'),
            fileType: '*.png;*.jpg;*.jpeg;*.gif;*.bmp',
            checkFile: function () {
                return {
                    right: aImage.length < 9,
                    msg: '一次只能上传9张图片'
                };
            },
            progress: function (nProgress) {
                console.log(nProgress);
            },
            call: function (oResult) {
                var sUrl = $.trim(oResult.url);
                if (sUrl) {
                    aImage.push(sUrl);
                    _fRefreshImage();
                }
            },
            error: function (oResult) {
                alert(oResult.msg);
            }
        });
        // 发布内容
        oSubmit.on('click', _fPublish);

        function _fRefreshImage() {
            var that = this;
            var sHtml = '';
            $.each(aImage, function (_, sUrl) {
                sHtml += [
                    '<a href="' + sUrl+ '" target="_blank">',
                        '<img src="' + sUrl + '" />',
                    '</a>'].join('');
            });
            oImageDv.html(sHtml);
        }

        function _fPublish() {
            var sContent = $.trim(oTextarea.val());
            if (!sContent) {
                return alert('内容不能为空');
            }
            if (that.isLockPublish) {
                return;
            }
            that.isLockPublish = true;
            nc.util.ajax({
                url: '/addWeibo',
                method: 'POST',
                data: {content: sContent, images:aImage.join('|')},
                call: function () {
                    // 清空内容
                    oTextarea.val('');
                    aImage.length = 0;
                    _fRefreshImage();
                    // 提示然后刷新下页面
                    alert('发布成功');
                    window.location.reload();
                },
                error: function (oResult) {
                    alert(oResult.msg);
                },
                always: function () {
                    that.isLockPublish = true;
                }
            });
        }
    }

    function fInitItemAction() {
        var that = this;
        var oDoc = $(document);
        var aAction = [{cls: '.js-follow', name: 'clickFollow'},
            {cls: '.js-comment', name: 'clickComment'},
            {cls: '.js-like', name: 'clickLike'}];
        // 使用事件代理实现每条微博的操作
        // map表示代理的class以及对应的处理函数
        $.each(aAction, function (_, oAction) {
            oDoc.on('click', oAction.cls, function (oEvent) {
                var oEl = $(oEvent.currentTarget);
                var oDv = oEl.closest('.js-wb-item');
                that[oAction.name] && that[oAction.name](oDv, oEl);
            });
        });
    }

    function fClickFollow(oDv, oEl) {
        var that = this;
        var sUid = $.trim(oDv.attr('data-user-id'));
        var bFollow = $.trim(oDv.attr('data-followed')) === '1';
        if (that.isLockFollow) {
            return;
        }
        that.isLockFollow = true;
        nc.util.ajax({
            url: bFollow ? '/unfollow' : '/follow',
            data: {userId: sUid},
            call: function () {
                // 改编同一作者的所有微博状态
                var oAllDv = $('.js-wb-item[data-user-id=' + sUid + ']');
                oAllDv.attr('data-followed', bFollow ? 0 : 1);
                oAllDv.find('.js-follow')[bFollow ? 'removeClass' : 'addClass']('actived');
            },
            error: function (oResult) {
                alert(oResult.msg);
            },
            always: function () {
                that.isLockFollow = false;
            }
        });
    }

    function fClickComment(oDv, oEl) {
        var that = this;
        var sId = $.trim(oDv.attr('data-wb-id'));
        var oComment = that.comment[sId];
        if (!oComment) {
            oComment = that.comment[sId] = new window.nc.Comment(oDv);
        }
        oComment.toggle();
    }

    function fClickLike(oDv, oEl) {
        var that = this;
        var sId = $.trim(oDv.attr('data-wb-id'));
        var bLike = $.trim(oDv.attr('data-liked')) === '1';
        if (that.isLockLike) {
            return;
        }
        that.isLockLike = true;
        nc.util.ajax({
            url: bLike ? '/dislike' : '/like',
            data: {entityType: 1, entityId: sId},
            call: function (oResult) {
                oDv.attr('data-liked', bLike ? 0 : 1);
                oEl[bLike ? 'removeClass' : 'addClass']('actived');
                oEl.html('<i class="ico-like"></i>' + oResult.count);
            },
            error: function (oResult) {
                alert(oResult.msg);
            },
            always: function () {
                that.isLockLike = false;
            }
        });
    }
});