var exec = require('cordova/exec');

var IdCardPlugin = {};
IdCardPlugin.readIdCard = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, "IdCardReader", "readIdCard", []]);
};
module.exports = IdCardPlugin;