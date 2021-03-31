package com.zd.mole.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpXMLRequest {
	
	private final static int CONNECT_TIMEOUT = 30000;
	private final static int READ_TIMEOUT = 30000;

	public static void setRequestProperty(URLConnection connection) {
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//      connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
		connection.setRequestProperty("Cache-Control","max-age=0");
		connection.setRequestProperty("Connection","keep-alive");
//      connection.setRequestProperty("Cookie","JSESSIONID=2781761395CD78BF139B49468C32CA7C; Hm_lvt_b1b4b9ea61b6f1627192160766a9c55c=1508248943; Hm_lpvt_b1b4b9ea61b6f1627192160766a9c55c=1508248954");
		connection.setRequestProperty("Host","jzsc.mohurd.gov.cn");
		connection.setRequestProperty("Referer","http://jzsc.mohurd.gov.cn/asite/jsbpp/jsp/news.jsp");
		connection.setRequestProperty("Upgrade-Insecure-Requests","1");
		connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
	}
	
	/**
	 * http get 方式请求
	 * @param httpUrl
	 * @param proxy
	 * @param out
	 * @throws IOException 
	 * @throws IOException
	 */
	public static void sendGet(String httpUrl, Proxy proxy, OutputStream out) throws IOException  {
		
		InputStream in = null;
		try {
			URL url = new URL(httpUrl);
			URLConnection connection = proxy == null ? url.openConnection() : url.openConnection(proxy);
			setRequestProperty(connection);
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
	        // 建立实际的连接
	        connection.connect();
        
	        // 获取所有响应头字段
//	        Map<String, List<String>> map = connection.getHeaderFields();
	        // 遍历所有的响应头字段
//	        for (String key : map.keySet()) {
//	            System.out.println(key + "--->" + map.get(key));
//	        }
	        in = connection.getInputStream();
			byte[] buff = new byte[10240];
			int i = 0;
			while( (i = in.read(buff, 0, buff.length)) != -1) {
				out.write(buff, 0, i);
			};
			out.flush();
        } finally {
			if(in != null)
				in.close();
		}
	}
	
	/**
	 * http post 方式请求
	 * @param httpUrl
	 * @param proxy
	 * @param params
	 * @param out
	 * @throws IOException
	 */
	public static void sendPost(String httpUrl, Proxy proxy, String params, OutputStream out) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(httpUrl);
			connection = proxy == null ? (HttpURLConnection) url.openConnection() : (HttpURLConnection) url.openConnection(proxy);
			setRequestProperty(connection);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
//			connection.setDoInput(true); 默认是true 可不显示调用
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			
			
			OutputStreamWriter requestOut = new OutputStreamWriter(connection.getOutputStream());
			requestOut.write(params);
			requestOut.flush();
			requestOut.close();
			
			// 获取所有响应头字段
//	        Map<String, List<String>> map = connection.getHeaderFields();
	        // 遍历所有的响应头字段
//	        map.forEach((k, v) -> {
//	        	System.out.println(k + "--->" + v);
//	        });
			InputStream in = connection.getInputStream();
			byte[] buff = new byte[10240];
			int i = 0;
			while( (i = in.read(buff, 0, buff.length)) != -1) {
				out.write(buff, 0, i);
			};
			out.flush();
		} finally {
			if(connection != null)
				connection.disconnect();
		}
	}
	
	public static void main(String[] args) throws IOException {
		HttpXMLRequest request = new HttpXMLRequest();
		ProxyManager pm = ProxyManager.newInstance();
		Map<String, Object> params = new HashMap<>();
		params.put("$reload", 0);
		params.put("$total", 268689);
		params.put("$pg", 4);
		params.put("$pgsz", 15);
		FileOutputStream out = new FileOutputStream("d://b.html");
		request.sendGet("http://jzsc.mohurd.gov.cn/dataservice/query/comp/list", pm.get(), out);
//		request.sendPost("http://jzsc.mohurd.gov.cn/dataservice/query/comp/list", pm.get(), params, out);
		out.close();
	}
}
