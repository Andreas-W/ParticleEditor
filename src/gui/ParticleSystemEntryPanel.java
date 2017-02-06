package gui;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JSeparator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFormattedTextField;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JToggleButton;

import main.Main;
import main.Renderer;
import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.ParticleSystemType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.ImageIcon;

import java.awt.Font;

public class ParticleSystemEntryPanel extends JPanel {
	
	private JComboBox cb_particleName;
	private ValueTextField tf_Offset_X;
	private ValueTextField tf_Offset_Y;
	private ValueTextField tf_Offset_Z;
	private ValueTextField tf_DelayMin;
	private ValueTextField tf_DelayMax;
	private ValueTextField tf_Count;
	private ValueTextField tf_RadiusMin;
	private ValueTextField tf_RadiusMax;
	private ValueTextField tf_HeightMin;
	private ValueTextField tf_HeightMax;
	private JToggleButton tglbtnOptions;
	private JPopupMenu pm_optionsMenu;
	private JCheckBox chckbxmntmUsecallersradius;
	private JCheckBox chckbxmntmOrienttoobject;
	private JCheckBox chckbxmntmRicochet;
	private JCheckBox chckbxmntmCreateatgroundheight;
	private JCheckBox chckbxShowParticle;
	
	private PropertyChangeListener FXvaluesChangedListener;
	private ItemListener FXcheckboxChangedListener;
	
	public Renderer renderer;
	
	private ParticleSystemEntry entry;
	
	private boolean ignoreChanges = false; //used to temporarily disable changeListener
	protected String prevParticleSelection;
	private JButton btnDelete;

