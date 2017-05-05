package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import util.Undo.OperationType;
import main.Main;
import entitytypes.FXListType;
import entitytypes.ParticleSystemType;

public class Undo {
	
	public static LinkedList<Operation> undoStack = new LinkedList<Undo.Operation>();
	public static LinkedList<Operation> redoStack = new LinkedList<Undo.Operation>();
	
	public static abstract class Operation {
		public String text; //Description for GUI		
		public OperationType type;
		public Object data;
		public Operation(String text, OperationType type, Object data) {
			super();
			this.text = text;
			this.type = type;
			this.data = data;
		}	
		public abstract void restore();
		//public abstract void redo();
		public abstract String getText();
	}
	
	public static class FXOperation extends Operation {
		public FXListType ftype; //ActiveFXListType before edit
		public String fname;
		public FXOperation(FXListType ftype, String name, String text, OperationType type, Object data) {
			super(text, type, data);
			this.fname = name;
			this.ftype = ftype;
		}
		@Override
		public void restore() {
			switch (type) {
			case ADD:
				FXListType ftype_ = Main.getFXList(fname);
				if (Main.activeFXListType == ftype_) {
					int index = (Main.FXListNames.indexOf(ftype_) +1) % Main.FXListNames.size();
					Main.renderer.updateActiveFX(Main.FXListTypes.get(Main.FXListNames.get(index)), fname);
				}
				Main.FXListTypes.remove(fname);
				Main.FXListNames.remove(fname);
				Main.work_FXListTypes.remove(fname);
				Main.renderer.browsePanel.fillLists();
				this.type = OperationType.REMOVE;
				break;
			case REMOVE:
				if (!Main.work_FXListTypes.containsKey(fname)) { //CAN ONLY REMOVE ITEMS IN WORK SET RIGHT NOW
					Main.work_FXListTypes.put(fname, ftype);
					Main.FXListTypes.put(fname, ftype);
					Main.updateFXListNames();
					Main.renderer.browsePanel.fillLists();
					this.type = OperationType.ADD;
				}
				break;
			case EDIT:
			default:
				FXListType ftype_2 = Main.getFXList(fname);
				Main.FXListTypes.put(fname, ftype);
				Main.work_FXListTypes.put(fname, ftype);
				if (Main.activeFXListType == ftype_2) {
					Main.renderer.updateActiveFX(ftype, fname);
//					Main.renderer.editPanel.updateFXGUI();
//					Main.renderer.editPanel.updateFXCode();
				}
				this.ftype = ftype_2;			
				break;
			}
		}

		@Override
		public String getText() {
			return "FX '"+fname+"': "+text;
		}	
	}
	
	public static class ParticleOperation extends Operation {
		public ParticleSystemType ptype; //Active ParticleSystemType before edit
		public String pname;
		public ParticleOperation(ParticleSystemType ptype, String name, String text, OperationType type, Object data) {
			super(text, type, data);
			this.pname = name;
			this.ptype = ptype;
		}
		@Override
		public void restore() {
			switch (type) {
			case ADD:
				ParticleSystemType ptype_ = Main.getParticleSystem(pname);
				if (Main.activeParticleSystemType == ptype_) {
					int index = (Main.ParticleSystemNames.indexOf(ptype_) +1) % Main.ParticleSystemNames.size();
					Main.renderer.updateActiveParticle(Main.ParticleSystemTypes.get(Main.ParticleSystemNames.get(index)), pname);
				}
				Main.ParticleSystemTypes.remove(pname);
				Main.ParticleSystemNames.remove(pname);
				Main.work_ParticleSystemTypes.remove(pname);
				Main.renderer.browsePanel.fillLists();
				Main.renderer.editPanel.updateFXGUI();
				this.type = OperationType.REMOVE;
				break;
			case REMOVE:
				if (!Main.work_ParticleSystemTypes.containsKey(pname)) { //CAN ONLY REMOVE ITEMS IN WORK SET RIGHT NOW
					Main.work_ParticleSystemTypes.put(pname, ptype);
					Main.ParticleSystemTypes.put(pname, ptype);
					Main.updateParticleSystemNames();
					Main.renderer.browsePanel.fillLists();
					Main.renderer.editPanel.updateFXGUI();
					this.type = OperationType.ADD;
				}
				break;
			case EDIT:
			default:
				ParticleSystemType ptype_2 = Main.getParticleSystem(pname);
				Main.ParticleSystemTypes.put(pname, ptype);
				Main.work_ParticleSystemTypes.put(pname, ptype);
				if (Main.activeParticleSystemType == ptype_2) {
					Main.renderer.updateActiveParticle(ptype, pname);
					//Main.renderer.editPanel.updateParticleCode();
					//Main.renderer.editPanel.updateParticleGUI();
				}
				this.ptype = ptype_2;			
				break;
			}
		}
		@Override
		public String getText() {
			return "PS '"+pname+"': "+text;
		}
	}
	
