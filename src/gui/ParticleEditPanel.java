package gui;

import javax.imageio.ImageIO;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;

import java.awt.FlowLayout;

import javax.swing.border.CompoundBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

import main.Config;
import main.Main;
import main.Renderer;
import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.alphaEntry;
import entitytypes.ParticleSystemType.colorEntry;
import entitytypes.ParticleSystemType.e_VelocityType;
import entitytypes.ParticleSystemType.e_VolumeType;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;

import util.TextureListCellRenderer;
import util.Undo;
import util.Util;
import util.Undo.OperationType;

import com.sun.j3d.utils.image.TextureLoader;

import components.ImagePreview;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import entitytypes.ParticleSystemType.e_Shader;
import entitytypes.ParticleSystemType.e_Type;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ParticleEditPanel extends JPanel {
	private JPanel panel_VolSphere;
	private JPanel panel_VolBox;
	private JPanel panel_VolLine;
	private JPanel panel_VolCylinder;
	private JPanel panel_VelOrtho;
	private JPanel panel_VelCylindrical;
	private JPanel panel_VelOutward;
	private JPanel panel_VelHemispherical;
	private JPanel panel_VelSpherical;

	public Renderer renderer;
	public JPanel panel_AlphaEntries;
	public ArrayList<alphaEntry> alphaEntryList = new ArrayList<alphaEntry>();
	public ArrayList<colorEntry> colorEntryList = new ArrayList<colorEntry>();
	
	private JComboBox cbParticleType;
	private JComboBox cbShader;
	public JComboBox cbTexture;
	private JButton btnTexAdd;
	private ValueTextField tfAngleZMin;
	private ValueTextField tfAngleZMax;
	private JCheckBox chckbxGroundAligned;
	private ValueTextField tfAngleZRateMin;
	private ValueTextField tfAngleZRateMax;
	private ValueTextField tfAngleDampingMin;
	private ValueTextField tfAngleDampingMax;
	private ValueTextField tfSizeMin;
	private ValueTextField tfSizeMax;
	private ValueTextField tfSizeRateMin;
	private ValueTextField tfSizeRateMax;
	private ValueTextField tfSizeRateDampMin;
	private ValueTextField tfSizeRateDampMax;
	private ValueTextField tfStartSizeRateMin;
	private ValueTextField tfStartSizeRateMax;
	private ValueTextField tfLifeTimeMin;
	private ValueTextField tfLifeTimeMax;
	private ValueTextField tfSystemLifeTime;
	private ValueTextField tfBurstCountMin;
	private ValueTextField tfBurstCountMax;
	private ValueTextField tfBurstDelayMin;
	private ValueTextField tfBurstDelayMax;
	private ValueTextField tfInitDelayMin;
	private ValueTextField tfInitDelayMax;
	private JCheckBox chckbxIsoneshot;
	private JComboBox cbVelocityType;
	private JComboBox cbVolumeType;
	private JCheckBox chckbxHollow;
	private ValueTextField tfVelSphericalMin;
	private ValueTextField tfVelSphericalMax;
	private ValueTextField tfVelHemiMin;
	private ValueTextField tfVelHemiMax;
	private ValueTextField tfVelOutwardMin;
	private ValueTextField tfVelOutwardMax;
	private ValueTextField tfVelOutwardOtherMin;
	private ValueTextField tfVelOutwardOtherMax;
	private ValueTextField tfVelCylRadialMin;
	private ValueTextField tfVelCylRadialMax;
	private ValueTextField tfVelCylNormalMin;
	private ValueTextField tfVelCylNormalMax;
	private ValueTextField tfVolSphereRadius;
	private ValueTextField tfVolBoxX;
	private ValueTextField tfVolBoxY;
	private ValueTextField tfVolBoxZ;
	private ValueTextField tfVolLineStartX;
	private ValueTextField tfVolLineStartY;
	private ValueTextField tfVolLineStartZ;
	private ValueTextField tfVolLineEndX;
	private ValueTextField tfVolLineEndY;
	private ValueTextField tfVolCylLength;
	private ValueTextField tfVolCylRadius;
	private ValueTextField tfVolLineEndZ;
	private JComboBox cbAttachedSystem;
	private JCheckBox chAttachedSystem;
	private JCheckBox chSlavedSystem;
	private JComboBox cbSlavedSystem;
	private ValueTextField tfSlaveX;
	private ValueTextField tfSlaveY;
	private ValueTextField tfSlaveZ;
	private JComboBox cbPriority;
	private JCheckBox chckbxEmitAboveGround;
	private JCheckBox chckbxParticleuptowardsemitter;
	public JPanel panel_ColorEntries;
	private ValueTextField tfColorScaleMin;
	private ValueTextField tfColorScaleMax;
	private ValueTextField tfVelDampMin;
	private ValueTextField tfVelDampMax;
	private ValueTextField tfGravity;
	private ValueTextField tfVelOrthoXMin;
	private ValueTextField tfVelOrthoXMax;
	private ValueTextField tfVelOrthoYMin;
	private ValueTextField tfVelOrthoYMax;
	private ValueTextField tfVelOrthoZMin;
	private ValueTextField tfVelOrthoZMax;
	
	public boolean ignoreChanges = false;
	private ValueTextField tfDriftVelX;
	private ValueTextField tfDriftVelY;
	private ValueTextField tfDriftVelZ;
	private PropertyChangeListener particleValuesChangedListener;
	private ItemListener particleCheckboxChangedListener;
	/**
	 * Create the panel.
	 */
	private ItemListener particleSystemSelectListener;
	private JPanel panel_Alpha;
	public ParticleEditPanel(Renderer rend) {
		this.renderer = rend;
		
		particleValuesChangedListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				//System.out.println("PropertyChanged: "+((Component)e.getSource()).getName() + " - "+e.getPropertyName());
				if (!e.getNewValue().equals(e.getOldValue()) && e.getPropertyName().equals("value") && Main.activeParticleSystemType != null && !ignoreChanges) {
					
					String name = ((JComponent)e.getSource()).getName();
					if (name == null) name = "changed unnamed value";
					else name = "changed "+name;
					Undo.performParticleOperation(name, OperationType.EDIT);
					updateParticleValues();
					renderer.mainWindow.reset = true;
				}
			}
		};
		particleCheckboxChangedListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (Main.activeParticleSystemType != null && !ignoreChanges) {
					
					String name = ((JComponent)e.getSource()).getName();
					if (name == null) name = "changed unnamed value";
					else name = "changed "+name;
					Undo.performParticleOperation(name, OperationType.EDIT);
					//renderer.mainWindow.reset = true;
					updateParticleValues();
				}
			}
		};
		
		particleSystemSelectListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreChanges) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
//						renderer.editPanel.addToParticleSelection((String)e.getItem());
						updateParticleValues();
						renderer.mainWindow.reset = true;
						renderer.editPanel.updateFXGUI();
						
