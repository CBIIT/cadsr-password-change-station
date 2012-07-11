package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
//	private static final String BUNDLE_NAME = "gov.nih.nci.cadsr.cadsrpasswordchange.core.messages"; //dev
	private static final String BUNDLE_NAME = "messages"; //production

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
