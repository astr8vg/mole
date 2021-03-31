package com.zd.mole.net.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public interface ProxyListRequest {

	List<InetSocketAddress> get() throws IOException;
}
