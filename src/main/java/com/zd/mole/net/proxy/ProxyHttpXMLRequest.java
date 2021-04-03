package com.zd.mole.net.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyHttpXMLRequest {

	private static Logger log = LoggerFactory.getLogger(ProxyHttpXMLRequest.class);
	
	private final static int CONNECT_TIMEOUT = 60000;
	private final static int READ_TIMEOUT = 60000;

	private static LongAdder b = new LongAdder();
	private static long bs = 0;
	
	static {
		//计算每秒网速多少
		Thread t = new Thread(() -> {
			long interval = 5000;
			while(true) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bs = b.longValue() / (interval / 1000);
				b.reset();
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	public static void sendGet(String httpUrl, Map<String,String> headers, OutputStream out) throws IOException  {
		send(httpUrl, headers, "GET", null, out);
	}
	
	public static void sendPost(String httpUrl, Map<String,String> headers, String params, OutputStream out) throws IOException  {
		send(httpUrl, headers, "POST", params, out);
	}
	
	public static void send(String httpUrl, Map<String,String> headers, String method, String params, OutputStream out) throws IOException  {
		ProxyManager pm = ProxyManager.getInstance();
		BetterProxy proxy = pm.get();

		method = Optional.ofNullable(method).orElse("GET");
		log.info("请求连接(proxy->" + proxy + ")(method->" + method + ")：" + httpUrl);
		HttpURLConnection connection = null;
		long beginTime = System.currentTimeMillis();
		try {
			URL url = new URL(httpUrl);
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			headers = Optional.ofNullable(headers).orElse(new HashMap<>());
			for (Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			
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
			
			InputStream in = null;
			if("gzip".equals(connection.getContentEncoding())) {
	        	in = new GZIPInputStream(connection.getInputStream()); 
	        } else {
	        	in = connection.getInputStream();
	        }
			byte[] buff = new byte[1024];
			int i = 0;
			while( (i = in.read(buff, 0, buff.length)) != -1) {
				out.write(buff, 0, i);
				b.add(i);
			};
			out.flush();
			long spendTime = System.currentTimeMillis() - beginTime;
			proxy.setResponsedTime(spendTime);
			log.info("连接成功(spend time->" + spendTime + "ms)(proxy->" + proxy + "(method->" + method + ")：" + httpUrl);
		} catch (IOException e) {
			long spendTime = System.currentTimeMillis() - beginTime;
			proxy.setResponsedTime(CONNECT_TIMEOUT + 1);
			log.warn("连接失败(spend time->" + spendTime + "ms)(proxy->" + proxy + "(method->" + method + ")：" + httpUrl);
			log.warn(e.getMessage());
			throw e;
		} finally {
			if(connection != null)
				connection.disconnect();
			proxy.setLastRequestTime(System.currentTimeMillis());
			pm.putBack(proxy);
		}
	}

	/** 每秒byte数 */
	public static long getBs() {
		return bs;
	}
}
