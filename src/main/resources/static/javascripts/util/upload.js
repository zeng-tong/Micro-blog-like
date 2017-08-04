(function () {
    window.nc = window.nc || {};
    window.nc.upload = {
        file: fFile,

        _upload: _fUpload
    };

    /**
     * 上传
     * @param   {Object} oConf
     *  @param  {Object} oConf.el 上传元素
     *  @param  {Function} oConf.checkFile 上传之前的文件检查
     *  @param  {Function} oConf.progress 上传进度
     *  @param  {Function} oConf.call 成功回调
     *  @param  {Function} oConf.error 失败回调
     */
    function fFile(oConf) {
        var that = this;
        oEl = $(oConf.el);
        oEl.on('change', function (oEvent) {
            var oFile = (oEl.get(0).files || [])[0];
            if (!oFile) {
                return;
            }
            var sType = (oFile.name || "").split(".").pop();
            var nFileSize = oFile.size;
            if (nFileSize === 0) {
                return oConf.error && oConf.error('文件大小为0');
            }
            var aType = (oConf.fileType || '').toLowerCase().replace(/\*\./gi, "").split(";").filter(function (sType) {
                return $.trim(sType);
            });
            if (aType.indexOf(sType) < 0) {
                return oConf.error && oConf.error('错误的文件类型，支持以下文件类型：' + aType.join(','));
            }
            var oCheck = oConf.checkFile ? oConf.checkFile(oFile, nFileSize, sType) : null;
            if (oCheck && !oCheck.right) {
                return alert(oCheck.msg || '不允许上传');
            }

            var oData = new FormData();
            oData.append('file', oFile);
            that._upload({
                data: oData,
                call: oConf.call,
                error: oConf.error,
                progress: oConf.progress
            });
            oEl.val('');
        });
    }

    /**
     * 上传
     * @param   {Object} oConf
     *  @param  {Object} oConf.data 数据
     *  @param  {Function} oConf.progress 上传进度
     *  @param  {Function} oConf.call 成功回调
     *  @param  {Function} oConf.error 失败回调
     */
    function _fUpload(oConf) {
        var that = this;
        oConf = oConf || {};
        var oData = oConf.data;
        if (!oData) {
            return;
        }
        if (!FormData || !window.XMLHttpRequest || !window.JSON || !window.addEventListener) {
            return alert('不支持上传操作');
        }
        var XMLHttpRequest = window.XMLHttpRequest;
        var oXhr = new XMLHttpRequest();
        // 上传进度
        oXhr.upload.addEventListener("progress", function(oEvent) {
            var nLoaded = oEvent.loaded;
            var nTotal = oEvent.total || 1;
            var nProgress = +(nLoaded * 100 / nTotal).toFixed(2);
            oConf.progress && oConf.progress.call(that, nProgress, nLoaded, nTotal);
        }, false);
        // 上传完成
        oXhr.onreadystatechange = function(oEvent) {
            if (oXhr.readyState === 4) {
                var oResult = nc.util.json(oXhr.responseText, {
                    msg: '出现错误，请重试'
                });
                var nCode = +oResult.code;
                var bSuccess = nCode === 0;
                var sMethod = bSuccess ? "call" : "error";
                oConf[sMethod] && oConf[sMethod].call(that, oResult);
            }
        };
        oXhr.open("POST", '/uploadImage', true);
        oXhr.send(oData);
    }
})();