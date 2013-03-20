package com.example.socialconnect;


public class SocialConnectAccessorFactory {

	private ISocialConnectAccessor mUserSelectedSocialConnectAccessor;
	
	private static SocialConnectAccessorFactory sTheSocialConnectAccessorFactoryInstance;
	
	public static synchronized SocialConnectAccessorFactory getInstance() {
		if (sTheSocialConnectAccessorFactoryInstance == null) {
			sTheSocialConnectAccessorFactoryInstance = new SocialConnectAccessorFactory();
		}
		
		return sTheSocialConnectAccessorFactoryInstance;
	}
	
	private SocialConnectAccessorFactory() {
	}
	
	public void setSocialConnectAccessor(ISocialConnectAccessor userSelectedAccessor) {
		mUserSelectedSocialConnectAccessor = userSelectedAccessor;
	}
	
	public ISocialConnectAccessor getSocialConnectAccessor() {
		return mUserSelectedSocialConnectAccessor;
	}
}
