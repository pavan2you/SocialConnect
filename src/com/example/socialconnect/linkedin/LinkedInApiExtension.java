package com.example.socialconnect.linkedin;

import org.scribe.builder.api.LinkedInApi;

public class LinkedInApiExtension extends LinkedInApi {

	public String getRequestTokenEndpoint() {
		return "https://api.linkedin.com/uas/oauth/requestToken?scope=r_fullprofile+r_emailaddress+r_network";
	}
}
