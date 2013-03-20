package com.example.socialconnect.platform.authenticator;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.socialconnect.R;
import com.example.socialconnect.SocialConnectAccessorFactory;

public class UserAccountAuthenticatorActivity extends AccountAuthenticatorActivity {

	private static final String TAG = "UserAccountAuthenticatorActivity";
	
	private WebView mLoginWebView;
	private ProgressDialog mProgressDialog;
	private ApplicationAuthorizationTask mApplicationAuthorizationTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_web_view);

		mLoginWebView = (WebView) findViewById(R.id.login_webview);
		mLoginWebView.setWebViewClient(new LoginWebViewClient());

		showProgressDialog();
		mApplicationAuthorizationTask = new ApplicationAuthorizationTask();
		mApplicationAuthorizationTask.execute();
	}

	private class LoginWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, final String url) {
			super.shouldOverrideUrlLoading(view, url);

			if (url.startsWith("oauth")) {
				mLoginWebView.setVisibility(WebView.GONE);

				new Thread() {
					public void run() {
						addAccoutToAccountManagerFromAuthorizedVerifiedUrl(url);
					}
				}.start();
			}

			return false;
		}
	}

	private void addAccoutToAccountManagerFromAuthorizedVerifiedUrl(
			String authorizedVerifiedUrl) {
		AccountManager am = AccountManager.get(getApplicationContext());
		Intent resultIntent = SocialConnectAccessorFactory.getInstance().getSocialConnectAccessor()
				.createAccountAndGetResult(authorizedVerifiedUrl, am);

		if (resultIntent != null) {
			setAccountAuthenticatorResult(resultIntent.getExtras());
			setResult(RESULT_OK, resultIntent);
			finish();
		}
	}

	private class ApplicationAuthorizationTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... args) {
			// Temporary URL
			String authURL = "http://api.linkedin.com/";

			try {
				authURL = SocialConnectAccessorFactory.getInstance().getSocialConnectAccessor()
						.getAuthorizationUrl();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return authURL;
		}

		@Override
		protected void onPostExecute(String authURL) {
			mLoginWebView.loadUrl(authURL);
			hideProgressDialog();
		}
	}
	
	private void showProgressDialog() {
		mProgressDialog = ProgressDialog.show(this, "", "Processing...", true, true);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.i(UserAccountAuthenticatorActivity.TAG, "user cancelling authentication");
				if (mApplicationAuthorizationTask != null
						&& !mApplicationAuthorizationTask.isCancelled()) {
                	mApplicationAuthorizationTask.cancel(true);
                }
            }
        });
	}
	
	private void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}

