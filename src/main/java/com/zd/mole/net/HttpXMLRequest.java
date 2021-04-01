package com.zd.mole.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpXMLRequest {

	private static Logger log = LoggerFactory.getLogger(HttpXMLRequest.class);
	
	private final static int CONNECT_TIMEOUT = 15000;
	private final static int READ_TIMEOUT = 30000;
	
	public static void sendGet(String httpUrl, Map<String,String> headers, Proxy proxy, OutputStream out) throws IOException  {
		send(httpUrl, headers, proxy, "GET", null, out);
	}
	
	public static void sendPost(String httpUrl, Map<String,String> headers, Proxy proxy, String params, OutputStream out) throws IOException  {
		send(httpUrl, headers, proxy, "POST", params, out);
	}
	
	public static void send(String httpUrl, Map<String,String> headers, Proxy proxy, String method, String params, OutputStream out) throws IOException  {
		long beginTime = System.currentTimeMillis();
		method = Optional.ofNullable(method).orElse("GET");
		log.info("请求连接(proxy->" + proxy + ")(method->" + method + ")：" + httpUrl);
		HttpURLConnection connection = null;
		try {
			URL url = new URL(httpUrl);
			connection = proxy == null ? (HttpURLConnection) url.openConnection() : (HttpURLConnection) url.openConnection(proxy);
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			headers = Optional.ofNullable(headers).orElse(new HashMap<>());
			for (Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			
//			setRequestProperty(connection);
			if("POST".equals(method)) {
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
//				connection.setDoInput(true); 默认是true 可不显示调用
				OutputStreamWriter requestOut = new OutputStreamWriter(connection.getOutputStream());
				requestOut.write(params);
				requestOut.flush();
				requestOut.close();
			} else {
				// 建立实际的连接
		        connection.connect();
			}
			
			// 获取所有响应头字段
//	        Map<String, List<String>> map = connection.getHeaderFields();
	        // 遍历所有的响应头字段
//	        map.forEach((k, v) -> {
//	        	System.out.println(k + "--->" + v);
//	        });
			InputStream in = null;
			if("gzip".equals(connection.getContentEncoding())) {
	        	in = new GZIPInputStream(connection.getInputStream()); 
	        } else {
	        	in = connection.getInputStream();
	        }
			byte[] buff = new byte[10240];
			int i = 0;
			while( (i = in.read(buff, 0, buff.length)) != -1) {
				out.write(buff, 0, i);
			};
			out.flush();
			long spendTime = System.currentTimeMillis() - beginTime;
			log.info("连接成功(spend time->" + spendTime + "ms)(proxy->" + proxy + "(method->" + method + ")：" + httpUrl);
		} catch (Exception e) {
			long spendTime = System.currentTimeMillis() - beginTime;
			log.warn("连接失败(spend time->" + spendTime + "ms)(proxy->" + proxy + "(method->" + method + ")：" + httpUrl);
			log.warn(e.getMessage());
			throw e;
		} finally {
			if(connection != null)
				connection.disconnect();
		}
	}
}
