package gui.filter;

import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FilterDialog extends JDialog{
	
	private boolean init = false;
	public FilterPanel filterPanel;
	
	public FilterDialog(FilterPanel fpanel) {
		this.setModal(true);
		this.setPreferredSize(new Dimension(450, 350));
		this.setMinimumSize(new Dimension(450, 350));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (init) filterPanel.accept();
				closeDialog();
			}
		});
		panel.add(btnAccept);
		
		JButton btnRevert = new JButton("Revert");
		btnRevert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (init) filterPanel.revert();
				closeDialog();
			}
		});
		panel.add(btnRevert);
		
		this.filterPanel = fpanel;
		getContentPane().add(filterPanel, BorderLayout.CENTER);
		
		this.addWindowListener(new WindowAdapter() 
		{
		  public void windowClosing(WindowEvent e)
		  {
		    filterPanel.revert();
		  }
		});
		
		init = true;
	}

	protected void closeDialog() {
		this.setVisible(false);
		this.dispose();
	}
}
