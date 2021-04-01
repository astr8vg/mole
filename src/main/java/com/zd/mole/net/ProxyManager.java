package com.zd.mole.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.mole.net.proxy.BetterProxy;
import com.zd.mole.net.proxy.ProxyListRequest;
import com.zd.mole.net.proxy.impl.ArsenalProxyListRequest;

public class ProxyManager {
	
	private static ProxyManager pm = new ProxyManager(); 
	public static ProxyManager getInstance() {
		return pm;
	}
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	
	private Queue<BetterProxy> allProxyCache = new ConcurrentLinkedQueue<>(); 
	
	private Queue<BetterProxy> fastProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> normalProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> slowProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> timeOutProxyCache = new ConcurrentLinkedQueue<>();
	private Queue<BetterProxy> sleepProxyCache = new ConcurrentLinkedQueue<>();
	
	private long fastResponseTime 	= 10000;
	private long normalResponseTime = 30000;
	private long slowResponseTime 	= 60000;
	
	/** 使用间隔 */
	private long intervalTime = 2000;
	
	//未来可以增加代理使用记录，记录响应时间和失败次数
	private int picketLine = 10;
	private int getNum = 100;
	
	private ProxyListRequest proxyListRequest = new ArsenalProxyListRequest();
	
	private ProxyManager() {
		Runnable cacheUpdater = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					synchronized (allProxyCache) {
						while((fastProxyCache.size() + normalProxyCache.size() + slowProxyCache.size()) > picketLine) {
							try {
							allProxyCache.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						wakeUpProxy();
						if ((fastProxyCache.size() + normalProxyCache.size() + slowProxyCache.size()) < getNum) {
							makeProxys();
						}
						allProxyCache.notifyAll();
						clean();
					}
				}
			}
		};
		Thread t = new Thread(cacheUpdater);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * 唤醒代理
	 */
	private void wakeUpProxy() {
		int count = sleepProxyCache.size();
		sleepProxyCache.removeIf(proxy -> {
			if (System.currentTimeMillis() - proxy.getLastRequestTime() > intervalTime) { 
				if (proxy.getResponsedTime() < fastResponseTime){
					fastProxyCache.offer(proxy);
				} else if (proxy.getResponsedTime() < normalResponseTime) {
					normalProxyCache.offer(proxy);
				} else if (proxy.getResponsedTime() < slowResponseTime) {
					slowProxyCache.offer(proxy);
				} else {
					timeOutProxyCache.offer(proxy);
				}
				if(!allProxyCache.contains(proxy)) {
					allProxyCache.offer(proxy);
				}
				return true;
			}
			return false;
		});
		count -= sleepProxyCache.size();
		log.info("唤醒代理：" + count + " 个");
	}
	
	/**
	 * 清除无效代理
	 */
	private void clean() {
		int count = timeOutProxyCache.size();
		timeOutProxyCache.removeIf(proxy -> {
			boolean b = System.currentTimeMillis() - proxy.getLastRequestTime() > 1000 * 60 * 15;
			if(b) {
				allProxyCache.remove(proxy);
			}
			return b;
		});
		while(timeOutProxyCache.size() > 2000) {
			BetterProxy proxy = timeOutProxyCache.remove();
			allProxyCache.remove(proxy);
		}
		log.info("清除超时代理：" + (count - timeOutProxyCache.size()));
	}
	/**
	 * 下载代理
	 */
	private void makeProxys() {
		int count = 0;
		try {
			List<InetSocketAddress> addrs = proxyListRequest.get();
			for (InetSocketAddress addr : addrs) {
				BetterProxy proxy = new BetterProxy(Proxy.Type.HTTP, addr);
				if(!allProxyCache.contains(proxy)) {
					allProxyCache.offer(proxy);
					normalProxyCache.offer(proxy);
					count++;
				}
			}
			log.info("成功请求新代理：" + count);
		} catch (IOException e) {
			log.error("请求代理列表失败" + e.getMessage());
		}
	}
	
	/**
	 * 获取代理
	 * @return
	 */
	public BetterProxy get() {
		BetterProxy proxy = null;
		synchronized (allProxyCache) {
			log.info("fastProxyCache: " + fastProxyCache.size() 
				+ " normalProxyCache: " + normalProxyCache.size() 
				+ " slowProxyCache: " + slowProxyCache.size()
				+ " sleepProxyCache: " + sleepProxyCache.size()
				+ " timeOutProxyCache: " + timeOutProxyCache.size()
				+ " allProxyCache: " + allProxyCache.size());
			while((proxy = fastProxyCache.poll()) == null 
					&& (proxy = normalProxyCache.poll()) == null
					&& (proxy = slowProxyCache.poll()) == null) {
				try {
					allProxyCache.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			allProxyCache.notifyAll();
		}
		return proxy;
	}
	
	public void putBack(BetterProxy proxy) {
		sleepProxyCache.offer(proxy);
	}
}
