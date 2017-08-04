$(function () {
    var oPage = {
        init: fInit,
        doRegister: fDoRegister,
        getData: fGetData,
        isDataRight: fIsDataRight
    };

    oPage.init();

    function fInit() {
        var that = this;
        that.el = {
            name: $('.js-name'),
            account: $('.js-account'),
            password: $('.js-password'),
            passwordRepeat: $('.js-password-repeat'),
            submit: $('.js-submit'),
            nameIpt: $('.js-name input'),
            accountIpt: $('.js-account input'),
            passwordIpt: $('.js-password input'),
            passwordRepeatIpt: $('.js-password-repeat input')
        };

        // 点击提交
        that.el.submit.on('click', function (oEvent) {
            that.doRegister();
        });
        // 鼠标聚焦时，去掉错误样式
        $.each(['nameIpt', 'accountIpt', 'passwordIpt', 'passwordRepeatIpt'], function (_, sName) {
            that.el[sName].on('focus', function () {
                nc.util.setIptNone(that.el.name);
                nc.util.setIptNone(that.el.account);
                nc.util.setIptNone(that.el.password);
                nc.util.setIptNone(that.el.passwordRepeat);
            });
        });
    }

    function fDoRegister() {
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
            url: '/register',
            method: 'post',
            data: {name: oData.name, email: oData.email, password: oData.password},
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
            name: $.trim(oElMap.nameIpt.val()),
            email: $.trim(oElMap.accountIpt.val()),
            password: $.trim(oElMap.passwordIpt.val()),
            passwordRepeat: $.trim(oElMap.passwordRepeatIpt.val())
        };
    }

    function fIsDataRight() {
        var that = this;
        var oElMap = that.el;
        var oData = that.getData();
        var bResult = true;
        if (!oData.name) {
            bResult = false;
            nc.util.setIptStatus(oElMap.name, '请填写正确的昵称');
        }
        if (!nc.util.isEmail(oData.email)) {
            bResult = false;
            nc.util.setIptStatus(oElMap.account, '请填写正确的邮箱');
        }
        if (!nc.util.isPassword(oData.password)) {
            bResult = false;
            nc.util.setIptStatus(oElMap.password, '密码长度不能小于6位');
        }
        if (oData.password !== oData.passwordRepeat) {
            bResult = false;
            nc.util.setIptStatus(oElMap.passwordRepeat, '密码不一致');
        }
        return bResult;
    }
});