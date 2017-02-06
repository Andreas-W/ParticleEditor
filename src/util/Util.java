package util;

import java.io.File;
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
	
	public static String fmt(float f)
	{
	    if(f == (long) f)
	        return String.format("%d",(long)f);
	    else
	        return String.format("%s",f);
	}
	
	/**
     * Cut of file extension
     */
    public static String getTrimmedFilename(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i <= 0) {
        	return s;
        }else {
            return s.substring(0, i);
        }
    }

	public static boolean hasValues(float[] arr) {
		for (float f : arr) {
			if (f != 0) return true;
		}
		return false;
	}
	
	public static boolean hasValues(int[] arr) {
		for (int i : arr) {
			if (i != 0) return true;
		}
		return false;
	}

}
