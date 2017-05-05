package gui.filter;

import javax.swing.JPanel;

public abstract class FilterPanel extends JPanel{
	public abstract void accept();
	public abstract void revert();
}
