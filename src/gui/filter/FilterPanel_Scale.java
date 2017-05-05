package gui.filter;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

import util.Undo;
import util.Undo.OperationType;
import util.Util;
import entitytypes.FXListType;
import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.ParticleSystemType.posEntry;
import entitytypes.ParticleSystemType;
import gui.ValueTextField;
import main.Main;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class FilterPanel_Scale extends FilterPanel {
	private JPanel panel_Entries;

	private boolean init = false;
	
	private HashMap<String, ParticleSystemType> backup_particleSystems = new HashMap<String, ParticleSystemType>();
	private FXListType backup_FXList;
	
	private HashMap<ParticleSystemEntry, JCheckBox> entry_checkboxes = new HashMap<ParticleSystemEntry, JCheckBox>();
	private ValueTextField tfSize;
	private ValueTextField tfSpeed;
	private ValueTextField tfColor;
	private ValueTextField tfAlpha;
	private ValueTextField tfPartSize;
	private ValueTextField tfPartCount;
	
	private PropertyChangeListener valueListener;
	/** 
	 * Create the panel.
	 */
	public FilterPanel_Scale() {
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_Values = new JPanel();
		panel_1.add(panel_Values);
		panel_Values.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_Values.setLayout(new GridLayout(0, 2, 0, 6));
		
		JLabel lblSize = new JLabel("Total Size");
		panel_Values.add(lblSize);
		
		tfSize = new ValueTextField(0);
		tfSize.setText("1");
		tfSize.setLoseFocusOnEnter(false);
	
		panel_Values.add(tfSize);
		
		JLabel lblNewLabel = new JLabel("Speed");
		panel_Values.add(lblNewLabel);
		
		tfSpeed = new ValueTextField(0);
		tfSpeed.setText("1");
		tfSpeed.setMinValue(0.5f);
		tfSpeed.setMaxValue(2.0f);
		tfSpeed.setLoseFocusOnEnter(false);
		panel_Values.add(tfSpeed);
		
		JLabel lblColor = new JLabel("Color");
		lblColor.setEnabled(false);
		panel_Values.add(lblColor);
		
		tfColor = new ValueTextField(0);
		tfColor.setEnabled(false);
		tfColor.setText("1");
		tfColor.setLoseFocusOnEnter(false);
		panel_Values.add(tfColor);
		
		JLabel lblAlpha = new JLabel("Alpha");
		lblAlpha.setEnabled(false);
		panel_Values.add(lblAlpha);
		
		tfAlpha = new ValueTextField(0);
		tfAlpha.setEnabled(false);
		tfAlpha.setText("1");
		tfAlpha.setLoseFocusOnEnter(false);
		
		panel_Values.add(tfAlpha);
		
		JLabel lblParticleSize = new JLabel("Particle Size");
		lblParticleSize.setEnabled(false);
		panel_Values.add(lblParticleSize);
		
		tfPartSize = new ValueTextField(0);
		tfPartSize.setEnabled(false);
		tfPartSize.setText("1");
		tfPartSize.setLoseFocusOnEnter(false);
		panel_Values.add(tfPartSize);
		
		JLabel lblParticleCount = new JLabel("Particle Count");
		lblParticleCount.setEnabled(false);
		panel_Values.add(lblParticleCount);
		
		tfPartCount = new ValueTextField(0);
		tfPartCount.setEnabled(false);
		tfPartCount.setText("1");
		tfPartCount.setLoseFocusOnEnter(false);
		panel_Values.add(tfPartCount);
		
		JLabel lblNewLabel_3 = new JLabel("Scale factors:");
		panel_2.add(lblNewLabel_3, BorderLayout.NORTH);
		
		JPanel panel_Selection = new JPanel();
		add(panel_Selection, BorderLayout.CENTER);
		panel_Selection.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Apply to:");
		panel_Selection.add(lblNewLabel_1, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_Selection.add(scrollPane, BorderLayout.CENTER);
		
		panel_Entries = new JPanel();
		panel_Entries.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setViewportView(panel_Entries);
		panel_Entries.setLayout(new BoxLayout(panel_Entries, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel_2 = new JLabel("Filter: Scale Particle/FX Values");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(lblNewLabel_2);
		
		valueListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (init && e.getPropertyName().equals("value")) {
					applyFilter((ValueTextField)e.getSource());
				}
			}
		};
		tfAlpha.addPropertyChangeListener(valueListener);
		tfColor.addPropertyChangeListener(valueListener);
		tfPartCount.addPropertyChangeListener(valueListener);
		tfPartSize.addPropertyChangeListener(valueListener);
		tfSize.addPropertyChangeListener(valueListener);
		tfSpeed.addPropertyChangeListener(valueListener);
		
		initBackups();
		initList();
		
		init = true;
	}

	private void initList() {
		FXListType type = Main.activeFXListType;
		if (type == null) return;
		
		panel_Entries.removeAll();
		for (ParticleSystemEntry entry : type.ParticleSystems) {
			JCheckBox chb = new JCheckBox(entry.Name);
			chb.setSelected(true);
			
			chb.addItemListener(new ItemListener() {			
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (init) {
						JCheckBox source = (JCheckBox)e.getSource();
						//TODO: Revert systems if deselected					
						if (e.getStateChange() == ItemEvent.SELECTED) {
							applyFilter(null);
						}else if( e.getStateChange() == ItemEvent.DESELECTED) {
							ParticleSystemEntry psentry = null;
							for (ParticleSystemEntry entry : entry_checkboxes.keySet()) {
								if (entry_checkboxes.get(entry) == source) {
									psentry = entry;
									break;
								}
							}
							if (psentry != null) {
								psentry.setValues(backup_FXList.ParticleSystems.get(Main.activeFXListType.ParticleSystems.indexOf(psentry)));
								Main.ParticleSystemTypes.get(psentry.Name).setValues(backup_particleSystems.get(psentry.Name));
								Main.renderer.mainWindow.reset = true;
							}							
						}
					}
				}
			});
			
			
			entry_checkboxes.put(entry, chb);
			panel_Entries.add(chb);
		}
		
	}
	
	public void initBackups() {
		backup_FXList = new FXListType(Main.activeFXListType);	
		for (ParticleSystemEntry entry : backup_FXList.ParticleSystems) {
			ParticleSystemType ptype = Main.getParticleSystem(entry.Name);
			if (ptype != null) {
				backup_particleSystems.put(entry.Name, new ParticleSystemType(ptype));
			}
		}
	}
	
	public void applyFilter(ValueTextField source) {
		//Main.renderer.editPanel.particleEditPanel.ignoreChanges = true;
		//Main.renderer.editPanel.ignoreChanges = true;
		
		FXListType FXlist = Main.activeFXListType;
		
		for (int i = 0; i < FXlist.ParticleSystems.size(); i++) {
			ParticleSystemEntry entry = FXlist.ParticleSystems.get(i);
			ParticleSystemEntry o_entry = backup_FXList.ParticleSystems.get(i);
			if (entry_checkboxes.containsKey(entry) && entry_checkboxes.get(entry).isSelected()) {
				ParticleSystemType ptype = Main.getParticleSystem(entry.Name);
				if (ptype != null) {
					filterParticleSystem(entry, o_entry, ptype, source);
				}
			}
		}	
		Main.renderer.mainWindow.reset = true;
	}

	//TODO: Scale Speed and Size at the same time!!!
	
	private void filterParticleSystem(ParticleSystemEntry entry, ParticleSystemEntry o_entry, ParticleSystemType ptype, ValueTextField source) {
		ParticleSystemType o_ptype = backup_particleSystems.get(entry.Name);
		float f = 1.0f;
		if ((float)tfSize.getValue() != 1.0f || source == tfSize) {
			f = (float) tfSize.getValue();
			if (o_entry.Offset != null && Util.hasValues(o_entry.Offset)) {
				entry.Offset[0]=o_entry.Offset[0]*f;
				entry.Offset[1]=o_entry.Offset[1]*f;
				entry.Offset[2]=o_entry.Offset[2]*f;
			}
			if (o_entry.Height != null && Util.hasValues(o_entry.Height.data)) {
				entry.Height.data[0]=o_entry.Height.data[0]*f;
				entry.Height.data[1]=o_entry.Height.data[1]*f;
			}
			if (o_entry.Radius != null && Util.hasValues(o_entry.Radius.data)) {
				entry.Radius.data[0]=o_entry.Radius.data[0]*f;
				entry.Radius.data[1]=o_entry.Radius.data[1]*f;
			}
			scaleParticleField("Size", ptype, o_ptype, f);
			scaleParticleField("SizeRate", ptype, o_ptype, f);
			scaleParticleField("DriftVelocity", ptype, o_ptype, f);
			switch (ptype.VelocityType) {
				case CYLINDRICAL:
					scaleParticleField("VelCylindricalNormal", ptype, o_ptype, f);
					scaleParticleField("VelCylindricalRadial", ptype, o_ptype, f);
					break;
				case HEMISPHERICAL:
					scaleParticleField("VelHemispherical", ptype, o_ptype, f);
					break;
				case ORTHO:
					scaleParticleField("VelOrthoX", ptype, o_ptype, f);
					scaleParticleField("VelOrthoY", ptype, o_ptype, f);
					scaleParticleField("VelOrthoZ", ptype, o_ptype, f);
					break;
				case OUTWARD:
					scaleParticleField("VelOutward", ptype, o_ptype, f);
					scaleParticleField("VelOutwardOther", ptype, o_ptype, f);
					break;
				case SPHERICAL:
					scaleParticleField("VelSpherical", ptype, o_ptype, f);
					break;
			}
			switch (ptype.VolumeType) {
				case BOX:
					scaleParticleField("VolBoxHalfSize", ptype, o_ptype, f);
					break;
				case CYLINDER:
					scaleParticleField("VolCylinderLength", ptype, o_ptype, f);
					scaleParticleField("VolCylinderRadius", ptype, o_ptype, f);
					break;
				case LINE:
					scaleParticleField("VolLineStart", ptype, o_ptype, f);
					scaleParticleField("VolLineEnd", ptype, o_ptype, f);
					break;
				case SPHERE:
					scaleParticleField("VolSphereRadius", ptype, o_ptype, f);
					break;			
			}
			scaleParticleField("Gravity", ptype, o_ptype, f);
			
		}
		float f1 = 1.0f;
		if ((float)tfSpeed.getValue() != 1.0f || source == tfSpeed) {
			f1 = (float) tfSpeed.getValue();
			float f2 = (f1 != 0.0f) ? (1.0f/f1) : 0.0f;
			//float f3 = 1.0f-((1.0f-f2)*2f);
			//FX
			if (entry.InitialDelay != null && Util.hasValues(o_entry.InitialDelay.data)) {
				entry.InitialDelay.data[0] = (int) (o_entry.InitialDelay.data[0]*f2);
				entry.InitialDelay.data[1] = (int) (o_entry.InitialDelay.data[1]*f2);
			}
			//Particles
			scaleParticleField("SystemLifetime", ptype, o_ptype, f2);
			scaleParticleField("Lifetime", ptype, o_ptype, f2);
			scaleParticleField("InitialDelay", ptype, o_ptype, f2);
			
			//Delay/Count
			if (o_ptype.BurstDelay[0] > 0) {
				float bd1 = (float)(o_ptype.BurstDelay[0]+1) *f2;
				float bd1_b = (int) bd1;	
				float c1 = 1.0f + (bd1 - bd1_b) / bd1_b;		
				ptype.BurstDelay[0] = (int)bd1_b;
				ptype.BurstCount[0] = (int) (o_ptype.BurstCount[0] * c1);
			}else {
				ptype.BurstCount[0] = (int) Math.max(1.0f, o_ptype.BurstCount[0]*f1);
			}
			if (o_ptype.BurstDelay[1] > 0) {
				float bd2 = (float)(o_ptype.BurstDelay[1]+1) *f2;
				float bd2_b = (int) bd2;		
				float c2 = 1.0f + (bd2 - bd2_b) / bd2_b;		
				ptype.BurstDelay[1] = (int)bd2_b;		
				ptype.BurstCount[1] = (int) (o_ptype.BurstCount[1] * c2);	
			}else {
				ptype.BurstCount[1] = (int) Math.max(1.0f, o_ptype.BurstCount[1]*f1);
			}
			
			scaleParticleField("SizeRate", ptype, o_ptype, f1*f);
			//scaleParticleField("SizeRateDamping", ptype, o_ptype, f3);
			ptype.SizeRateDamping[0] = 1.0f-((1.0f-o_ptype.SizeRateDamping[0])*f1);
			ptype.SizeRateDamping[1] = 1.0f-((1.0f-o_ptype.SizeRateDamping[1])*f1);
			
			scaleParticleField("AngularRateZ", ptype, o_ptype, f1);
			//scaleParticleField("AngularDamping", ptype, o_ptype, f3);
			ptype.AngularDamping[0] = 1.0f-((1.0f-o_ptype.AngularDamping[0])*f1);
			ptype.AngularDamping[1] = 1.0f-((1.0f-o_ptype.AngularDamping[1])*f1);
			
			scaleParticleField("Gravity", ptype, o_ptype, f1*f);
			scaleParticleField("DriftVelocity", ptype, o_ptype, f1*f);
			//scaleParticleField("VelocityDamping", ptype, o_ptype, f3);
			ptype.VelocityDamping[0] = 1.0f-((1.0f-o_ptype.VelocityDamping[0])*f1);
			ptype.VelocityDamping[1] = 1.0f-((1.0f-o_ptype.VelocityDamping[1])*f1);
			
			switch (ptype.VelocityType) {
				case CYLINDRICAL:
					scaleParticleField("VelCylindricalNormal", ptype, o_ptype, f1*f);
					scaleParticleField("VelCylindricalRadial", ptype, o_ptype, f1*f);
					break;
				case HEMISPHERICAL:
					scaleParticleField("VelHemispherical", ptype, o_ptype, f1*f);
					break;
				case ORTHO:
					scaleParticleField("VelOrthoX", ptype, o_ptype, f1*f);
					scaleParticleField("VelOrthoY", ptype, o_ptype, f1*f);
					scaleParticleField("VelOrthoZ", ptype, o_ptype, f1*f);
					break;
				case OUTWARD:
					scaleParticleField("VelOutward", ptype, o_ptype, f1*f);
					scaleParticleField("VelOutwardOther", ptype, o_ptype, f1*f);
					break;
				case SPHERICAL:
					scaleParticleField("VelSpherical", ptype, o_ptype, f1*f);
					break;
			}
			
			if (o_ptype.Alpha1 != null && ptype.Alpha1.frame != 0) ptype.Alpha1.frame = (int)Math.max(1.0f, o_ptype.Alpha1.frame * f2);
			if (o_ptype.Alpha2 != null && ptype.Alpha2.frame != 0) ptype.Alpha2.frame = (int)Math.max(1.0f, o_ptype.Alpha2.frame * f2);
			if (o_ptype.Alpha3 != null && ptype.Alpha3.frame != 0) ptype.Alpha3.frame = (int)Math.max(1.0f, o_ptype.Alpha3.frame * f2);
			if (o_ptype.Alpha4 != null && ptype.Alpha4.frame != 0) ptype.Alpha4.frame = (int)Math.max(1.0f, o_ptype.Alpha4.frame * f2);
			if (o_ptype.Alpha5 != null && ptype.Alpha5.frame != 0) ptype.Alpha5.frame = (int)Math.max(1.0f, o_ptype.Alpha5.frame * f2);
			if (o_ptype.Alpha6 != null && ptype.Alpha6.frame != 0) ptype.Alpha6.frame = (int)Math.max(1.0f, o_ptype.Alpha6.frame * f2);
			if (o_ptype.Alpha7 != null && ptype.Alpha7.frame != 0) ptype.Alpha7.frame = (int)Math.max(1.0f, o_ptype.Alpha7.frame * f2);
			if (o_ptype.Alpha8 != null && ptype.Alpha8.frame != 0) ptype.Alpha8.frame = (int)Math.max(1.0f, o_ptype.Alpha8.frame * f2);
			if (o_ptype.Alpha9 != null && ptype.Alpha9.frame != 0) ptype.Alpha9.frame = (int)Math.max(1.0f, o_ptype.Alpha9.frame * f2);
			
			if (o_ptype.Color1 != null && ptype.Color1.frame != 0) ptype.Color1.frame = (int)Math.max(1.0f, o_ptype.Color1.frame * f2);
			if (o_ptype.Color2 != null && ptype.Color2.frame != 0) ptype.Color2.frame = (int)Math.max(1.0f, o_ptype.Color2.frame * f2);
			if (o_ptype.Color3 != null && ptype.Color3.frame != 0) ptype.Color3.frame = (int)Math.max(1.0f, o_ptype.Color3.frame * f2);
			if (o_ptype.Color4 != null && ptype.Color4.frame != 0) ptype.Color4.frame = (int)Math.max(1.0f, o_ptype.Color4.frame * f2);
			if (o_ptype.Color5 != null && ptype.Color5.frame != 0) ptype.Color5.frame = (int)Math.max(1.0f, o_ptype.Color5.frame * f2);
			if (o_ptype.Color6 != null && ptype.Color6.frame != 0) ptype.Color6.frame = (int)Math.max(1.0f, o_ptype.Color6.frame * f2);
			if (o_ptype.Color7 != null && ptype.Color7.frame != 0) ptype.Color7.frame = (int)Math.max(1.0f, o_ptype.Color7.frame * f2);
			if (o_ptype.Color8 != null && ptype.Color8.frame != 0) ptype.Color8.frame = (int)Math.max(1.0f, o_ptype.Color8.frame * f2);
			if (o_ptype.Color9 != null && ptype.Color9.frame != 0) ptype.Color9.frame = (int)Math.max(1.0f, o_ptype.Color9.frame * f2);
		}
	}
	
	private void scaleParticleField (String attr, ParticleSystemType ptype, ParticleSystemType o_ptype, float f) {
		try {
			Field field = ParticleSystemType.class.getField(attr);
			Class c = field.getType();
			if (c == Integer.TYPE) {
				field.set(ptype, (int)Math.max(1.0f,(int)field.get(o_ptype)*f));				
			}else if (c == Float.TYPE) {
				field.set(ptype, ((float)field.get(o_ptype)*f));				
			}else if (c == float[].class) {
				float[] data = (float[])field.get(o_ptype);
				float[] data2 = (float[])field.get(ptype);
				if (data != null && data2 != null) {
					for (int i = 0; i < data.length; i++) {
						data2[i] = data[i] * f;
					}
				}
			}else if (c == int[].class) {
				int[] data = (int[])field.get(o_ptype);
				int[] data2 = (int[])field.get(ptype);
				if (data != null && data2 != null) {
					for (int i = 0; i < data.length; i++) {
						data2[i] = (int) Math.max(1.0f, data[i] * f);
					}
				}
			}else if (c == posEntry.class) {
				posEntry p1 = (posEntry)field.get(o_ptype);
				posEntry p2 = (posEntry)field.get(ptype);
				if (p1 != null && p2 != null && !p1.isZero()) {
					p2.x = p1.x*f;
					p2.y = p1.y*f;
					p2.z = p1.z*f;
				}
			}
					
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void accept() {
		boolean editperformed = false;
		FXListType FXlist = Main.activeFXListType;
		
		for (int i = 0; i < FXlist.ParticleSystems.size(); i++) {
			ParticleSystemEntry entry = FXlist.ParticleSystems.get(i);
			ParticleSystemEntry o_entry = backup_FXList.ParticleSystems.get(i);
			if (entry_checkboxes.containsKey(entry) && entry_checkboxes.get(entry).isSelected()) {
				editperformed = true;
				ParticleSystemType ptype = Main.getParticleSystem(entry.Name);
				if (ptype != null) {
					if (!Main.work_ParticleSystemTypes.containsKey(entry.Name)) {
						Main.work_ParticleSystemTypes.put(entry.Name, ptype);					
					}
				}
			}
		}			
		if (editperformed) {
			if (!Main.activeFXListType.isTemporary()) {
				String fName = Main.activeFXName();
				if (!Main.work_FXListTypes.containsKey(fName)) {
					Main.work_FXListTypes.put(fName, Main.activeFXListType);
				}
			}
			Undo.performFilterOperation(Main.activeFXName(),backup_FXList, backup_particleSystems, "Filter: Scale Values", OperationType.EDIT);
			Main.renderer.browsePanel.browse_Working.fillList(Main.work_FXListTypes.keySet(), Main.work_ParticleSystemTypes.keySet());
		}
		

		
		Main.renderer.editPanel.updateFXGUI();
		Main.renderer.editPanel.updateFXCode();
		Main.renderer.editPanel.updateParticleGUI();
		Main.renderer.editPanel.updateParticleCode();
	}

	@Override
	public void revert() {
		String fname = Main.activeFXName();
		Main.FXListTypes.put(fname, backup_FXList);
		Main.work_FXListTypes.put(fname, backup_FXList);
		Main.renderer.updateActiveFX(backup_FXList, fname);
		
		for (String pname : backup_particleSystems.keySet()) {
			Main.ParticleSystemTypes.put(pname, backup_particleSystems.get(pname));
		}
		Main.renderer.mainWindow.reset = true;
	}

}
