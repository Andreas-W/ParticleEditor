package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entitytypes.FXListType;
import main.Main;
import main.Renderer;

public class BrowseTab extends JPanel {

	public Renderer renderer;
	private JTabbedPane tpane_browse;
	private JList a_lst_Particles;
	private JList a_lst_FX;
	
	private JScrollPane sp_Part;
	private JScrollPane sp_FX;
	
	public int tabIndex;
	
	/**
	 * Create the panel.
	 */
	public BrowseTab(Renderer rend, int tab) {
		this.renderer = rend;
		this.tabIndex = tab;
		
		setLayout(new BorderLayout(0,0));
		tpane_browse = new JTabbedPane(JTabbedPane.TOP);
		add(tpane_browse, BorderLayout.CENTER);
		
		sp_FX = new JScrollPane();
		tpane_browse.addTab("FX Lists", null, sp_FX, null);
		
		a_lst_FX = new JList();
		a_lst_FX.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//System.out.println("This.Parent:"+BrowseTab.this.getParent() + " getSelectedComponent():"+tpane_browse.getSelectedComponent());
				if (!e.getValueIsAdjusting() && tpane_browse.getSelectedComponent() == sp_FX && renderer.browsePanel.tpane_BrowsingSet.getSelectedIndex() == tabIndex) {
					//System.out.println("Selection Changed in FX_"+tabIndex +" isAdjusting="+e.getValueIsAdjusting());
					String val = (String)a_lst_FX.getSelectedValue();
					Main.activeFXListType = Main.getFXList(val);
					//Main.activeParticleSystemType = null;
					renderer.mainWindow.reset = true;
					if (renderer.editPanel != null) {
						renderer.editPanel.setEditingEnabled(tpane_browse.getSelectedIndex()==0);
						renderer.editPanel.getTxtFXName().setText(val);
						renderer.editPanel.selectionChanged();
					}
				}
			}
		});
		sp_FX.setViewportView(a_lst_FX);
		
		sp_Part = new JScrollPane();
		tpane_browse.addTab("Particle Systems", null, sp_Part, null);
		
		a_lst_Particles = new JList();
		a_lst_Particles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && tpane_browse.getSelectedComponent() == sp_Part && renderer.browsePanel.tpane_BrowsingSet.getSelectedIndex() == tabIndex) {
					//System.out.println("Selection Changed in Particles_"+tabIndex);
					String val = (String)a_lst_Particles.getSelectedValue();
					Main.activeFXListType = FXListType.getFXTypeFromParticle(val);
					Main.activeParticleSystemType = Main.getParticleSystem(val);
					renderer.mainWindow.reset = true;
					if (renderer.editPanel != null) {
						renderer.editPanel.setEditingEnabled(tpane_browse.getSelectedIndex()==0);
						renderer.editPanel.getTxtFXName().setText("<"+val+">");
						renderer.editPanel.selectionChanged();
					}
				}
			}
		});
		sp_Part.setViewportView(a_lst_Particles);
	}

	
	public JTabbedPane getTpane_browse() {
		return tpane_browse;
	}
	public JList getA_lst_Particles() {
		return a_lst_Particles;
	}

	public JList getA_lst_FX() {
		return a_lst_FX;
	}
	
	public void fillList(Set<String> FXListNames, Set<String> ParticleNames) {
		//FX List list
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(FXListNames);
		Collections.sort(list);
		for (String fx : list) {
			listModel.addElement(fx);
		}
		a_lst_FX.setModel(listModel);
		a_lst_FX.setSelectedIndex(0);
		//ParticleSystem list
		listModel = new DefaultListModel<String>();
		list = new ArrayList<String>();
		list.addAll(ParticleNames);
		Collections.sort(list);
		for (String fx : list) {
			listModel.addElement(fx);
		}
		a_lst_Particles.setModel(listModel);
		a_lst_Particles.setSelectedIndex(0);
	}
}
