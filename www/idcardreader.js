var exec = require('cordova/exec');

var IdCardPlugin = {};
IdCardPlugin.readIdCard = function(successCallback, errorCallback, appkey) {
  exec(successCallback, errorCallback, "IdCardReader", "readIdCard", [{
  	"appkey":appkey
  }]);
};
module.exports = IdCardPlugin;