	public static class MultiOperation extends Operation {

		public HashMap<String, ParticleSystemType> ptypes;
		public FXListType ftype;
		public String fname;
		
		public MultiOperation(String fname, FXListType ftype, HashMap<String, ParticleSystemType> ptypes, String text, OperationType type, Object data) {
			super(text, type, data);
			this.fname = fname;
			this.ptypes = ptypes;
			this.ftype = ftype;
		}

		@Override
		public void restore() {
			for (String pname : ptypes.keySet()) {
				ParticleSystemType ptype = ptypes.get(pname);
				ParticleSystemType ptype_2 = Main.getParticleSystem(pname);
				Main.ParticleSystemTypes.put(pname, ptype);
				Main.work_ParticleSystemTypes.put(pname, ptype);
				if (Main.activeParticleSystemType == ptype_2) {
					Main.renderer.updateActiveParticle(ptype, pname);
				}
				ptypes.put(pname, ptype_2);	
			}
			if (!ftype.isTemporary()) {
				FXListType ftype_2 = Main.getFXList(fname);
				Main.FXListTypes.put(fname, ftype);
				Main.work_FXListTypes.put(fname, ftype);
				if (Main.activeFXListType == ftype_2) {
					Main.renderer.updateActiveFX(ftype, fname);
				}
				this.ftype = ftype_2;		
			}
		}

		@Override
		public String getText() {
			return text;
		}
		
	}
	
	
	public enum OperationType {
		ADD, EDIT, REMOVE;
	}
	
	public static String undoText() {
		if (undoStack.size() > 0) {
			Operation op = undoStack.getFirst();
			return "Undo: "+op.getText();
		}else {
			return null;
		}
	}
	
	public static String redoText() {
		if (redoStack.size() > 0) {
			Operation op = redoStack.getFirst();
			return "Redo: "+op.getText();
		}else {
			return null;
		}
	}
	
	
	public static void undo() {
		Operation op = undoStack.removeFirst();
		op.restore();
		redoStack.addFirst(op);
	}
	
	public static void redo() {
		Operation op = redoStack.removeFirst();
		op.restore();
		undoStack.addFirst(op);
	}
	
    public static void performFilterOperation(String fname, FXListType bak_ftype, HashMap<String,ParticleSystemType> bak_ptypes, String text, OperationType type) {
    	Operation op = new MultiOperation(fname, bak_ftype, bak_ptypes, text, type, null); 
		undoStack.addFirst(op);
		System.out.println(op.getText());
		Main.renderer.mainWindow.updateUndoText();
    }

	public static void performFXOperation(String text, OperationType type) {
		Operation op = new FXOperation(new FXListType(Main.activeFXListType), Main.activeFXName(), text, type, null); 
		undoStack.addFirst(op);
		System.out.println(op.getText());
		Main.renderer.mainWindow.updateUndoText();
	}
	
	public static void performFXOperation(FXListType ftype, String fname, String text, OperationType type) {
		if (ftype != null) ftype = new FXListType(ftype); //new Object
		Operation op = new FXOperation(ftype, fname, text, type, null); 
		undoStack.addFirst(op);
		System.out.println(op.getText());
		Main.renderer.mainWindow.updateUndoText();
	}
	
	public static void performParticleOperation(String text, OperationType type) {
		Operation op = new ParticleOperation(new ParticleSystemType(Main.activeParticleSystemType), Main.activeParticleName(), text, type, null); 
		undoStack.addFirst(op);
		System.out.println(op.getText());
		Main.renderer.mainWindow.updateUndoText();
	}
	
	public static void performParticleOperation(ParticleSystemType ptype, String pname, String text, OperationType type) {
		if (ptype != null) ptype = new ParticleSystemType(ptype);
		Operation op = new ParticleOperation(ptype, pname, text, type, null); 
		undoStack.addFirst(op);
		System.out.println(op.getText());
		Main.renderer.mainWindow.updateUndoText();
	}
	
}
