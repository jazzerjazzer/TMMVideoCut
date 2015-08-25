package controller;

import java.util.ArrayList;

public class LogController {
	
	ArrayList<String> logs = new ArrayList<>();
	
	public void addLog(String logMessage){
		logs.add(logMessage);
	}
}
