package gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
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
import util.Undo;
import util.Util;
import util.Undo.OperationType;
import entitytypes.FXListType;
import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.ParticleSystemType;

import java.awt.GridBagLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class EditPanel extends JPanel {

	
	public Renderer renderer;
	public JFormattedTextField txtFXName;
	private JEditorPane editor_FX;
	private JEditorPane editor_Particle;
	private JPanel panel_3;
	private JScrollPane scrollPane_ParticleEntries;
	private JPanel panel_ParticleEntries;
	private JButton btnParseCode;
	private JButton btnReset;
	public JComboBox cb_ParticleSystems;
	public ParticleEditPanel particleEditPanel;
	private JButton btnAddParticlesystem;
	//private JComboBox<String> cb_FXLists;
	public boolean ignoreChanges;
	
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
		scrollPane_ParticleEntries.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane_1.addTab("Edit Values", null, scrollPane_ParticleEntries, null);
		
		panel_3 = new JPanel();
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		scrollPane_ParticleEntries.setColumnHeaderView(panel_3);
		scrollPane_ParticleEntries.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_ParticleEntries.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		btnAddParticlesystem = new JButton("Add ParticleSystem");
		
		panel_3.add(btnAddParticlesystem);
		
		panel_ParticleEntries = new JPanel();
		scrollPane_ParticleEntries.setViewportView(panel_ParticleEntries);
		panel_ParticleEntries.setLayout(new BoxLayout(panel_ParticleEntries, BoxLayout.Y_AXIS));
					
		btnAddParticlesystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Undo.performFXOperation("Add ParticleSystem", OperationType.EDIT);
				ParticleSystemEntry entry = Main.activeFXListType.new ParticleSystemEntry();
				entry.Name = Main.ParticleSystemNames.get(0);
				Main.activeFXListType.ParticleSystems.add(entry);
				ParticleSystemEntryPanel pse_panel = new ParticleSystemEntryPanel(renderer, entry);
				panel_ParticleEntries.add(pse_panel);
				updateFXGUI();
				
				fxEditPerformed();
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane_1.addTab("Edit Code", null, scrollPane_2, null);
		
		editor_FX = new JEditorPane();
		editor_FX.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPane_2.setViewportView(editor_FX);
		
		JPanel panel = new JPanel();
		scrollPane_2.setColumnHeaderView(panel);
		
		btnParseCode = new JButton("Parse Code");
		btnParseCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parseFXCode();
			}
		});
		panel.add(btnParseCode);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateFXCode();
			}
		});
		panel.add(btnReset);
		
		JButton btnFXtoClipboard = new JButton("to clipboard");
		btnFXtoClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Util.toClipboard(Main.activeFXListType.getFormattedCode(Main.activeFXName()));
			}
		});
		btnFXtoClipboard.setToolTipText("Copy formatted Code to clipboard to manually paste into an ini file");
		panel.add(btnFXtoClipboard);
		
		JPanel panel_2 = new JPanel();
		panel_fx.add(panel_2, BorderLayout.NORTH);
		
		txtFXName = new JFormattedTextField("");
		txtFXName.setColumns(26);
		txtFXName.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				//System.out.println("Event: PropertyName="+e.getPropertyName()+" OldV="+e.getOldValue()+" NewV="+e.getNewValue());
				if (!ignoreChanges && e.getPropertyName().equals("value")) {
					int result = JOptionPane.showConfirmDialog(EditPanel.this, "Rename FX '"+e.getOldValue()+"' to '"+e.getNewValue()+"'?");
					if (result == JOptionPane.YES_OPTION) {
						ignoreChanges = true;
						FXListType type = Main.getFXList((String)e.getOldValue());
						if (type != null) {
							Main.FXListTypes.remove((String)e.getOldValue());
							Main.FXListTypes.put((String)e.getNewValue(), type);
							if (Main.work_FXListTypes.containsKey((String)e.getOldValue()))
									Main.work_FXListTypes.remove((String)e.getOldValue());
							Main.updateFXListNames();
							renderer.browsePanel.setIgnoreChanges(true);
							renderer.browsePanel.browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
							renderer.browsePanel.browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
							renderer.updateActiveFX(type, (String)e.getNewValue());
							renderer.browsePanel.setIgnoreChanges(false);
							renderer.editPanel.fxEditPerformed();
						}
						ignoreChanges = false;
					}else {
						ignoreChanges = true;
						txtFXName.setValue(e.getOldValue());
						ignoreChanges = false;
					}
				}
			}
		});
		
		txtFXName.setEditable(false);
		
		
		panel_2.add(txtFXName);
		
