package com.example.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 排行网络访问
 * @author Administrator
 *
 */
public class HotProtocol extends BaseProtocol<ArrayList<String>> {

	@Override
	public String getKey() {
		return "hot";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<String> parseData(String result) {
		try {
			JSONArray ja = new JSONArray(result);
			
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < ja.length(); i++) {
				String content = ja.getString(i);
				list.add(content);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
