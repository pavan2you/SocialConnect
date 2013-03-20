package com.example.socialconnect.linkedin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.socialconnect.ISocialConnectAccessor;
import com.example.socialconnect.platform.SocialNetworkEnvironment;
import com.example.socialconnect.platform.JsonHelper;
import com.example.socialconnect.platform.db.RawContact;

public class LinkedInAccessor implements ISocialConnectAccessor {

	public final static String APIKEY = "qam9tekjo07m";
	public final static String APISECRET = "1tTkP2rjQUufbzfO";
	public final static String CALLBACK = "oauth://linkedin";

	private OAuthService mOAuthService;
	private Token mRequestToken;
	private Token mAccessToken;

	public LinkedInAccessor() {
		SocialNetworkEnvironment.init("com.linkedin", "com.linkedin");

		mOAuthService = new ServiceBuilder().provider(LinkedInApiExtension.class)
				.apiKey(LinkedInAccessor.APIKEY).apiSecret(LinkedInAccessor.APISECRET)
				.callback(LinkedInAccessor.CALLBACK).build();
	}

	private void loadRequestToken() throws OAuthException {
		mRequestToken = mOAuthService.getRequestToken();
	}

	public String getAuthorizationUrl() throws OAuthException {
		loadRequestToken();
		return mOAuthService.getAuthorizationUrl(mRequestToken);
	}

	private void loadAccessToken(String url) throws OAuthException {
		Uri uri = Uri.parse(url);
		String verifier = uri.getQueryParameter("oauth_verifier");
		Verifier v = new Verifier(verifier);
		mAccessToken = mOAuthService.getAccessToken(mRequestToken, v);
	}

	public Intent createAccountAndGetResult(String autorizedVerifiedUrl,
			AccountManager accountManager) {

		loadAccessToken(autorizedVerifiedUrl);

		String emailAddress = getCurrentUserEmailAddress();

		final Account account = new Account(emailAddress, SocialNetworkEnvironment.ENV_VAR_ACCOUNT_TYPE);
		boolean accountCreated = accountManager.addAccountExplicitly(account,
				mAccessToken.getToken(), null);
		Intent intent = null;
		if (accountCreated) {
			accountManager.setUserData(account, "ACCESS_SECRET", mAccessToken.getSecret());
			intent = new Intent();
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, emailAddress);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, SocialNetworkEnvironment.ENV_VAR_ACCOUNT_TYPE);
		}

		return intent;
	}

	private String getCurrentUserEmailAddress() {
		String currentUserDataUrl = "http://api.linkedin.com/v1/people/~/email-address?format=json";

		OAuthRequest request = new OAuthRequest(Verb.GET, currentUserDataUrl);
		Token t = new Token(mAccessToken.getToken(), mAccessToken.getSecret());
		mOAuthService.signRequest(t, request);

		Response response = null;
		String emailAddress = null;
		try {
			response = request.send();
			if (response.isSuccessful()) {
				emailAddress = response.getBody();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return emailAddress;
	}

	public List<RawContact> syncContacts(Context context, Account account, String authtoken,
			long serverSyncState, List<RawContact> dirtyContacts) throws JSONException,
			ParseException, IOException, AuthenticationException {

		SharedPreferences prefs = context.getSharedPreferences(
				"LinkedInConnect_LinkedIn_Preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();

		long lastSyncedTimeInMillies = prefs.getLong("LinkedInContacts_LastSynced_Time_In_Millies",
				0);
		
		String syncUrl = "http://api.linkedin.com/v1/people/~/connections";
		syncUrl += "?format=json";
		
		if (lastSyncedTimeInMillies == 0) {
			syncUrl += "&count=10"; // Remove to download all contacts			
		}
		else {
			syncUrl += "modified=new&modified-since=" + lastSyncedTimeInMillies;
		}

		OAuthService mOAuthService = new ServiceBuilder().provider(LinkedInApiExtension.class)
				.apiKey(LinkedInAccessor.APIKEY).apiSecret(LinkedInAccessor.APISECRET)
				.callback(LinkedInAccessor.CALLBACK).build();

		AccountManager accountManager = AccountManager.get(context);
		String accessToken = accountManager.getPassword(account);
		String accessSecret = accountManager.getUserData(account, "ACCESS_SECRET");

		OAuthRequest request = new OAuthRequest(Verb.GET, syncUrl);
		Token t = new Token(accessToken, accessSecret);
		mOAuthService.signRequest(t, request);

		Response response = null;
		ArrayList<RawContact> serverDirtyList = new ArrayList<RawContact>();
		try {
			response = request.send();
			if (isAccessTokenExpiredOrInvalid(response)) {
				/*
				 *  App won't prompt login activity up to the next call to getAuthToken()!
				 */
				accountManager.setPassword(account, null);
				prefsEditor.clear();
			} else if (response.isSuccessful()) {
				prefsEditor.putLong("LinkedInContacts_LastSynced_Time_In_Millies",
						System.currentTimeMillis());
				prefsEditor.commit();
				
				if (response.getBody() != null && response.getBody().length() > 0) {
					PersonList personList = (PersonList) JsonHelper.getDTO(response.getBody(),
							PersonList.class.getName());
					if (personList != null) {
						serverDirtyList = personList.toRawContactList();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return serverDirtyList;
	}

	private boolean isAccessTokenExpiredOrInvalid(Response response) {
		return response.getCode() == 401;
	}
}
