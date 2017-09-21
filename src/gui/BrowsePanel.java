package gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import entitytypes.FXListType;
import entitytypes.ParticleSystemType;
import main.Main;
import main.Renderer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import util.Undo;
import util.Undo.OperationType;

public class BrowsePanel extends JPanel {
	
	public Renderer renderer;

	private JPanel panel_All;
	public JTabbedPane tpane_BrowsingSet;
	private JPanel panel_Working;
	//private JButton btn_edit;
	//private JButton reload_files;
	
	public BrowseTab browse_All;
	public BrowseTab browse_Working;
	//private JButton btn_reload;
	
	public JButton btn_new;
	public JButton btn_clone;
	private JSeparator separator;
	private JTextField tfFilter;
	private JLabel lblFilter;
	//public JButton btn_remove;
	
	/**
	 * Create the panel.
	 */
	public BrowsePanel(Renderer rend) {
		this.renderer = rend;
		setLayout(new BorderLayout(0, 0));
		
		tfFilter = new JTextField();
		
		tpane_BrowsingSet = new JTabbedPane(JTabbedPane.TOP);
		add(tpane_BrowsingSet, BorderLayout.CENTER);
		tpane_BrowsingSet.addChangeListener(new ChangeListener() {
	      public void stateChanged(ChangeEvent changeEvent) {
	//    	  System.out.println("StateChanged");
	    	  if (browse_All != null && browse_Working != null) {
		        if (tpane_BrowsingSet.getSelectedIndex() == 0) {
		        	browse_All.updateSelection();
		        }else{
		        	browse_Working.updateSelection(); 
		        }
		      }
	      	}
		    });
		
		FlowLayout fl_panel_controls = new FlowLayout();
		fl_panel_controls.setAlignment(FlowLayout.LEFT);
		JPanel panel_controls = new JPanel(fl_panel_controls);
//		btn_edit = new JButton("Edit");
//		btn_edit.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (getActiveTab().getTpane_browse().getSelectedIndex() == 1) {
//					String part = (String) getActiveTab().getA_lst_Particles().getSelectedValue();
//					if (part != null) {
//						ParticleSystemType ptype = Main.getParticleSystem(part);
//						if (ptype != null) {
//							Main.work_ParticleSystemTypes.put(part, ptype);
//						}
//					}
//				}else {
//					String fx = (String) getActiveTab().getA_lst_FX().getSelectedValue();
//					if (fx != null) {
//						FXListType ftype = Main.getFXList(fx);
//						if (ftype != null) {
//							Main.work_FXListTypes.put(fx, ftype);
//						}
//					}
//				}
//				browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
//				//renderer.editPanel.loadWorkingSet();
//			}
//		});
		
//		btn_reload = new JButton("Reload Files");
//		btn_reload.setEnabled(false);
//		btn_reload.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			
//			}
//		});
		//panel_controls.add(btn_reload);
		//panel_controls.add(btn_edit);
		
		btn_new = new JButton("new");
		btn_clone = new JButton("clone");
		
		lblFilter = new JLabel("Filter:");
		panel_controls.add(lblFilter);
		
		tfFilter.setHorizontalAlignment(SwingConstants.LEFT);
		tfFilter.setToolTipText("Filter by name");
		panel_controls.add(tfFilter);
		tfFilter.setColumns(16);
		tfFilter.getDocument().addDocumentListener(new DocumentListener(){
            @Override public void insertUpdate(DocumentEvent e) { filterAllLists(); }
            @Override public void removeUpdate(DocumentEvent e) { filterAllLists(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });
		
		separator = new JSeparator();
		panel_controls.add(separator);
		panel_controls.add(btn_new);
		panel_controls.add(btn_clone);
		
		btn_new.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getActiveTab().isFxMode()) {
					String name = JOptionPane.showInputDialog(BrowsePanel.this, "Create New FX with name:");
					if (name != null && !name.equals("")) {
						Undo.performFXOperation(null, name, "new FXList", OperationType.ADD); 
						setIgnoreChanges(true);
						FXListType type = new FXListType();
						Main.FXListTypes.put(name, type);
						Main.work_FXListTypes.put(name, type);
						Main.updateFXListNames();
						renderer.updateActiveFX(type, name);	
						browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
						browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
						getActiveTab().getActiveList().setSelectedValue(name, true);
						filterAllLists();
						setIgnoreChanges(false);
					}
				}else {
					String name = JOptionPane.showInputDialog(BrowsePanel.this, "Create New ParticleSystem with name:");
					if (name != null && !name.equals("")) {
						Undo.performParticleOperation(null, name, "new ParticleSystem", OperationType.ADD); 
						setIgnoreChanges(true);
						ParticleSystemType ptype = new ParticleSystemType();
						Main.ParticleSystemTypes.put(name, ptype);
						Main.work_ParticleSystemTypes.put(name, ptype);
						Main.updateParticleSystemNames();
						renderer.editPanel.particleEditPanel.fillParticleLists();
						FXListType type = FXListType.getFXTypeFromParticle(name);
						renderer.updateActiveFX(type, name);	
						browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
						browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
						getActiveTab().getActiveList().setSelectedValue(name, true);
						renderer.addNewParticleToLists(ptype, name);
						filterAllLists();
						setIgnoreChanges(false);
					}
				}
			}
		});
		btn_clone.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getActiveTab().isFxMode()) {
					String o_name = (String) renderer.editPanel.txtFXName.getValue();
					String name = JOptionPane.showInputDialog(BrowsePanel.this, "Clone FX '"+o_name+"', new name:");
					if (name != null && !name.equals("")) {
						Undo.performFXOperation(null, name, "cloned FXList", OperationType.ADD); 
						setIgnoreChanges(true);
						FXListType type = new FXListType(Main.activeFXListType);
						Main.FXListTypes.put(name, type);
						Main.work_FXListTypes.put(name, type);
						Main.updateFXListNames();
						renderer.updateActiveFX(type, name);
						browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
						browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
						getActiveTab().getActiveList().setSelectedValue(name, true);
						filterAllLists();
						setIgnoreChanges(false);
					}
				}else {
					String o_name = (String) renderer.editPanel.cb_ParticleSystems.getSelectedItem();
					String name = JOptionPane.showInputDialog(BrowsePanel.this, "Clone ParticleSystem '"+o_name+"', new name:");
					if (name != null && !name.equals("")) {
						Undo.performParticleOperation(null, name, "cloned ParticleSystem", OperationType.ADD); 
						setIgnoreChanges(true);
						ParticleSystemType ptype = new ParticleSystemType(Main.getParticleSystem(o_name));
						Main.ParticleSystemTypes.put(name, ptype);
						Main.work_ParticleSystemTypes.put(name, ptype);
						Main.updateParticleSystemNames();
						FXListType type = FXListType.getFXTypeFromParticle(name);
						renderer.updateActiveFX(type, name);	
						browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
						browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
						renderer.addNewParticleToLists(ptype, name);
						getActiveTab().getActiveList().setSelectedValue(name, true);
						filterAllLists();
						setIgnoreChanges(false);
					}
				}
			}
		});
		
		add(panel_controls, BorderLayout.NORTH);
		
		panel_All = new JPanel();
		
		panel_All.setLayout(new BorderLayout(0, 0));
		browse_All = new BrowseTab(renderer, 0);
		panel_All.add(browse_All, BorderLayout.CENTER);  //COMMENT-OUT THIS LINE TO GET WINDOW BUILDER WORKING
		
		tpane_BrowsingSet.addTab("Browse All", null, panel_All, null);
		
		panel_Working = new JPanel();
		panel_Working.setLayout(new BorderLayout(0, 0));
		browse_Working = new BrowseTab(renderer, 1);
		panel_Working.add(browse_Working, BorderLayout.CENTER);  //COMMENT-OUT THIS LINE TO GET WINDOW BUILDER WORKING
		
		tpane_BrowsingSet.addTab("Working Set", null, panel_Working, null);
		
	}	
	
	protected void filterAllLists() {
		String filter = tfFilter.getText();
		String selectedItem = (String) getActiveTab().getActiveList().getSelectedValue();
		setIgnoreChanges(true);
		boolean empty = filter.equals("");
		Pattern p = Pattern.compile(".*"+filter.toLowerCase()+".*");
		Matcher m = p.matcher("");
		
		//---
		//FX
		DefaultListModel<String> model = browse_All.lmodel_FX;
		model.removeAllElements();
		for (String s : Main.FXListNames) {
            if (empty || m.reset(s.toLowerCase()).matches()) {
            	model.addElement(s);
            }
        }
		model = browse_Working.lmodel_FX;
		model.removeAllElements();
		for (String s : Main.work_FXListTypes.keySet()) {
            if (empty || m.reset(s.toLowerCase()).matches()) {
            	model.addElement(s);  
            }
        }
		//Particles
		model = browse_All.lmodel_part;
		model.removeAllElements();
		for (String s : Main.ParticleSystemNames) {
            if (empty || m.reset(s.toLowerCase()).matches()) {
            	model.addElement(s);
            }
        }
		model = browse_Working.lmodel_part;
		model.removeAllElements();
		for (String s : Main.work_ParticleSystemTypes.keySet()) {
            if (empty || m.reset(s.toLowerCase()).matches()) {
            	model.addElement(s);
            }
        }
		 
		setIgnoreChanges(false);
		model = getActiveTab().getActiveListModel();
		if (model.contains(selectedItem)) {
			getActiveTab().getActiveList().setSelectedValue(selectedItem, true);
		}
		getActiveTab().repaint();
		getActiveTab().revalidate();

		
	}
	
	
	protected void filterActiveList_OFF() {
        String filter = tfFilter.getText();
        Collection<String> list;
        if (getActiveTab() == browse_All) {
        	if (browse_All.isFxMode()) list = Main.FXListNames;
			else list = Main.ParticleSystemNames;
        }else {
        	if (browse_Working.isFxMode()) list = Main.work_FXListTypes.keySet();
        	else list = Main.work_ParticleSystemTypes.keySet();
        }
        JList jlist = getActiveTab().getActiveList();
        filterList(jlist, filter, list);
	}


	protected void filterList(JList list, String filter, Collection<String> defaultValues) {
		if (!(list.getModel() instanceof DefaultListModel)) return; //Dirty Hack
		DefaultListModel<String> model = (DefaultListModel<String>)(list.getModel());
		String selectedItem = (String) list.getSelectedValue();
		setIgnoreChanges(true);
		boolean empty = (filter.equals("")); 
		filter = filter.toLowerCase();
		model.removeAllElements();
		 for (String s : defaultValues) {
            if (empty || s.toLowerCase().contains(filter)) {
            	model.addElement(s);           
            }
        }
		setIgnoreChanges(false);
		if (!model.isEmpty()) {
			if (selectedItem != null && model.contains(selectedItem)) {
				list.setSelectedValue(selectedItem, true);
			}
		}
	}
	
	public void fillLists() {
		setIgnoreChanges(true);
		browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
		browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
		setIgnoreChanges(false);
	}

	public BrowseTab getActiveTab() {
		if (this.tpane_BrowsingSet.getSelectedIndex() == 1) return browse_Working;
		else return browse_All;
	}

	public void setIgnoreChanges(boolean b) {
		browse_All.ignoreChanges = b;
		browse_Working.ignoreChanges = b;
	}
	
//	public void updateSelection() {
//		boolean workingSet = (this.tpane_BrowsingSet.getSelectedIndex() == 1);
//		boolean particleMode = getActiveTab().getTpane_browse().getSelectedIndex() == 1;
//		
//		renderer.editPanel.setEditingEnabled(workingSet);
//		
//	}
	
	public JTextField getTfFilter() {
		return tfFilter;
	}
}
