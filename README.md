# cordova-plugin-idcardreader
A cordova plugin of android platform for intsig idcard ocr sdk

#使用方法
- 安装插件
使用名命令行进行安装
```javascript
 cordova plugin add https://github.com/wangsongyan/cordova-plugin-idcardreader.git --variable IDCARD_TEMP_PATH=/sdcard/idcardscan
```

- 使用插件
```javascript
    function read(){
    	navigator.IdCardReader.readIdCard(function(successMsg){
    		alert(successMsg);
    	},function(errorMsg){
    		alert(errorMsg);
    	},'your appkey')
    }
```
 执行成功，返回json字符串
