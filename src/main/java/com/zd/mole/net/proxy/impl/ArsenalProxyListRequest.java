package com.zd.mole.net.proxy.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.mole.net.HttpXMLRequest;
import com.zd.mole.net.proxy.ProxyListRequest;

public class ArsenalProxyListRequest implements ProxyListRequest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private String url = "http://39.106.76.185:52410/proxy";
	
	@Override
	public List<InetSocketAddress> get() throws IOException {
		
//		System.getProperties()
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			headers.put("Accept-Language", "zh-CN,zh;q=0.8");
			headers.put("Accept-Encoding", "gzip");
			headers.put("Cache-Control", "max-age=0");
			headers.put("Connection", "keep-alive");
//			headers.put("Host", "dev.kuaidaili.com");
//			headers.put("Upgrade-Insecure-Requests", "1");
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			HttpXMLRequest.sendGet(url, headers, null, out);
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			InputStreamReader isreader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isreader);
			String line = null;
			List<InetSocketAddress> addrs = new ArrayList<>(999);
			while((line = br.readLine()) != null) {
				if(line.startsWith("ERROR")) {
					log.error("获的代理地址失败：" + line);
					break;
				}
				log.debug("获的代理地址：" + line);
				String[] split = line.split(":");
				if(split.length == 2) {
					String host = split[0];
					int port = Integer.parseInt(split[1]);
					addrs.add( InetSocketAddress.createUnresolved(host, port) );
				}
			}
			log.info("获得代理数量：" + addrs.size());
			return addrs;
		} finally {
		}
	}
}
