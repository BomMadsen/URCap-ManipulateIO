package com.ur.urcap.sample.manipulateIO.impl;

import java.awt.EventQueue;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
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

	private final URCapAPI api;
	private final DataModel model;

	public ManipulateProgramContribution(URCapAPI api, DataModel model) {
		this.api = api;
		this.model = model;
		
		// Grab the IO's here, so we do not have to iterate endlessly to find them again
		tool_out0 = getDigitalIO("tool_out[0]");
		tool_out1 = getDigitalIO("tool_out[1]");
		tool_in0 = getDigitalIO("tool_in[0]");
		tool_in1 = getDigitalIO("tool_in[1]");
		analog_in2 = getAnalogIO("analog_in[2]");
	}
	
	@Input(id="radio_grip")
	InputRadioButton RADIO_GRIP;
	
	@Input(id="radio_release")
	InputRadioButton RADIO_RELEASE;
	
	@Input(id="btn_Grip")
	InputButton BTN_GRIP;
	
	@Input(id="btn_Release")
	InputButton BTN_RELEASE;
	
	@Label(id="lbl_GripperPosition")
	LabelComponent LBL_GripperPos;
	
	@Label(id="lbl_GripperHolding")
	LabelComponent LBL_GripperHolding;
	
	@Label(id="lbl_Error")
	LabelComponent LBL_Error;
	
	/************************'
	 * Methods for manipulating IO's from Java
	 * 
	 */
	
	private DigitalIO tool_out0;
	private DigitalIO tool_out1;
	private DigitalIO tool_in0;
	private DigitalIO tool_in1;
	private AnalogIO analog_in2;
	
	@Input(id="btn_Grip")
	public void onTestGripClick(InputEvent event){
		if(event.getEventType() == InputEvent.EventType.ON_PRESSED){
			testGrip();
		}
	}
	
	@Input(id="btn_Release")
	public void onTestReleaseClick(InputEvent event){
		if(event.getEventType() == InputEvent.EventType.ON_PRESSED){
			testRelease();
		}
	}
	
	private void testGrip(){
		if(tool_out0!=null && tool_out1 != null){
			tool_out1.setValue(false);
			tool_out0.setValue(true);
		}
	}
	
	private void testRelease(){
		if(tool_out0!=null && tool_out1 != null){
			tool_out1.setValue(true);
			tool_out0.setValue(false);
		}
	}
	
	private String gripperPosition(){
		if(tool_out1 != null && tool_out1 != null){
			if(tool_out0.getValue() && !tool_out1.getValue()){
				return "Closed";
			}
			else if (!tool_out0.getValue() && tool_out1.getValue()){
				return "Open";
			}
		}
		// Error
		return "Unknown";
	}
	
	private int getAnalogGripperPosition(){
		if(analog_in2 != null){
			// "Gripper" only works with voltage feedback
			if(analog_in2.isVoltage()){
				double max = analog_in2.getMaxRangeValue();
				double min = analog_in2.getMinRangeValue();
				double actual = analog_in2.getValue();
				
				int openness = (int) Math.round((actual-min)/(max-min) * 100);
				return openness;
			}
		}
		// Error
		return 0;
	}
	
	private boolean isGripperHolding(){
		if(tool_in0 != null){
			// High for holding, low for lost... 
			return tool_in0.getValue();
		}
		// Error
		return false;
	}
	
	private boolean isGripperError(){
		if(tool_in1 != null){
			// High for error, low for no error 
			return tool_in1.getValue();
		}
		// Error
		return false;
	}
	
	/*Returns a DigitalIO object found by its default name
	 * Default names are: 
	 * 	digital_in[0]
	 *  digital_in[1]
	 *  ...
	 *  digital_in[7]
	 *  digital_out[0]
	 *  digital_out[1]
	 *  ...
	 *  digital_out[7]
	 *  tool_in[0]
	 *  tool_in[1]
	 *  tool_out[0]
	 *  tool_out[1]
	 *  config_in[0]
	 *  config_in[1]
	 *  ...
	 *  config_in[7]
	 *  config_out[0]
	 *  config_out[1]
	 *  ...
	 *  config_out[7]
	 * 
	 */
	private DigitalIO getDigitalIO(String defaultName){
		Collection<DigitalIO> IOcollection = api.getIOs().getIOs(DigitalIO.class);
		int IO_count = IOcollection.size();
		if(IO_count > 0){
			Iterator<DigitalIO> IO_itr = IOcollection.iterator();
			while(IO_itr.hasNext()){
				DigitalIO thisIO = IO_itr.next();
				String thisDefaultName = thisIO.getDefaultName();
				System.out.println("Found an IO named "+thisDefaultName);
				if(thisDefaultName.equals(defaultName)){
					return thisIO;
				}
			}
		}
		return null;
	}
	
	/*Returns an AnalogIO object found by its default name
	 * Default names are: 
	 *  analog_in[0]
	 *  analog_in[1]
	 *  analog_in[2] 	(Tool analog in 0)
	 *  analog_in[3]	(Tool analog in 1)
	 *  analog_out[0]
	 *  analog_out[1]
	 * 
	 */
	private AnalogIO getAnalogIO(String defaultName){
		Collection<AnalogIO> IOcollection = api.getIOs().getIOs(AnalogIO.class);
		int IO_count = IOcollection.size();
		if(IO_count > 0){
			Iterator<AnalogIO> IO_itr = IOcollection.iterator();
			while(IO_itr.hasNext()){
				AnalogIO thisIO = IO_itr.next();
				String thisDefaultName = thisIO.getDefaultName();
				System.out.println("Found an IO named "+thisDefaultName);
				if(thisDefaultName.equals(defaultName)){
					return thisIO;
				}
			}
		}
		return null;
	}
	
	/*************************'
	 * Methods for generating the correct script for runtime
	 * 
	 */
	
	private static final String KEY_NODEFUNCTION = "gripperNodeFunction";
	
	@Input(id="radio_grip")
	public void inRadioChange(InputEvent event){
		if(event.getEventType() == InputEvent.EventType.ON_CHANGE){
			setGripNode(RADIO_GRIP.isSelected());
		}
	}
	
	private boolean isGripNode(){
		return model.get(KEY_NODEFUNCTION, true);
	}
	private void setGripNode(boolean gripNotRelease){
		model.set(KEY_NODEFUNCTION, gripNotRelease);
	}
	
	/*************
	 * Methods for setting up GUI and labels
	 */
	
	private void updateLabels(){
		LBL_GripperPos.setText("Gripper Position: "+gripperPosition()+" / "+getAnalogGripperPosition()+" mm");
		
		String gripperHoldingText = "Gripper Holding Status: ";
		if(isGripperHolding()){
			gripperHoldingText += "Holding";
		}
		else{
			gripperHoldingText += "Not holding";
		}
		LBL_GripperHolding.setText(gripperHoldingText);
		
		String gripperErrorStatus = "Error Status: ";
		if(isGripperError()){
			gripperErrorStatus += "Gripper Error!";
		}
		else{
			gripperErrorStatus += "No errors to report.";
		}
		LBL_Error.setText(gripperErrorStatus);
		
	}
	
	private Timer uiTimer;
	
	@Override
	public void openView() {
		BTN_GRIP.setText("GRIP");
		BTN_RELEASE.setText("RELEASE");
		if(isGripNode()){
			RADIO_GRIP.setSelected();
		}
		else{
			RADIO_RELEASE.setSelected();
		}
		
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateLabels();
					}
				});
			}
		}, 0, 500);
	}

	@Override
	public void closeView() {
		if(uiTimer != null){
			uiTimer.cancel();
		}
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

}
