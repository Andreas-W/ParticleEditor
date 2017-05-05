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
	
	Object maxValue;
	Object minValue;
	
	public final static int VALUE_FLOAT = 0;
	public final static int VALUE_INT = 1;
	
	private int valueType = 0;
	
	private Object value = null;
	
	boolean loseFocusOnEnter = true;

	public ValueTextField(int valType) {
		super();
		this.valueType = valType;
		this.setText("0");
		verifyText();
		
		addActionListener(new AbstractAction() {
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				if (loseFocusOnEnter) {
					ValueTextField.this.setFocusable(false);
					ValueTextField.this.setFocusable(true);
				}else {
					ValueTextField.this.setFocusable(false);
					ValueTextField.this.setFocusable(true);
					ValueTextField.this.requestFocus();
				}
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
				
				if (minValue != null) value = Math.max((int)minValue,  (int)value);
				if (maxValue != null) value = Math.min((int)maxValue,  (int)value);
				
				setText(""+value);
			}else if (valueType == VALUE_FLOAT) {
				value = Float.parseFloat(getText());
				
				if (minValue != null) value = Math.max((float)minValue,  (float)value);
				if (maxValue != null) value = Math.min((float)maxValue,  (float)value);
				
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
	
	public void setMaxValue(float f) {
		this.maxValue = f;
	}
	
	public void setMinValue(float f) {
		this.minValue = f;
	}
	
    public void setMaxValue(int i) {
    	this.maxValue = i;
	}
	
	public void setMinValue(int i) {
		this.minValue = i;
	}
	
	public boolean isLoseFocusOnEnter() {
		return loseFocusOnEnter;
	}


	public void setLoseFocusOnEnter(boolean loseFocusOnEnter) {
		this.loseFocusOnEnter = loseFocusOnEnter;
	}
}
