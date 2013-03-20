package com.example.socialconnect.facebook;

import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

import com.example.socialconnect.ISocialConnectAccessor;
import com.example.socialconnect.platform.db.RawContact;

public class FacebookAccessor implements ISocialConnectAccessor {

	@Override
	public Intent createAccountAndGetResult(String authorizedVerifiedUrl, AccountManager am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthorizationUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RawContact> syncContacts(Context mContext, Account account, String authtoken,
			long lastSyncMarker, List<RawContact> dirtyContacts) throws JSONException,
			ParseException, IOException, AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
