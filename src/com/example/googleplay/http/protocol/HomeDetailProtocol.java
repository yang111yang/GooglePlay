package com.example.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.domain.AppInfo.SafeInfo;
import com.example.googleplay.utils.LogUtils;

/**
 * 首页应用详情页网络访问
 * @author Administrator
 *
 */
public class HomeDetailProtocol extends BaseProtocol<AppInfo> {

	private String packageName;

	public HomeDetailProtocol(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	public String getParams() {
		return "&packageName=" + packageName;
	}

	@Override
	public AppInfo parseData(String result) {
		LogUtils.i("result" + result);
		try {
			JSONObject jo = new JSONObject(result);
			
			AppInfo info = new AppInfo();
			info.id = jo.getString("id");
			info.name = jo.getString("name");
			info.packageName = jo.getString("packageName");
			info.iconUrl = jo.getString("iconUrl");
			info.downloadUrl = jo.getString("downloadUrl");
			info.des = jo.getString("des");
			info.stars = jo.getDouble("stars");
			info.size = jo.getLong("size");
			
			info.author = jo.getString("author");
			info.date = jo.getString("date");
			info.downloadNum = jo.getString("downloadNum");
			info.version = jo.getString("version");
			
			// 解析安全相关信息
			JSONArray ja = jo.getJSONArray("safe");
			
			ArrayList<SafeInfo> safe = new ArrayList<SafeInfo>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo1 = ja.getJSONObject(i);
				SafeInfo safeInfo = new SafeInfo();
				safeInfo.safeDes = jo1.getString("safeDes");
				safeInfo.safeDesUrl = jo1.getString("safeDesUrl");
				safeInfo.safeUrl = jo1.getString("safeUrl");
				safe.add(safeInfo);
			}
			
			info.safe = safe;
			
			// 解析图片信息
			JSONArray ja1 = jo.getJSONArray("screen");
			
			ArrayList<String> screen = new ArrayList<String>();
			for (int i = 0; i < ja1.length(); i++) {
				String pic = ja1.getString(i);
				screen.add(pic);
			}
			
			info.screen = screen;
			
			return info;
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
