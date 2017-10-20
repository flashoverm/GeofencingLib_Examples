package de.geofencing.products.productnotificationapp;

import com.google.firebase.iid.FirebaseInstanceIdService;

import de.geofencing.firebaseClient.FirebaseClient;

/** Implementation of FirebaseInstanceIdService for GeofencingDevice.
 * Sends token on refresh to the server.
 * 
 * @author Markus Thral
 *
 */
public class FirebaseDeviceIdService extends FirebaseInstanceIdService {

	@Override
	public void onTokenRefresh() {
		new FirebaseClient(getApplicationContext()).sendToken();
	}
}
