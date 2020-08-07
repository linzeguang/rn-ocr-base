/**
 * @format
 */

import {AppRegistry, Platform} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {check, request} from 'react-native-permissions';
import {PERMISSIONS, RESULTS} from 'react-native-permissions';
import {initOCR} from 'src/native';

if (Platform.OS === 'ios') {
  checkiOSPermissions();
} else {
  checkAndroidPermissions();
}

// 初始化百度OCR
initOCR();

AppRegistry.registerComponent(appName, () => App);

async function checkiOSPermissions() {
  const CAMERA = await check(PERMISSIONS.IOS.CAMERA);
  const PHOTO_LIBRARY = await check(PERMISSIONS.IOS.PHOTO_LIBRARY);
  if (CAMERA === RESULTS.DENIED) {
    await request(PERMISSIONS.IOS.CAMERA);
  }
  if (PHOTO_LIBRARY === RESULTS.DENIED) {
    await request(PERMISSIONS.IOS.PHOTO_LIBRARY);
  }
}

async function checkAndroidPermissions() {
  const CAMERA = await check(PERMISSIONS.ANDROID.CAMERA);
  const WRITE_EXTERNAL_STORAGE = await check(
    PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE,
  );
  if (CAMERA === RESULTS.DENIED) {
    await request(PERMISSIONS.ANDROID.CAMERA);
  }
  if (WRITE_EXTERNAL_STORAGE === RESULTS.DENIED) {
    await request(PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE);
  }
}
