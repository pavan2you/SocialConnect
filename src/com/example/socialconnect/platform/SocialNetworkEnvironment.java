package com.example.socialconnect.platform;

public class SocialNetworkEnvironment {

	public static String ENV_VAR_ACCOUNT_TYPE = "com.linkedin";

    public static String ENV_VAR_AUTHTOKEN_TYPE = "com.linkedin";
    
    public static void init(String envAccountType, String envAccountAuthTokenType) {
    	ENV_VAR_ACCOUNT_TYPE = envAccountType;
    	ENV_VAR_AUTHTOKEN_TYPE = envAccountAuthTokenType;
    }
}
