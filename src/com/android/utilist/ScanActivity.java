package com.android.utilist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.model.ShoppingListItem;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

/**
 * Simple demo application illustrating the use of the Scandit SDK.
 * 
 * Important information for the developer with respect to Android 2.1 support!
 * 
 * Android 2.1 differs from subsequent versions of Android OS in that it does
 * not offer a camera preview mode in portrait mode (landscape only). Android
 * 2.2+ offers both - a camera preview in landscape mode and in portrait mode.
 * There are certain devices that run Android 2.2+ but do not properly implement
 * the methods needed for a portrait camera view.
 * 
 * To address this difference between the Android versions, the Scandit SDK
 * offers the following approaches and the developer needs to choose his
 * preferred option:
 * 
 * If you are showing the scanner on the full screen in a new Activity:
 * 
 * - Instantiate the ScanditSDKAutoAdjustingBarcodePicker which will choose
 * whether to use the new or legacy picker.
 * 
 * If you want to show the picker inside a view hierarchy/cropped/scaled you
 * have to make the distinction between the different pickers yourself. Fore
 * devices that don't support the new picker the following options exist:
 * 
 * - a scan view in landscape mode scanning only(!) that is fully customizable
 * by the developer - ScanditSDKBarcodePicker.class
 * 
 * - our own custom scan view with portrait mode scanning that offers only
 * limited customization options (show/hide title & tool bars, but no additional
 * Android UI elements) - LegacyPortraitScanditSDKBarcodePicker.class
 * 
 * For devices that do support the new picker the following options exist:
 * 
 * - a scan view with portrait mode scanning that is fully customizable by the
 * developer (RECOMMENDED) - ScanditSDKBarcodePicker.class
 * 
 * - any of the options listed under Android 2.1
 * 
 * We recommend that developers choose the scan view in portrait mode on Android
 * 2.2. It has the native Android look&feel and provides full customization. We
 * provide our own custom scan view
 * (LegacyPortraitScanditSDKBarcodePicker.class) in Android 2.1 to provide
 * backwards compatibility with Android 2.1.
 * 
 * To integrate the Scandit SDK, carry out the following three steps:
 * 
 * 1. Create a BarcodePicker object that manages camera access and bar code
 * scanning:
 * 
 * e.g. ScanditSDKBarcodePicker barcodePicker = new
 * ScanditSDKBarcodePicker(this, R.raw.class, "your app key", true,
 * ScanditSDKBarcodePicker.LOCATION_PROVIDED_BY_SCANDIT_SDK);
 * 
 * IMPORTANT: Make sure add your app key here. It is available from your Scandit
 * SDK account.
 * 
 * 2. Add it to the activity: my_activity.setContentView(barcodePicker);
 * 
 * 3. Implement the ScanditSDKListener interface (didCancel, didScanBarcode,
 * didManualSearch) and register with the ScanditSDKOverlayView to receive
 * callbacks: barcodePicker.getOverlayView().addListener(this);
 * 
 * 
 * If you want to use the custom scan view for scanning in portrait mode in
 * Android 2.1, instantiate the LegacyPortraitScanditSDKBarcodePicker class (as
 * shown in the example below). There is utility method available to determine
 * whenever the default portrait scan view is not available
 * ScanditSDKBarcodePicker.canRunPortraitPicker().
 * 
 * 
 * 
 * Copyright 2010 Mirasense AG
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied See the
 * License for the specific language governing premissions and limitations under
 * the License.
 */
public class ScanActivity extends Activity implements ScanditSDKListener {

