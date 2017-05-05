package gui.filter;

import gui.ValueTextField;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;

import java.awt.Font;

public class ParticleFilterPanel extends JPanel {
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the panel.
	 */
	public ParticleFilterPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel_6 = new JPanel();
		add(panel_6);
		
		JLabel lblTitle = new JLabel("New label");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_6.add(lblTitle);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel);
		
		JRadioButton rdbtnApplyToParticlesystem = new JRadioButton("Apply to ParticleSystem");
		panel.add(rdbtnApplyToParticlesystem);
		buttonGroup.add(rdbtnApplyToParticlesystem);
		
		JRadioButton rdbtnApplyToWhole = new JRadioButton("Apply to whole FX (And all ParticleSystems)");
		panel.add(rdbtnApplyToWhole);
		buttonGroup.add(rdbtnApplyToWhole);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_1);
		
		JLabel lblScaleByFactor = new JLabel("Scale by factor:");
		panel_1.add(lblScaleByFactor);
		
		ValueTextField valueTextField = new ValueTextField(0);
		valueTextField.setColumns(5);
		panel_1.add(valueTextField);
		
		JPanel panel_4 = new JPanel();
		add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		panel_4.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("FX_Test", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JTextPane textPane = new JTextPane();
		panel_3.add(textPane);
		
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.WEST);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		
		JCheckBox checkBox = new JCheckBox("");
		panel_5.add(checkBox);
		
		JPanel panel_2 = new JPanel();
		add(panel_2);

	}
}
