package com.ur.urcap.sample.manipulateIO.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.domain.ApplicationAPI;
import com.ur.urcap.api.domain.io.AnalogIO;
import com.ur.urcap.api.domain.io.DigitalIO;
import com.ur.urcap.api.domain.io.IOModel;
import com.ur.urcap.api.ui.component.InputEvent;

public class GripperLiveControl extends JPanel {

	private final ContributionProvider<ManipulateProgramContribution> provider;
	private final ApplicationAPI applicationAPI;
	
	private enum ContributionType {
		TOOLBAR,
		PROGRAMNODE
	}
	private final ContributionType contributionType;
	
	private IOHandler ioHandler;
	private DigitalIO tool_out0;
	private DigitalIO tool_out1;
	private DigitalIO tool_in0;
	private DigitalIO tool_in1;
	private AnalogIO analog_in2;
	
	/*****
	 * Constructor when called in a Toolbar
	 * @param toolbarAPI
	 */
	public GripperLiveControl(ApplicationAPI applicationAPI) {
		this.provider = null;
		this.applicationAPI = applicationAPI; 
		
		this.contributionType = ContributionType.TOOLBAR;
	}
	
	/*****
	 * Constructor when called in a Program Node
	 * @param programAPI
	 */
	public GripperLiveControl(ContributionProvider<ManipulateProgramContribution> provider) {
		this.provider = provider;
		this.applicationAPI = null; 
		
		this.contributionType = ContributionType.PROGRAMNODE;
	}
	
	// provider.get() cannot be called in buildUI, so we may first initialize this in openView
	// Anyway the user will only look at it, when the view is opened :-) 
	private void InitializeIO() {
		if(contributionType.equals(ContributionType.TOOLBAR)) {
			this.ioHandler = new IOHandler(applicationAPI.getIOModel());
		} else if (contributionType.equals(ContributionType.PROGRAMNODE)) {
			this.ioHandler = new IOHandler(provider.get().getProgramAPI().getIOModel());
		}
		
		tool_out0 = 	ioHandler.getDigitalIO("tool_out[0]");
		tool_out1 = 	ioHandler.getDigitalIO("tool_out[1]");
		tool_in0 = 		ioHandler.getDigitalIO("tool_in[0]");
		tool_in1 = 		ioHandler.getDigitalIO("tool_in[1]");
		analog_in2 = 	ioHandler.getAnalogIO("analog_in[2]");
	}
	
	// Updatable labels
	private JLabel gripperPosition = new JLabel();
	private JLabel gripperHolding = new JLabel();
	private JLabel gripperError = new JLabel();
	
	public void createUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		this.add(createDescriptionLabel("Click GRIP or RELEASE below, to test the gripper:"));
		this.add(createHorizontalSpacer(5));
		this.add(createGripperTestButtons("GRIP", "RELEASE"));
		this.add(createHorizontalSpacer(25));
		
		this.add(createStatusLabel("Gripper position:", gripperPosition));
		this.add(createHorizontalSpacer(5));
		this.add(createStatusLabel("Gripper holding:", gripperHolding));
		this.add(createHorizontalSpacer(5));
		this.add(createStatusLabel("Gripper error:", gripperError));
	}
	
	private Timer uiTimer;
	
	public void openView() {
		if(ioHandler==null) {
			InitializeIO();
			
			uiTimer = new Timer(true);
			uiTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							UpdateStatusLabels();
						}
					});
				}
			}, 0, 500);
		}
	}
	
	public void closeView() {
		if(uiTimer!=null) {
			uiTimer.cancel();
		}
	}
	
	private void UpdateStatusLabels(){
		gripperPosition.setText(gripperPosition()+" / "+getAnalogGripperPosition()+" mm");
		
		gripperHolding.setText(isGripperHolding() ? "Holding" : "Not holding");
		
		gripperError.setText(isGripperError() ? "Gripper error!" : "No errors to report");
	}

	/*****
	 * Component builders
	 */
	
	/*****
	 * Creates a rigid area with the specified height
	 * @param height The height of the spacer
	 * @return A Component that can be used as a spacer
	 */
	private Component createHorizontalSpacer(int height){
		Component spacer = Box.createRigidArea(new Dimension(0, height));
		return spacer;
	}
	
	/*****
	 * Creates a Box with a Label containing the specified text
	 * @param text The specified text
	 * @return The creates box
	 */
	private Box createDescriptionLabel(String text) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(text);
		
		box.add(label);
		
		return box;
	}
	
	private Box createGripperTestButtons(String text1, String text2) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JButton button1 = new JButton(text1);
		JButton button2 = new JButton(text2);
		
		Dimension buttonSize = new Dimension(200, 50);
		
		button1.setPreferredSize(buttonSize);
		button1.setMinimumSize(button1.getPreferredSize());
		button1.setMaximumSize(button1.getPreferredSize());
		button2.setPreferredSize(buttonSize);
		button2.setMinimumSize(button2.getPreferredSize());
		button2.setMaximumSize(button2.getPreferredSize());
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testGrip();
			}
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testRelease();
			}
		});
		
		box.add(button1);
		box.add(button2);
		
		return box;
	}
	
	private Box createStatusLabel(String description, JLabel status) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel desc = new JLabel(description);
		desc.setPreferredSize(new Dimension(200, 20));
		desc.setMinimumSize(desc.getPreferredSize());
		
		box.add(desc);
		box.add(status);
		
		return box;
	}
	
	/*****
	 * Gripper IO functionality
	 */
	
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
	
}
