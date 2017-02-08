package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import main.Renderer;

public class Util {
	
	public static final String LOGFILE = "log.txt";
	
	static {
		try {
			FileWriter fw;
			fw = new FileWriter(new File(LOGFILE));
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			fw.write(dateFormat.format(date)+"\n");	
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
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
	
	public static JWindow showLoadingWindow(Frame owner) {
		JWindow window = new JWindow(owner);
		int w = 300;
		int h = 75;
		//window.setBounds(owner.getWidth()/2 - w/2, owner.getHeight()/2-h/2, w, h);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		window.setBounds((int)screensize.getWidth()/2 - w/2, (int)screensize.getHeight()/2-h/2, w, h);
		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(5));
		window.setContentPane(panel);
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel("Loading Files...");
		panel.add(label, BorderLayout.CENTER);
		window.setVisible(true);
		window.toFront();
		//window.setAlwaysOnTop(true);
		return window;
	}

	public static void printToLog(String text) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(LOGFILE, true));
			pw.println(text);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
