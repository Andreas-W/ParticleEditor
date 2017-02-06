package gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
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

public class BrowsePanel extends JPanel {
	
	public Renderer renderer;

	private JPanel panel_All;
	public JTabbedPane tpane_BrowsingSet;
	private JPanel panel_Working;
	private JButton btn_edit;
	private JButton reload_files;
	
	public BrowseTab browse_All;
	public BrowseTab browse_Working;
	private JButton btn_reload;
	

	/**
	 * Create the panel.
	 */
	public BrowsePanel(Renderer rend) {
		this.renderer = rend;
		setLayout(new BorderLayout(0, 0));
		
		tpane_BrowsingSet = new JTabbedPane(JTabbedPane.TOP);
		add(tpane_BrowsingSet, BorderLayout.CENTER);
		
		JPanel panel_controls = new JPanel(new FlowLayout());
		btn_edit = new JButton("Edit");
		btn_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (getActiveTab().getTpane_browse().getSelectedIndex() == 1) {
					String part = (String) getActiveTab().getA_lst_Particles().getSelectedValue();
					if (part != null) {
						ParticleSystemType ptype = Main.getParticleSystem(part);
						if (ptype != null) {
							Main.work_ParticleSystemTypes.put(part, ptype);
						}
					}
				}else {
					String fx = (String) getActiveTab().getA_lst_FX().getSelectedValue();
					if (fx != null) {
						FXListType ftype = Main.getFXList(fx);
						if (ftype != null) {
							Main.work_FXListTypes.put(fx, ftype);
						}
					}
				}
				browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
				//renderer.editPanel.loadWorkingSet();
			}
		});
		
		btn_reload = new JButton("Reload Files");
		btn_reload.setEnabled(false);
//		btn_reload.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			
//			}
//		});
		panel_controls.add(btn_reload);
		panel_controls.add(btn_edit);
		add(panel_controls, BorderLayout.NORTH);
		
		panel_All = new JPanel();
		tpane_BrowsingSet.addTab("Browse All", null, panel_All, null);
		panel_All.setLayout(new BorderLayout(0, 0));
		browse_All = new BrowseTab(renderer, 0);
		panel_All.add(browse_All, BorderLayout.CENTER);  //COMMENT-OUT THIS LINE TO GET WINDOW BUILDER WORKING
		
		panel_Working = new JPanel();
		tpane_BrowsingSet.addTab("Working Set", null, panel_Working, null);
		panel_Working.setLayout(new BorderLayout(0, 0));
		browse_Working = new BrowseTab(renderer, 1);
		panel_Working.add(browse_Working, BorderLayout.CENTER);  //COMMENT-OUT THIS LINE TO GET WINDOW BUILDER WORKING
		panel_Working.setEnabled(false);
		
	}	
	
	public BrowseTab getActiveTab() {
		if (this.tpane_BrowsingSet.getSelectedIndex() == 1) return browse_Working;
		else return browse_All;
	}
	
	public void updateSelection() {
		boolean workingSet = (this.tpane_BrowsingSet.getSelectedIndex() == 1);
		boolean particleMode = getActiveTab().getTpane_browse().getSelectedIndex() == 1;
		
		renderer.editPanel.setEditingEnabled(workingSet);
		
	}
	
}
