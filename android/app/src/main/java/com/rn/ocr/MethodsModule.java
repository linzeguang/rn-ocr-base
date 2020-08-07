package com.rn.ocr;

import androidx.annotation.NonNull;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class MethodsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static ReactApplicationContext reactContext;

    MethodsModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        reactContext.addLifecycleEventListener(this);
    }

    @NonNull
    @Override
    public String getName() {
        return "NativeMethods";
    }

    @Override
    public void onHostResume() {}

    @Override
    public void onHostPause() {}

    @Override
    public void onHostDestroy() {}

    /**
     * aip.license文件初始化 安全模式
     */
    @ReactMethod
    public void initOCR() {
        OCR.getInstance(reactContext).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                // 调用成功，返回AccessToken对象 String token = result.getAccessToken();
                String token = accessToken.getAccessToken();
                WritableMap params = Arguments.createMap();
                params.putBoolean("success", true);
                params.putString("token", token);
                params.putString("error", null);
                sendEvent("initOCR", params);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                error.printStackTrace();
                WritableMap params = Arguments.createMap();
                params.putBoolean("success", false);
                params.putString("token", null);
                params.putDouble("error", error.getErrorCode());
                sendEvent("initOCR", params);
            }
        }, reactContext);
    }

    /**
     * Ak、Sk初始化
     * @param ak
     * @param sk
     */
    @ReactMethod
    public void initOCRWithAkSk(final String ak, final String sk) {
        OCR.getInstance(reactContext).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                // 调用成功，返回AccessToken对象 String token = result.getAccessToken();
                String token = accessToken.getAccessToken();
                WritableMap params = Arguments.createMap();
                params.putBoolean("success", true);
                params.putString("token", token);
                params.putString("error", null);
                sendEvent("initOCRWithAkSk", params);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                error.printStackTrace();
                WritableMap params = Arguments.createMap();
                params.putBoolean("success", false);
                params.putString("token", null);
                params.putString("ak", ak);
                params.putString("sk", sk);
                params.putDouble("error", error.getErrorCode());
                sendEvent("initOCRWithAkSk", params);
            }
        }, reactContext, ak, sk);
    }

    /**
     * 通知前端
     * @param eventName
     * @param params
     */
    private void sendEvent(final String eventName, WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
