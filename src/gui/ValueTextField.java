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
	
	private Object value = null;
	
	public ValueTextField(int valType) {
		super();
		this.valueType = valType;
		this.setText("0");
		verifyText();
		
		addActionListener(new AbstractAction() {
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				Object oldValue = value;
		        verifyText();
		        firePropertyChange("value", oldValue, ValueTextField.this.getValue());
		    }
		});
		
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				Object oldValue = value;
				verifyText();
				firePropertyChange("value", oldValue, ValueTextField.this.getValue());
			}			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
	}
	
	
	private void verifyText() {
		try {
			if (valueType == VALUE_INT) {
				value = (int)(Float.parseFloat(getText())); 
				setText(""+value);
			}else if (valueType == VALUE_FLOAT) {
				value = Float.parseFloat(getText());
				setText(Util.fmt((float)value));
			}
		} catch (NumberFormatException ex) {
			setText(""+0);
		}
	}

	public Object getValue() {
//		if (valueType == VALUE_INT) {
//			return Integer.parseInt(getText());
//		}else if (valueType == VALUE_FLOAT) {
//			return Float.parseFloat(getText());
//		}
//		return 0;
		return value;
	}

	public void setValue(int i) {
		setText(""+i);
		value = i;
	}

	public void setValue(float f) {
		setText(Util.fmt(f));
		value = f;
	}
}
