/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {Component, Fragment} from 'react';
import {StyleSheet, StatusBar} from 'react-native';
import {SafeAreaView, TouchableOpacity, Text} from 'react-native';
import ImagePicker from 'react-native-image-picker';

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
    ImagePicker.showImagePicker(options, (response) => {
      if (response.didCancel) {
        console.log('User cancelled image picker');
      } else if (response.error) {
        console.log('ImagePicker Error: ', response.error);
      } else if (response.customButton) {
        console.log('User tapped custom button: ', response.customButton);
      } else {
        console.log(response);
        const uri = 'data:image/jpeg;base64,' + response.data;
        console.log(uri);
      }
    });
  };

  public render() {
    return (
      <Fragment>
        <StatusBar barStyle="dark-content" backgroundColor="transparent" />
        <SafeAreaView style={styles.view_wrap}>
          <TouchableOpacity style={styles.btn} onPress={this.openPhotoAlbum}>
            <Text>身份证识别</Text>
          </TouchableOpacity>
        </SafeAreaView>
      </Fragment>
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
    backgroundColor: 'pink',
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default App;
