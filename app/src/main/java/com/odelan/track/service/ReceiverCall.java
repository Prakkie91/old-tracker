package com.odelan.track.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.odelan.track.utils.Common;


public class ReceiverCall extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("Service Stops", "Ohhhhhhh");

		//context.startService(new Intent(context, LocationService.class));
	}
}