/*						if (((JComboBox)e.getSource()).getSelectedIndex() == -1) {							
							NewParticleDialog dialog = new NewParticleDialog(renderer, (String)e.getItem(), "");
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
								Main.updateParticleSystemNames();
								fillParticleLists();
								((JComboBox)e.getSource()).setSelectedItem(pname);
								//updateParticleValues();
								//renderer.updateActiveParticle(ptype, pname);
								ignoreChanges = false;
								updateParticleValues();
								renderer.editPanel.updateFXGUI();
							}else {
								//ignoreChanges = true;
								((JComboBox)e.getSource()).setSelectedIndex(0);
								//ignoreChanges = false;
								updateParticleValues();
							}
						}else {
							updateParticleValues();
							//renderer.updateActiveParticle(Main.getParticleSystem((String)e.getItem()), (String)e.getItem());
							//renderer.editPanel.selectionChanged();
							renderer.mainWindow.reset = true;
						}*/
					}
				}
			}
		};
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel_Tex = new JPanel();
		panel_Tex.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_Tex.setAlignmentY(Component.TOP_ALIGNMENT);
		add(panel_Tex);
		panel_Tex.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_17 = (FlowLayout) panel_3.getLayout();
		flowLayout_17.setAlignment(FlowLayout.LEFT);
		panel_Tex.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblType = new JLabel("Type:");
		panel_3.add(lblType);
		
		cbParticleType = new JComboBox();
		cbParticleType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreChanges && e.getStateChange() == ItemEvent.SELECTED) {
					Undo.performParticleOperation("changed ParticleType", OperationType.EDIT);
					ParticleSystemType type = Main.activeParticleSystemType;
					type.Type = (e_Type) cbParticleType.getSelectedItem();
					renderer.editPanel.particleEditPerformed();
					renderer.mainWindow.reset = true;
				}
			}
		});
		panel_3.add(cbParticleType);
		cbParticleType.setModel(new DefaultComboBoxModel(e_Type.values()));
		cbParticleType.setSelectedIndex(0);
		
		cbShader = new JComboBox();
		cbShader.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreChanges && e.getStateChange() == ItemEvent.SELECTED) {
					Undo.performParticleOperation("changed Shader", OperationType.EDIT);
					ParticleSystemType type = Main.activeParticleSystemType;
					type.Shader = (e_Shader) cbShader.getSelectedItem();
					renderer.editPanel.particleEditPerformed();
					renderer.mainWindow.reset = true;
				}
			}
		});
		panel_3.add(cbShader);
		cbShader.setModel(new DefaultComboBoxModel(e_Shader.values()));
		
		JPanel panel_22 = new JPanel();
		FlowLayout flowLayout_18 = (FlowLayout) panel_22.getLayout();
		flowLayout_18.setAlignment(FlowLayout.LEFT);
		panel_Tex.add(panel_22, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("Texture:");
		panel_22.add(lblNewLabel);
		
		cbTexture = new JComboBox();
		cbTexture.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				renderer.texturePreview.setVisible(false);
			}
		});
		cbTexture.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				renderer.texturePreview.setVisible(false);
			}
		});
		cbTexture.setRenderer(new TextureListCellRenderer(renderer));
		cbTexture.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				if (!ignoreChanges && e.getStateChange() == ItemEvent.SELECTED) {
					Undo.performParticleOperation("changed Texture", OperationType.EDIT);
					String name = (String) cbTexture.getSelectedItem();
					ParticleSystemType type = Main.activeParticleSystemType;
					type.ParticleName = name+".tga";
					renderer.editPanel.particleEditPerformed();
					
					renderer.mainWindow.reset = true;
					renderer.texturePreview.setVisible(false);
				}
			}
		});
		loadTextureNames();
		panel_22.add(cbTexture);
		
		btnTexAdd = new JButton("...");
		btnTexAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getTextureFromFile();
				renderer.mainWindow.reset = true;
			}
		});
		btnTexAdd.setPreferredSize(new Dimension(22,22));
		panel_22.add(btnTexAdd);
		
		JPanel panel_Angle = new JPanel();
		panel_Angle.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Angle);
		panel_Angle.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_Angle.add(panel, BorderLayout.NORTH);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		
		JLabel lblAnglez = new JLabel("AngleZ:");
		
		chckbxGroundAligned = new JCheckBox("Ground Aligned");
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel.add(lblAnglez);
		
		tfAngleZMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfAngleZMin.setColumns(3);
		panel.add(tfAngleZMin);
		
		tfAngleZMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfAngleZMax.setColumns(3);
		panel.add(tfAngleZMax);
		panel.add(chckbxGroundAligned);
		
		JPanel panel_2 = new JPanel();
		panel_Angle.add(panel_2, BorderLayout.SOUTH);
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_2.setAlignmentY(Component.TOP_ALIGNMENT);
		
		JLabel lblAngularratez = new JLabel("Rate:");
		panel_2.add(lblAngularratez);
		
		tfAngleZRateMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfAngleZRateMin.setColumns(3);
		panel_2.add(tfAngleZRateMin);
		
		tfAngleZRateMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfAngleZRateMax.setColumns(3);
		panel_2.add(tfAngleZRateMax);
		
		JLabel lblDamping = new JLabel("Damping:");
		panel_2.add(lblDamping);
		
		tfAngleDampingMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfAngleDampingMin.setColumns(3);
		panel_2.add(tfAngleDampingMin);
		
		tfAngleDampingMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfAngleDampingMax.setColumns(3);
		panel_2.add(tfAngleDampingMax);
		
		JPanel panel_Size = new JPanel();
		panel_Size.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Size);
		panel_Size.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_Size.add(panel_4, BorderLayout.NORTH);
		
		JLabel lblSize = new JLabel("Size:");
		panel_4.add(lblSize);
		
		tfSizeMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSizeMin.setColumns(3);
		panel_4.add(tfSizeMin);
		
		tfSizeMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSizeMax.setColumns(3);
		panel_4.add(tfSizeMax);
		
		JLabel lblStartsizerate = new JLabel("Rate:");
		panel_4.add(lblStartsizerate);
		
		tfSizeRateMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSizeRateMin.setColumns(3);
		panel_4.add(tfSizeRateMin);
		
		tfSizeRateMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSizeRateMax.setColumns(3);
		panel_4.add(tfSizeRateMax);
		
		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_5.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_Size.add(panel_5, BorderLayout.SOUTH);
		
		JLabel lblRate = new JLabel("Damp.:");
		panel_5.add(lblRate);
		
		tfSizeRateDampMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSizeRateDampMin.setColumns(3);
		panel_5.add(tfSizeRateDampMin);
		
		tfSizeRateDampMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSizeRateDampMax.setColumns(3);
		panel_5.add(tfSizeRateDampMax);
		
		JLabel lblDamping_1 = new JLabel("StartRate:");
		panel_5.add(lblDamping_1);
		
		tfStartSizeRateMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfStartSizeRateMin.setColumns(3);
		panel_5.add(tfStartSizeRateMin);
		
		tfStartSizeRateMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfStartSizeRateMax.setColumns(3);
		panel_5.add(tfStartSizeRateMax);
		
		JPanel panel_Lifetime = new JPanel();
		panel_Lifetime.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Lifetime);
		panel_Lifetime.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblLifetime = new JLabel("Lifetime:");
		panel_Lifetime.add(lblLifetime);
		
		tfLifeTimeMin = new ValueTextField(ValueTextField.VALUE_INT);
		panel_Lifetime.add(tfLifeTimeMin);
		tfLifeTimeMin.setColumns(3);
		
		tfLifeTimeMax = new ValueTextField(ValueTextField.VALUE_INT);
		panel_Lifetime.add(tfLifeTimeMax);
		tfLifeTimeMax.setColumns(3);
		
		JLabel lblSystem = new JLabel("System L.:");
		panel_Lifetime.add(lblSystem);
		
		tfSystemLifeTime = new ValueTextField(ValueTextField.VALUE_INT);
		panel_Lifetime.add(tfSystemLifeTime);
		tfSystemLifeTime.setColumns(3);
		
		panel_Alpha = new JPanel();
		add(panel_Alpha);
		panel_Alpha.setLayout(new BorderLayout(0, 0));
		
		panel_AlphaEntries = new JPanel();
		panel_Alpha.add(panel_AlphaEntries);
		panel_AlphaEntries.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_AlphaEntries.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_AlphaEntries.setLayout(new BoxLayout(panel_AlphaEntries, BoxLayout.Y_AXIS));
		
		
		JPanel panel_Color = new JPanel();
		panel_Color.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_Color.setAlignmentY(Component.TOP_ALIGNMENT);
		add(panel_Color);
		panel_Color.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_20 = new JPanel();
		FlowLayout flowLayout_15 = (FlowLayout) panel_20.getLayout();
		flowLayout_15.setAlignment(FlowLayout.LEFT);
		panel_Color.add(panel_20, BorderLayout.SOUTH);
		
		JLabel lblColorscale = new JLabel("ColorScale:");
		panel_20.add(lblColorscale);
		
		tfColorScaleMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfColorScaleMin.setColumns(3);
		panel_20.add(tfColorScaleMin);
		
		tfColorScaleMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfColorScaleMax.setColumns(3);
		panel_20.add(tfColorScaleMax);
		
		panel_ColorEntries = new JPanel();
		panel_Color.add(panel_ColorEntries, BorderLayout.NORTH);
		panel_ColorEntries.setLayout(new BoxLayout(panel_ColorEntries, BoxLayout.Y_AXIS));
		
		JPanel panel_Count = new JPanel();
		panel_Count.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Count);
		panel_Count.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_6.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_Count.add(panel_6, BorderLayout.NORTH);
		
		JLabel lblBurstcount = new JLabel("BurstCount:");
		panel_6.add(lblBurstcount);
		
		tfBurstCountMin = new ValueTextField(ValueTextField.VALUE_INT);
		panel_6.add(tfBurstCountMin);
		tfBurstCountMin.setColumns(2);
		
		tfBurstCountMax = new ValueTextField(ValueTextField.VALUE_INT);
		panel_6.add(tfBurstCountMax);
		tfBurstCountMax.setColumns(2);
		
		JLabel lblBurstdelay = new JLabel("BurstDelay:");
		panel_6.add(lblBurstdelay);
		
		tfBurstDelayMin = new ValueTextField(ValueTextField.VALUE_INT);
		tfBurstDelayMin.setColumns(2);
		panel_6.add(tfBurstDelayMin);
		
		tfBurstDelayMax = new ValueTextField(ValueTextField.VALUE_INT);
		tfBurstDelayMax.setColumns(2);
		panel_6.add(tfBurstDelayMax);
		
		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_7.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_Count.add(panel_7, BorderLayout.SOUTH);
		
		JLabel lblInitialdelay = new JLabel("InitialDelay:");
		panel_7.add(lblInitialdelay);
		
		tfInitDelayMin = new ValueTextField(ValueTextField.VALUE_INT);
		tfInitDelayMin.setColumns(2);
		panel_7.add(tfInitDelayMin);
		
		tfInitDelayMax = new ValueTextField(ValueTextField.VALUE_INT);
		tfInitDelayMax.setColumns(2);
		panel_7.add(tfInitDelayMax);
		
		chckbxIsoneshot = new JCheckBox("isOneShot");
		panel_7.add(chckbxIsoneshot);
		
		JPanel panel_Velocity = new JPanel();
		panel_Velocity.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Velocity);
		panel_Velocity.setLayout(new BoxLayout(panel_Velocity, BoxLayout.Y_AXIS));
		
		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_8.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		panel_Velocity.add(panel_8);
		
		JLabel lblVelocityType = new JLabel("Velocity Type:");
		panel_8.add(lblVelocityType);
		
		cbVelocityType = new JComboBox();
		cbVelocityType.setModel(new DefaultComboBoxModel(e_VelocityType.values()));
		panel_8.add(cbVelocityType);
		
		panel_VelSpherical = new JPanel();
		FlowLayout fl_panel_VelSpherical = (FlowLayout) panel_VelSpherical.getLayout();
		fl_panel_VelSpherical.setAlignment(FlowLayout.LEFT);
		panel_Velocity.add(panel_VelSpherical);
		
		JLabel lblVelocitySpherical = new JLabel("Velocity Spherical:");
		panel_VelSpherical.add(lblVelocitySpherical);
		
		tfVelSphericalMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelSphericalMin.setColumns(4);
		panel_VelSpherical.add(tfVelSphericalMin);
		
		tfVelSphericalMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelSphericalMax.setColumns(4);
		panel_VelSpherical.add(tfVelSphericalMax);
		
		panel_VelHemispherical = new JPanel();
		FlowLayout fl_panel_VelHemispherical = (FlowLayout) panel_VelHemispherical.getLayout();
		fl_panel_VelHemispherical.setAlignment(FlowLayout.LEFT);
		panel_Velocity.add(panel_VelHemispherical);
		
		JLabel lblVelocityHemispherical = new JLabel("Velocity Hemispherical:");
		panel_VelHemispherical.add(lblVelocityHemispherical);
		
		tfVelHemiMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelHemiMin.setColumns(4);
		panel_VelHemispherical.add(tfVelHemiMin);
		
		tfVelHemiMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelHemiMax.setColumns(4);
		panel_VelHemispherical.add(tfVelHemiMax);
		
		panel_VelOutward = new JPanel();
		panel_Velocity.add(panel_VelOutward);
		panel_VelOutward.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblVelOutward = new JLabel("Vel:");
		panel_VelOutward.add(lblVelOutward);
		
		tfVelOutwardMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_VelOutward.add(tfVelOutwardMin);
		tfVelOutwardMin.setColumns(4);
		
		tfVelOutwardMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_VelOutward.add(tfVelOutwardMax);
		tfVelOutwardMax.setColumns(4);
		
		JLabel label = new JLabel("/");
		panel_VelOutward.add(label);
		
		tfVelOutwardOtherMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_VelOutward.add(tfVelOutwardOtherMin);
		tfVelOutwardOtherMin.setColumns(4);
		
		tfVelOutwardOtherMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_VelOutward.add(tfVelOutwardOtherMax);
		tfVelOutwardOtherMax.setColumns(4);
		
		panel_VelCylindrical = new JPanel();
		FlowLayout fl_panel_VelCylindrical = (FlowLayout) panel_VelCylindrical.getLayout();
		fl_panel_VelCylindrical.setAlignment(FlowLayout.LEFT);
		panel_Velocity.add(panel_VelCylindrical);
		
		JLabel lblVelocityCylindrical = new JLabel("Radial:");
		panel_VelCylindrical.add(lblVelocityCylindrical);
		
		tfVelCylRadialMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelCylRadialMin.setColumns(3);
		panel_VelCylindrical.add(tfVelCylRadialMin);
		
		tfVelCylRadialMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelCylRadialMax.setColumns(3);
		panel_VelCylindrical.add(tfVelCylRadialMax);
		
		JLabel lblNormal = new JLabel("Normal:");
		panel_VelCylindrical.add(lblNormal);
		
		tfVelCylNormalMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelCylNormalMin.setColumns(3);
		panel_VelCylindrical.add(tfVelCylNormalMin);
		
		tfVelCylNormalMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelCylNormalMax.setColumns(3);
		panel_VelCylindrical.add(tfVelCylNormalMax);
		
		panel_VelOrtho = new JPanel();
		FlowLayout fl_panel_VelOrtho = (FlowLayout) panel_VelOrtho.getLayout();
		fl_panel_VelOrtho.setAlignment(FlowLayout.LEFT);
		panel_Velocity.add(panel_VelOrtho);
		
		JLabel lblX = new JLabel("X");
		lblX.setHorizontalAlignment(SwingConstants.LEFT);
		panel_VelOrtho.add(lblX);
		
		tfVelOrthoXMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelOrthoXMin.setColumns(3);
		panel_VelOrtho.add(tfVelOrthoXMin);
		
		tfVelOrthoXMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelOrthoXMax.setColumns(3);
		panel_VelOrtho.add(tfVelOrthoXMax);
		
		JLabel lblY = new JLabel("Y");
		lblY.setHorizontalAlignment(SwingConstants.LEFT);
		panel_VelOrtho.add(lblY);
		
		tfVelOrthoYMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelOrthoYMin.setColumns(3);
		panel_VelOrtho.add(tfVelOrthoYMin);
		
		tfVelOrthoYMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelOrthoYMax.setColumns(3);
		panel_VelOrtho.add(tfVelOrthoYMax);
		
		JLabel lblZ = new JLabel("Z");
		lblZ.setHorizontalAlignment(SwingConstants.LEFT);
		panel_VelOrtho.add(lblZ);
		
		tfVelOrthoZMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelOrthoZMin.setColumns(3);
		panel_VelOrtho.add(tfVelOrthoZMin);
		
		tfVelOrthoZMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelOrthoZMax.setColumns(3);
		panel_VelOrtho.add(tfVelOrthoZMax);
		
		JPanel panel_9 = new JPanel();
		panel_Velocity.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_10 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_10.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		panel_9.add(panel_10, BorderLayout.NORTH);
		
		JLabel lblDriftVelocity = new JLabel("Drift Velocity:");
		panel_10.add(lblDriftVelocity);
		
		tfDriftVelX = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_10.add(tfDriftVelX);
		tfDriftVelX.setColumns(4);
		
		tfDriftVelY = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_10.add(tfDriftVelY);
		tfDriftVelY.setColumns(4);
		
		tfDriftVelZ = new ValueTextField(ValueTextField.VALUE_FLOAT);
		panel_10.add(tfDriftVelZ);
		tfDriftVelZ.setColumns(4);
		
		JPanel panel_11 = new JPanel();
		FlowLayout flowLayout_10 = (FlowLayout) panel_11.getLayout();
		flowLayout_10.setAlignment(FlowLayout.LEFT);
		panel_9.add(panel_11, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_1 = new JLabel("Vel. Damping:");
		panel_11.add(lblNewLabel_1);
		
		tfVelDampMin = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelDampMin.setColumns(3);
		panel_11.add(tfVelDampMin);
		
		tfVelDampMax = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVelDampMax.setColumns(3);
		panel_11.add(tfVelDampMax);
		
		JLabel lblGravity = new JLabel("Gravity:");
		panel_11.add(lblGravity);
		
		tfGravity = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfGravity.setColumns(4);
		panel_11.add(tfGravity);
		
		JPanel panel_Volume = new JPanel();
		panel_Volume.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Volume);
		panel_Volume.setLayout(new BoxLayout(panel_Volume, BoxLayout.Y_AXIS));
		
		JPanel panel_12 = new JPanel();
		FlowLayout flowLayout_11 = (FlowLayout) panel_12.getLayout();
		flowLayout_11.setAlignment(FlowLayout.LEFT);
		panel_Volume.add(panel_12);
		
		JLabel lblNewLabel_2 = new JLabel("Volume Type:");
		panel_12.add(lblNewLabel_2);
		
		cbVolumeType = new JComboBox();
		cbVolumeType.setModel(new DefaultComboBoxModel(e_VolumeType.values()));
		panel_12.add(cbVolumeType);
		
		chckbxHollow = new JCheckBox("Hollow");
		panel_12.add(chckbxHollow);
		
		panel_VolSphere = new JPanel();
		panel_Volume.add(panel_VolSphere);
		panel_VolSphere.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblSphereRadius = new JLabel("Sphere Radius:");
		panel_VolSphere.add(lblSphereRadius);
		
		tfVolSphereRadius = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolSphereRadius.setColumns(4);
		panel_VolSphere.add(tfVolSphereRadius);
		
		panel_VolBox = new JPanel();
		panel_Volume.add(panel_VolBox);
		panel_VolBox.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblHalfsize = new JLabel("HalfSize");
		panel_VolBox.add(lblHalfsize);
		
		JLabel lblX_1 = new JLabel("X:");
		panel_VolBox.add(lblX_1);
		
		tfVolBoxX = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolBoxX.setColumns(4);
		panel_VolBox.add(tfVolBoxX);
		
		JLabel lblY_1 = new JLabel("Y:");
		panel_VolBox.add(lblY_1);
		
		tfVolBoxY = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolBoxY.setColumns(4);
		panel_VolBox.add(tfVolBoxY);
		
		JLabel lblZ_1 = new JLabel("Z:");
		panel_VolBox.add(lblZ_1);
		
		tfVolBoxZ = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolBoxZ.setColumns(4);
		panel_VolBox.add(tfVolBoxZ);
		
		panel_VolLine = new JPanel();
		panel_Volume.add(panel_VolLine);
		panel_VolLine.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_13 = new JPanel();
		panel_VolLine.add(panel_13, BorderLayout.NORTH);
		panel_13.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblStart = new JLabel("Start");
		panel_13.add(lblStart);
		
		JLabel label_2 = new JLabel("X:");
		panel_13.add(label_2);
		
		tfVolLineStartX = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolLineStartX.setColumns(4);
		panel_13.add(tfVolLineStartX);
		
		JLabel label_3 = new JLabel("Y:");
		panel_13.add(label_3);
		
		tfVolLineStartY = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolLineStartY.setColumns(4);
		panel_13.add(tfVolLineStartY);
		
		JLabel label_4 = new JLabel("Z:");
		panel_13.add(label_4);
		
		tfVolLineStartZ = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolLineStartZ.setColumns(4);
		panel_13.add(tfVolLineStartZ);
		
		JPanel panel_14 = new JPanel();
		panel_VolLine.add(panel_14, BorderLayout.SOUTH);
		panel_14.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblEnd = new JLabel("End  ");
		lblEnd.setHorizontalAlignment(SwingConstants.LEFT);
		panel_14.add(lblEnd);
		
		JLabel label_5 = new JLabel("X:");
		panel_14.add(label_5);
		
		tfVolLineEndX = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolLineEndX.setColumns(4);
		panel_14.add(tfVolLineEndX);
		
		JLabel label_6 = new JLabel("Y:");
		panel_14.add(label_6);
		
		tfVolLineEndY = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolLineEndY.setColumns(4);
		panel_14.add(tfVolLineEndY);
		
		JLabel label_7 = new JLabel("Z:");
		panel_14.add(label_7);
		
		tfVolLineEndZ = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolLineEndZ.setColumns(4);
		panel_14.add(tfVolLineEndZ);
		
		panel_VolCylinder = new JPanel();
		panel_Volume.add(panel_VolCylinder);
		panel_VolCylinder.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblCylRadius = new JLabel("Cyl. Radius:");
		panel_VolCylinder.add(lblCylRadius);
		
		tfVolCylRadius = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolCylRadius.setColumns(4);
		panel_VolCylinder.add(tfVolCylRadius);
		
		JLabel lblLength = new JLabel("Length:");
		panel_VolCylinder.add(lblLength);
		
		tfVolCylLength = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfVolCylLength.setColumns(4);
		panel_VolCylinder.add(tfVolCylLength);
		
		JPanel panel_Slave = new JPanel();
		panel_Slave.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panel_Slave);
		panel_Slave.setLayout(new BoxLayout(panel_Slave, BoxLayout.Y_AXIS));
		
		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout_9 = (FlowLayout) panel_16.getLayout();
		flowLayout_9.setAlignment(FlowLayout.LEFT);
		panel_Slave.add(panel_16);
		
		chAttachedSystem = new JCheckBox("");
		panel_16.add(chAttachedSystem);
		
		JLabel lblAttachedsystem = new JLabel("Attached System:");
		panel_16.add(lblAttachedsystem);
		
		cbAttachedSystem = new JComboBox();
		panel_Slave.add(cbAttachedSystem);
		cbAttachedSystem.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cbAttachedSystem.setEnabled(false);
		cbAttachedSystem.setPreferredSize(new Dimension(275,22));
		cbAttachedSystem.addItemListener(particleSystemSelectListener);
		
		JPanel panel_17 = new JPanel();
		FlowLayout flowLayout_12 = (FlowLayout) panel_17.getLayout();
		flowLayout_12.setAlignment(FlowLayout.LEFT);
		panel_Slave.add(panel_17);
		
		chSlavedSystem = new JCheckBox("");
		panel_17.add(chSlavedSystem);
		
		JLabel lblSlaved = new JLabel("Slaved System:");
		panel_17.add(lblSlaved);
		
		cbSlavedSystem = new JComboBox();
		panel_Slave.add(cbSlavedSystem);
		cbSlavedSystem.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cbSlavedSystem.setEnabled(false);
		cbSlavedSystem.setPreferredSize(new Dimension(275,22));
		cbSlavedSystem.addItemListener(particleSystemSelectListener);
		
		JPanel panel_18 = new JPanel();
		FlowLayout flowLayout_13 = (FlowLayout) panel_18.getLayout();
		flowLayout_13.setAlignment(FlowLayout.LEFT);
		panel_Slave.add(panel_18);
		
		JLabel lblNewLabel_3 = new JLabel("Slave Offset");
		panel_18.add(lblNewLabel_3);
		
		JLabel label_1 = new JLabel("X:");
		panel_18.add(label_1);
		
		tfSlaveX = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSlaveX.setColumns(4);
		panel_18.add(tfSlaveX);
		
		JLabel label_8 = new JLabel("Y:");
		panel_18.add(label_8);
		
		tfSlaveY = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSlaveY.setColumns(4);
		panel_18.add(tfSlaveY);
		
		JLabel label_9 = new JLabel("Z:");
		panel_18.add(label_9);
		
		tfSlaveZ = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfSlaveZ.setColumns(4);
		panel_18.add(tfSlaveZ);
		
		JPanel panel_Misc = new JPanel();
		panel_Misc.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_Misc.setAlignmentY(Component.TOP_ALIGNMENT);
		add(panel_Misc);
		panel_Misc.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel_1.getLayout();
		flowLayout_7.setAlignment(FlowLayout.LEFT);
		panel_Misc.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblPriority = new JLabel("Priority:");
		panel_1.add(lblPriority);
		lblPriority.setHorizontalAlignment(SwingConstants.LEFT);
		
		cbPriority = new JComboBox();
		panel_1.add(cbPriority);
		cbPriority.setModel(new DefaultComboBoxModel(new String[] {"WEAPON_EXPLOSION", "WEAPON_TRAIL", "DEATH_EXPLOSION", "UNIT_DAMAGE_FX", "CRITICAL", "AREA_EFFECT", "ALWAYS_RENDER", "CONSTANT", "SEMI_CONSTANT", "DEBRIS_TRAIL", "BUILDUP", "DUST_TRAIL", "SCORCHMARK"}));
		cbPriority.setSelectedIndex(0);
		
		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) panel_15.getLayout();
		flowLayout_8.setAlignment(FlowLayout.LEFT);
		panel_Misc.add(panel_15, BorderLayout.SOUTH);
		
		chckbxEmitAboveGround = new JCheckBox("EmitAboveGroundOnly");
		chckbxEmitAboveGround.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_15.add(chckbxEmitAboveGround);
		
		chckbxParticleuptowardsemitter = new JCheckBox("UpTowardsEmitter");
		chckbxParticleuptowardsemitter.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_15.add(chckbxParticleuptowardsemitter);
		
		tfAngleDampingMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfAngleDampingMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfAngleZMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfAngleZMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfAngleZRateMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfAngleZRateMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfBurstCountMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfBurstCountMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfBurstDelayMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfBurstDelayMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfColorScaleMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfColorScaleMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfDriftVelX.addPropertyChangeListener("value", particleValuesChangedListener);
		tfDriftVelY.addPropertyChangeListener("value", particleValuesChangedListener);
		tfDriftVelZ.addPropertyChangeListener("value", particleValuesChangedListener);
		tfGravity.addPropertyChangeListener("value", particleValuesChangedListener);
		tfInitDelayMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfInitDelayMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfLifeTimeMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfLifeTimeMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSizeMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSizeMin.addPropertyChangeListener("value", particleValuesChangedListener);		
		tfSizeRateMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSizeRateMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSizeRateDampMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSizeRateDampMax.addPropertyChangeListener("value", particleValuesChangedListener);		
		tfSlaveX.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSlaveY.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSlaveZ.addPropertyChangeListener("value", particleValuesChangedListener);
		tfStartSizeRateMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfStartSizeRateMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfSystemLifeTime.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelCylNormalMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelCylNormalMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelCylRadialMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelCylRadialMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelDampMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelDampMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelHemiMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelHemiMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOrthoXMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOrthoXMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOrthoYMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOrthoYMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOrthoZMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOrthoZMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOutwardMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOutwardMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOutwardOtherMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelOutwardOtherMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelSphericalMax.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVelSphericalMin.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolBoxX.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolBoxY.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolBoxZ.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolCylLength.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolCylRadius.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolLineEndX.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolLineEndY.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolLineEndZ.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolLineStartX.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolLineStartY.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolLineStartZ.addPropertyChangeListener("value", particleValuesChangedListener);
		tfVolSphereRadius.addPropertyChangeListener("value", particleValuesChangedListener);
		
		chckbxHollow.addItemListener(particleCheckboxChangedListener);
		chckbxEmitAboveGround.addItemListener(particleCheckboxChangedListener);
		chckbxIsoneshot.addItemListener(particleCheckboxChangedListener);
		chckbxGroundAligned.addItemListener(particleCheckboxChangedListener);
		chckbxParticleuptowardsemitter.addItemListener(particleCheckboxChangedListener);
		
		chAttachedSystem.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (!ignoreChanges) {
					Undo.performParticleOperation("changed AttachedSystem", OperationType.EDIT);
					cbAttachedSystem.setEnabled(chAttachedSystem.isSelected());
					
					updateParticleValues();
				}
			}
		});
		chSlavedSystem.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreChanges) {
					Undo.performParticleOperation("changed SlavedSystem", OperationType.EDIT);
					cbSlavedSystem.setEnabled(chSlavedSystem.isSelected());				
					updateParticleValues();
				}
			}
		});
		cbVelocityType.addItemListener(new ItemListener() {
			e_VelocityType velPrev = null;
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreChanges) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						velPrev = (e_VelocityType) e.getItem();
					} else if (e.getStateChange() == ItemEvent.SELECTED) {
						Undo.performParticleOperation("changed VelocityType", OperationType.EDIT);
						copyVelValues(velPrev, (e_VelocityType)e.getItem());
						Main.activeParticleSystemType.VelocityType = (e_VelocityType)e.getItem();
						renderer.editPanel.particleEditPerformed();
						
						insertGuiValues();
					}
				}
			}
		});
		cbVolumeType.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreChanges && e.getStateChange() == ItemEvent.SELECTED) {
					Undo.performParticleOperation("changed VolumeType", OperationType.EDIT);
					Main.activeParticleSystemType.VolumeType = (e_VolumeType)e.getItem();
					renderer.editPanel.particleEditPerformed();
					
					insertGuiValues();
				}
			}
		});
		
		fillParticleLists();
	}
	
	public void fillParticleLists() {
		ignoreChanges = true;
		cbAttachedSystem.removeAllItems();
		cbSlavedSystem.removeAllItems();
		for (String s : Main.ParticleSystemNames) {
			cbAttachedSystem.addItem(s);
			cbSlavedSystem.addItem(s);
		}
		ParticleSystemType type = Main.activeParticleSystemType;
		if (type != null) {
			if (type.PerParticleAttachedSystem != null && !type.PerParticleAttachedSystem.equals("")) {
				cbAttachedSystem.setSelectedItem(type.PerParticleAttachedSystem);
			}
			if (type.SlaveSystem != null && !type.SlaveSystem.equals("")) {
				cbSlavedSystem.setSelectedItem(type.SlaveSystem);
			}
		}
		ignoreChanges = false;
	}

	protected void hideShowVelocityPanels() {
		e_VelocityType vtype = (e_VelocityType)cbVelocityType.getSelectedItem();
		panel_VelCylindrical.setVisible(vtype == e_VelocityType.CYLINDRICAL);
		panel_VelHemispherical.setVisible(vtype == e_VelocityType.HEMISPHERICAL);
		panel_VelOrtho.setVisible(vtype == e_VelocityType.ORTHO);
		panel_VelOutward.setVisible(vtype == e_VelocityType.OUTWARD);
		panel_VelSpherical.setVisible(vtype == e_VelocityType.SPHERICAL);
	}
	
	protected void hideShowVolumePanels() {
		e_VolumeType vtype = (e_VolumeType)cbVolumeType.getSelectedItem();
		panel_VolBox.setVisible(vtype == e_VolumeType.BOX);
		panel_VolCylinder.setVisible(vtype == e_VolumeType.CYLINDER);
		panel_VolLine.setVisible(vtype == e_VolumeType.LINE);
		panel_VolSphere.setVisible(vtype == e_VolumeType.SPHERE);
	}

	protected void copyVelValues(e_VelocityType src, e_VelocityType dst) {
		ParticleSystemType type = Main.activeParticleSystemType;
		float sVel1 = 0, sVel2 = 0, sVel3 = 0, sVel4 = 0;
		if (src != null) {
			switch (src) {
			case CYLINDRICAL:
				sVel1 = type.VelCylindricalNormal[0];
				sVel2 = type.VelCylindricalNormal[1];
				sVel3 = type.VelCylindricalRadial[0];
				sVel4 = type.VelCylindricalRadial[1];
				break;
			case HEMISPHERICAL:
				sVel1 = type.VelHemispherical[0];
				sVel2 = type.VelHemispherical[1];
				break;
			case OUTWARD:
				sVel1 = type.VelOutward[0];
				sVel2 = type.VelOutward[1];
				sVel3 = type.VelOutwardOther[0];
				sVel4 = type.VelOutwardOther[1];
				break;
			case SPHERICAL:
				sVel1 = type.VelSpherical[0];
				sVel2 = type.VelSpherical[1];
				break;
			}
			//----
			switch(dst) {
			case CYLINDRICAL:
				if (!(type.VelCylindricalNormal[0] != 0 || type.VelCylindricalNormal[1] != 0)) {
					type.VelCylindricalNormal[0] = sVel1;
					type.VelCylindricalNormal[1] = sVel2;
				}
				if (!(type.VelCylindricalRadial[0] != 0 || type.VelCylindricalRadial[1] != 0)) {
					type.VelCylindricalRadial[0] = sVel3;
					type.VelCylindricalRadial[1] = sVel4;
				}
				break;
			case HEMISPHERICAL:
				if (!(type.VelHemispherical[0] != 0 || type.VelHemispherical[1] != 0)) {
					type.VelHemispherical[0] = sVel1;
					type.VelHemispherical[1] = sVel2;
				}
				break;
			case OUTWARD:
				if (!(type.VelOutward[0] != 0 || type.VelOutward[1] != 0)) {
					type.VelOutward[0] = sVel1;
					type.VelOutward[1] = sVel2;
				}
				if (!(type.VelOutwardOther[0] != 0 || type.VelOutwardOther[1] != 0)) {
					type.VelOutwardOther[0] = sVel3;
					type.VelOutwardOther[1] = sVel4;
				}
				break;
			case SPHERICAL:
				if (!(type.VelSpherical[0] != 0 || type.VelSpherical[1] != 0)) {
					type.VelSpherical[0] = sVel1;
					type.VelSpherical[1] = sVel2;
				}
				break;
			}
		}
		
	}
	public void loadTextureNames() {
		ignoreChanges = true;
		cbTexture.removeAllItems();
		ArrayList<String> names = new ArrayList<String>();
		names.addAll(renderer.TextureMap.keySet());
		Collections.sort(names);
		for (String s : names) {
			cbTexture.addItem(s);
		}
		ignoreChanges = false;
	}

	
