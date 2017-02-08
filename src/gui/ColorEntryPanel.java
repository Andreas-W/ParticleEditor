package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.alphaEntry;
import entitytypes.ParticleSystemType.colorEntry;
import main.Renderer;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;

import java.awt.Color;

import javax.swing.border.MatteBorder;

public class ColorEntryPanel extends JPanel {
	
	public colorEntry cEntry;
	
	public JButton btn_Add;
	public JButton btn_Remove;
	public JButton btn_moveUp;
	public JButton btn_moveDown;

	public Renderer renderer;
	private ValueTextField tf_frame;
	
	public PropertyChangeListener valuesChangedListener;
	private boolean ignoreChanges = false;
	private JButton btnColor;
	
	/**
	 * Create the panel.
	 */
	public ColorEntryPanel(Renderer rend, colorEntry entry) {
		
		valuesChangedListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				//System.out.println("PropertyChanged: "+((Component)e.getSource()).getName() + " - "+e.getPropertyName());
				if (e.getPropertyName().equals("value") && cEntry != null && !ignoreChanges) {
					updateValues();
//					renderer.editPanel.FXvaluesChanged();
					renderer.mainWindow.reset = true;
				}
			}
		};
		
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		this.cEntry = entry;
		this.renderer = rend;
		
		JLabel lblColor = new JLabel("Color:");
		add(lblColor);
		
		
		btnColor = new JButton("");
		btnColor.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnColor.setMargin(new Insets(0,0,0,0));
		btnColor.setPreferredSize(new Dimension(24,24));
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JColorChooser jcol = new JColorChooser(btnColor.getBackground());
				//Remove unncesseary tabs
				AbstractColorChooserPanel[] panels = jcol.getChooserPanels();
				for (AbstractColorChooserPanel accp : panels) {
				   if(!(accp.getDisplayName().equals("HSV") || accp.getDisplayName().equals("RGB")) ) {
				      jcol.removeChooserPanel(accp);
				   } 
				}
				//---
				JDialog jdia = JColorChooser.createDialog(ColorEntryPanel.this, "Choose Particle Color", true, jcol, null, null);
				jdia.setVisible(true);
				Color c = jcol.getColor();
				if (c!= null) {
					btnColor.setBackground(c);
				}		
				updateValues();
//				renderer.editPanel.FXvaluesChanged();
				renderer.mainWindow.reset = true;
			}
		});
		add(btnColor);
		
		
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
				renderer.editPanel.particleEditPanel.newColorEntry(cEntry);
				renderer.editPanel.particleEditPanel.updateColorPanels();
				renderer.editPanel.particleEditPanel.colorEntriesFromList();
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
				renderer.editPanel.particleEditPanel.panel_ColorEntries.remove(ColorEntryPanel.this);
				renderer.editPanel.particleEditPanel.removeColorEntry(cEntry);
				renderer.editPanel.particleEditPanel.updateColorPanels();
				renderer.editPanel.particleEditPanel.colorEntriesFromList();
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
				renderer.editPanel.particleEditPanel.moveColorEntry(ColorEntryPanel.this, -1);
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
				renderer.editPanel.particleEditPanel.moveColorEntry(ColorEntryPanel.this, 1);
				renderer.editPanel.updateParticleCode();
				renderer.editPanel.particleEditPerformed();
			}
		});
		panel.add(btn_moveDown, BorderLayout.SOUTH);
		btn_moveDown.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btn_moveDown.setPreferredSize(new Dimension(20, 14));
		btn_moveDown.setMargin(new Insets(0, 0, 0, 0));
		tf_frame.addPropertyChangeListener(valuesChangedListener);
		
		loadValues();
	}

	protected void updateValues() {
		Color c = btnColor.getBackground();
		this.cEntry.r = c.getRed();
		this.cEntry.g = c.getGreen();
		this.cEntry.b = c.getBlue();
		this.cEntry.frame = (int) tf_frame.getValue();
	}

	private void loadValues() {
		ignoreChanges = true;
		Color c = new Color(cEntry.r, cEntry.g, cEntry.b);
		btnColor.setBackground(c);
		tf_frame.setValue(this.cEntry.frame);
		ignoreChanges = false;
	}

	
//	protected void enableButtons() {
//		int size = renderer.editPanel.particleEditPanel.alphaEntryList.size();
//		btn_Remove.setEnabled(size > 1);
//		btn_Add.setEnabled(size < 9);
//	}

}
