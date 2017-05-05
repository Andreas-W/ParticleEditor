package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;

import main.Config;
import main.Main;

import java.awt.Component;

import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Preferences extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfPSFile;
	private JTextField tfFXFile;
	private JTextField tfTextures2;
	private JTextField tfTextures1;
	private JRadioButton rbLoadUsedTextures;
	private JTextField tfTexturePattern;
	private JRadioButton rbLoadAllTextures;
	
	private boolean init = false;

	/**
	 * Create the dialog.
	 */
	public Preferences() {
		setModal(true);
		setTitle("Preferences");
		setBounds(100, 100, 519, 287);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPanel.add(panel);
		JLabel lblPrimaryTextureFolder = new JLabel("Primary Texture folder:");
		panel.add(lblPrimaryTextureFolder);
		tfTextures1 = new JTextField();
		tfTextures1.setEditable(false);
		tfTextures1.setColumns(24);
		panel.add(tfTextures1);

		JButton btnTextures1Edit = new JButton("edit");
		btnTextures1Edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(Config.DefaultFolder);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setDialogTitle("Choose Primary Texture folder");   				 
				int userSelection = fileChooser.showOpenDialog(Preferences.this);				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					Config.TextureFolder1 = fileChooser.getSelectedFile().getAbsolutePath();
					Config.DefaultFolder = fileChooser.getCurrentDirectory().getAbsolutePath();
					Config.writeConfigFile(Main.configFilePath);
					loadValues();
				}
			}
		});
		panel.add(btnTextures1Edit);

		JPanel panel2 = new JPanel();
		FlowLayout flowLayout2 = (FlowLayout) panel2.getLayout();
		flowLayout2.setAlignment(FlowLayout.RIGHT);
		contentPanel.add(panel2);

		JLabel lblSecondaryTextureFolder = new JLabel(
				"Secondary Texture folder:");
		lblSecondaryTextureFolder.setToolTipText("OPTIONAL. Use a secondary folder if you want to keep default ZH textures in a different directory");
		panel2.add(lblSecondaryTextureFolder);

		tfTextures2 = new JTextField();
		tfTextures2.setEditable(false);
		tfTextures2.setColumns(24);
		panel2.add(tfTextures2);

		JButton btnTextures2Edit = new JButton("edit");
		btnTextures2Edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(Config.DefaultFolder);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setDialogTitle("Choose Secondary Texture folder");   				 
				int userSelection = fileChooser.showOpenDialog(Preferences.this);				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					Config.TextureFolder2 = fileChooser.getSelectedFile().getAbsolutePath();
					Config.DefaultFolder = fileChooser.getCurrentDirectory().getAbsolutePath();
					Config.writeConfigFile(Main.configFilePath);
					loadValues();
				}
			}
		});
		panel2.add(btnTextures2Edit);
		
		JPanel panel_1 = new JPanel();
		contentPanel.add(panel_1);
		
		ButtonGroup bg = new ButtonGroup();
		
		rbLoadUsedTextures = new JRadioButton("Load used textures only");
		rbLoadUsedTextures.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (init && e.getStateChange() == ItemEvent.SELECTED) {
					tfTexturePattern.setEnabled(false);
				}
			}
		});
		panel_1.add(rbLoadUsedTextures);
		
		rbLoadAllTextures = new JRadioButton("Load all textures matching:");
		rbLoadAllTextures.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (init && e.getStateChange() == ItemEvent.SELECTED) {
					tfTexturePattern.setEnabled(true);
				}
			}
		});
		rbLoadAllTextures.setSelected(true);
		panel_1.add(rbLoadAllTextures);
		
		bg.add(rbLoadAllTextures);
		bg.add(rbLoadUsedTextures);
		
		tfTexturePattern = new JTextField();
		panel_1.add(tfTexturePattern);
		tfTexturePattern.setColumns(16);
		if (Config.TextureLoadPattern != null) {
			tfTexturePattern.setText(Config.TextureLoadPattern);
			rbLoadAllTextures.setSelected(true);
		}
		

		JSeparator separator = new JSeparator();
		separator.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPanel.add(separator);

		JPanel panel3 = new JPanel();
		FlowLayout flowLayout3 = (FlowLayout) panel3.getLayout();
		flowLayout3.setAlignment(FlowLayout.RIGHT);
		contentPanel.add(panel3);

		JLabel lblDefaultParticlesystemFile = new JLabel(
				"Default ParticleSystem file:");
		panel3.add(lblDefaultParticlesystemFile);

		tfPSFile = new JTextField();
		tfPSFile.setEditable(false);
		panel3.add(tfPSFile);
		tfPSFile.setColumns(24);

		JButton btnPSFileEdit = new JButton("edit");
		btnPSFileEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(Config.DefaultFolder);
				fileChooser.setDialogTitle("Choose Default ParticleSystem file");   				 
				int userSelection = fileChooser.showOpenDialog(Preferences.this);				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					Config.ParticleSystemFile = fileChooser.getSelectedFile().getAbsolutePath();
					Config.DefaultFolder = fileChooser.getCurrentDirectory().getAbsolutePath();
					Config.writeConfigFile(Main.configFilePath);
					loadValues();
				}
			}
		});
		panel3.add(btnPSFileEdit);

		JPanel panel4 = new JPanel();
		FlowLayout flowLayout4 = (FlowLayout) panel4.getLayout();
		flowLayout4.setAlignment(FlowLayout.RIGHT);
		contentPanel.add(panel4);

		JLabel lblDefaultFxlistFile = new JLabel("Default FXList file:");
		panel4.add(lblDefaultFxlistFile);

		tfFXFile = new JTextField();
		tfFXFile.setEditable(false);
		tfFXFile.setColumns(24);
		panel4.add(tfFXFile);

		JButton btnFXFileEdit = new JButton("edit");
		btnFXFileEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(Config.DefaultFolder);
				fileChooser.setDialogTitle("Choose Default FXList file");   				 
				int userSelection = fileChooser.showOpenDialog(Preferences.this);				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					Config.FXListFile = fileChooser.getSelectedFile().getAbsolutePath();
					Config.DefaultFolder = fileChooser.getCurrentDirectory().getAbsolutePath();
					Config.writeConfigFile(Main.configFilePath);
					loadValues();
				}
			}
		});
		panel4.add(btnFXFileEdit);
		
		JSeparator separator_1 = new JSeparator();
		contentPanel.add(separator_1);
		separator_1.setAlignmentY(0.0f);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rbLoadAllTextures.isSelected() && tfTexturePattern.getText() != null && !tfTexturePattern.getText().equals("")) {
					Config.TextureLoadPattern = tfTexturePattern.getText();
				}else {
					Config.TextureLoadPattern = null;
				}
				setVisible(false);
				dispose();
			}
		});
		btnOK.setActionCommand("OK");
		buttonPane.add(btnOK);
		getRootPane().setDefaultButton(btnOK);

		loadValues();
		init = true;
	}

	private void loadValues() {
		tfFXFile.setText(Config.FXListFile);
		tfPSFile.setText(Config.ParticleSystemFile);
		tfTextures1.setText(Config.TextureFolder1);
		tfTextures2.setText(Config.TextureFolder2);
	}
	
}
