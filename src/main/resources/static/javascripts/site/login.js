$(function () {
    var oPage = {
        init: fInit,
        doLogin: fDoLogin,
        getData: fGetData,
        isDataRight: fIsDataRight
    };

    oPage.init();

    function fInit() {
        var that = this;
        that.el = {
            account: $('.js-account'),
            password: $('.js-password'),
            submit: $('.js-submit'),
            accountIpt: $('.js-account input'),
            passwordIpt: $('.js-password input')
        };

        // 点击提交
        that.el.submit.on('click', function (oEvent) {
            that.doLogin();
        });
        // 鼠标聚焦时，去掉错误样式
        $.each(['accountIpt', 'passwordIpt'], function (_, sName) {
            that.el[sName].on('focus', function () {
                nc.util.setIptNone(that.el.account);
                nc.util.setIptNone(that.el.password);
            });
        });
    }

    function fDoLogin() {
        var that = this;
        if (!that.isDataRight()) {
            return;
        }
        // 上一个请求没结束，忽略当前的请求
        if (that.isLock) {
            return;
        }
        that.isLock = true;

        var oData = that.getData();
        nc.util.ajax({
            url: '/login',
            method: 'post',
            data: {email: oData.email, password: oData.password},
            call: function () {
                alert('操作成功');
                window.location.href = '/index';
            },
            error: function (oResult) {
                alert(oResult.msg);
            },
            always: function () {
                that.isLock = false;
            }
        });
    }

    function fGetData() {
        var that = this;
        var oElMap = that.el;
        return {
            email: $.trim(oElMap.accountIpt.val()),
            password: $.trim(oElMap.passwordIpt.val())
        };
    }

    function fIsDataRight() {
        var that = this;
        var oElMap = that.el;
        var oData = that.getData();
        var bResult = true;
        if (!nc.util.isEmail(oData.email)) {
            bResult = false;
            nc.util.setIptStatus(oElMap.account, '请填写正确的邮箱');
        }
        if (!nc.util.isPassword(oData.password)) {
            bResult = false;
            nc.util.setIptStatus(oElMap.password, '密码长度不能小于6位');
        }
        return bResult;
    }
});