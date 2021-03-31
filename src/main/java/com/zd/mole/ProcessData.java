package com.zd.mole;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.process.ProcessManager;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.Task;
import com.zd.mole.task.TaskManager;
import com.zd.mole.task.TaskService;
import com.zd.mole.task.TaskStatus;

/**
 * 处理数据
 * @author ZhangDi
 *
 */
public class ProcessData implements Runnable {
	
	private Task task;
	
	public ProcessData(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		String data = "";
		String path = task.getFolderURI() + task.getFileURL();
		try ( InputStream in = new FileInputStream( path ) ){
			
			InputStreamReader inputStreamReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			//拼接数据
			data = bufferedReader.lines().reduce("", String::concat);

			task.setStatus(TaskStatus.Parseing);
			TaskManager.newInstance().update(task);
			
			ProcessHandler ph = ProcessManager.newInstance().get(task.getProcessHandlerClassName());
			List<Task> newTasks = Optional.ofNullable( ph.handler(task, data) ).orElse(new ArrayList<>(0));
			newTasks.forEach(newTask -> {
				newTask.setHostURL( newTask.getHostURL() != null ? newTask.getHostURL() : task.getHostURL() );
				newTask.setFolderURI( newTask.getFolderURI() != null ? newTask.getFolderURI() : task.getFolderURI() );
				newTask.setMethod( newTask.getMethod() != null ? newTask.getMethod(): RequestMethod.GET );
//				String uuid = UUID.randomUUID().toString();
//				newTask.setFileName( newTask.getFileName() != null ? newTask.getFileName() : uuid );
//				newTask.setFileURL( newTask.getFileURL() != null ? newTask.getFileURL() : newTask.getRequestURL().replaceAll("/", "\\\\") + "\\" + newTask.getFileName() );
				TaskManager.newInstance().newTask(newTask);
			});
			
			task.setStatus(TaskStatus.SaveSucceed);
			TaskService.newInstance().update(task);
			
//			DataParser parser = new HtmlDataParser();
//			parser.parser(task, in);
			
//			task.getMatchRules().stream().forEach(rule -> {
//				//如果是url则创建新任务
//				if (rule.isUrl()) {
//					rule.getValues().stream().forEach(value -> {
//						Task t = new Task();
//						t.setStatus(TaskStatus.ReadyToDownload);
////						t.setHostURL(task.getHostURL());
//						t.setMethod(rule.getMethod() == null ? RequestMethod.GET : rule.getMethod());
//						
//						Pattern p = Pattern.compile("((https?://)?([\\w\\d]+\\.)+[\\w\\d]+)?(/?.+)");
//						Matcher m = p.matcher(value);
//						if (m.find()) {
//							t.setHostURL(m.group(1) == null ? task.getHostURL() : m.group(2));
//							t.setRequestURL(m.group(4));
//							t.setFolderURI(task.getFolderURI());
//							t.setFileURL(m.group(4));
////							t.setFileName(rule.getCode());
////							List<MatchRule> matchRules = MatchRuleManager.newInstance().get(t.getFileURL());
////							t.setMatchRules(matchRules);
//							Pattern p2 = Pattern.compile(rule.getCodeRegex());
//							Matcher m2 = p2.matcher(value);
//							if (m2.find()) {
//								t.setCode(m2.group(1));
//								t.setFileName(m2.group(1));
//								//创建新下载任务
//								manager.save(t);
//								//TODO 保存数据到三方数据库
//							}
//						}
//						
//					});
//				}
//			});
//			System.out.println("处理完成");
		} catch (FileNotFoundException e) {
			task.setStatus(TaskStatus.ParseFileNotFound);
			System.err.println("warn: " + e.getMessage());
			TaskManager.newInstance().update(task);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main(String[] args) throws IOException {
		String url = "www.baidu.com/aadf/dsfwe";
//		String url = "http://www.baidu.com/aadf/dsfwe";
//		String url = "https://www.baidu.com/aadf/dsfwe";
//		String url = "/dataservice/query/comp/compDetail/001607220057288243";
//		String url = "dataservice/query/comp/compDetail/001607220057288243";
		Pattern p = Pattern.compile("((https?://)?([\\w\\d]+\\.)+[\\w\\d]+)?(/?.+)");
		Matcher m = p.matcher(url);
		if (m.find()) {
			System.out.println(m.groupCount());
			System.out.println("all:  " + m.group(0));
			System.out.println("host: " + m.group(1));
			System.out.println("协议: " + m.group(2));
			System.out.println("url:  " + m.group(4));
		}
		
	}

}
