package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.Texture;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import util.Util;

import com.sun.j3d.utils.image.TextureLoader;
import components.ImagePreview;

import main.Config;
import main.Main;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ViewConfig extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfGroundTexture;
	private JRadioButton rbGrid;
	private JRadioButton rbTexture;
	
	private Color bgColor_OLD;
	private Color gridColor_OLD;
	private String groundTex_OLD;
	private float groundSize_OLD;
	private float groundRes_OLD;
	
	private JButton btnBGColor;
	private JButton btnGridColor;
	private JButton btnPick;
	private ValueTextField tfGroundSize;
	private ValueTextField tfGroundResolution;
	private byte groundType_OLD = Config.GroundType;

	private boolean init = false;
	
	/**
	 * Create the dialog.
	 */
	public ViewConfig() {
		this.setModal(true);
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPanel.add(panel);
		
		JLabel lblBackgroundColor = new JLabel("Background Color:");
		panel.add(lblBackgroundColor);
		
		bgColor_OLD = new Color(Config.BGColor);

		btnBGColor = new JButton("");
		btnBGColor.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnBGColor.setFocusPainted(false);
		btnBGColor.setContentAreaFilled(false);
		btnBGColor.setOpaque(true);
		btnBGColor.setMargin(new Insets(0,0,0,0));
		btnBGColor.setPreferredSize(new Dimension(24,24));
		btnBGColor.setBackground(bgColor_OLD);
		btnBGColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Color c = Util.pickColor("Choose Background Color", bgColor_OLD);
				if (c!= null) {
					btnBGColor.setBackground(c);
					Config.BGColor = c.getRGB();
					applyChanges();
				}		
			}
		});
		panel.add(btnBGColor);
		
		JSeparator separator = new JSeparator();
		separator.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPanel.add(separator);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		contentPanel.add(panel_2);
		
		JLabel lblGround = new JLabel("Ground Appearance:");
		panel_2.add(lblGround);
		lblGround.setHorizontalAlignment(SwingConstants.CENTER);
		lblGround.setAlignmentY(Component.TOP_ALIGNMENT);
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPanel.add(panel_1);
		
		ButtonGroup rbgroup = new ButtonGroup();
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		panel_1.add(separator_1);
		
		rbGrid = new JRadioButton("grid");
		rbGrid.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (init && e.getStateChange() == ItemEvent.SELECTED) updateGroundRBs();
			}
		});
		panel_1.add(rbGrid);
		
		gridColor_OLD = new Color(Config.GridColor);
		
		btnGridColor = new JButton("");
		btnGridColor.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnGridColor.setFocusPainted(false);
		btnGridColor.setContentAreaFilled(false);
		btnGridColor.setOpaque(true);
		btnGridColor.setMargin(new Insets(0,0,0,0));
		btnGridColor.setPreferredSize(new Dimension(24,24));
		btnGridColor.setBackground(gridColor_OLD);
		btnGridColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = Util.pickColor("Choose Grid Color", new Color(Config.GridColor));
				if (c!= null) {
					btnGridColor.setBackground(c);
					Config.GridColor = c.getRGB();
					applyChanges();
				}		
			}
		});
		btnGridColor.setEnabled(false);
		panel_1.add(btnGridColor);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		panel_1.add(separator_2);
		
		rbTexture = new JRadioButton("texture");
		rbTexture.setSelected(true);
		rbTexture.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (init && e.getStateChange() == ItemEvent.SELECTED) updateGroundRBs();
			}
		});
		panel_1.add(rbTexture);
		rbgroup.add(rbGrid);
		rbgroup.add(rbTexture);
		if (groundType_OLD == 0) rbGrid.setSelected(true);
		else if (groundType_OLD == 1) rbTexture.setSelected(true);
		
		groundTex_OLD = Config.GroundTexture;
		tfGroundTexture = new JTextField();
		tfGroundTexture.setEditable(false);
		panel_1.add(tfGroundTexture);
		tfGroundTexture.setColumns(16);
		tfGroundTexture.setText(groundTex_OLD);
		
		groundTex_OLD = Config.GroundTexture;
		
		btnPick = new JButton("Pick");
		btnPick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		        int returnVal = fc.showDialog(ViewConfig.this, "Accept");

		        //Process the results.
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            tfGroundTexture.setText(file.getName());
		            Config.GroundTexture = file.getAbsolutePath();
		            applyChanges();
		        }
			}
		});
		panel_1.add(btnPick);
		
		JPanel panel_3 = new JPanel();
		panel_3.setAlignmentY(Component.TOP_ALIGNMENT);
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		contentPanel.add(panel_3);
		
		JLabel lblSize = new JLabel("Size:");
		panel_3.add(lblSize);
		
		groundSize_OLD = Config.GroundSize;
		tfGroundSize = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfGroundSize.setValue(groundSize_OLD);
		tfGroundSize.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("value")) {
					Config.GroundSize = (float) tfGroundSize.getValue();
					applyChanges();
				}
			}
		});
		panel_3.add(tfGroundSize);
		tfGroundSize.setColumns(10);
		
		JLabel lblResolution = new JLabel("Resolution:");
		panel_3.add(lblResolution);

		groundRes_OLD = Config.GroundResolution;
		tfGroundResolution = new ValueTextField(ValueTextField.VALUE_FLOAT);
		tfGroundResolution.setValue(groundRes_OLD);
		tfGroundResolution.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("value")) {
					Config.GroundResolution= (float) tfGroundResolution.getValue();
					applyChanges();
				}
			}
		});
		panel_3.add(tfGroundResolution);
		tfGroundResolution.setColumns(10);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelChanges();
				setVisible(false);
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		init = true;
		updateGroundRBs();
	}

	protected void updateGroundRBs() {
		tfGroundTexture.setEnabled(rbTexture.isSelected());
		btnPick.setEnabled(rbTexture.isSelected());
		btnGridColor.setEnabled(rbGrid.isSelected());
		
		if (rbTexture.isSelected()) Config.GroundType = 1;
		else if (rbGrid.isSelected()) Config.GroundType = 0;
			
		applyChanges();
	}
	
	protected void applyChanges() {
		//Update BG Color
		//Update Ground
		Main.renderer.setupGround();
		Main.renderer.setBackgroundColor(new Color(Config.BGColor));
	}
	
	protected void cancelChanges() {
		Config.BGColor = bgColor_OLD.getRGB();
		Config.GridColor = gridColor_OLD.getRGB();
		Config.GroundSize = groundSize_OLD;
		Config.GroundResolution = groundRes_OLD;
		Config.GroundType = groundType_OLD;
		Config.GroundTexture = groundTex_OLD;
		
		applyChanges();
	}

}
