'use strict';

import { PropTypes } from 'prop-types';
import { requireNativeComponent, View } from 'react-native';

module.exports = requireNativeComponent('RNTUiTimer', {
	name: 'RNTUiTimer',
	propTypes: {
		timeout: PropTypes.number,
		...View.propTypes // include the default view properties
	}
});