	// The main object for recognizing a displaying barcodes.
	private ScanditSDK mBarcodePicker;
	private ShoppingListItem scannedItem;
	// Enter your Scandit SDK App key here.
	// Your Scandit SDK App key is available via your Scandit SDK web account.
	public static final String sScanditSdkAppKey = "kpBpTiviLf/262MM8XVZPns7tOonYzJAvoj/n22vuYA";
	public static final String UPCapi = "http://api.upcdatabase.org/json/b84e469ef23e420ab024fceebc9eb954/";
	public static final String WalmartApi = "http://walmartlabs.api.mashery.com/v1/search?query=UPC&format=json&apiKey=d9mx6dkzt8syqj4gk3aym6t9";
	public static final String SCANNED_ITEM_KEY = "scanned_item_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize and start the bar code recognition.
		initializeAndStartBarcodeScanning();
	}

	@Override
	protected void onPause() {
		// When the activity is in the background immediately stop the
		// scanning to save resources and free the camera.
		mBarcodePicker.stopScanning();

		super.onPause();
	}

	@Override
	protected void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		mBarcodePicker.startScanning();
		super.onResume();
	}

	/**
	 * Initializes and starts the bar code scanning.
	 */
	public void initializeAndStartBarcodeScanning() {
		// Switch to full screen.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// We instantiate the automatically adjusting barcode picker that will
		// choose the correct picker to instantiate. Be aware that this picker
		// should only be instantiated if the picker is shown full screen as the
		// legacy picker will rotate the orientation and not properly work in
		// non-fullscreen.
		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
				this, sScanditSdkAppKey,
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

		// Add both views to activity, with the scan GUI on top.
		setContentView(picker);
		mBarcodePicker = picker;

		// Register listener, in order to be notified about relevant events
		// (e.g. a successfully scanned bar code).
		mBarcodePicker.getOverlayView().addListener(this);

		// Show a search bar in the scan user interface.
		mBarcodePicker.getOverlayView().showSearchBar(true);
	}

	/**
	 * Called when a barcode has been decoded successfully.
	 * 
	 * @param barcode
	 *            Scanned barcode content.
	 * @param symbology
	 *            Scanned barcode symbology.
	 */
	@Override
	public void didScanBarcode(String barcode, String symbology) {
		// Remove non-relevant characters that might be displayed as rectangles
		// on some devices. Be aware that you normally do not need to do this.
		// Only special GS1 code formats contain such characters.
		String cleanedBarcode = "";
		StringBuilder searchItemUrl = new StringBuilder(UPCapi);
		String walmartUrl = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		searchItemUrl.append(cleanedBarcode);
		walmartUrl = WalmartApi.replace("UPC", cleanedBarcode);
		// Log.d("demo", walmartUrl + " " + searchItemUrl);
		new SearchShoppingListItem().execute(walmartUrl,
				searchItemUrl.toString());
		mBarcodePicker.stopScanning();
	}

	/**
	 * Called when the user entered a bar code manually.
	 * 
	 * @param entry
	 *            The information entered by the user.
	 */
	@Override
	public void didManualSearch(String entry) {
		String searchItemUrl = UPCapi + entry;
		String walmartUrl = WalmartApi.replace("UPC", entry);
		// Log.d("demo", searchItemUrl + " "+ walmartUrl);
		new SearchShoppingListItem().execute(walmartUrl, searchItemUrl);
		mBarcodePicker.stopScanning();
		// Toast.makeText(this, "User entered: " + entry, Toast.LENGTH_LONG)
		// .show();
	}

	@Override
	public void didCancel() {
		mBarcodePicker.stopScanning();
		// setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onBackPressed() {
		mBarcodePicker.stopScanning();
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	public class SearchShoppingListItem extends
			AsyncTask<String, Void, ShoppingListItem> {

		@Override
		protected ShoppingListItem doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statuscode = con.getResponseCode();
				if (statuscode == HttpURLConnection.HTTP_OK) {
					InputStream in = con.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder sb = new StringBuilder();
					String line = reader.readLine();
					while (line != null) {
						sb.append(line);
						line = reader.readLine();
					}

					scannedItem = new ShoppingListItem();
					scannedItem = ShoppingListItemSearchUtil.ShoppingListItemJSONParser
							.parseItemWalmart(sb.toString());
					if (scannedItem.getName() == null) {
						url = new URL(params[1]);
						con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");
						con.connect();
						statuscode = con.getResponseCode();
						if (statuscode == HttpURLConnection.HTTP_OK) {
							in = con.getInputStream();
							reader = new BufferedReader(new InputStreamReader(
									in));
							sb = new StringBuilder();
							line = reader.readLine();
							while (line != null) {
								sb.append(line);
								line = reader.readLine();
							}
							scannedItem = ShoppingListItemSearchUtil.ShoppingListItemJSONParser
									.parseItem(sb.toString());
						}
					}

					// Log.d("demo", scannedItem.toString());

					return scannedItem;

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;

		}

		@Override
		protected void onPostExecute(ShoppingListItem result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (scannedItem != null) {
				Intent intent = new Intent();
				intent.putExtra(SCANNED_ITEM_KEY, scannedItem);
				setResult(RESULT_OK, intent);
			} else {
				setResult(RESULT_CANCELED);
			}

			finish();
		}

	}
}
