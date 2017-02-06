package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JComboBox;

import main.Main;
import main.Renderer;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewParticleDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfParticleName;
	private JRadioButton rbEmpty;

	/**
	 * Create the dialog.
	 */
	public Renderer renderer;
	private JComboBox cbParticleSystems;
	private JRadioButton rbClone;
	
	int result = 0;
	
	public NewParticleDialog(Renderer rend, String name) {
		this.renderer = rend;
		setModal(true);
		setBounds(100, 100, 443, 231);
		
		ButtonGroup group = new ButtonGroup();
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				JLabel lblAddNewParticlesystem = new JLabel("Add New ParticleSystem");
				lblAddNewParticlesystem.setFont(new Font("Tahoma", Font.BOLD, 12));
				panel.add(lblAddNewParticlesystem);
			}
			{
				tfParticleName = new JTextField(name);
				panel.add(tfParticleName);
				tfParticleName.setColumns(22);
			}
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel1 = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel1.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel1);
			{
				rbEmpty = new JRadioButton("Create Empty");
				rbEmpty.setSelected(true);
				group.add(rbEmpty);
				panel1.add(rbEmpty);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			cbParticleSystems = new JComboBox(Main.ParticleSystemNames.toArray());
			cbParticleSystems.setEnabled(false);
			contentPanel.add(panel);
			{
				rbClone = new JRadioButton("Clone Existing");
				rbClone.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent arg0) {
						cbParticleSystems.setEnabled(rbClone.isSelected());
					}
				});
				group.add(rbClone);
				panel.add(rbClone);
			}
			panel.add(cbParticleSystems);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result = 1;
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result = 1;
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String getName() {
		return this.tfParticleName.getText();
	}
	
	public String getCloneFrom() {
		if (this.rbClone.isSelected()) {
			return (String) cbParticleSystems.getSelectedItem();
		}else {
			return null;
		}
	}

	public int showDialog() {
		setVisible(true);
		return result;
	}

}