//		cb_FXLists = new JComboBox<String>();
//		panel_1.add(cb_FXLists, BorderLayout.NORTH);
		
		JPanel panel_particle = new JPanel();
		tPane_EditMain.addTab("Particle Systems", null, panel_particle, null);
		panel_particle.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_particle.add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane_pValues = new JScrollPane();
		scrollPane_pValues.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane.addTab("Edit Values", null, scrollPane_pValues, null);
		particleEditPanel = new ParticleEditPanel(renderer);
		scrollPane_pValues.setViewportView(particleEditPanel);
		
		JScrollPane scrollPane_pCode = new JScrollPane();
		scrollPane_pCode.getVerticalScrollBar().setUnitIncrement(16);
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
		
		JButton btnPtoClipboard = new JButton("to clipboard");
		btnPtoClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				Util.toClipboard(Main.activeParticleSystemType.getFormattedCode(Main.activeParticleName()));
			}
		});
		btnPtoClipboard.setToolTipText("Copy formatted Code to clipboard to manually paste into an ini file");
		panel_4.add(btnPtoClipboard);
		
		cb_ParticleSystems = new JComboBox();
		cb_ParticleSystems.setModel(new DefaultComboBoxModel<String>());
		cb_ParticleSystems.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && cb_ParticleSystems.getSelectedIndex() != -1) {
					String pName = (String) cb_ParticleSystems.getSelectedItem();
					ParticleSystemType ptype = Main.getParticleSystem(pName);
					if (ptype != null) {
						renderer.updateActiveParticle(ptype, pName);
						//Main.activeParticleSystemType = Main.getParticleSystem(pName);
						//particleEditPanel.loadValues();
					}
				}
			}
		});
		panel_particle.add(cb_ParticleSystems, BorderLayout.NORTH);

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
			String fName = (String)txtFXName.getValue();
			if (!Main.work_FXListTypes.containsKey(fName)) {
				Main.work_FXListTypes.put(fName, Main.activeFXListType);
				renderer.browsePanel.browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
			}
		}
	}

	public void setFXEditsEnabled(boolean b) {
		this.editor_FX.setEnabled(b);
		this.btnParseCode.setEnabled(b);
		this.btnReset.setEnabled(b);
		this.btnAddParticlesystem.setEnabled(b);
		this.txtFXName.setEditable(b);
		
	}
	
//	public void selectionChanged() {
//		
//		panel_ParticleEntries.removeAll();
//		FXListType type = Main.activeFXListType;
//		if (type == null){
//			System.out.println("Type ist null, why is this happening?");
//			return;
//		}
//		updateFXCode();
//		
//		this.particleEditPanel.loadValues();
//	}
	
	public void updateFXGUI() {
		cb_ParticleSystems.removeAllItems();
		panel_ParticleEntries.removeAll();
		panel_ParticleEntries.revalidate();
		panel_ParticleEntries.repaint();
		FXListType type = Main.activeFXListType;
		for (ParticleSystemEntry entry : type.ParticleSystems) {
			if (Main.ParticleSystemTypes.containsKey(entry.Name)) {
				//entry.setVisible(true);
				if (!type.isTemporary()) {
					ParticleSystemEntryPanel pse_panel = new ParticleSystemEntryPanel(renderer, entry);
					panel_ParticleEntries.add(pse_panel);
				}
				addToParticleSelection(entry.Name);
				ParticleSystemType ptype = Main.getParticleSystem(entry.Name);
				if (ptype.PerParticleAttachedSystem != null && !ptype.PerParticleAttachedSystem.equals("") && Main.ParticleSystemTypes.containsKey(ptype.PerParticleAttachedSystem)) {
					addToParticleSelection(ptype.PerParticleAttachedSystem);
					//We need to go deeper
					if (Main.ParticleSystemTypes.containsKey(ptype.PerParticleAttachedSystem)) {
						ParticleSystemType atype = Main.getParticleSystem(ptype.PerParticleAttachedSystem);
						if (atype.PerParticleAttachedSystem != null && !atype.PerParticleAttachedSystem.equals("") && Main.ParticleSystemTypes.containsKey(atype.PerParticleAttachedSystem)) {
							addToParticleSelection(ptype.PerParticleAttachedSystem);
						}
					}
					
				}
				if (ptype.SlaveSystem != null && !ptype.SlaveSystem.equals("") && Main.ParticleSystemTypes.containsKey(ptype.SlaveSystem)) {
					addToParticleSelection(ptype.SlaveSystem);
				}	
			}
		}
	}
	
	private void addToParticleSelection(String name) {
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cb_ParticleSystems.getModel();
		if (model.getIndexOf(name) == -1) cb_ParticleSystems.addItem(name);
	}

	public void updateParticleGUI() {
		particleEditPanel.insertGuiValues();
	}
	
	public void updateParticleCode() {
		editor_Particle.setText(Main.activeParticleSystemType.createInnerCode());
	}
	
	public void parseParticleCode() {
		Undo.performParticleOperation("parsed Code", OperationType.EDIT);
		ParticleSystemType ptype = Parser.parseParticleSystemCode(this.editor_Particle.getText(), (String)cb_ParticleSystems.getSelectedItem());
		Main.activeParticleSystemType = ptype;
		particleEditPanel.insertGuiValues();
		particleEditPerformed();
		
	}
	
	public void updateFXCode() {
		editor_FX.setText(Main.activeFXListType.createInnerCode());
	}
	
	public void parseFXCode() {
		Undo.performFXOperation("Parse Code", OperationType.EDIT);
		cb_ParticleSystems.removeAllItems();
		FXListType type = Parser.parseFXListCode(this.editor_FX.getText(), (String)this.txtFXName.getValue());
		Main.activeFXListType = type;
		panel_ParticleEntries.removeAll();
		//panel_ParticleEntries.updateUI();
		for (ParticleSystemEntry entry : type.ParticleSystems) {
			if (Main.ParticleSystemTypes.containsKey(entry.Name)) {
				//entry.setVisible(true);
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
		}
		fxEditPerformed();
		
	}

	public void setFXTextAuto(String text) {
		ignoreChanges = true;
		txtFXName.setValue(text);
		ignoreChanges = false;
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
		updateFXCode();
	}
	
}
