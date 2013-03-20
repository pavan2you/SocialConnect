package com.example.socialconnect.linkedin;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.socialconnect.platform.IDataTransferObject;
import com.example.socialconnect.platform.db.RawContact;

public class PersonList implements IDataTransferObject {

	private int _total;
	private Person[] values;

	public int get_total() {
		return _total;
	}

	public void set_total(int _total) {
		this._total = _total;
	}
	
	public Person[] getvalues() {
		return values;
	}

	public void setvalues(Person[] persons) {
		this.values = persons;
	}

	public ArrayList<RawContact> toRawContactList() {
		ArrayList<RawContact> rawContactList = new ArrayList<RawContact>();
		for (Person p : values) {
			rawContactList.add(p.toRawContact());
		}
		return rawContactList;
	}
	
	@Override
	public String toString() {
		return "PersonList [persons=" + Arrays.toString(values) + "]";
	}
}
