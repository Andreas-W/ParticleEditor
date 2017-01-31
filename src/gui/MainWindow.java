package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import main.Main;
import main.Renderer;
import net.miginfocom.swing.MigLayout;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

	
	public Renderer renderer;
	
	private JPanel contentPane;
	private JLabel lblFps_val;
	private JButton btnReset;
	private JToggleButton tglbtnAutoplay;

	public boolean running = true;
	public boolean reset = false;
	
	private JToggleButton tglbtnPlay;
	private JSlider sld_FPS;
	/**
	 * Create the frame.
	 */
	public MainWindow(Renderer rend) {
		this.renderer = rend;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 690, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JToolBar toolBar = new JToolBar();
		panel_1.add(toolBar);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset = true;
			}
		});
		
		tglbtnPlay = new JToggleButton("Pause");
		toolBar.add(tglbtnPlay);
		toolBar.add(btnReset);
		
		tglbtnAutoplay = new JToggleButton("Repeat");
		tglbtnAutoplay.setSelected(true);
		toolBar.add(tglbtnAutoplay);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		toolBar.add(panel);
		
		JLabel lblFps = new JLabel("FPS:");
		panel.add(lblFps);
		
		sld_FPS = new JSlider();
		panel.add(sld_FPS);
		sld_FPS.setMajorTickSpacing(5);
		sld_FPS.setSnapToTicks(true);
		sld_FPS.setMinorTickSpacing(1);
		sld_FPS.setMinimum(15);
		sld_FPS.setMaximum(60);
		sld_FPS.setValue(30);
		sld_FPS.setPreferredSize(new Dimension(150, 25));
		sld_FPS.addChangeListener(new ChangeListener() {
	      public void stateChanged(ChangeEvent event) {
	        int value = sld_FPS.getValue();
	        lblFps_val.setText(""+value);
	        Main.FPS = value;
	      }
		});
		
		lblFps_val = new JLabel("30");
		panel.add(lblFps_val);
		
		JToolBar toolBar_1 = new JToolBar();
		panel_1.add(toolBar_1);
		
		JButton btnResetView = new JButton("Reset View");
		btnResetView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				renderer.resetCamera();
			}
		});
		toolBar_1.add(btnResetView);
	}

	public JLabel getLblFps_val() {
		return lblFps_val;
	}
	public JButton getBtnReset() {
		return btnReset;
	}
	public JToggleButton getTglbtnAutoplay() {
		return tglbtnAutoplay;
	}
	public JPanel getContentPane() {
		return contentPane;
	}

	public boolean isRunning() {
		return running;
	}
	public JToggleButton getTglbtnPlay() {
		return tglbtnPlay;
	}
	public JSlider getSld_FPS() {
		return sld_FPS;
	}
}
