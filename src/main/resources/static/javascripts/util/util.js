(function () {
    window.nc = window.nc || {};
    window.nc.util = {
        ajax: fAjax,
        json: fJson,
        // 数据格式判断
        isEmail: fIsEmail,
        isPassword: fIsPassword,
        // 元素操作
        setIptStatus: fSetIptStatus,
        setIptNone: fSetIptNone
    };


    function fJson(sStr, oDefault) {
        oDefault = oDefault || null;
        try {
            return sStr ? window.JSON.parse(sStr) : oDefault;
        } catch (e1) {
            try {
                var rvalidchars = /^[\],:{}\s]*$/;
                var rvalidescape = /\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g;
                var rvalidtokens = /"[^"\\\r\n]*"|true|false|null|-?(?:\d+\.|)\d+(?:[eE][+-]?\d+|)/g;
                var rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g;
                if (rvalidchars.test(sStr.replace(rvalidescape, "@").replace(rvalidtokens, "]").replace(rvalidbraces, ""))) {
                    return new window.Function("return " + sStr)();
                }
            } catch (e2) {}
        }
        return oDefault;
    }

    function fAjax(oParam) {
        $.ajax({
            method: oParam.method || 'POST',
            url: oParam.url,
            data: oParam.data,
            dataType: oParam.dataType || 'json'
        }).done(function (oResult) {
            var nCode = oResult.code;
            var bRight = nCode === 0;
            bRight && oParam.call && oParam.call(oResult);
            !bRight && oParam.error && oParam.error(oResult);
            oParam.always && oParam.always(oResult);
        }).fail(oParam.error).always(oParam.always);
    }

    function fIsEmail(sEmail) {
        sEmail = $.trim(sEmail);
        return sEmail && /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(sEmail);
    }

    function fIsPassword(sPassword) {
        sPassword = $.trim(sPassword);
        return sPassword && sPassword.length >= 4;
    }

    function fSetIptStatus(oEl, sMsg, bError) {
        var that = this;
        that.setIptNone(oEl);
        oEl.addClass(bError === false ? 'success' : 'error');
        oEl.append('<span class="input-tip">' + $.trim(sMsg) + '</span>');
    }

    function fSetIptNone(oEl) {
        oEl.removeClass('error success');
        oEl.find('span.input-tip').remove();
    }
})();