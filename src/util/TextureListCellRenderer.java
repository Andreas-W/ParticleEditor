package util;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

import main.Renderer;

public class TextureListCellRenderer extends DefaultListCellRenderer {

	public Renderer renderer;
	
	public TextureListCellRenderer(Renderer rend) {
		this.renderer = rend;
	}
	
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);

        if (isSelected && index > -1 && value != null && !renderer.editPanel.particleEditPanel.ignoreChanges) {
        	//System.out.println("ListItem value=" +value + ", isSelected="+isSelected+", cellHasFocus="+ cellHasFocus +", index="+index);
        	renderer.texturePreview.showTexture((String)value);
        	renderer.texturePreview.setVisible(true);
        }
        return comp;
    }

}
