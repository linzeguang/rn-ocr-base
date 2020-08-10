/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {Component} from 'react';
import {StyleSheet, StatusBar} from 'react-native';
import {SafeAreaView, TouchableOpacity, Text} from 'react-native';
import ImagePicker from 'react-native-image-picker';
import {recIDCardFront, scanIDCardFront, scanIDCardBack} from 'src/native';

class App extends Component<{}> {
  public openPhotoAlbum = () => {
    // 图片选择器参数设置
    const options = {
      title: '请选择照片来源',
      cancelButtonTitle: '取消',
      takePhotoButtonTitle: '拍照',
      chooseFromLibraryButtonTitle: '相册',
      quality: 0.2,
      storageOptions: {
        skipBackup: true,
        path: 'images',
      },
    };
    ImagePicker.showImagePicker(options, async (response) => {
      if (response.didCancel) {
        console.log('User cancelled image picker');
      } else if (response.error) {
        console.log('ImagePicker Error: ', response.error);
      } else if (response.customButton) {
        console.log('User tapped custom button: ', response.customButton);
      } else {
        console.log(response);
        const path = response.path || '';
        if (!path) {
          return;
        }
        const data = await recIDCardFront(path);
        console.log('data>>>>>>>>>>>>>>>>>>>>>>>', data);
      }
    });
  };

  public render() {
    return (
      <SafeAreaView style={styles.view_wrap}>
        <StatusBar barStyle="dark-content" backgroundColor="transparent" />
        <SafeAreaView style={styles.view_wrap}>
          <TouchableOpacity style={styles.btn} onPress={() => {}}>
            <Text>init</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.btn} onPress={this.openPhotoAlbum}>
            <Text>身份证识别</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={styles.btn}
            onPress={() => scanIDCardFront()}>
            <Text>身份证正面扫描</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.btn} onPress={() => scanIDCardBack()}>
            <Text>身份证反面扫描</Text>
          </TouchableOpacity>
        </SafeAreaView>
      </SafeAreaView>
    );
  }
}

const styles = StyleSheet.create({
  view_wrap: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  btn: {
    width: 200,
    height: 56,
    marginBottom: 10,
    backgroundColor: 'pink',
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default App;
