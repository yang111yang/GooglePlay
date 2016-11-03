package com.example.googleplay.domain;

import java.util.ArrayList;

/**
 * 应用对象
 * @author Administrator
 *
 */
public class AppInfo {
	
	public String id;
	public String name;
	public String packageName;
	public String iconUrl;
	public String downloadUrl;
	public String des;
	public long size;
	public double stars;
	
	// 补充字段，以供首页详情页使用
	public String author;
	public String date;
	public String downloadNum;
	public String version;
	
	public ArrayList<SafeInfo> safe;
	public ArrayList<String> screen;
	
	public static class SafeInfo {
		public String safeDes;
		public String safeDesUrl;
		public String safeUrl;
	}
	
}
