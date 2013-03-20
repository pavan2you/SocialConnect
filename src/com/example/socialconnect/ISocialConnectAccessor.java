package com.example.socialconnect;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import com.example.socialconnect.platform.db.RawContact;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

public interface ISocialConnectAccessor {

	Intent createAccountAndGetResult(String authorizedVerifiedUrl, AccountManager am);

	String getAuthorizationUrl();

	List<RawContact> syncContacts(Context mContext, Account account, String authtoken,
			long lastSyncMarker, List<RawContact> dirtyContacts) throws JSONException,
			ParseException, IOException, AuthenticationException;

}
