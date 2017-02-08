package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.alphaEntry;
import main.Renderer;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AlphaEntryPanel extends JPanel {
	
	public alphaEntry aEntry;
	
	public JButton btn_Add;
	public JButton btn_Remove;
	public JButton btn_moveUp;
	public JButton btn_moveDown;

	public Renderer renderer;
	private ValueTextField tf_alphaMin;
	private ValueTextField tf_alphaMax;
	private ValueTextField tf_frame;
	
	public PropertyChangeListener valuesChangedListener;
	private boolean ignoreChanges = false;
	
	/**
	 * Create the panel.
	 */
	public AlphaEntryPanel(Renderer rend, alphaEntry entry) {
		
		valuesChangedListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				//System.out.println("PropertyChanged: "+((Component)e.getSource()).getName() + " - "+e.getPropertyName());
				if (e.getPropertyName().equals("value") && aEntry != null && !ignoreChanges) {
					updateValues();
//					renderer.editPanel.FXvaluesChanged();
					renderer.mainWindow.reset = true;
				}
			}
		};
		
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		this.aEntry = entry;
		this.renderer = rend;
		
		JLabel lblAlpha = new JLabel("Alpha:");
		add(lblAlpha);
		
		tf_alphaMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		add(tf_alphaMin);
		tf_alphaMin.setColumns(3);
		
		tf_alphaMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		add(tf_alphaMax);
		tf_alphaMax.setColumns(3);
		
		JLabel lblTime = new JLabel("Time:");
		add(lblTime);
		
		tf_frame = new ValueTextField(ValueTextField.VALUE_INT);
		add(tf_frame);
		tf_frame.setColumns(3);
		
		JPanel panel_23 = new JPanel();
		add(panel_23);
		panel_23.setBorder(null);
		FlowLayout fl_panel_23 = new FlowLayout(FlowLayout.CENTER, 0, 0);
		panel_23.setLayout(fl_panel_23);
		
		//----CONTROLS-----
		
		btn_Add = new JButton("+");
		btn_Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				renderer.editPanel.particleEditPanel.newAlphaEntry(aEntry);
				renderer.editPanel.particleEditPanel.updateAlphaPanels();
				renderer.editPanel.particleEditPanel.alphaEntriesFromList();
				renderer.editPanel.updateParticleCode();
				renderer.editPanel.particleEditPerformed();
			}
		});
		
		
		btn_Add.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_23.add(btn_Add);
		btn_Add.setPreferredSize(new Dimension(20,20));
		btn_Add.setMargin(new java.awt.Insets(0, 0, 0, 0));
		
		btn_Remove = new JButton("-");
		btn_Remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renderer.editPanel.particleEditPanel.panel_AlphaEntries.remove(AlphaEntryPanel.this);
				renderer.editPanel.particleEditPanel.removeAlphaEntry(aEntry);
				renderer.editPanel.particleEditPanel.updateAlphaPanels();
				renderer.editPanel.particleEditPanel.alphaEntriesFromList();
				renderer.editPanel.updateParticleCode();
				renderer.editPanel.particleEditPerformed();
			}
		});
		panel_23.add(btn_Remove);
		btn_Remove.setPreferredSize(new Dimension(20,20));
		btn_Remove.setMargin(new java.awt.Insets(0, 0, 0, 0));
		
		JPanel panel = new JPanel();
		panel_23.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		btn_moveUp = new JButton("\u02C4");
		btn_moveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renderer.editPanel.particleEditPanel.moveAlphaEntry(AlphaEntryPanel.this, -1);
				renderer.editPanel.updateParticleCode();
				renderer.editPanel.particleEditPerformed();
			}
		});
		panel.add(btn_moveUp, BorderLayout.NORTH);
		btn_moveUp.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_moveUp.setPreferredSize(new Dimension(20, 14));
		btn_moveUp.setMargin(new Insets(0, 0, 0, 0));
		
		btn_moveDown = new JButton("\u02C5");
		btn_moveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renderer.editPanel.particleEditPanel.moveAlphaEntry(AlphaEntryPanel.this, 1);
				renderer.editPanel.updateParticleCode();
				renderer.editPanel.particleEditPerformed();
			}
		});
		panel.add(btn_moveDown, BorderLayout.SOUTH);
		btn_moveDown.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_moveDown.setPreferredSize(new Dimension(20, 14));
		btn_moveDown.setMargin(new Insets(0, 0, 0, 0));
		
		tf_alphaMax.addPropertyChangeListener(valuesChangedListener);
		tf_alphaMin.addPropertyChangeListener(valuesChangedListener);
		tf_frame.addPropertyChangeListener(valuesChangedListener);
		
		loadValues();
	}

	protected void updateValues() {
		this.aEntry.alpha[0] = (float) tf_alphaMin.getValue();
		this.aEntry.alpha[1] = (float) tf_alphaMax.getValue();
		this.aEntry.frame = (int) tf_frame.getValue();
	}

	private void loadValues() {
		ignoreChanges = true;
		tf_alphaMin.setValue(this.aEntry.alpha[0]);
		tf_alphaMax.setValue(this.aEntry.alpha[1]);
		tf_frame.setValue(this.aEntry.frame);
		ignoreChanges = false;
	}

	
//	protected void enableButtons() {
//		int size = renderer.editPanel.particleEditPanel.alphaEntryList.size();
//		btn_Remove.setEnabled(size > 1);
//		btn_Add.setEnabled(size < 9);
//	}

}
