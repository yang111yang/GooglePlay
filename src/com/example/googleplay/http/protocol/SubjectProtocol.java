package com.example.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.googleplay.domain.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<SubjectInfo> parseData(String result) {
		try {
			JSONArray ja = new JSONArray(result);
			ArrayList<SubjectInfo> subjectInfoList = new ArrayList<SubjectInfo>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.des = jo.getString("des");
				subjectInfo.url = jo.getString("url");
				subjectInfoList.add(subjectInfo);
			}
			return subjectInfoList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
