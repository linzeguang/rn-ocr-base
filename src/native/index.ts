import {NativeModules, NativeEventEmitter} from 'react-native';

const {NativeMethods} = NativeModules;
const Emitter = new NativeEventEmitter(NativeMethods);
console.log('-----------------------');
console.log(NativeMethods);

export function initOCR() {
  // 订阅返回, 返回后取消订阅
  Emitter.addListener('initOCR', (data) => {
    console.log('>>>>>>>>>>>initOCR<<<<<<<<<<<', data);
    Emitter.removeAllListeners('initOCR');
  });
  // 调用
  NativeMethods.initOCR();
}
export function initOCRWithAkSk(ak: string, sk: string) {
  // 订阅返回, 返回后取消订阅
  Emitter.addListener('initOCRWithAkSk', (data) => {
    console.log('>>>>>>>>>>>initOCRWithAkSk<<<<<<<<<<<', data);
    Emitter.removeAllListeners('initOCRWithAkSk');
  });
  // 调用
  NativeMethods.initOCRWithAkSk(ak, sk);
}

export function scanIDCardFront() {}
