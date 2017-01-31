package gui;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import java.awt.FlowLayout;
import javax.swing.JSpinner;

public class StatusPanel extends JPanel {
	private JLabel lbl_frame;
	private JLabel lbl_minFPS;
	private JLabel lbl_pCount;
	private JLabel lbl_FPS;
	private JLabel lbl_maxParticles;

	/**
	 * Create the panel.
	 */
	public StatusPanel() {
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		setLayout(new GridLayout(1, 5, 0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		add(panel);
		
		JLabel lbl_pCount_txt = new JLabel("Particle Count:");
		panel.add(lbl_pCount_txt);
		
		lbl_pCount = new JLabel("");
		lbl_pCount.setHorizontalAlignment(SwingConstants.TRAILING);
		panel.add(lbl_pCount);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_1.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		add(panel_1);
		
		JLabel lbl_frame_txt = new JLabel("Frame:");
		panel_1.add(lbl_frame_txt);
		
		lbl_frame = new JLabel("");
		lbl_frame.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_1.add(lbl_frame);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		add(panel_2);
		
		JLabel lbl_FPS_txt = new JLabel("FPS:");
		panel_2.add(lbl_FPS_txt);
		
		lbl_FPS = new JLabel("");
		lbl_FPS.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(lbl_FPS);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_3);
		
		JLabel lbl_minFPS_txt = new JLabel("min. FPS:");
		panel_3.add(lbl_minFPS_txt);
		
		lbl_minFPS = new JLabel("");
		lbl_minFPS.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_3.add(lbl_minFPS);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel_4);
		
		JLabel lbl_maxParticles_txt = new JLabel("max. Particles:");
		panel_4.add(lbl_maxParticles_txt);
		
		lbl_maxParticles = new JLabel("");
		lbl_maxParticles.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_4.add(lbl_maxParticles);

	}
	
	public void updateStatus(int particleCount, int fps, int minFPS, int maxParticles, int totalFrames) {
		if (particleCount != -1) lbl_pCount.setText(""+particleCount);
		if (fps != -1) lbl_FPS.setText(""+fps);
		if (minFPS != -1) lbl_minFPS.setText(""+minFPS);
		if (maxParticles != -1) lbl_maxParticles.setText(""+maxParticles);
		if (totalFrames != -1) lbl_frame.setText(""+totalFrames);
	}

	public JLabel getLbl_frame() {
		return lbl_frame;
	}
	public JLabel getLbl_minFPS() {
		return lbl_minFPS;
	}
	public JLabel getLbl_pCount() {
		return lbl_pCount;
	}
	public JLabel getLbl_FPS() {
		return lbl_FPS;
	}
	public JLabel getLbl_maxParticles() {
		return lbl_maxParticles;
	}
}
