package com.example.socialconnect;

import com.example.socialconnect.linkedin.LinkedInAccessor;

import android.app.Application;

/**
 * This app does syncing user account(s) of several networking sites. But showcases an example with
 * connecting to Linked and syncing his/her connections. 
 * 
 * @author pkunchapu
 *
 */
public class SocialConnectApplication extends Application {
	
	public void onCreate() {
		super.onCreate();
		
		LinkedInAccessor linkedInAccessor = new LinkedInAccessor();
		SocialConnectAccessorFactory.getInstance().setSocialConnectAccessor(linkedInAccessor); 
	}
}
