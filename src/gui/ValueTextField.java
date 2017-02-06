package gui;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextField;

import util.Util;

public class ValueTextField extends JTextField{
	
	public final static int VALUE_FLOAT = 0;
	public final static int VALUE_INT = 1;
	
	private int valueType = 0;
	
	public ValueTextField(int valType) {
		super();
		this.valueType = valType;
		this.setText("0");
		
		addActionListener(new AbstractAction() {
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
		        verifyText();
		        firePropertyChange("value", null, ValueTextField.this.getValue());
		    }
		});
		
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				verifyText();
				firePropertyChange("value", null, ValueTextField.this.getValue());
			}			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
	}
	
	
	private void verifyText() {
		try {
			if (valueType == VALUE_INT) {
				setText(""+(int)(Float.parseFloat(getText())));
			}else if (valueType == VALUE_FLOAT) {
				setText(Util.fmt(Float.parseFloat(getText())));
			}
		} catch (NumberFormatException ex) {
			setText(""+0);
		}
	}

	public Object getValue() {
		if (valueType == VALUE_INT) {
			return Integer.parseInt(getText());
		}else if (valueType == VALUE_FLOAT) {
			return Float.parseFloat(getText());
		}
		return 0;
	}

	public void setValue(int i) {
		setText(""+i);
	}

	public void setValue(float f) {
		setText(Util.fmt(f));
	}
}
