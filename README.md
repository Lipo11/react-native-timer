# React native UI timer
Native graphical timer for your react native app.

### Installing
```
npm install react-native-ui-timer --save
- or -
yarn add react-native-ui-timer
```

### Usage
```
import React from 'react';
import RNUiTimer from 'react-native-ui-timer';

export default class Example extends React.Component
{
    render() {
        return (
            <View>
				<RNUiTimer style={styles.timer} timeout={ 90 /*time in seconds*/ } />
			</View>
        );
    }
}
```
### API

#### timeout
Timeout in seconds