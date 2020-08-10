package com.rn.ocr;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;

import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;
import java.util.Objects;

public class MethodsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static ReactApplicationContext reactContext;
    private static final int REQUEST_CODE_BANKCARD = 111; //银行卡
    private static final int REQUEST_CODE_CARD_FRONT = 151; //身份证正面
    private static final int REQUEST_CODE_CARD_BACK = 152; //身份证反面

    MethodsModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        reactContext.addLifecycleEventListener(this);

        ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                Log.d("BaiDuOCR", "requestCode=" + requestCode);
                Log.d("BaiDuOCR", "resultCode=" + resultCode);
                Log.d("BaiDuOCR", "Activity.RESULT_OK=" + Activity.RESULT_OK);
                if (requestCode == REQUEST_CODE_CARD_FRONT && resultCode == Activity.RESULT_OK) {
                    String filePath = FileUtil.getSaveFile(getReactApplicationContext()).getAbsolutePath();
                    recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath, "scanIDCardFront"); //解析身份证正面

                } else if (requestCode == REQUEST_CODE_CARD_BACK && resultCode == Activity.RESULT_OK) {
                    String filePath = FileUtil.getSaveFile(getReactApplicationContext()).getAbsolutePath();
                    recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath, "scanIDCardBack"); //解析身份证反面

                }

            }
        };

        reactContext.addActivityEventListener(mActivityEventListener);
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
     * 身份证识别
     * @param idCardSide
     * @param filePath
     * @param eventName
     */
    @ReactMethod
    public void recIDCard(final String idCardSide, final String filePath, final String eventName) {
        IDCardParams params = new IDCardParams();
        // 设置图片信息
        params.setImageFile(new File(filePath));
        // 设置身份证正反面
        params.setIdCardSide(idCardSide);
        // 设置方向检测
        params.setDetectDirection(true);

        OCR.getInstance(reactContext).recognizeIDCard(params, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult idCardResult) {
                WritableMap params = Arguments.createMap();
                WritableMap data = Arguments.createMap();
                // 判空
                if (idCardResult != null) {
                    params.putBoolean("success", true);
                    // 判断正反面
                    if (idCardSide.equals("front")) {
                        String name = idCardResult.getName().toString();
                        String gender = idCardResult.getGender().toString();
                        String ethnic = idCardResult.getEthnic().toString();
                        String address = idCardResult.getAddress().toString();
                        String idNumber = idCardResult.getIdNumber().toString();
                        String birthday = idCardResult.getBirthday().toString();
                        data.putString("name", name);
                        data.putString("gender", gender);
                        data.putString("ethnic", ethnic);
                        data.putString("address", address);
                        data.putString("idNumber", idNumber);
                        data.putString("birthday", birthday);
                    } else {
                        String signDate = idCardResult.getSignDate().toString();
                        String expiryDate = idCardResult.getExpiryDate().toString();
                        String issueAuthority = idCardResult.getIssueAuthority().toString();
                        data.putString("signDate", signDate);
                        data.putString("expiryDate", expiryDate);
                        data.putString("issueAuthority", issueAuthority);
                    }
                    params.putMap("data", data);
                } else {
                    params.putBoolean("success", false);
                    params.putString("error", null);
                    params.putString("data", null);
                }
                sendEvent(eventName, params);
            }

            @Override
            public void onError(OCRError error) {
                WritableMap params = Arguments.createMap();
                params.putBoolean("success", false);
                params.putString("error", error.getMessage());
                sendEvent(eventName, params);
            }
        });
    }

    /**
     * 身份证正面扫描
     */
    @ReactMethod
    public void scanIDCardFront() {
        Intent intent = new Intent(getCurrentActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getReactApplicationContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        Objects.requireNonNull(getCurrentActivity()).startActivityForResult(intent, REQUEST_CODE_CARD_FRONT);
    }

    /**
     * 身份证反面扫描
     */
    @ReactMethod
    public void scanIDCardBack() {
        Intent intent = new Intent(getCurrentActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getReactApplicationContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
        Objects.requireNonNull(getCurrentActivity()).startActivityForResult(intent, REQUEST_CODE_CARD_BACK);
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
