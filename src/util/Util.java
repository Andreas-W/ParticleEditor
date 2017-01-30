package util;

import java.util.HashMap;

public class Util {
	public static StringBuilder timerLog = new StringBuilder();
	public static HashMap<String, Long> times = new HashMap<String, Long>();
	public static void startTimer(String name)  {
		timerLog.append("Start("+name+") ... \n");
		times.put(name, System.nanoTime());
	}
	
	public static void stopTimer(String name) {
		long time = System.nanoTime()-times.get(name);
		timerLog.append("Stop("+name+"): "+(time)+"ns\n");
	}

	public static void clearTimer() {
		timerLog = new StringBuilder();
	}
}
