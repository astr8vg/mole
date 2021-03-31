package com.zd.mole.net.proxy;

import java.net.Proxy;
import java.net.SocketAddress;

public class BetterProxy extends Proxy implements Comparable<BetterProxy> {
	
	/** 最后请求时间 */
	private long lastRequestTime;
		
	/** 响应时间 */
	private long responsedTime;
	
	public BetterProxy(Type type, SocketAddress sa) {
		super(type, sa);
	}
	
	@Override
	public String toString() {
		return super.toString() + " responseTime: " + responsedTime;
	}

	@Override
	public int compareTo(BetterProxy o) {
		if (this.responsedTime > o.getResponsedTime()) {
			return 1;
		} else if (this.responsedTime == o.getResponsedTime()) {
			return 0;
		}
		return -1;
	}

	public long getResponsedTime() {
		return responsedTime;
	}

	public void setResponsedTime(long responsedTime) {
		this.responsedTime = responsedTime;
	}


	public long getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(long lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

}
