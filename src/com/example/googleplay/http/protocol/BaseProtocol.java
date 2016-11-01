package com.example.googleplay.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.http.HttpHelper.HttpResult;
import com.example.googleplay.utils.IOUtils;
import com.example.googleplay.utils.LogUtils;
import com.example.googleplay.utils.StringUtils;
import com.example.googleplay.utils.UIUtils;

/**
 * 访问网络的基类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseProtocol<T> {

	/**
	 * 获取网络数据
	 * 
	 * @param index
	 *            表示的是从哪个位置开始返回20条数据，用于分页
	 */
	public T getData(int index) {
		// 先判断是否有缓存，如果有的话现价在缓存
		String result = getCache(index);
		if (StringUtils.isEmpty(result)) { // 如果没有缓存，或者缓存失效，
			// 请求服务器
			result = getDataFromServer(index); 
		}
		
		// 开始解析数据
		if (result != null) {
			T data = parseData(result);
			return data;
		}
		return null;
	}

	/**
	 * 从网络获取数据
	 * 
	 * @param index
	 *            表示的是从哪个位置开始返回20条数据，用于分页
	 */
	private String getDataFromServer(int index) {
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
				+ "?index=" + index + getParams());
		if (httpResult != null) {
			String result = httpResult.getString();
			LogUtils.i("访问结果：" + result);
			// 写缓存
			if (!StringUtils.isEmpty(result)) {
				setCache(index, result);
			}
			return result;
		}
		return null;
	}

	/**
	 * 获取网络链接关键词，子类必须实现
	 * 
	 * @return
	 */
	public abstract String getKey();

	/**
	 * 获取网络链接参数，子类必须实现
	 * 
	 * @return
	 */
	public abstract String getParams();

	/**
	 * 写缓存
	 * 以url为key，以json为value
	 * 
	 * @param index
	 * @param json
	 */
	public void setCache(int index, String json) {
		// 以url为文件名，json为文件内容，保存在本地
		File cacheDir = UIUtils.getContext().getCacheDir(); // 本应用的缓存文件夹
		// 生成缓存文件
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParams());
		FileWriter writer = null;
		try {
			writer = new FileWriter(cacheFile);
			// 缓存失效的截止时间
			long deadline = System.currentTimeMillis() + 30 * 60 * 1000;
			writer.write(deadline + "\n");
			writer.write(json); // 写入json
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(writer);
		}
	}

	/**
	 * 读缓存
	 * @param index
	 * @return
	 */
	public String getCache(int index) {
		// 以url为文件名，json为文件内容，保存在本地
		File cacheDir = UIUtils.getContext().getCacheDir(); // 本应用的缓存文件夹
		// 生成缓存文件
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParams());
		// 判断缓存是否存在
		if (cacheFile.exists()) {
			// 判断缓存是否有效
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(cacheFile));
				String deadline = reader.readLine();
				long deadtime = Long.parseLong(deadline);
				if (System.currentTimeMillis() < deadtime) {
					// 缓存有效
					String line = null;
					StringBuffer sb = new StringBuffer();
					while((line = reader.readLine()) != null){
						sb.append(line);
					}
					return sb.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.close(reader);
			}
		}
		return null;
	}
	
	public abstract T parseData(String result);

}
