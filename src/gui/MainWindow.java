package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JWindow;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import main.Config;
import main.Main;
import main.Renderer;
import net.miginfocom.swing.MigLayout;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import parser.Parser;
import util.Util;
import entitytypes.FXListType;
import entitytypes.ParticleSystemType;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {

	
	public Renderer renderer;
	
	private JPanel contentPane;
	private JLabel lblFps_val;
	private JButton btnReset;
	private JToggleButton tglbtnAutoplay;

	public boolean running = true;
	public boolean reset = false;
	
	public ArrayList<Color> colors;
	
	private JToggleButton tglbtnPlay;
	private JSlider sld_FPS;
	private JFormattedTextField txt_ZOffset;
	private JCheckBox chckbxShowGround;
	private JComboBox cb_bgColor;
	/**
	 * Create the frame.
	 */
	public MainWindow(Renderer rend) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Config.writeConfigFile(Main.configFilePath);
			}
		});
		this.renderer = rend;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 338);
		
		//MENU BAR
		//------------------------
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExportWorkingSet = new JMenuItem("Export working set to file...");
		mntmExportWorkingSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   				 
				int userSelection = fileChooser.showSaveDialog(MainWindow.this);				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File file = fileChooser.getSelectedFile();
				    saveWorkingSetToFile(file);
				}
			}
		});
		
		JMenuItem mntmImportiniFiles = new JMenuItem("Import .INI files...");
		mntmImportiniFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//ParticleSystems first
				JFileChooser fileChooser = new JFileChooser(Config.DefaultFolder);
				fileChooser.setDialogTitle("Specify ParticleSystem.ini file");   				 
				int userSelection = fileChooser.showOpenDialog(MainWindow.this);				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				   String ps_path = fileChooser.getSelectedFile().getAbsolutePath();
				   JFileChooser fileChooser2 = new JFileChooser(fileChooser.getCurrentDirectory());
				   fileChooser2.setDialogTitle("Specify FXList.ini file");		
				   userSelection = fileChooser2.showOpenDialog(MainWindow.this);				 
				   if (userSelection == JFileChooser.APPROVE_OPTION) {
					   Config.DefaultFolder = fileChooser2.getCurrentDirectory().getAbsolutePath();
					   String fx_path = fileChooser2.getSelectedFile().getAbsolutePath();
					   renderer.browsePanel.setIgnoreChanges(true);
					   renderer.canvas.setVisible(false);
					   JWindow w = Util.showLoadingWindow(renderer.mainWindow);
					   Main.loadFiles(ps_path, fx_path);
					   renderer.loadTextures();
					   renderer.browsePanel.browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
					   renderer.browsePanel.browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
					   renderer.editPanel.particleEditPanel.fillParticleLists();
					   w.setVisible(false);
					   w.dispose();
					   renderer.canvas.setVisible(true);
					   reset = true;
					   renderer.browsePanel.setIgnoreChanges(false);
				   }
				}
				//Then FXList
			}
		});
		mnFile.add(mntmImportiniFiles);
		mnFile.add(mntmExportWorkingSet);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenuItem mi_Preferences = new JMenuItem("Preferences");
		mi_Preferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Preferences prefDialog = new Preferences();
				prefDialog.setVisible(true);
			}
		});
		mnSettings.add(mi_Preferences);
		
//		JMenuItem mi_ChooseTextureFolder = new JMenuItem("Choose Primary Texture folder...");
//		mnSettings.add(mi_ChooseTextureFolder);
//		mi_ChooseTextureFolder.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser fileChooser = new JFileChooser();
//				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				fileChooser.setAcceptAllFileFilterUsed(false);
//				fileChooser.setDialogTitle("Choose Texture folder");   				 
//				int userSelection = fileChooser.showOpenDialog(MainWindow.this);				 
//				if (userSelection == JFileChooser.APPROVE_OPTION) {
//					Config.TextureFolder1 = fileChooser.getSelectedFile().getAbsolutePath();
//				}
//			}
//		});
		
		//---------------------------------------
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JToolBar toolBar = new JToolBar();
		panel_1.add(toolBar);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset = true;
			}
		});
		
		tglbtnPlay = new JToggleButton("Pause");
		toolBar.add(tglbtnPlay);
		toolBar.add(btnReset);
		
		tglbtnAutoplay = new JToggleButton("Repeat");
		tglbtnAutoplay.setSelected(true);
		toolBar.add(tglbtnAutoplay);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		toolBar.add(panel);
		
		JLabel lblFps = new JLabel("FPS:");
		panel.add(lblFps);
		
		sld_FPS = new JSlider();
		panel.add(sld_FPS);
		sld_FPS.setMajorTickSpacing(5);
		sld_FPS.setSnapToTicks(true);
		sld_FPS.setMinorTickSpacing(1);
		sld_FPS.setMinimum(15);
		sld_FPS.setMaximum(60);
		sld_FPS.setValue(30);
		sld_FPS.setPreferredSize(new Dimension(150, 25));
		sld_FPS.addChangeListener(new ChangeListener() {
	      public void stateChanged(ChangeEvent event) {
	        int value = sld_FPS.getValue();
	        lblFps_val.setText(""+value);
	        Main.FPS = value;
	      }
		});
		
		lblFps_val = new JLabel("30");
		panel.add(lblFps_val);
		
		JToolBar toolBar_1 = new JToolBar();
		panel_1.add(toolBar_1);
		
		JButton btnResetView = new JButton("Reset View");
		btnResetView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				renderer.resetCamera();
			}
		});
		toolBar_1.add(btnResetView);
		
		JPanel panel_2 = new JPanel();
		toolBar_1.add(panel_2);
		
		chckbxShowGround = new JCheckBox("Show Ground");
		chckbxShowGround.setSelected(true);
		chckbxShowGround.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				renderer.setGroundVisible(chckbxShowGround.isSelected());
			}
		});

