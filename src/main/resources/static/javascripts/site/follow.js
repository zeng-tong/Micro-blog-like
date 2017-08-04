$(function () {
    var oPage = {
        init: fInit,
        initAction: fInitAction,

        clickFollow: fClickFollow
    };

    oPage.init();

    function fInit() {
        var that = this;
        that.initAction();
    }

    function fInitAction() {
        var that = this;
        var oDoc = $(document);
        oDoc.on('click', '.js-follow', function (oEvent) {
            var oEl = $(oEvent.currentTarget);
            var oDv = oEl.closest('.js-item');
            that.clickFollow(oDv, oEl);
        });
    }

    function fClickFollow(oDv, oEl) {
        var that = this;
        var sId = $.trim(oDv.attr('data-user-id'));
        var bFollow = $.trim(oDv.attr('data-followed')) === '1';
        if (that.isLockFollow) {
            return;
        }
        that.isLockFollow = true;
        nc.util.ajax({
            url: bFollow ? '/unfollow' : '/follow',
            data: {userId: sId},
            call: function (oResult) {
                oEl.html(bFollow ? '关注他' : '取消关注');
                oDv.attr('data-followed', bFollow ? 0 : 1);
                oEl[bFollow ? 'addClass' : 'removeClass']('btn-green');
            },
            error: function (oResult) {
                alert(oResult.msg);
            },
            always: function () {
                that.isLockFollow = false;
            }
        });
    }
});