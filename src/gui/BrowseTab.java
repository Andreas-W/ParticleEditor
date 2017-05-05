package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entitytypes.FXListType;
import entitytypes.ParticleSystemType;
import main.Main;
import main.Renderer;

public class BrowseTab extends JPanel {

	public Renderer renderer;
	JTabbedPane tpane_browse;
	private JList a_lst_Particles;
	private JList a_lst_FX;
	
	public DefaultListModel<String> lmodel_part = new DefaultListModel<String>();
	public DefaultListModel<String> lmodel_FX = new DefaultListModel<String>();

	private JScrollPane sp_Part;
	private JScrollPane sp_FX;

	public int tabIndex;
	public boolean ignoreChanges;
	
	private boolean init = false;

	/**
	 * Create the panel.
	 */
	public BrowseTab(Renderer rend, int tab) {
		this.renderer = rend;
		this.tabIndex = tab;

		setLayout(new BorderLayout(0, 0));
		tpane_browse = new JTabbedPane(JTabbedPane.TOP);
		add(tpane_browse, BorderLayout.CENTER);

		tpane_browse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
//				System.out.println("StateChanged");
				if (init) {
//					String filter = renderer.browsePanel.getTfFilter().getText();
//					if (BrowseTab.this == renderer.browsePanel.browse_All) {
//						if (isFxMode()) renderer.browsePanel.filterList(a_lst_FX, filter, Main.FXListNames);
//						else renderer.browsePanel.filterList(a_lst_Particles, filter, Main.ParticleSystemNames);
//					} else {
//						if (isFxMode()) renderer.browsePanel.filterList(a_lst_FX, filter, Main.work_FXListTypes.keySet());
//						else renderer.browsePanel.filterList(a_lst_Particles, filter, Main.work_ParticleSystemTypes.keySet());
//					}
					updateSelection();
				}
			}
		});

		sp_FX = new JScrollPane();
		tpane_browse.addTab("FX Lists", null, sp_FX, null);

		a_lst_FX = new JList();
		a_lst_FX.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// System.out.println("This.Parent:"+BrowseTab.this.getParent()
				// +
				// " getSelectedComponent():"+tpane_browse.getSelectedComponent());
				if (!ignoreChanges
						&& !e.getValueIsAdjusting()
						&& e.getFirstIndex() != -1
						&& e.getLastIndex() != -1
						&& tpane_browse.getSelectedComponent() == sp_FX
						&& renderer.browsePanel.tpane_BrowsingSet
								.getSelectedIndex() == tabIndex) {
					// System.out.println("Selection Changed in FX_"+tabIndex
					// +" isAdjusting="+e.getValueIsAdjusting());
					String val = (String) a_lst_FX.getSelectedValue();
					FXListType type = Main.getFXList(val);
					renderer.updateActiveFX(type, val);
					renderer.mainWindow.reset = true;
				}
			}
		});
		sp_FX.setViewportView(a_lst_FX);

		sp_Part = new JScrollPane();
		tpane_browse.addTab("Particle Systems", null, sp_Part, null);

		a_lst_Particles = new JList();
		a_lst_Particles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!ignoreChanges
						&& !e.getValueIsAdjusting()
						&& e.getFirstIndex() != -1
						&& e.getLastIndex() != -1
						&& tpane_browse.getSelectedComponent() == sp_Part
						&& renderer.browsePanel.tpane_BrowsingSet
								.getSelectedIndex() == tabIndex) {
					// System.out.println("Selection Changed in Particles_"+tabIndex);
					String val = (String) a_lst_Particles.getSelectedValue();
					FXListType type = FXListType.getFXTypeFromParticle(val);
					renderer.updateActiveFX(type, val);
					renderer.updateActiveParticle(Main.getParticleSystem(val), val);
					renderer.mainWindow.reset = true;
				}
			}
		});
		sp_Part.setViewportView(a_lst_Particles);
		
		init = true;
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
		// FX List list
		ArrayList<String> list = new ArrayList<String>();
		lmodel_FX.removeAllElements();
		list.addAll(FXListNames);
		Collections.sort(list);
		for (String fx : list) {
			lmodel_FX.addElement(fx);
		}
		a_lst_FX.setModel(lmodel_FX);
		a_lst_FX.setSelectedIndex(0);
		// ParticleSystem list
		lmodel_part.removeAllElements();
		list = new ArrayList<String>();
		list.addAll(ParticleNames);
		Collections.sort(list);
		for (String part : list) {
			lmodel_part.addElement(part);
		}
		a_lst_Particles.setModel(lmodel_part);
		a_lst_Particles.setSelectedIndex(0);
	}

	public boolean isFxMode() {
		return (this.tpane_browse.getSelectedComponent() == sp_FX);
	}

	public JList getActiveList() {
		if (isFxMode())
			return a_lst_FX;
		else
			return a_lst_Particles;
	}

	public DefaultListModel<String> getActiveListModel() {
		if (isFxMode())
			return lmodel_FX;
		else
			return lmodel_part;
	}

	public void updateSelection() {
		if (this.isFxMode()) {
			if (a_lst_FX.getSelectedIndex() != -1) {
				String fx = (String) this.a_lst_FX.getSelectedValue();
				FXListType type = Main.getFXList(fx);
				renderer.updateActiveFX(type,  fx);
				renderer.mainWindow.reset = true;
			}
		}else {
			if (a_lst_Particles.getSelectedIndex() != -1) {
				String ps = (String) this.a_lst_Particles.getSelectedValue();
				ParticleSystemType type = Main.getParticleSystem(ps);
				FXListType ftype = FXListType.getFXTypeFromParticle(ps);
				renderer.updateActiveFX(ftype, ps);
				renderer.updateActiveParticle(type,  ps);
				renderer.mainWindow.reset = true;
			}
		}
	}
}
