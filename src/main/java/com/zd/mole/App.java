package com.zd.mole;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.mole.process.ProcessManager;
import com.zd.mole.site.mohurd.MohurdTaskHandler;
import com.zd.mole.site.mohurd.process.MohurdDataserviceQueryCompCaDetailListHandler;
import com.zd.mole.site.mohurd.process.MohurdDataserviceQueryCompCompDetailHandler;
import com.zd.mole.site.mohurd.process.MohurdDataserviceQueryCompListHandler;
import com.zd.mole.task.Task;
import com.zd.mole.task.TaskHandler;
import com.zd.mole.task.TaskManager;
import com.zd.mole.task.TaskService;
import com.zd.mole.task.TaskStatus;

/**
 * Hello world!
 *
 */
public class App {
    	
	private static Logger log = LoggerFactory.getLogger(App.class); 
	
	private final static int THEAD_COUNT = 1;
	
	public static void main(String[] args) {
		
		//创建新任务
		TaskHandler taskHandler = new MohurdTaskHandler();
		List<Task> tasks = taskHandler.init();
		tasks.forEach(task -> {
			task.setStatus(TaskStatus.Ready);
			TaskService.newInstance().save(task);
		});
		log.info("任务创建完成");
		
		ProcessManager pm = ProcessManager.newInstance();
		pm.add(MohurdDataserviceQueryCompListHandler.class.getName(), new MohurdDataserviceQueryCompListHandler());
		pm.add(MohurdDataserviceQueryCompCompDetailHandler.class.getName(), new MohurdDataserviceQueryCompCompDetailHandler());
		pm.add(MohurdDataserviceQueryCompCaDetailListHandler.class.getName(), new MohurdDataserviceQueryCompCaDetailListHandler());
		
		
		
		TaskManager tm = TaskManager.newInstance();
		log.info("初始化“任务管理器”");
		
		//创建线程池
		ThreadPoolExecutor dataParserPool = (ThreadPoolExecutor)Executors.newFixedThreadPool(THEAD_COUNT);
		//是否所有任务已经完成，或插入三方库失败
		while(!tm.isAllFinsh() || dataParserPool.getActiveCount() != 0) {
			//如果活动线程数量低于设定值，提交线程总数十分之一的任务进入线程池等待
			if( THEAD_COUNT >= dataParserPool.getActiveCount() ) {
				//获取下载任务
				Task readyToDownloadTask = tm.getReadyToDownload();
				if (readyToDownloadTask != null)
    					dataParserPool.execute(new DownloadData(readyToDownloadTask));
				//获取准备解析任务
				if( THEAD_COUNT >= dataParserPool.getActiveCount() ) {
					Task readyToParseTask = tm.getReadyToParse();
					if (readyToParseTask != null)
						dataParserPool.execute(new ProcessData(readyToParseTask));
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		dataParserPool.shutdown();
		tm.close();
		log.info("全部任务处理完成\n程序关闭");
    }
}
