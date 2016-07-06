module.exports = {
    getToken: function (success, fail) {
        cordova.exec(success, fail, 'XinGe', 'getToken', [])
    }
};