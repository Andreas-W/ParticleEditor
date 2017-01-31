package gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import main.Renderer;

public class BrowsePanel extends JPanel {
	private JList a_lst_FX;
	
	public Renderer renderer;

	/**
	 * Create the panel.
	 */
	public BrowsePanel(Renderer rend) {
		this.renderer = rend;
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("All FX", null, scrollPane, null);
		
		a_lst_FX = new JList();
		a_lst_FX.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				renderer.mainWindow.reset = true;
			}
		});
		scrollPane.setViewportView(a_lst_FX);

	}

	public JList getA_lst_FX() {
		return a_lst_FX;
	}
	
	public void fillList(Set<String> FXListNames) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(FXListNames);
		Collections.sort(list);
		for (String fx : list) {
			listModel.addElement(fx);
		}
		a_lst_FX.setModel(listModel);
		a_lst_FX.setSelectedIndex(0);
	}
}
