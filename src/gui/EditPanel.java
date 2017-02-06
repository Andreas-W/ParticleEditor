package gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import main.Main;
import main.Renderer;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import parser.Parser;
import entitytypes.FXListType;
import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.ParticleSystemType;

import java.awt.GridBagLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Font;

public class EditPanel extends JPanel {

	
	public Renderer renderer;
	private JTextField txtFXName;
	private JToggleButton tglbtn_renameFX;
	private JEditorPane editor_FX;
	private JEditorPane editor_Particle;
	private JPanel panel_3;
	private JScrollPane scrollPane_ParticleEntries;
	private JPanel panel_ParticleEntries;
	private JButton btnParseCode;
	private JButton btnReset;
	private JComboBox cb_ParticleSystems;
	public ParticleEditPanel particleEditPanel;
	//private JComboBox<String> cb_FXLists;
	
	/**
	 * Create the panel.
	 */
	public EditPanel(Renderer rend) {
		this.renderer = rend;
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tPane_EditMain = new JTabbedPane(JTabbedPane.TOP);
		add(tPane_EditMain, BorderLayout.CENTER);
		
		JPanel panel_fx = new JPanel();
		tPane_EditMain.addTab("FXList", null, panel_fx, null);
		panel_fx.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		panel_fx.add(tabbedPane_1);
		
		scrollPane_ParticleEntries = new JScrollPane();
		tabbedPane_1.addTab("Edit Values", null, scrollPane_ParticleEntries, null);
		
		panel_3 = new JPanel();
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		scrollPane_ParticleEntries.setColumnHeaderView(panel_3);
		scrollPane_ParticleEntries.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_ParticleEntries.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton btnAddParticlesystem = new JButton("Add ParticleSystem");
		
		panel_3.add(btnAddParticlesystem);
		
		panel_ParticleEntries = new JPanel();
		scrollPane_ParticleEntries.setViewportView(panel_ParticleEntries);
		panel_ParticleEntries.setLayout(new BoxLayout(panel_ParticleEntries, BoxLayout.Y_AXIS));
		
				
				
				btnAddParticlesystem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ParticleSystemEntry entry = Main.activeFXListType.new ParticleSystemEntry();
						entry.Name = Main.ParticleSystemNames.get(0);
						Main.activeFXListType.ParticleSystems.add(entry);
						ParticleSystemEntryPanel pse_panel = new ParticleSystemEntryPanel(renderer, entry);
						panel_ParticleEntries.add(pse_panel);
						selectionChanged();
					}
				});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane_1.addTab("Edit Code", null, scrollPane_2, null);
		
		editor_FX = new JEditorPane();
		editor_FX.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPane_2.setViewportView(editor_FX);
		
		JPanel panel = new JPanel();
		scrollPane_2.setColumnHeaderView(panel);
		
		btnParseCode = new JButton("Parse Code");
		btnParseCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FXcodeChanged();
			}
		});
		panel.add(btnParseCode);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FXvaluesChanged();
			}
		});
		panel.add(btnReset);
		
		JPanel panel_2 = new JPanel();
		panel_fx.add(panel_2, BorderLayout.NORTH);
		
		txtFXName = new JTextField();
		txtFXName.setEditable(false);
		panel_2.add(txtFXName);
		txtFXName.setColumns(16);
		
		tglbtn_renameFX = new JToggleButton("Rename");
		panel_2.add(tglbtn_renameFX);
		
