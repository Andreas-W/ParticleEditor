package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import main.Renderer;

public class TexturePreviewFrame extends JWindow {

	private JPanel contentPane;
	private JLabel lblTexture;
	
	public Renderer renderer;

	/**
	 * Create the frame.
	 */
	public TexturePreviewFrame(Renderer rend) {
		this.renderer = rend;
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		lblTexture = new JLabel("");
		contentPane.add(lblTexture, BorderLayout.CENTER);
	}
	
	public void showTexture(String textureName) {
		Texture texture = renderer.TextureMap.get(textureName);
		ImageComponent2D imgC = (ImageComponent2D) texture.getImage(0);
		//imgC.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);
		int width = imgC.getWidth();
		int height = imgC.getHeight();
		int x = renderer.editPanel.getX()-width;
		int y = 200;
		this.setBounds(x, y, width, height);
		lblTexture.setIcon(new ImageIcon(imgC.getImage()));
	}

}
