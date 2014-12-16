package com.example.utilist;

import com.example.utilist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashScreenActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		if(isNetworkAvailable()){
			new CountDownTimer(3000, 1000) {
	
			     public void onTick(long millisUntilFinished) {
			         
			     }
	
			     public void onFinish() {
			        Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
			        startActivity(intent);
			        finish();
			     }
			  }.start();
		}
		else{
			showNoInternet();
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void showNoInternet(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Internet Connection Error");
		builder.setMessage("There is no Internet connection.\n Pleace connect to Internet... ");
		builder.setCancelable(false);
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
				
			}
		});
		builder.create().show();
	}
}
