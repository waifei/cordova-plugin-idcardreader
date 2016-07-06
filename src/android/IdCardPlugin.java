package org.apache.cordova.idcard;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.intsig.idcardscan.sdk.ISCardScanActivity;
import com.intsig.idcardscan.sdk.ResultData;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by songy on 2016/7/6.
 */
public class IdCardPlugin extends CordovaPlugin {

  private static final String TAG = "MainActivity";
  private static final String APP_KEY ="M0NSRQr5DbgAf2e62TVVfUfL";//替换您申请的合合信息授权提供的APP_KEY;
  private static final int REQ_CODE_CAPTURE = 100;
  public static final String PLUGIN_ACTION = "readIdCard";

  private CallbackContext callbackContext;
  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

    this.callbackContext = callbackContext;
    if(!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
        callbackContext.error("未识别到存储卡，请检查后重试！");
        return false;
    }

    if (action.equals(PLUGIN_ACTION)) {

      //通过Intent调用SDK中的相机拍摄模块ISCardScanActivity进行识别
      Intent intent = new Intent(cordova.getActivity(), ISCardScanActivity.class);
      //指定要临时保存的身份证图片路径
      intent.putExtra(ISCardScanActivity.EXTRA_KEY_IMAGE_FOLDER, preferences.getString("idcard_temp_path",""));
      //指定SDK相机模块ISCardScanActivity四边框角线条,检测到身份证图片后的颜色,可以不传递
      intent.putExtra(ISCardScanActivity.EXTRA_KEY_COLOR_MATCH, 0xffff0000);
      //指定SDK相机模块ISCardScanActivity四边框角线条颜色，正常显示颜色,可以不传递
      intent.putExtra(ISCardScanActivity.EXTRA_KEY_COLOR_NORMAL, 0xff00ff00);
      //合合信息授权提供的APP_KEY
      intent.putExtra(ISCardScanActivity.EXTRA_KEY_APP_KEY, preferences.getString("APP_KEY",""));
      //指定SDK相机模块ISCardScanActivity界面提示字符串，可以自定义
      intent.putExtra(ISCardScanActivity.EXTRA_KEY_TIPS, "请将身份证放在框内识别");
      this.cordova.startActivityForResult(this,intent, REQ_CODE_CAPTURE);

      //下面三句为cordova插件回调页面的逻辑代码
      //PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
      //mPlugin.setKeepCallback(true);

      //callbackContext.sendPluginResult(mPlugin);
     //callbackContext.success("success");

      return true;
    }
    callbackContext.error("method not exist!!");
    return false;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_CAPTURE){
      //获取身份证图片绝对路径
      //String imagePath = data.getStringExtra(ISCardScanActivity.EXTRA_KEY_RESULT_IMAGE);
      //获取身份证头像图片路径
      //String avatarPath = data.getStringExtra(ISCardScanActivity.EXTRA_KEY_RESULT_AVATAR);
      //获取身份证识别ResultData识别结果
      ResultData result = (ResultData) data.getSerializableExtra(ISCardScanActivity.EXTRA_KEY_RESULT_DATA);

      if(result.isFront()){
        JSONObject object = new JSONObject();
        try {
          object.put("name",result.getName());
          object.put("sex",result.getSex());
          object.put("national",result.getNational());
          object.put("birthday",result.getBirthday());
          object.put("address",result.getAddress());
          object.put("id",result.getId());
          //object.put("isFont",result.isFront());
         // object.put("authority",result.getIssueauthority());
          //object.put("validity",result.getValidity());

          callbackContext.success(object.toString());

        } catch (JSONException e) {
          callbackContext.error("error");
        }
      }else{
        callbackContext.error("请扫描身份证正面");
      }

    } else if (resultCode == Activity.RESULT_CANCELED && requestCode == REQ_CODE_CAPTURE) {
        Toast.makeText(this.cordova.getActivity(),"识别取消或失败",Toast.LENGTH_SHORT).show();
    }
  }

}


