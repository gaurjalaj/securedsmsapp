import React from 'react';
import {
  Button,
  NativeModules,
  PermissionsAndroid,
  SafeAreaView,
  Text,
  useColorScheme,
} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';

function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const test = async () => {
    try {
      console.log('hit');
      await PermissionsAndroid.request('android.permission.READ_SMS');
      // const res = await NativeModules.SmsModule.getSmsByAddress(
      // '+918475949605',
      // );
      const res = await NativeModules.SmsModule.getSmsConversationsList();
      console.log(res);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <Text>Secured Sms App</Text>
      <Button onPress={test} title="test" />
    </SafeAreaView>
  );
}

export default App;