	/**
	 * Create the panel.
	 */
	public ParticleSystemEntryPanel(Renderer rend, ParticleSystemEntry entry) {
		this(rend);
		this.entry = entry;
		loadValues(entry);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ParticleSystemEntryPanel(Renderer rend) {
		this.renderer = rend;
		setPreferredSize(new Dimension(250, 160));
		setMaximumSize(new Dimension(1000, 160));
		setLayout(new BorderLayout(0, 0));
		
		FXvaluesChangedListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				//System.out.println("PropertyChanged: "+((Component)e.getSource()).getName() + " - "+e.getPropertyName());
				if (e.getPropertyName().equals("value") && entry != null && !ignoreChanges) {
					updateEntryValues();
					renderer.editPanel.FXvaluesChanged();
					renderer.mainWindow.reset = true;
				}
			}
		};
		FXcheckboxChangedListener = new ItemListener() {		
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (entry != null && !ignoreChanges) {
					updateEntryValues();
					renderer.editPanel.FXvaluesChanged();
					//renderer.mainWindow.reset = true;
				}
			}
		};
		
		JPanel panel_pAttributes = new JPanel();
		add(panel_pAttributes, BorderLayout.CENTER);
		panel_pAttributes.setLayout(new BoxLayout(panel_pAttributes, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEADING);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_pAttributes.add(panel);
		
		chckbxShowParticle = new JCheckBox("", true);
		chckbxShowParticle.setToolTipText("Show this ParticleSystem");
		panel.add(chckbxShowParticle);
		chckbxShowParticle.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				entry.setVisible(chckbxShowParticle.isSelected());
				renderer.mainWindow.reset = true;
			}
		});
		
		cb_particleName = new JComboBox(Main.ParticleSystemNames.toArray());
		cb_particleName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (entry != null && !ignoreChanges) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						//System.out.println("SelectionIndex="+cb_particleName.getSelectedIndex());
						if (cb_particleName.getSelectedIndex() == -1) {
//							int response = JOptionPane.showConfirmDialog(null, "Create ParticleSystem '"+(String)cb_particleName.getSelectedItem()+"'?", "Confirm",
//							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//							if (response == JOptionPane.YES_OPTION) {
//								ignoreChanges = true;
//								String pname = (String)cb_particleName.getSelectedItem();
//								ParticleSystemType ptype = new ParticleSystemType();
//								Main.ParticleSystemTypes.put(pname, ptype);
//								entry.Name = pname;
//								Main.updateParticleSystemNames();
//								cb_particleName.removeAllItems();
//								fillParticleNames();
//								cb_particleName.setSelectedItem(pname);
//								ignoreChanges = false;
//							}else {
//								ignoreChanges = true;
//								cb_particleName.setSelectedItem(prevParticleSelection);
//								ignoreChanges = false;
//							}
							
							NewParticleDialog dialog = new NewParticleDialog(renderer, (String)e.getItem());
							int result = dialog.showDialog();
							if (result == 1) {
								ignoreChanges = true;
								String pname = dialog.getName();
								ParticleSystemType ptype;
								if (dialog.getCloneFrom() != null) {
									ParticleSystemType other = Main.getParticleSystem(dialog.getCloneFrom());
									if (other != null) {
										ptype = new ParticleSystemType(other);
									}else{
										ptype = new ParticleSystemType();
									}
								}else {
									ptype = new ParticleSystemType();
								}
								Main.ParticleSystemTypes.put(pname, ptype);
								entry.Name = pname;
								Main.updateParticleSystemNames();
								cb_particleName.removeAllItems();
								fillParticleNames();
								cb_particleName.setSelectedItem(pname);
								ignoreChanges = false;
								updateEntryValues();
								//renderer.editPanel.FXvaluesChanged();
								renderer.editPanel.selectionChanged();
								renderer.mainWindow.reset = true;
							}else {
								ignoreChanges = true;
								cb_particleName.setSelectedItem(prevParticleSelection);
								ignoreChanges = false;
							}
						}else {
							updateEntryValues();
							renderer.editPanel.selectionChanged();
							renderer.mainWindow.reset = true;
						}
					}else if (e.getStateChange() == ItemEvent.DESELECTED){
						prevParticleSelection = (String)e.getItem();
					}
				}
			}
		});
		cb_particleName.setEditable(true);
		cb_particleName.setMinimumSize(new Dimension(150, 20));
		cb_particleName.setMaximumSize(new Dimension(250, 20));
		panel.add(cb_particleName);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEADING);
		panel_4.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_pAttributes.add(panel_4);
		
		JLabel lblOffset = new JLabel("Offset:");
		panel_4.add(lblOffset);
		
		tf_Offset_X = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tf_Offset_X.setColumns(5);
		panel_4.add(tf_Offset_X);
		
		tf_Offset_Y = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tf_Offset_Y.setColumns(5);
		panel_4.add(tf_Offset_Y);
		
		tf_Offset_Z = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tf_Offset_Z.setColumns(5);
		panel_4.add(tf_Offset_Z);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		panel_2.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_pAttributes.add(panel_2);
		
		JLabel lblDelay = new JLabel("Delay:");
		panel_2.add(lblDelay);
		lblDelay.setAlignmentY(Component.TOP_ALIGNMENT);
		
		tf_DelayMin = new ValueTextField(ValueTextField.VALUE_INT);
		tf_DelayMin.setColumns(8);
		panel_2.add(tf_DelayMin);
		
		tf_DelayMax = new ValueTextField(ValueTextField.VALUE_INT);
		tf_DelayMax.setColumns(8);
		panel_2.add(tf_DelayMax);
		
		JPanel panel_3 = new JPanel();
		panel_3.setAlignmentY(Component.TOP_ALIGNMENT);
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEADING);
		panel_pAttributes.add(panel_3);
		
		JLabel lblCount = new JLabel("Count:");
		panel_3.add(lblCount);
		
		tf_Count = new ValueTextField(ValueTextField.VALUE_INT);
		tf_Count.setColumns(4);
		panel_3.add(tf_Count);
		
		JSeparator separator_1 = new JSeparator();
		panel_3.add(separator_1);
		
		JLabel lblRadius = new JLabel("Radius:");
		panel_3.add(lblRadius);
		
		tf_RadiusMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_3.add(tf_RadiusMin);
		tf_RadiusMin.setColumns(5);
		
		tf_RadiusMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_3.add(tf_RadiusMax);
		tf_RadiusMax.setColumns(5);
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		FlowLayout flowLayout_3 = (FlowLayout) panel_1.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEADING);
		panel_pAttributes.add(panel_1);
		
		JLabel lblHeight = new JLabel("Height:");
		panel_1.add(lblHeight);
		
		tf_HeightMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tf_HeightMin.setColumns(5);
		panel_1.add(tf_HeightMin);
		
		tf_HeightMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tf_HeightMax.setColumns(5);
		panel_1.add(tf_HeightMax);
		
		JSeparator separator_2 = new JSeparator();
		panel_1.add(separator_2);
		
		tglbtnOptions = new JToggleButton("Options");
		panel_1.add(tglbtnOptions);
		
		btnDelete = new JButton("");
		btnDelete.setToolTipText("Remove this ParticleSystem entry");
		panel_1.add(btnDelete);
		btnDelete.setBorder(null); 
		btnDelete.setIcon(new ImageIcon(ParticleSystemEntryPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
		btnDelete.setContentAreaFilled(false); 
		btnDelete.setMargin(new Insets(0, 0, 0, 0));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renderer.editPanel.removeParticleEntry(entry);
				JPanel pp = renderer.editPanel.getPanel_ParticleEntries();
				pp.remove(ParticleSystemEntryPanel.this);
				pp.validate();
		        pp.repaint();
			}
		});
		
		pm_optionsMenu = new JPopupMenu("Options");
		chckbxmntmUsecallersradius = new JCheckBox("UseCallersRadius");
		chckbxmntmOrienttoobject = new JCheckBox("OrientToObject");
		chckbxmntmRicochet = new JCheckBox("Ricochet");
		chckbxmntmCreateatgroundheight = new JCheckBox("CreateAtGroundHeight");
		pm_optionsMenu.add(chckbxmntmUsecallersradius);
		pm_optionsMenu.add(chckbxmntmOrienttoobject);
		pm_optionsMenu.add(chckbxmntmRicochet);
		pm_optionsMenu.add(chckbxmntmCreateatgroundheight);
		tglbtnOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (tglbtnOptions.isSelected()) {
                    pm_optionsMenu.show(tglbtnOptions, 0, tglbtnOptions.getBounds().height);
                } else {
                    pm_optionsMenu.setVisible(false);
                }
            }
        });
		
		JSeparator separator = new JSeparator();
		add(separator, BorderLayout.SOUTH);
		
		tf_Offset_X.addPropertyChangeListener(FXvaluesChangedListener);
		tf_Offset_Y.addPropertyChangeListener(FXvaluesChangedListener);
		tf_Offset_Z.addPropertyChangeListener(FXvaluesChangedListener);		
		tf_Count.addPropertyChangeListener(FXvaluesChangedListener);
		tf_DelayMin.addPropertyChangeListener(FXvaluesChangedListener);
		tf_DelayMax.addPropertyChangeListener(FXvaluesChangedListener);
		tf_RadiusMin.addPropertyChangeListener(FXvaluesChangedListener);
		tf_RadiusMax.addPropertyChangeListener(FXvaluesChangedListener);
		tf_HeightMin.addPropertyChangeListener(FXvaluesChangedListener);
		tf_HeightMax.addPropertyChangeListener(FXvaluesChangedListener);
		
		chckbxmntmCreateatgroundheight.addItemListener(FXcheckboxChangedListener);
		chckbxmntmOrienttoobject.addItemListener(FXcheckboxChangedListener);
		chckbxmntmRicochet.addItemListener(FXcheckboxChangedListener);
		chckbxmntmUsecallersradius.addItemListener(FXcheckboxChangedListener);
		
		//cb_particleName.addPropertyChangeListener(FXvaluesChangedListener);

	}
	
	protected void fillParticleNames() {
		for (String s : Main.ParticleSystemNames) {
			cb_particleName.addItem(s);
		}
	}
	
	public void updateEntryValues() {
		entry.Count = (int)tf_Count.getValue();
		entry.CreateAtGroundHeight = chckbxmntmCreateatgroundheight.isSelected();
		entry.Ricochet = chckbxmntmRicochet.isSelected();
		entry.UseCallersRadius = chckbxmntmUsecallersradius.isSelected();
		entry.OrientToObject = chckbxmntmOrienttoobject.isSelected();
		entry.Height[0] = (float)tf_HeightMin.getValue();
		entry.Height[1] = (float)tf_HeightMax.getValue();
		entry.InitialDelay[0] = (int)tf_DelayMin.getValue();
		entry.InitialDelay[1] = (int)tf_DelayMax.getValue();
		entry.Name = (String)cb_particleName.getSelectedItem();
		entry.Offset[0] =(float) tf_Offset_X.getValue();
		entry.Offset[1] = (float) tf_Offset_Y.getValue();
		entry.Offset[2] = (float) tf_Offset_Z.getValue();
		entry.Radius[0] = (float) tf_RadiusMin.getValue();
		entry.Radius[1] = (float) tf_RadiusMax.getValue();
	}

	public void loadValues(ParticleSystemEntry entry) {
		ignoreChanges = true;
		for (int i = 0; i < cb_particleName.getItemCount(); i++) {
			if (((String)cb_particleName.getItemAt(i)).equals(entry.Name))  {
				cb_particleName.setSelectedIndex(i);
				break;
			}
		}
		this.chckbxmntmCreateatgroundheight.setSelected(entry.CreateAtGroundHeight);
		this.chckbxmntmOrienttoobject.setSelected(entry.OrientToObject);
		this.chckbxmntmRicochet.setSelected(entry.Ricochet);
		this.chckbxmntmUsecallersradius.setSelected(entry.UseCallersRadius);
		this.tf_Count.setValue(entry.Count);
		this.tf_DelayMin.setValue(entry.InitialDelay[0]);
		this.tf_DelayMax.setValue(entry.InitialDelay[1]);
		this.tf_HeightMin.setValue(entry.Height[0]);
		this.tf_HeightMax.setValue(entry.Height[1]);
		this.tf_Offset_X.setValue(entry.Offset[0]);
		this.tf_Offset_Y.setValue(entry.Offset[1]);
		this.tf_Offset_Z.setValue(entry.Offset[2]);
		this.tf_RadiusMin.setValue(entry.Radius[0]);
		this.tf_RadiusMax.setValue(entry.Radius[1]);
		ignoreChanges = false;
	}

	public JComboBox getCb_particleName() {
		return cb_particleName;
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	public JToggleButton getTglbtnOptions() {
		return tglbtnOptions;
	}
	public JCheckBox getChckbxShowParticle() {
		return chckbxShowParticle;
	}
}