//		cb_FXLists = new JComboBox<String>();
//		panel_1.add(cb_FXLists, BorderLayout.NORTH);
		
		JPanel panel_particle = new JPanel();
		tPane_EditMain.addTab("Particle Systems", null, panel_particle, null);
		panel_particle.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_particle.add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane_pValues = new JScrollPane();
		tabbedPane.addTab("Edit Values", null, scrollPane_pValues, null);
		particleEditPanel = new ParticleEditPanel(renderer);
		scrollPane_pValues.setViewportView(particleEditPanel);
		
		JScrollPane scrollPane_pCode = new JScrollPane();
		tabbedPane.addTab("Edit Code", null, scrollPane_pCode, null);
		
		editor_Particle = new JEditorPane();
		editor_Particle.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPane_pCode.setViewportView(editor_Particle);
		
		JPanel panel_4 = new JPanel();
		scrollPane_pCode.setColumnHeaderView(panel_4);
		
		JButton btnParseCodeP = new JButton("Parse Code");
		btnParseCodeP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parseParticleCode();
			}
		});
		panel_4.add(btnParseCodeP);
		
		JButton btnResetCodeP = new JButton("Reset");
		btnResetCodeP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateParticleCode();
			}
		});
		panel_4.add(btnResetCodeP);
		
		cb_ParticleSystems = new JComboBox();
		cb_ParticleSystems.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cb_ParticleSystems.getSelectedIndex() != -1) {
					String pName = (String) cb_ParticleSystems.getSelectedItem();
					Main.activeParticleSystemType = Main.getParticleSystem(pName);
					particleEditPanel.loadValues();
				}
			}
		});
		panel_particle.add(cb_ParticleSystems, BorderLayout.NORTH);

		setEditingEnabled(true);
	}
	
	public void particleEditPerformed() {
		//TODO: Undo stack
		ParticleSystemType type = Main.activeParticleSystemType;
		String pName = (String)cb_ParticleSystems.getSelectedItem();
		if (!Main.work_ParticleSystemTypes.containsKey(pName)) {
			Main.work_ParticleSystemTypes.put(pName, type);
			renderer.browsePanel.browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
		}
//		if (type.SlaveSystem != null && !type.SlaveSystem.equals("") && !Main.work_ParticleSystemTypes.containsKey(type.SlaveSystem)) {
//			Main.work_ParticleSystemTypes.put(type.SlaveSystem, Main.getParticleSystem(type.SlaveSystem));
//		}
//		if (type.PerParticleAttachedSystem != null && !type.PerParticleAttachedSystem.equals("") && !Main.work_ParticleSystemTypes.containsKey(type.PerParticleAttachedSystem)) {
//			Main.work_ParticleSystemTypes.put(type.PerParticleAttachedSystem, Main.getParticleSystem(type.PerParticleAttachedSystem));
//		}
	}
	
	public void fxEditPerformed() {
		//TODO: Undo stack
		if (!Main.activeFXListType.isTemporary()) {
			String fName = (String)txtFXName.getText();
			if (!Main.work_FXListTypes.containsKey(fName)) {
				Main.work_FXListTypes.put(fName, Main.activeFXListType);
				renderer.browsePanel.browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
			}
		}
	}

	public void setEditingEnabled(boolean enabled) {
		this.tglbtn_renameFX.setEnabled(enabled);
		this.editor_FX.setEnabled(enabled);
		this.editor_Particle.setEnabled(enabled);
		//TODO: Add every edit control here
	}
	
	
	public void selectionChanged() {
		cb_ParticleSystems.removeAllItems();
		panel_ParticleEntries.removeAll();
		FXListType type = Main.activeFXListType;
		if (type == null){
			System.out.println("Type ist null, why is this happening?");
			return;
		}
		for (ParticleSystemEntry entry : type.ParticleSystems) {
			entry.setVisible(true);
			ParticleSystemEntryPanel pse_panel = new ParticleSystemEntryPanel(renderer, entry);
			panel_ParticleEntries.add(pse_panel);
			//--
			cb_ParticleSystems.addItem(entry.Name);
			ParticleSystemType ptype = Main.getParticleSystem(entry.Name);
			if (ptype.PerParticleAttachedSystem != null && !ptype.PerParticleAttachedSystem.equals("") && Main.ParticleSystemTypes.containsKey(ptype.PerParticleAttachedSystem)) {
				cb_ParticleSystems.addItem(ptype.PerParticleAttachedSystem);
			}
			if (ptype.SlaveSystem != null && !ptype.SlaveSystem.equals("") && Main.ParticleSystemTypes.containsKey(ptype.SlaveSystem)) {
				cb_ParticleSystems.addItem(ptype.SlaveSystem);
			}			
		}
		FXvaluesChanged();
		
		this.particleEditPanel.loadValues();
	}
	
	public void updateParticleCode() {
		editor_Particle.setText(Main.activeParticleSystemType.createInnerCode());
		particleEditPerformed();
	}
	
	public void parseParticleCode() {
		ParticleSystemType ptype = Parser.parseParticleSystemCode(this.editor_Particle.getText(), (String)cb_ParticleSystems.getSelectedItem());
		Main.activeParticleSystemType = ptype;
		particleEditPanel.loadValues();
		particleEditPerformed();
	}
	
	public void FXvaluesChanged() {
		editor_FX.setText(Main.activeFXListType.createInnerCode());
		fxEditPerformed();
	}
	
	public void FXcodeChanged() {
		cb_ParticleSystems.removeAllItems();
		FXListType type = Parser.parseFXListCode(this.editor_FX.getText(), this.txtFXName.getText());
		Main.activeFXListType = type;
		panel_ParticleEntries.removeAll();
		//panel_ParticleEntries.updateUI();
		for (ParticleSystemEntry entry : type.ParticleSystems) {
			entry.setVisible(true);
			ParticleSystemEntryPanel pse_panel = new ParticleSystemEntryPanel(renderer, entry);
			panel_ParticleEntries.add(pse_panel);
			cb_ParticleSystems.addItem(entry.Name);
			ParticleSystemType ptype = Main.getParticleSystem(entry.Name);
			if (ptype.PerParticleAttachedSystem != null || !ptype.PerParticleAttachedSystem.equals("")) {
				cb_ParticleSystems.addItem(ptype.PerParticleAttachedSystem);
			}
			if (ptype.SlaveSystem != null || !ptype.SlaveSystem.equals("")) {
				cb_ParticleSystems.addItem(ptype.SlaveSystem);
			}
		}
		fxEditPerformed();
	}

	
	public JTextField getTxtFXName() {
		return txtFXName;
	}
	public JToggleButton getTglbtn_renameFX() {
		return tglbtn_renameFX;
	}
	public JEditorPane getEditor_FX() {
		return editor_FX;
	}
	public JEditorPane getEditor_Particle() {
		return editor_Particle;
	}
	public JScrollPane getScrollPane_ParticleEntries() {
		return scrollPane_ParticleEntries;
	}
	public JPanel getPanel_ParticleEntries() {
		return panel_ParticleEntries;
	}
	public JButton getBtnParseCode() {
		return btnParseCode;
	}
	public JButton getBtnReset() {
		return btnReset;
	}
	public JComboBox getCb_ParticleSystems() {
		return cb_ParticleSystems;
	}

	public void removeParticleEntry(ParticleSystemEntry entry) {
		Main.activeFXListType.ParticleSystems.remove(entry);
		FXvaluesChanged();
	}
	
}
