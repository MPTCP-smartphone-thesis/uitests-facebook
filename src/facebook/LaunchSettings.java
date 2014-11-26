package facebook;

import java.util.Date;

import utils.Utils;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchSettings extends UiAutomatorTestCase {

	private static final String ID_STATUS_BUTTON = "com.facebook.katana:id/publisher_button0";
	private static final String ID_STATUS_TEXT = "com.facebook.katana:id/status_text";
	private static final String ID_POST = "com.facebook.katana:id/primary_named_button";
	private static final String ID_PHOTO_BUTTON = "com.facebook.katana:id/publisher_button1";
	private static final String ID_TAKE_PHOTO = "com.facebook.katana:id/image_camera_icon";
	private static final String ID_CAPTURE_PHOTO = "com.android.camera2:id/shutter_button";
	private static final String ID_ACCEPT_PHOTO = "com.android.camera2:id/done_button";
	private static final String ID_ACCEPT_PHOTO_2 = "com.android.camera2:id/btn_done";
	private static final String ID_CHECKOUT_BUTTON = "com.facebook.katana:id/publisher_button2";
	private static final String ID_LIST_CHECK = "com.facebook.katana:id/list_view";
	private static final String ID_DONE_BUTTON = "com.facebook.katana:id/reaction_done_button";
	private static final String ID_LIST_FEED = "android:id/list";
	private static final String ID_PRIVACY_BUTTON = "com.facebook.katana:id/public_privacy_button";
	private static final String ID_LOCATION_WARNING = "android:id/button2";

	private static final int NB_TIMES = 3;

	private void returnToMainMenu() {
		UiObject backButton = Utils.getObjectWithDescription("Back");
		while (backButton.exists()) {
			Utils.click(backButton);
			UiObject deleteText = Utils.getObjectWithText("Yes, Delete");
			if (deleteText.exists()) {
				assertTrue("Delete button diseappeared???",
						Utils.click(deleteText));
			}
			sleep(3000);
			backButton = Utils.getObjectWithDescription("Back");
		}
	}

	/**
	 * Remove privacy warning if any
	 */
	private void removePrivacyWarning() {
		try {
			UiObject privacy = Utils.getObjectWithId(ID_PRIVACY_BUTTON);
			if (privacy.isEnabled()) {
				Utils.click(privacy);
				sleep(1000);
			}
		} catch (UiObjectNotFoundException e) {
			// Button is not there, no problem
		}
	}

	private void textStatus() {
		removePrivacyWarning();
		assertTrue("Cannot publish new status", Utils.click(ID_STATUS_BUTTON));

		sleep(1000);
		removePrivacyWarning();
		Date now = new Date();
		assertTrue("Cannot write my status",
				Utils.setText(ID_STATUS_TEXT,
						"This is a test status " + now.getTime()));

		removePrivacyWarning();
		assertTrue("Cannot post my status", Utils.click(ID_POST));
	}

	/**
	 * Remove location warning: Remember photo locations?
	 *
	 * Tag your photos and videos with the locations where they are taken.
	 *
	 * Other apps can access this information along with your saved images.
	 */
	private void removeLocationWarning() {
		try {
			UiObject location = Utils.getObjectWithId(ID_LOCATION_WARNING);
			if (location.isEnabled() && location.getText().equals("No thanks")) {
				Utils.click(location);
				sleep(1000);
			}
		} catch (UiObjectNotFoundException e) {
			// Button is not there, no problem
		}

	}

	private void photoStatus() {
		sleep(1000);
		assertTrue("Cannot publish new photo", Utils.click(ID_PHOTO_BUTTON));
		sleep(2000);
		removePrivacyWarning();
		removeLocationWarning();
		assertTrue("Cannot take new photo", Utils.click(ID_TAKE_PHOTO));
		sleep(2000);
		removePrivacyWarning();
		removeLocationWarning();
		assertTrue("Cannot capture photo", Utils.click(ID_CAPTURE_PHOTO));
		sleep(4000);
		removePrivacyWarning();
		if (!Utils.click(ID_ACCEPT_PHOTO)) // 2 possible buttons...
			assertTrue("Cannot accept the photo", ID_ACCEPT_PHOTO_2);
		sleep(3000);
		removePrivacyWarning();
		Date now = new Date();
		assertTrue(
				"Cannot write my status",
				Utils.setText(ID_STATUS_TEXT,
						"This is a test photo " + now.getTime()));
		assertTrue("Cannot post my status", Utils.click(ID_POST));
	}

	private void checkoutStatus() {
		sleep(1000);
		assertTrue("Cannot click the checkout button",
				Utils.click(ID_CHECKOUT_BUTTON));
		sleep(1000);
		assertTrue(
				"Cannot select the first location",
				Utils.click(new UiObject(new UiSelector().resourceId(
						"android:id/list").childSelector(
						new UiSelector().className(
								"android.widget.RelativeLayout").instance(0)))));
		sleep(1500);
		assertTrue("Cannot choose my buddy :'(", Utils.click(new UiObject(
				new UiSelector().resourceId(ID_LIST_CHECK).childSelector(
						new UiSelector().className("android.view.View")
								.instance(3)))));
		sleep(1000);
		removePrivacyWarning();
		assertTrue("Cannot post my status", Utils.click(ID_POST));

		Date now = new Date();
		assertTrue(
				"Cannot write my status",
				Utils.setText(ID_STATUS_TEXT,
						"This is a test checkout " + now.getTime()));
		assertTrue("Cannot post my status", Utils.click(ID_POST));
		sleep(1000);
		Utils.click(ID_DONE_BUTTON);
	}

	private void updateFeed() {
		sleep(1000);
		Utils.scrollBackward(Utils.getScrollableWithId(ID_LIST_FEED));
		sleep(3000);
	}

	public void testDemo() throws UiObjectNotFoundException {
		assertTrue("OOOOOpps",
				Utils.openApp(this, "Facebook", "com.facebook.katana"));
		sleep(2000);

		returnToMainMenu();

		for (int i = 0; i < NB_TIMES; i++) {
			updateFeed();
			textStatus();
			photoStatus();
			checkoutStatus();
		}

	}

}