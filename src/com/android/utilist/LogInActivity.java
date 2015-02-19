package com.android.utilist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.unccUtilistProject.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogInActivity extends Activity {
	EditText email, password;
	Button logIn, signUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		email = (EditText) findViewById(R.id.login_editText_email);
		password = (EditText) findViewById(R.id.login_editText_password);
		logIn = (Button) findViewById(R.id.login_button_login);
		signUp = (Button) findViewById(R.id.login_button_signup);

		ParseUser currentUser = ParseUser.getCurrentUser();
		// currentUser.logOut();
		if (currentUser != null) {
			Intent intent = new Intent(LogInActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		logIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (email.getText().toString().equals("")
						|| password.getText().toString().equals("")) {
					Toast.makeText(LogInActivity.this,
							"Email/Password not provided", Toast.LENGTH_SHORT)
							.show();
				} else {
					ParseUser.logInInBackground(email.getText().toString(),
							password.getText().toString(), new LogInCallback() {
								@Override
								public void done(ParseUser user,
										ParseException e) {
									if (user != null) {
										successfulLogin();
									} else {
										Toast.makeText(LogInActivity.this,
												"Login was unsuccessful",
												Toast.LENGTH_SHORT).show();
									}
								}

							});
				}
			}
		});

		signUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LogInActivity.this,
						SignUpActivity.class);
				startActivity(intent);
			}
		});
	}

	public void successfulLogin() {
		Toast.makeText(this, "Login was successful", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(LogInActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
