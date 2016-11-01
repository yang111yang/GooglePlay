package com.example.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.googleplay.domain.AppInfo;

/**
 * 首页网络数据解析
 * @author Administrator
 *
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParams() {
		return ""; // 此处应该传空串，不要传null
	}

	@Override
	public ArrayList<AppInfo> parseData(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			// 解析应用列表数据
			JSONArray ja = jo.getJSONArray("list");
			ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo1 = ja.getJSONObject(i);
				AppInfo info = new AppInfo();
				info.id = jo1.getString("id");
				info.name = jo1.getString("name");
				info.packageName = jo1.getString("packageName");
				info.iconUrl = jo1.getString("iconUrl");
				info.downloadUrl = jo1.getString("downloadUrl");
				info.des = jo1.getString("des");
				info.stars = jo1.getDouble("stars");
				info.size = jo1.getLong("size");
				appInfoList.add(info);
			}
			
			// 解析轮播图数据
			JSONArray ja1 = jo.getJSONArray("picture");
			ArrayList<String> pictures = new ArrayList<String>();
			for (int i = 0; i < ja1.length(); i++) {
				String pic = ja1.getString(i);
				pictures.add(pic);
			}
			
			return appInfoList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
