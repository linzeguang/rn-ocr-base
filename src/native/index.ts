import {NativeModules, NativeEventEmitter} from 'react-native';

const {NativeMethods} = NativeModules;
const Emitter = new NativeEventEmitter(NativeMethods);

// 身份证正面识别
export function recIDCardFront(filePath: string) {
  return new Promise((resolve) => {
    // 订阅返回, 返回后取消订阅
    Emitter.addListener('recIDCardFront', (data) => {
      console.log('>>>>>>>>>>>recIDCardFront<<<<<<<<<<<', data);
      resolve(data);
      Emitter.removeAllListeners('recIDCardFront');
    });
    // 调用
    NativeMethods.recIDCard('front', filePath, 'recIDCardFront');
  });
}

// 身份证背面识别
export function recIDCardBack(filePath: string) {
  // 订阅返回, 返回后取消订阅
  Emitter.addListener('recIDCardBack', (data) => {
    console.log('>>>>>>>>>>>recIDCardBack<<<<<<<<<<<', data);
    Emitter.removeAllListeners('recIDCardBack');
  });
  // 调用
  NativeMethods.recIDCard('back', filePath, 'recIDCardBack');
}

// 身份证正面扫描
export function scanIDCardFront() {
  return new Promise((resolve) => {
    // 订阅返回, 返回后取消订阅
    Emitter.addListener('scanIDCardFront', (data) => {
      console.log('>>>>>>>>>>>scanIDCardFront<<<<<<<<<<<', data);
      resolve(data);
      Emitter.removeAllListeners('scanIDCardFront');
    });
    // 调用
    NativeMethods.scanIDCardFront();
  });
}

// 身份证反面扫描
export function scanIDCardBack() {
  return new Promise((resolve) => {
    // 订阅返回, 返回后取消订阅
    Emitter.addListener('scanIDCardBack', (data) => {
      console.log('>>>>>>>>>>>scanIDCardBack<<<<<<<<<<<', data);
      resolve(data);
      Emitter.removeAllListeners('scanIDCardBack');
    });
    // 调用
    NativeMethods.scanIDCardBack();
  });
}
