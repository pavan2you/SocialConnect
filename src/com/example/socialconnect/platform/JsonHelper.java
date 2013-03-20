package com.example.socialconnect.platform;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JsonHelper {
	
	public static String getJSONString(IDataTransferObject dto) {
		Gson gson = new Gson();
		String jsonData = gson.toJson(dto);
		return jsonData;
	}

	public static IDataTransferObject getDTO(String jsonString, String dtoClassName) {
		Gson gson = new Gson();
		Class<? extends IDataTransferObject> dtoClass;
		try {
			dtoClass = Class.forName(dtoClassName).asSubclass(IDataTransferObject.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return gson.fromJson(jsonString, dtoClass);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getJSONStringFromJSONString(String jsonString, String jsonElementName) {
		String jsonDataForElement = null;
		try {
			JSONObject jsonData = new JSONObject(new String(jsonString));
			jsonDataForElement = jsonData.getString(jsonElementName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonDataForElement;
	}
}
