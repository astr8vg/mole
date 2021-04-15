package com.zd.mole.net.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.mole.net.proxy.impl.ArsenalProxyListRequest;

public class ProxyManager {
	
	private static ProxyManager pm = new ProxyManager(); 
	public static ProxyManager getInstance() {
		return pm;
	}
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	
	private Queue<BetterProxy> allProxyCache = new ConcurrentLinkedQueue<>(); 
	
	private Queue<BetterProxy> newProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> fastProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> normalProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> slowProxyCache = new ConcurrentLinkedQueue<>(); 
	private Queue<BetterProxy> timeOutProxyCache = new ConcurrentLinkedQueue<>();
	private Queue<BetterProxy> sleepProxyCache = new ConcurrentLinkedQueue<>();
	
	private long fastResponseTime 	= 10000;
	private long normalResponseTime = 30000;
	private long slowResponseTime 	= 60000;
	
	/** 使用间隔 */
	private long intervalTime = 5000;
	
	//未来可以增加代理使用记录，记录响应时间和失败次数
	private int fastProxyCount = 100;
	//需要获取代理的下限值
	private int picketLine = fastProxyCount / 2;
	
	private ProxyListRequest proxyListRequest = new ArsenalProxyListRequest();
	
	private ProxyManager() {
		
		Thread t = new Thread( () -> {
			while(true) {
				synchronized (allProxyCache) {
					while(fastProxyCache.size() > picketLine || newProxyCache.size() > 0) {
						try {
							allProxyCache.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				downProxys();
				clean();
				//休眠1秒，避免请求速度过快
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} ); 
		t.setDaemon(true);
		t.start();
		
		t = new Thread( () -> { 
			while(true) {
				//唤醒代理
				wakeUpProxy();
				try {
					Thread.sleep(intervalTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(true);
		t.start();

	}
	
	/**
	 * 下载代理
	 */
	private void downProxys() {
		int count = 0;
		try {
			List<InetSocketAddress> addrs = proxyListRequest.get();
			synchronized (allProxyCache) {
				for (InetSocketAddress addr : addrs) {
					BetterProxy proxy = new BetterProxy(Proxy.Type.HTTP, addr);
					if(!allProxyCache.contains(proxy)) {
						allProxyCache.offer(proxy);
						newProxyCache.offer(proxy);
						count++;
					}
				}
				allProxyCache.notifyAll();
			}
			log.info("成功请求新代理：" + count);
		} catch (IOException e) {
			log.error("请求代理列表失败" + e.getMessage());
		}
	}
	
	/**
	 * 唤醒代理
	 */
	private void wakeUpProxy() {
		int wakeupCount = 0;
		int cacheCount = sleepProxyCache.size();
		for (int i = 0; i < cacheCount; i++) {
			BetterProxy proxy = sleepProxyCache.poll();
			if (proxy == null) {
				break;
			} else if(System.currentTimeMillis() - proxy.getLastRequestTime() > intervalTime) {
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
				wakeupCount++;
			} else {
				sleepProxyCache.offer(proxy);
			}
		}
		if(wakeupCount > 0) {
			log.info("唤醒代理：" + wakeupCount + " 个");
		} else {
			log.debug("唤醒代理：" + wakeupCount + " 个");
		}
//		if(wakeupCount == 0) {
//			synchronized (allProxyCache) {
//				try {
//					allProxyCache.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
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
			BetterProxy proxy = timeOutProxyCache.poll();
			allProxyCache.remove(proxy);
		}
		log.info("清除超时代理：" + (count - timeOutProxyCache.size()));
	}
	
	
	/**
	 * 获取代理
	 * @return
	 */
	public BetterProxy get() {
		BetterProxy proxy = null;
		synchronized (allProxyCache) {
			log.info("fast: " + fastProxyCache.size() 
				+ " new: " + newProxyCache.size() 
				+ " normal: " + normalProxyCache.size() 
				+ " slow: " + slowProxyCache.size()
				+ " sleep: " + sleepProxyCache.size()
				+ " timeOut: " + timeOutProxyCache.size()
				+ " all: " + allProxyCache.size());
			while((proxy = fastProxyCache.poll()) == null 
					&& (proxy = newProxyCache.poll()) == null
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

	public void setFastProxyCount(int fastProxyCount) {
		this.fastProxyCount = fastProxyCount;
	}
}
