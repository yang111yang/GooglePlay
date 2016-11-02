package com.example.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.googleplay.domain.AppInfo;

/**
 * 应用网络请求
 * @author Administrator
 *
 */
public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<AppInfo> parseData(String result) {
		
		try {
			JSONArray ja = new JSONArray(result);
			ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				AppInfo info = new AppInfo();
				info.id = jo.getString("id");
				info.name = jo.getString("name");
				info.packageName = jo.getString("packageName");
				info.iconUrl = jo.getString("iconUrl");
				info.downloadUrl = jo.getString("downloadUrl");
				info.des = jo.getString("des");
				info.stars = jo.getDouble("stars");
				info.size = jo.getLong("size");
				appInfoList.add(info);
			}
			return appInfoList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
