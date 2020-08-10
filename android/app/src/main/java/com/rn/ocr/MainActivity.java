package com.rn.ocr;

import android.os.Bundle;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "rn_ocr";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    initOCR();  // 或者initOCRWithAkSk()  传入ak、sk
    super.onCreate(savedInstanceState);
  }

  /**
   * aip.license文件初始化 安全模式
   */
  public void initOCR() {
    OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
      @Override
      public void onResult(AccessToken accessToken) {
        // 调用成功，返回AccessToken对象 String token = result.getAccessToken();
        String token = accessToken.getAccessToken();
        Log.d("BaiDuOCR", "百度OCR初始化成功" + token);
        initLocalQualityControl();
      }

      @Override
      public void onError(OCRError error) {
        // 调用失败，返回OCRError子类SDKError对象
        Log.d("BaiDuOCR", "百度OCR初始化失败" + error.getMessage());
      }
    }, this);
  }

  /**
   * Ak、Sk初始化
   * @param ak
   * @param sk
   */
  public void initOCRWithAkSk(final String ak, final String sk) {
    OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
      @Override
      public void onResult(AccessToken accessToken) {
        // 调用成功，返回AccessToken对象 String token = result.getAccessToken();
        String token = accessToken.getAccessToken();
        Log.d("BaiDuOCR", "百度OCR初始化成功" + token);
        initLocalQualityControl();
      }

      @Override
      public void onError(OCRError error) {
        // 调用失败，返回OCRError子类SDKError对象
        Log.d("BaiDuOCR", "百度OCR初始化失败" + error.getMessage());
      }
    }, this, ak, sk);
  }

  /**
   * 初始化本地质量控制模型
   */
  public void initLocalQualityControl() {
    CameraNativeHelper.init(
            this,
            OCR.getInstance(this).getLicense(),
            new CameraNativeHelper.CameraNativeInitCallback() {
              @Override
              public void onError(int errorCode, Throwable e) {
                String msg;
                switch (errorCode) {
                  case CameraView.NATIVE_SOLOAD_FAIL:
                    msg = "加载so失败，请确保apk中存在ui部分的so";
                    break;
                  case CameraView.NATIVE_AUTH_FAIL:
                    msg = "授权本地质量控制token获取失败";
                    break;
                  case CameraView.NATIVE_INIT_FAIL:
                    msg = "本地质量控制";
                    break;
                  default:
                    msg = String.valueOf(errorCode);
                }
                System.out.println("百度OCR 本地质量控制初始化错误，错误原因： " + msg);
              }
            }
    );
  }
}
