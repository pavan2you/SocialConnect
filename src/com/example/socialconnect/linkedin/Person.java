package com.example.socialconnect.linkedin;

import com.example.socialconnect.platform.IDataTransferObject;
import com.example.socialconnect.platform.db.RawContact;

public class Person implements IDataTransferObject {

	private String firstName;
	private String lastName;
	private String id;
	private String pictureUrl;
	private Location location;
	private String emailAddress;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}
	
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public RawContact toRawContact() {
		
		// INFO : for time being I've hard coded phone numbers and emails for linked in api.
		
		RawContact rawContact = RawContact.create(firstName + lastName, firstName, lastName,
				"9090909090", "0909090909", "9999900000", firstName + "." + lastName
						+ "@linkedIn.com", "active", false, -1, id);
		return rawContact;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", id=" + id
				+ ", pictureUrl=" + pictureUrl + ", location=" + location + "]";
	}
}
