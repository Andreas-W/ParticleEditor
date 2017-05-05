package util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import entitytypes.FXListType;
import entitytypes.ParticleSystemType;
import gui.ColorEntryPanel;
import main.Config;
import main.Main;
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
			System.out.println(text);
			PrintWriter pw = new PrintWriter(new FileWriter(LOGFILE, true));
			pw.println(text);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveToFXListIniFile() {
//		Scanner sc = new Scanner(file);
//		sc.useLocale(Locale.ENGLISH);
//		sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
		
		File fx_ini_old = new File(Config.currentFXListFile);
		ArrayList<String> workingFX = new ArrayList<String>();
		workingFX.addAll(Main.work_FXListTypes.keySet());
		LinkedList<String> orderedList = new LinkedList<String>();
		//Pattern p = Pattern.compile("^FXList\\s");
		try {
			//Scan trough whole file to determine order of Working FX;
			Scanner sc = new Scanner(fx_ini_old);
			sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
			sc.useLocale(Locale.ENGLISH);
			boolean eof = false;
			String token = "";
			while(!eof && sc.hasNextLine()) {
				if (sc.hasNext()) token = sc.next();
				else eof = true;
				if (token.equals("FXList")) {
					if (sc.hasNext()) {
						String s = sc.next();
						if (workingFX.contains(s)) {
							System.out.println("Found FXList "+s+" in file.");
							orderedList.add(s);
							workingFX.remove(s);
						}
					}
				}
			}
			//add remaining items
			for (String s : workingFX) {
				System.out.println("Couldn't find ParticleSystem "+s+" in file. Add at bottom.");
				orderedList.add(s);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file: "+fx_ini_old);
			e.printStackTrace();
		}		
		try {
			File fx_ini_new = new File(Config.currentFXListFile+"_new");
			PrintWriter pw = new PrintWriter(new FileWriter(fx_ini_new));
			Scanner sc = new Scanner(fx_ini_old);
			Pattern p_token = Pattern.compile("^\\s*(ViewShake|Sound|ParticleSystem|TerrainScorch|LightPulse|Tracer)\\s*(;.*)*$");
			Pattern p_end = Pattern.compile("^\\s*End\\s*(;.*)*$");
			sc.useLocale(Locale.ENGLISH);
			boolean done = false;
			String name = "";
			while (sc.hasNextLine()) {
				if (orderedList.isEmpty()) done = true;
				else name = orderedList.getFirst();
				String line = sc.nextLine();
				if (!done && line.matches("^FXList\\s" + name + "\\s*(;.*)*$")) {
					boolean end = false;
					while (sc.hasNextLine() && !end) { //Skip this entry
						String s = sc.nextLine();
						if (p_end.matcher(s).matches()) {
							end = true;
						}else if (p_token.matcher(s).matches()) {
							while (sc.hasNextLine() && !end) {
								s = sc.nextLine();
								if (p_end.matcher(s).matches()) {
									end = true;
								}
							}
							end = false;
						}
					}
					pw.write(Main.getFXList(name).getFormattedCode(name)+"\n");
					orderedList.removeFirst();
				}else {
					pw.write(line+"\n");
				}
			}
			sc.close();
			//write remaining items
			for (String s : orderedList) {
				pw.write(Main.getFXList(name).getFormattedCode(s)+"\n");
			}
			pw.flush();
			pw.close();
			
			File ini_bak = new File(Config.currentFXListFile+".bak");
			Files.deleteIfExists(ini_bak.toPath());
			
			if (fx_ini_old.renameTo(ini_bak)) {
				boolean success = fx_ini_new.renameTo(new File(Config.currentFXListFile));
				if (success) {
					JOptionPane.showMessageDialog(null, "Successfuly saved '"+Config.currentFXListFile+"' (Saved backup of previous version)");
				}else {
					JOptionPane.showMessageDialog(null, "Something went wrong!");
				}
			}else {
				JOptionPane.showMessageDialog(null, "Something went wrong!");
			}
			
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong!");
			e.printStackTrace();
		}
	}
	
	public static void toClipboard(String s) {
		StringSelection selection = new StringSelection(s);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, selection);
	}

	public static void saveToParticleSystemIniFile() {
		File part_ini_old = new File(Config.currentParticleSystemFile);
		ArrayList<String> workingPart = new ArrayList<String>();
		workingPart.addAll(Main.work_ParticleSystemTypes.keySet());
		LinkedList<String> orderedList = new LinkedList<String>();
		Pattern p = Pattern.compile("^ParticleSystem\\s");
		try {
			//Scan trough whole file to determine order of Working FX;
			Scanner sc = new Scanner(part_ini_old);
			sc.useLocale(Locale.ENGLISH);
			sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
			String token = "";
			boolean eof = false;
			while(!eof && sc.hasNextLine()) {
				if (sc.hasNext()) token = sc.next();
				else eof = true;
				if (token.equals("ParticleSystem")) {				
					if (sc.hasNext()) {
						String s = sc.next();
						if (workingPart.contains(s)) {
							System.out.println("Found ParticleSystem "+s+" in file.");
							orderedList.add(s);
							workingPart.remove(s);
						}
					}
				}
			}
			//add remaining items
			for (String s : workingPart) {
				System.out.println("Couldn't find ParticleSystem "+s+" in file. Add at bottom.");
				orderedList.add(s);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file: "+part_ini_old);
			e.printStackTrace();
		}		
		try {
			File part_ini_new = new File(Config.currentParticleSystemFile+"_new");
			PrintWriter pw = new PrintWriter(new FileWriter(part_ini_new));
			Scanner sc = new Scanner(part_ini_old);
			sc.useLocale(Locale.ENGLISH);
			boolean done = false;
			String name = "";
			while (sc.hasNextLine()) {
				if (orderedList.isEmpty()) done = true;
				else name = orderedList.getFirst();
				String line = sc.nextLine();
				if (!done && line.matches("^ParticleSystem\\s"+name+ "\\s*(;.*)*$")) {
					boolean end = false;
					while (sc.hasNextLine() && !end) { //Skip this entry
						String s = sc.nextLine();
						if(s.matches("^End\\s*(;.*)*$")) {
							end = true;
						}
					}
					pw.write(Main.getParticleSystem(name).getFormattedCode(name)+"\n");
					orderedList.removeFirst();
				}else {
					pw.write(line+"\n");
				}
			}
			sc.close();
			//write remaining items
			for (String s : orderedList) {
				pw.write(Main.getParticleSystem(name).getFormattedCode(s)+"\n");
			}
			pw.flush();
			pw.close();
			
			File ini_bak = new File(Config.currentParticleSystemFile+".bak");
			Files.deleteIfExists(ini_bak.toPath());
			
			if (part_ini_old.renameTo(ini_bak)) {
				boolean success = part_ini_new.renameTo(new File(Config.currentParticleSystemFile));
				if (success) {
					JOptionPane.showMessageDialog(null, "Successfuly saved '"+Config.currentParticleSystemFile+"' (Saved backup of previous version)");
				}else {
					JOptionPane.showMessageDialog(null, "Something went wrong!");
				}
			}else {
				JOptionPane.showMessageDialog(null, "Something went wrong!");
			}
			
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong!");
			e.printStackTrace();
		}
	}
	
	private static JColorChooser jcol = null;
	private static Color col = null;
	
	public static Color pickColor(String message, Color oldColor) {
		jcol = new JColorChooser(oldColor);
		col = oldColor;
		//Remove unncesseary tabs
		AbstractColorChooserPanel[] panels = jcol.getChooserPanels();
		for (AbstractColorChooserPanel accp : panels) {
		   if(!(accp.getDisplayName().equals("HSV") || accp.getDisplayName().equals("RGB")) ) {
		      jcol.removeChooserPanel(accp);
		   } 
		}
		
		ActionListener okListener = new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	        col = jcol.getColor();
	      }
	    };
		
		//---
		JDialog jdia = JColorChooser.createDialog(Main.renderer.mainWindow, message, true, jcol, okListener, null);
		jdia.setVisible(true);
		return col;
	}

}