//		chckbxShowGround.addPropertyChangeListener(new PropertyChangeListener() {
//			public void propertyChange(PropertyChangeEvent evt) {
//				renderer.setGroundVisible(chckbxShowGround.isSelected());
//			}
//		});
		panel_2.add(chckbxShowGround);
		
		JLabel lblNewLabel = new JLabel("Ground Offset:");
		panel_2.add(lblNewLabel);
		
		txt_ZOffset = new JFormattedTextField(); //NumberFormat.getNumberInstance());
		txt_ZOffset.setValue(new Float(0.0f));
		txt_ZOffset.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if (txt_ZOffset.getValue() != null) {
					renderer.setGroundOffset((float)txt_ZOffset.getValue());
				}
			}
		});
		txt_ZOffset.setColumns(8);
		panel_2.add(txt_ZOffset);
		
		JPanel panel_3 = new JPanel();
		toolBar_1.add(panel_3);
		
		JLabel lblBackground = new JLabel("Background:");
		panel_3.add(lblBackground);
		
		cb_bgColor = new JComboBox(); //COLORS		

		colors = new ArrayList<Color>();
		colors.add(Color.BLACK); cb_bgColor.addItem("black");
		colors.add(Color.WHITE); cb_bgColor.addItem("white");
		colors.add(Color.LIGHT_GRAY); cb_bgColor.addItem("light gray");
		colors.add(Color.GRAY); cb_bgColor.addItem("gray");
		colors.add(Color.DARK_GRAY); cb_bgColor.addItem("dark gray");
		colors.add(Color.RED); cb_bgColor.addItem("red");
		colors.add(Color.GREEN); cb_bgColor.addItem("green");
		colors.add(Color.BLUE); cb_bgColor.addItem("blue");
		colors.add(Color.CYAN); cb_bgColor.addItem("cyan");
		colors.add(Color.MAGENTA); cb_bgColor.addItem("magenta");
		colors.add(Color.YELLOW); cb_bgColor.addItem("yellow");
		colors.add(Color.ORANGE); cb_bgColor.addItem("orange");
		colors.add(Color.PINK); cb_bgColor.addItem("pink");
		cb_bgColor.setSelectedIndex(0);
		
		cb_bgColor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				renderer.setBackgroundColor(colors.get(cb_bgColor.getSelectedIndex()));
			}
		});

		panel_3.add(cb_bgColor);
	}

	public void saveWorkingSetToFile(File file) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			for (String fxName : Main.work_FXListTypes.keySet()) {
				FXListType ftype = Main.work_FXListTypes.get(fxName);
				pw.println("FXList "+fxName);
				String[] lines = ftype.createInnerCode().split("\n");
				for (String l : lines) {
					pw.println("  "+l);
				}
				pw.println("End");
			}
			pw.println();
			pw.println(";--------------------------------");
			pw.println(";------ Particle Systems  -------");
			pw.println(";--------------------------------");
			pw.println();
			for (String pName : Main.work_ParticleSystemTypes.keySet()) {
				ParticleSystemType ptype = Main.work_ParticleSystemTypes.get(pName);
				pw.println("ParticleSystem "+pName);
				String[] lines = ptype.createInnerCode().split("\n");
				for (String l : lines) {
					pw.println("  "+l);
				}
				pw.println("End");
			}
			
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			System.out.println("Could not write File "+file.getName());
			e.printStackTrace();
		}
	}

	public JLabel getLblFps_val() {
		return lblFps_val;
	}
	public JButton getBtnReset() {
		return btnReset;
	}
	public JToggleButton getTglbtnAutoplay() {
		return tglbtnAutoplay;
	}
	public JPanel getContentPane() {
		return contentPane;
	}

	public boolean isRunning() {
		return running;
	}
	public JToggleButton getTglbtnPlay() {
		return tglbtnPlay;
	}
	public JSlider getSld_FPS() {
		return sld_FPS;
	}
	public JFormattedTextField getTxt_ZOffset() {
		return txt_ZOffset;
	}
	public JCheckBox getChckbxShowGround() {
		return chckbxShowGround;
	}
	public JComboBox getCb_bgColor() {
		return cb_bgColor;
	}

}
