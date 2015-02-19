package com.android.utilist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.unccUtilistProject.R;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity implements ValidationListener {
	@Required(order = 1)
	@TextRule(order = 2, message = "Please Enter Your Frist Name!")
	EditText firstName;
	@Required(order = 3)
	@TextRule(order = 4, message = "Please Enter Your Last Name!")
	EditText lastName;
	@Required(order = 5)
	@Email(order = 6, message = "Please Enter a Valid Email Address!")
	EditText email;
	@Password(order = 7)
	@TextRule(order = 8, minLength = 8, message = "Please Enter at Least 8 Characters.")
	EditText password;
	@ConfirmPassword(order = 9)
	EditText confirmPassword;
	Button createAccount, signUpLater;

	Validator validator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		validator = new Validator(this);
		validator.setValidationListener(this);
		firstName = (EditText) findViewById(R.id.signup_editText_firstName);
		lastName = (EditText) findViewById(R.id.signup_editText_lastName);
		email = (EditText) findViewById(R.id.signup_editText_email);
		password = (EditText) findViewById(R.id.signup_editText_password);
		confirmPassword = (EditText) findViewById(R.id.signup_editText_confirmPassword);
		createAccount = (Button) findViewById(R.id.signup_button_createAccount);
		signUpLater = (Button) findViewById(R.id.signup_button_signUpLater);

		signUpLater.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpActivity.this,
						LogInActivity.class);
				startActivity(intent);
				finish();
			}
		});

		createAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// // if((email.getText().toString().equals("") )||
				// (password.getText().toString().equals("")) ||
				// (firstName.getText().toString().equals(""))||
				// // (lastName.getText().toString().equals("")) ||
				// (confirmPassword.getText().toString().equals(""))){
				// // Toast.makeText(SignUpActivity.this,
				// "Name/Email/Password not provided",
				// Toast.LENGTH_SHORT).show();
				// // }
				// if((email.getText().toString().compareTo("") == 0)||
				// (password.getText().toString().compareTo("") == 0) ||
				// (firstName.getText().toString().compareTo("") == 0)||
				// (lastName.getText().toString().compareTo("") == 0) ||
				// (confirmPassword.getText().toString().compareTo("") == 0)){
				// Toast.makeText(SignUpActivity.this,
				// "Name/Email/Password not provided",
				// Toast.LENGTH_SHORT).show();
				// }
				// else {
				// if(password.getText().toString().equals(confirmPassword.getText().toString())){
				// ParseUser user = new ParseUser();
				// user.setUsername(email.getText().toString());
				// user.setPassword(password.getText().toString());
				// user.setEmail(email.getText().toString());
				// user.put("firstName", firstName.getText().toString());
				// user.put("lastName", lastName.getText().toString());
				//
				// user.signUpInBackground(new SignUpCallback() {
				// @Override
				// public void done(ParseException e) {
				// if (e == null) {
				// successfulAccCreation();
				// } else {
				// if(e.getCode() == ParseException.USERNAME_TAKEN){
				// showError();
				// }
				// }
				// }
				// });
				// }
				// else {
				// Toast.makeText(SignUpActivity.this, "Password missmatch",
				// Toast.LENGTH_SHORT).show();
				// }
				// }
				validator.validate();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	public void successfulAccCreation() {
		Toast.makeText(this, "Account Created Successful", Toast.LENGTH_SHORT)
				.show();
		Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	public void showError() {
		Toast.makeText(this, "Username is taken", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();

		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onValidationSucceeded() {
		ParseUser user = new ParseUser();
		user.setUsername(email.getText().toString());
		user.setPassword(password.getText().toString());
		user.setEmail(email.getText().toString());
		user.put("firstName", firstName.getText().toString());
		user.put("lastName", lastName.getText().toString());
		user.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					successfulAccCreation();
				} else {
					if (e.getCode() == ParseException.USERNAME_TAKEN) {
						showError();
					}
				}
			}
		});
	}
}
