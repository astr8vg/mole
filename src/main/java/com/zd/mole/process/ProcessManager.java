package com.zd.mole.process;

import java.util.HashMap;
import java.util.Map;

public class ProcessManager {
	private static ProcessManager processManager = new ProcessManager();

	private Map<String, ProcessHandler> handlers = new HashMap<>();
	
	private ProcessManager(){}
	
	public static ProcessManager newInstance() {
		return processManager;
	}
	
	public void add(String className, ProcessHandler handler) {
		handlers.put(className, handler);
	}
	
	public ProcessHandler get(String className) {
		return handlers.get(className);
	}
}
