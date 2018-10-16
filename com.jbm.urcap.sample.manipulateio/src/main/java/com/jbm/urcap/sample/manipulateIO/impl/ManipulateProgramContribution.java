package com.jbm.urcap.sample.manipulateIO.impl;

import java.awt.EventQueue;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputRadioButton;
import com.ur.urcap.api.ui.component.InputTextField;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.api.domain.io.AnalogIO;
import com.ur.urcap.api.domain.io.IO;
import com.ur.urcap.api.domain.io.IO.IOType;
import com.ur.urcap.api.domain.io.IO.InterfaceType;
import com.ur.urcap.api.domain.io.IOFilterFactory;
import com.ur.urcap.api.domain.io.IOModel;
import com.ur.urcap.api.domain.io.DigitalIO;

public class ManipulateProgramContribution implements ProgramNodeContribution {

	private final ProgramAPI programAPI;
	private final DataModel model;
	private final ManipulateProgramNodeView view;
	private final UndoRedoManager undoRedoManager;

	public ManipulateProgramContribution(ProgramAPIProvider apiProvider, ManipulateProgramNodeView view,
			DataModel model, CreationContext context) {
		this.programAPI = apiProvider.getProgramAPI();
		this.model = model;
		this.view = view;
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
		
	}
	
	/*************************'
	 * Methods for generating the correct script for runtime
	 * 
	 */
	
	private static final String KEY_NODEFUNCTION = "gripperNodeFunction";
	
	private boolean isGripNode(){
		return model.get(KEY_NODEFUNCTION, true);
	}
	
	/*****
	 * Called by View to select whether this is a Grip or Release node
	 * @param gripNotRelease True if Grip, False if Release
	 */
	public void setGripNode(final boolean gripNotRelease){
		// Capture the changes as an undoable action
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(KEY_NODEFUNCTION, gripNotRelease);
			}
		});
	}
	
	/*************
	 * Methods for setting up GUI
	 */

	@Override
	public void openView() {
		System.out.println("openView of ManipulateIO program node");
		view.setRadioButtons(isGripNode());
		view.updateLiveControl();
	}

	@Override
	public void closeView() {
		view.stopLiveControl();
	}

	@Override
	public String getTitle() {
		if(isGripNode()){
			return "IO Manipulator: Grip";
		}
		else{
			return "IO Manipulator: Release";
		}
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		if(isGripNode()){
			writer.appendLine("set_tool_digital_out(1, False)");
			writer.appendLine("set_tool_digital_out(0, True)");
			writer.sleep(0.2);
		}
		else{
			writer.appendLine("set_tool_digital_out(0, False)");
			writer.appendLine("set_tool_digital_out(1, True)");
			writer.sleep(0.2);
		}
	}
	
	public ProgramAPI getProgramAPI() {
		return this.programAPI;
	}

}