//	public void loadValues() {
//		
//		//loadCode();
//		insertGuiValues();
//	}
//	private void loadCode() {
//		// TODO Auto-generated method stub
//		
//	}
	public void insertGuiValues() {
		if(cbTexture.getItemCount() == 0 && renderer.TextureMap.size() != 0) {
			loadTextureNames();
		}		
		ignoreChanges = true;
		ParticleSystemType type = Main.activeParticleSystemType;
		alphaEntriesToList();
		colorEntriesToList();
		createAlphaEntryPanels();
		createColorEntryPanels();
		tfAngleDampingMin.setValue(type.AngularDamping[0]);
		tfAngleDampingMax.setValue(type.AngularDamping[1]);
		tfAngleZMin.setValue(type.AngleZ[0]);
		tfAngleZMax.setValue(type.AngleZ[1]);
		tfAngleZRateMin.setValue(type.AngularRateZ[0]);
		tfAngleZRateMax.setValue(type.AngularRateZ[1]);
		tfBurstCountMin.setValue(type.BurstCount[0]);
		tfBurstCountMax.setValue(type.BurstCount[1]);
		tfBurstDelayMin.setValue(type.BurstDelay[0]);
		tfBurstDelayMax.setValue(type.BurstDelay[1]);
		tfColorScaleMin.setValue(type.ColorScale[0]);
		tfColorScaleMax.setValue(type.ColorScale[1]);
		tfDriftVelX.setValue(type.DriftVelocity.x);
		tfDriftVelY.setValue(type.DriftVelocity.y);
		tfDriftVelZ.setValue(type.DriftVelocity.z);
		tfGravity.setValue(type.Gravity);
		tfInitDelayMin.setValue(type.InitialDelay[0]);
		tfInitDelayMax.setValue(type.InitialDelay[1]);
		tfLifeTimeMin.setValue(type.Lifetime[0]);
		tfLifeTimeMax.setValue(type.Lifetime[1]);
		tfSizeMin.setValue(type.Size[0]);
		tfSizeMax.setValue(type.Size[1]);
		tfSizeRateDampMin.setValue(type.SizeRateDamping[0]);
		tfSizeRateDampMax.setValue(type.SizeRateDamping[1]);
		tfSizeRateMin.setValue(type.SizeRate[0]);
		tfSizeRateMax.setValue(type.SizeRate[1]);
		if (type.SlavePosOffset != null) {
			tfSlaveX.setValue(type.SlavePosOffset.x);
			tfSlaveY.setValue(type.SlavePosOffset.y);
			tfSlaveZ.setValue(type.SlavePosOffset.z);
		}
		tfStartSizeRateMin.setValue(type.StartSizeRate[0]);
		tfStartSizeRateMax.setValue(type.StartSizeRate[1]);
		tfSystemLifeTime.setValue(type.SystemLifetime);
		cbVelocityType.setSelectedItem(type.VelocityType);
		switch (type.VelocityType) {
			case CYLINDRICAL:
				tfVelCylNormalMin.setValue(type.VelCylindricalNormal[0]);
				tfVelCylNormalMax.setValue(type.VelCylindricalNormal[1]);
				tfVelCylRadialMin.setValue(type.VelCylindricalRadial[0]);
				tfVelCylRadialMax.setValue(type.VelCylindricalRadial[1]);
				break;
			case HEMISPHERICAL:
				tfVelHemiMin.setValue(type.VelHemispherical[0]);
				tfVelHemiMax.setValue(type.VelHemispherical[1]);
				break;
			case ORTHO:
				tfVelOrthoXMin.setValue(type.VelOrthoX[0]);
				tfVelOrthoXMax.setValue(type.VelOrthoX[1]);
				tfVelOrthoYMin.setValue(type.VelOrthoY[0]);
				tfVelOrthoYMax.setValue(type.VelOrthoY[1]);
				tfVelOrthoZMin.setValue(type.VelOrthoZ[0]);
				tfVelOrthoZMax.setValue(type.VelOrthoZ[1]);
				break;
			case OUTWARD:
				tfVelOutwardMin.setValue(type.VelOutward[0]);
				tfVelOutwardMax.setValue(type.VelOutward[1]);
				tfVelOutwardOtherMin.setValue(type.VelOutwardOther[0]);
				tfVelOutwardOtherMax.setValue(type.VelOutwardOther[1]);
				break;
			case SPHERICAL:
				tfVelSphericalMin.setValue(type.VelSpherical[0]);
				tfVelSphericalMax.setValue(type.VelSpherical[1]);
				break;
			case NONE:
			default:
				break;			
		}
		tfVelDampMin.setValue(type.VelocityDamping[0]);
		tfVelDampMax.setValue(type.VelocityDamping[1]);
		cbVolumeType.setSelectedItem(type.VolumeType);
		switch (type.VolumeType) {
			case BOX:
				tfVolBoxX.setValue(type.VolBoxHalfSize.x);
				tfVolBoxY.setValue(type.VolBoxHalfSize.y);
				tfVolBoxZ.setValue(type.VolBoxHalfSize.z);
			case CYLINDER:
				tfVolCylLength.setValue(type.VolCylinderLength);
				tfVolCylRadius.setValue(type.VolCylinderRadius);
				break;
			case LINE:
				tfVolLineStartX.setValue(type.VolLineStart.x);
				tfVolLineStartY.setValue(type.VolLineStart.y);
				tfVolLineStartZ.setValue(type.VolLineStart.z);
				tfVolLineEndX.setValue(type.VolLineEnd.x);
				tfVolLineEndY.setValue(type.VolLineEnd.y);
				tfVolLineEndZ.setValue(type.VolLineEnd.z);
				break;
			case SPHERE:
				tfVolSphereRadius.setValue(type.VolSphereRadius);
				break;
			case POINT:
			default:
				break;	
		}
		
		if (type.PerParticleAttachedSystem != null && !type.PerParticleAttachedSystem.equals("")) {
			cbAttachedSystem.setSelectedItem(type.PerParticleAttachedSystem);
			cbAttachedSystem.setEnabled(true);
			chAttachedSystem.setSelected(true);
		}else {
			chAttachedSystem.setSelected(false);
			cbAttachedSystem.setEnabled(false);
		}
		if (type.SlaveSystem != null && type.SlavePosOffset != null && !type.SlaveSystem.equals("")) {
			chSlavedSystem.setSelected(true);
			cbSlavedSystem.setEnabled(true);
			cbSlavedSystem.setSelectedItem(type.SlaveSystem);
			tfSlaveX.setEnabled(true);
			tfSlaveY.setEnabled(true);
			tfSlaveZ.setEnabled(true);
			tfSlaveX.setValue(type.SlavePosOffset.x);
			tfSlaveY.setValue(type.SlavePosOffset.y);
			tfSlaveZ.setValue(type.SlavePosOffset.z);
		}else {
			chSlavedSystem.setSelected(false);
			cbSlavedSystem.setEnabled(false);
			tfSlaveX.setEnabled(false);
			tfSlaveY.setEnabled(false);
			tfSlaveZ.setEnabled(false);
		}
		
		chckbxEmitAboveGround.setSelected(type.IsEmitAboveGroundOnly);
		chckbxGroundAligned.setSelected(type.IsGroundAligned);
		chckbxHollow.setSelected(type.IsHollow);
		chckbxIsoneshot.setSelected(type.IsOneShot);
		chckbxParticleuptowardsemitter.setSelected(type.IsParticleUpTowardsEmitter);
		
		cbPriority.setSelectedItem(type.Priority);
		cbParticleType.setSelectedItem(type.Type);
		cbShader.setSelectedItem(type.Shader);
		
		cbTexture.setSelectedItem(type.ParticleName.substring(0, type.ParticleName.length()-4));
		
		hideShowVelocityPanels();
		hideShowVolumePanels();
		ignoreChanges = false;
		renderer.editPanel.updateParticleCode();
	}
	
	public void updateParticleValues() {
		ParticleSystemType type = Main.activeParticleSystemType;
		type.AngleZ[0] = (float) tfAngleZMin.getValue();
		type.AngleZ[1] = (float) tfAngleZMax.getValue();
		type.AngularDamping[0] = (float) tfAngleDampingMin.getValue();
		type.AngularDamping[1] = (float) tfAngleDampingMax.getValue();
		type.AngularRateZ[0] = (float)tfAngleZRateMin.getValue();
		type.AngularRateZ[1] = (float)tfAngleZRateMax.getValue();
		type.BurstCount[0] = (int) tfBurstCountMin.getValue();
		type.BurstCount[1] = (int) tfBurstCountMax.getValue();
		type.BurstDelay[0] = (int) tfBurstDelayMin.getValue();
		type.BurstDelay[1] = (int) tfBurstDelayMax.getValue();
		type.ColorScale[0] = (float) tfColorScaleMin.getValue();
		type.ColorScale[1] = (float) tfColorScaleMax.getValue();
		type.DriftVelocity.x = (float) tfDriftVelX.getValue();
		type.DriftVelocity.y = (float) tfDriftVelY.getValue();
		type.DriftVelocity.z = (float) tfDriftVelZ.getValue();
		type.Gravity = (float) tfGravity.getValue();
		type.InitialDelay[0] = (int) tfInitDelayMin.getValue();
		type.InitialDelay[1] = (int) tfInitDelayMax.getValue();
		type.Lifetime[0] = (int) tfLifeTimeMin.getValue();
		type.Lifetime[1] = (int) tfLifeTimeMax.getValue();
		type.ParticleName = (String) cbTexture.getSelectedItem()+".tga";
		type.IsEmitAboveGroundOnly = chckbxEmitAboveGround.isSelected();
		type.IsGroundAligned = chckbxGroundAligned.isSelected();
		type.IsHollow = chckbxHollow.isSelected();
		type.IsOneShot = chckbxIsoneshot.isSelected();
		type.IsParticleUpTowardsEmitter = chckbxParticleuptowardsemitter.isSelected();
		if (chAttachedSystem.isSelected()) type.PerParticleAttachedSystem = (String) cbAttachedSystem.getSelectedItem();
		else type.PerParticleAttachedSystem = null;
		if (chSlavedSystem.isSelected()) type.SlaveSystem = (String) cbSlavedSystem.getSelectedItem();
		else type.SlaveSystem = null;
		type.SlavePosOffset.x = (float) tfSlaveX.getValue();
		type.SlavePosOffset.y = (float) tfSlaveY.getValue();
		type.SlavePosOffset.z = (float) tfSlaveZ.getValue();
		type.Priority = (String) cbPriority.getSelectedItem();
		type.Shader = (e_Shader) cbShader.getSelectedItem();
		type.Size[0] = (float) tfSizeMin.getValue();
		type.Size[1] = (float) tfSizeMax.getValue();
		type.SizeRate[0] = (float) tfSizeRateMin.getValue();
		type.SizeRate[1] = (float) tfSizeRateMax.getValue();
		type.SizeRateDamping[0] = (float) tfSizeRateDampMin.getValue();
		type.SizeRateDamping[1] = (float) tfSizeRateDampMax.getValue();
		type.StartSizeRate[0] = (float) tfStartSizeRateMin.getValue();
		type.StartSizeRate[1] = (float) tfStartSizeRateMax.getValue();
		type.SystemLifetime = (int) tfSystemLifeTime.getValue();
		
		type.VelocityType = (e_VelocityType) cbVelocityType.getSelectedItem();
		type.VelocityDamping[0] = (float) tfVelDampMin.getValue();
		type.VelocityDamping[1] = (float) tfVelDampMax.getValue();
		type.VelCylindricalNormal[0] = (float) tfVelCylNormalMin.getValue();
		type.VelCylindricalNormal[1] = (float) tfVelCylNormalMax.getValue();
		type.VelCylindricalRadial[0] = (float) tfVelCylRadialMin.getValue();
		type.VelCylindricalRadial[1] = (float) tfVelCylRadialMax.getValue();
		type.VelHemispherical[0] = (float) tfVelHemiMin.getValue();
		type.VelHemispherical[1] = (float) tfVelHemiMax.getValue();
		type.VelOrthoX[0] = (float) tfVelOrthoXMin.getValue();
		type.VelOrthoX[1] = (float) tfVelOrthoXMax.getValue();
		type.VelOrthoY[0] = (float) tfVelOrthoYMin.getValue();
		type.VelOrthoY[1] = (float) tfVelOrthoYMax.getValue();
		type.VelOrthoZ[0] = (float) tfVelOrthoZMin.getValue();
		type.VelOrthoZ[1] = (float) tfVelOrthoZMax.getValue();
		type.VelOutward[0] = (float)tfVelOutwardMin.getValue();
		type.VelOutward[1] = (float)tfVelOutwardMax.getValue();
		type.VelOutwardOther[0] = (float)tfVelOutwardOtherMin.getValue();
		type.VelOutwardOther[1] = (float)tfVelOutwardOtherMax.getValue();
		type.VelSpherical[0] = (float) tfVelSphericalMin.getValue();
		type.VelSpherical[1] = (float) tfVelSphericalMax.getValue();
		
		type.VolumeType = (e_VolumeType) cbVolumeType.getSelectedItem();
		type.VolBoxHalfSize.x = (float) tfVolBoxX.getValue();
		type.VolBoxHalfSize.y = (float) tfVolBoxY.getValue();
		type.VolBoxHalfSize.z = (float) tfVolBoxZ.getValue();
		type.VolCylinderLength = (float) tfVolCylLength.getValue();
		type.VolCylinderRadius = (float) tfVolCylRadius.getValue();
		type.VolLineStart.x = (float) tfVolLineStartX.getValue();
		type.VolLineStart.y = (float) tfVolLineStartY.getValue();
		type.VolLineStart.z = (float) tfVolLineStartZ.getValue();
		type.VolLineEnd.x = (float) tfVolLineEndX.getValue();
		type.VolLineEnd.y = (float) tfVolLineEndY.getValue();
		type.VolLineEnd.z = (float) tfVolLineEndZ.getValue();
		type.VolSphereRadius = (float) tfVolSphereRadius.getValue();

		renderer.editPanel.updateParticleCode();
		renderer.editPanel.particleEditPerformed();
	}
	
	
	public void newColorEntry(colorEntry pos) {
		ParticleSystemType type = Main.activeParticleSystemType;
		int i = colorEntryList.indexOf(pos)+1;
		colorEntry entry = type.new colorEntry(0, 0, 0, 0);
		colorEntryList.add(i, entry);
		ColorEntryPanel cPanel = new ColorEntryPanel(renderer, entry);
		panel_ColorEntries.add(cPanel, i);
		colorEntriesFromList();
	}
	public void newAlphaEntry(alphaEntry pos) {
		ParticleSystemType type = Main.activeParticleSystemType;
		int i = alphaEntryList.indexOf(pos)+1;
		alphaEntry entry = type.new alphaEntry(0, 0, 0);
		alphaEntryList.add(i, entry);
		AlphaEntryPanel aPanel = new AlphaEntryPanel(renderer, entry);
		panel_AlphaEntries.add(aPanel, i);
		alphaEntriesFromList();
	}
	
	public void alphaEntriesToList(){
		ParticleSystemType type = Main.activeParticleSystemType;
		this.alphaEntryList = new ArrayList<ParticleSystemType.alphaEntry>();
		if (type.Alpha1 != null) alphaEntryList.add(type.Alpha1);
		if (type.Alpha2 != null) alphaEntryList.add(type.Alpha2);
		if (type.Alpha3 != null) alphaEntryList.add(type.Alpha3);
		if (type.Alpha4 != null) alphaEntryList.add(type.Alpha4);
		if (type.Alpha5 != null) alphaEntryList.add(type.Alpha5);
		if (type.Alpha6 != null) alphaEntryList.add(type.Alpha6);
		if (type.Alpha7 != null) alphaEntryList.add(type.Alpha7);
		if (type.Alpha8 != null) alphaEntryList.add(type.Alpha8);
		if (type.Alpha9 != null) alphaEntryList.add(type.Alpha9);
	}
	public void alphaEntriesFromList(){
		ParticleSystemType type = Main.activeParticleSystemType;
		//type.Alpha1 = null;
		type.Alpha2 = null;
		type.Alpha3 = null;
		type.Alpha4 = null;
		type.Alpha5 = null;
		type.Alpha6 = null;
		type.Alpha7 = null;
		type.Alpha8 = null;
		type.Alpha9 = null;		
		int i = 0;
		if (i < alphaEntryList.size()) type.Alpha1 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha2 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha3 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha4 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha5 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha6 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha7 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha8 = alphaEntryList.get(i++); else return;
		if (i < alphaEntryList.size()) type.Alpha9 = alphaEntryList.get(i++); else return;
	}
	public void colorEntriesToList(){
		ParticleSystemType type = Main.activeParticleSystemType;
		this.colorEntryList = new ArrayList<ParticleSystemType.colorEntry>();
		if (type.Color1 != null) colorEntryList.add(type.Color1);
		if (type.Color2 != null) colorEntryList.add(type.Color2);
		if (type.Color3 != null) colorEntryList.add(type.Color3);
		if (type.Color4 != null) colorEntryList.add(type.Color4);
		if (type.Color5 != null) colorEntryList.add(type.Color5);
		if (type.Color6 != null) colorEntryList.add(type.Color6);
		if (type.Color7 != null) colorEntryList.add(type.Color7);
		if (type.Color8 != null) colorEntryList.add(type.Color8);
		if (type.Color9 != null) colorEntryList.add(type.Color9);

	}
	public void colorEntriesFromList(){
		ParticleSystemType type = Main.activeParticleSystemType;
		type.Color2 = null;
		type.Color3 = null;
		type.Color4 = null;
		type.Color5 = null;
		type.Color6 = null;
		type.Color7 = null;
		type.Color8 = null;
		type.Color9 = null;
		int i = 0;
		if (i < colorEntryList.size()) type.Color1 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color2 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color3 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color4 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color5 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color6 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color7 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color8 = colorEntryList.get(i++); else return;
		if (i < colorEntryList.size()) type.Color9 = colorEntryList.get(i++); else return;
	}
	
	public void createAlphaEntryPanels() {
		panel_AlphaEntries.removeAll();
		for (alphaEntry entry : alphaEntryList) {
			AlphaEntryPanel aPanel = new AlphaEntryPanel(renderer, entry);
			panel_AlphaEntries.add(aPanel);
		}
		updateAlphaPanels();
	}
	
	public void removeAlphaEntry(alphaEntry aEntry) {
		alphaEntryList.remove(aEntry);
		alphaEntriesFromList();
	}
	
	public void createColorEntryPanels() {
		panel_ColorEntries.removeAll();
		for (colorEntry entry : colorEntryList) {
			ColorEntryPanel cPanel = new ColorEntryPanel(renderer, entry);
			panel_ColorEntries.add(cPanel);
		}
		updateColorPanels();
	}
	
	public void removeColorEntry(colorEntry cEntry) {
		colorEntryList.remove(cEntry);
		colorEntriesFromList();
	}
	
	public void updateAlphaPanels() {
		int size = alphaEntryList.size();
		for (Component c : panel_AlphaEntries.getComponents()) {
			if (c instanceof AlphaEntryPanel) {
				AlphaEntryPanel p = (AlphaEntryPanel)c;				
				p.btn_Remove.setEnabled(size > 1);
				p.btn_Add.setEnabled(size < 9);
			}
		}
	}
	public void moveAlphaEntry(AlphaEntryPanel alphaEntryPanel, int dir) {
		//Component:
		int index = this.panel_AlphaEntries.getComponentZOrder(alphaEntryPanel);
		index += dir;
		if (index >= 0 && index < this.panel_AlphaEntries.getComponentCount()) {
			this.panel_AlphaEntries.setComponentZOrder(alphaEntryPanel, index);
			//Entry:
			index = alphaEntryList.indexOf(alphaEntryPanel.aEntry);
			int index2 = index+dir;
			alphaEntryList.set(index, alphaEntryList.get(index2));
			alphaEntryList.set(index2, alphaEntryPanel.aEntry);
			alphaEntriesFromList();
		}
	}
	public void updateColorPanels() {
		int size = colorEntryList.size();
		for (Component c : panel_ColorEntries.getComponents()) {
			if (c instanceof ColorEntryPanel) {
				ColorEntryPanel p = (ColorEntryPanel)c;				
				p.btn_Remove.setEnabled(size > 1);
				p.btn_Add.setEnabled(size < 9);
			}
		}
	}
	public void moveColorEntry(ColorEntryPanel colorEntryPanel, int dir) {
		//Component:
		int index = this.panel_ColorEntries.getComponentZOrder(colorEntryPanel);
		index += dir;
		if (index >= 0 && index < this.panel_ColorEntries.getComponentCount()) {
			this.panel_ColorEntries.setComponentZOrder(colorEntryPanel, index);
			//Entry:
			index = colorEntryList.indexOf(colorEntryPanel.cEntry);
			int index2 = index+dir;
			colorEntryList.set(index, colorEntryList.get(index2));
			colorEntryList.set(index2, colorEntryPanel.cEntry);
			colorEntriesFromList();
		}
	}
	
	public void getTextureFromFile() {
		String oldTexture = (String) cbTexture.getSelectedItem();
		ignoreChanges = true;
		
    	//Set up the file chooser.
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(Config.TextureFolder1));
        FileFilter filter = new FileNameExtensionFilter(
        	    "Image files", ImageIO.getReaderFileSuffixes());
        fc.addChoosableFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);
            
	    //Add the preview pane.
        fc.setAccessory(new ImagePreview(fc));

        //Show it.
        int returnVal = fc.showDialog(this, "Accept");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String texturename = Util.getTrimmedFilename(file).toLowerCase();
			if (!renderer.TextureMap.containsKey(texturename)) {
				try {
					BufferedImage image = ImageIO.read(file);
					TextureLoader loader = new TextureLoader(image);
					Texture texture = loader.getTexture();
					texture.setBoundaryModeS(Texture.WRAP);
					texture.setBoundaryModeT(Texture.WRAP);
					texture.setCapability(Texture.ALLOW_IMAGE_READ);
					texture.getImage(0).setCapability(ImageComponent.ALLOW_IMAGE_READ);
					renderer.TextureMap.put(texturename, texture);
					System.out.println("Loaded Texture "+file.getAbsolutePath());
					Main.activeParticleSystemType.ParticleName = texturename;
					this.loadTextureNames();
					cbTexture.setSelectedItem(texturename);
					Main.activeParticleSystemType.ParticleName = texturename+".tga";
					
				} catch (IOException e) {
					System.out.println("Couldn't read file: "+file.getAbsolutePath());
				}
			}else {
				cbTexture.setSelectedItem(texturename);
			}
            
        }

        ignoreChanges = false;
    }
	public JComboBox getCbAttachedSystem() {
		return cbAttachedSystem;
	}
	public JComboBox getCbSlavedSystem() {
		return cbSlavedSystem;
	}
}
