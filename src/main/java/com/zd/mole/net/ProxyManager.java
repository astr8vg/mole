package com.zd.mole.net;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProxyManager {
	
	private static ProxyManager proxyManager = new ProxyManager();

	public static ProxyManager newInstance() {
		return proxyManager;
	}
	
	private Queue<Proxy> cache = new ConcurrentLinkedQueue<>(); 
	
	//未来可以增加代理使用记录，记录响应时间和失败次数
	/** 一个代理ip被重复创建的次数 */
	private int repeatTimes = 5;
	
	private ProxyManager() {
		Runnable cacheUpdater = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(cache.size() == 0) {
						downProxys();
						synchronized (cache) {
							cache.notifyAll();
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(cacheUpdater);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * 下载代理
	 */
	private void downProxys() {
		for (int i = 0; i < repeatTimes; i++) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("111.62.243.64", 8080));
//		InetSocketAddress addr = new InetSocketAddress("182.141.46.215", 9000);
//		InetSocketAddress addr = new InetSocketAddress("121.232.144.13", 9000);
//		InetSocketAddress addr = new InetSocketAddress("60.188.237.240", 9000);
//		InetSocketAddress addr = new InetSocketAddress("117.90.137.194", 9000);
//		InetSocketAddress addr = new InetSocketAddress("106.56.185.11", 80);
			cache.offer(proxy);
		}
	}
	
	/**
	 * 获取代理
	 * @return
	 */
	public Proxy get() {
		Proxy proxy = null;
//		synchronized (cache) {
//			while((proxy = cache.poll()) == null) {
//				try {
//					cache.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		return proxy;
	}
}
