package com.jbm.urcap.sample.manipulateIO.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class ManipulateProgramNodeView implements SwingProgramNodeView<ManipulateProgramContribution> {

	private final ViewAPIProvider apiProvider;
	private final boolean showGripperLiveControl = true;
	
	public ManipulateProgramNodeView(ViewAPIProvider apiProvider) {
		System.out.println("Constructing ManipulateIO View-class");
		this.apiProvider = apiProvider;
	}
	
	private ButtonGroup nodeFunctionButtonGroup = new ButtonGroup();
	private ActionListener nodeFunctionActionListener;
	private JRadioButton gripRadioButton = new JRadioButton("Grip");
	private JRadioButton releaseRadioButton = new JRadioButton("Release");
	
	private GripperLiveControl liveControl;
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<ManipulateProgramContribution> provider) {
		System.out.println("Building ManipulateIO UI");
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(createDescriptionLabel("This node can interact with a fictive gripper."));
		panel.add(createHorizontalSpacer(5));
		panel.add(createDescriptionLabel("When gripped: tool_out[0]=high, tool_out[1]=low"));
		panel.add(createDescriptionLabel("When released: tool_out[1]=high, tool_out[0]=low"));
		panel.add(createHorizontalSpacer(5));
		panel.add(createDescriptionLabel("Gripper holding reported with tool_in[0], high = holding"));
		panel.add(createDescriptionLabel("Gripper error reported with tool_in[1], high = error"));
		panel.add(createDescriptionLabel("Gripper position reported on tool analog in 0"));
		
		panel.add(createHorizontalSpacer(15));
		
		configureNodeFunctionRadioButtons(provider);
		panel.add(createRadioButton("Grip", gripRadioButton, nodeFunctionActionListener));
		panel.add(createHorizontalSpacer(5));
		panel.add(createRadioButton("Release", releaseRadioButton, nodeFunctionActionListener));
		
		if(showGripperLiveControl) {
			liveControl = new GripperLiveControl(provider);
			liveControl.createUI();
			
			TitledBorder liveControlBorder;
			Border border = BorderFactory.createLineBorder(new Color(179, 179, 179));
			liveControlBorder = BorderFactory.createTitledBorder(border, "Live Control");
			liveControlBorder.setTitleJustification(TitledBorder.CENTER);
			liveControl.setBorder(liveControlBorder);
			
			Box box = Box.createVerticalBox();
			box.setAlignmentX(Component.LEFT_ALIGNMENT);
			box.add(liveControl);
			
			panel.add(createHorizontalSpacer(15));
			panel.add(box);
		}
		
		System.out.println("Done building ManipulateIO UI");
	}
	
	/*****
	 * Call-ins from Contribution
	 */
	
	public void setRadioButtons(boolean gripActive) {
		gripRadioButton.setSelected(gripActive);
		releaseRadioButton.setSelected(!gripActive);
	}
	
	public void updateLiveControl() {
		if(showGripperLiveControl) {
			liveControl.openView();
		}
	}
	
	public void stopLiveControl() {
		if(showGripperLiveControl) {
			liveControl.closeView();
		}
	}
	
	/*****
	 * Configuration
	 */
	
	private void configureNodeFunctionRadioButtons(final ContributionProvider<ManipulateProgramContribution> provider) {
		nodeFunctionButtonGroup.add(gripRadioButton);
		nodeFunctionButtonGroup.add(releaseRadioButton);
		
		nodeFunctionActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Radio button action listener: "+e.getActionCommand());
				boolean gripSelected = e.getActionCommand().equals("Grip");
				provider.get().setGripNode(gripSelected);
			}
		};
	}
	
	/*****
	 * Rather generic component builders
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
	
	/*****
	 * 
	 * @param label
	 * @param radioButton
	 * @param actionListener
	 * @return
	 */
	private Box createRadioButton(String name, JRadioButton radioButton, ActionListener actionListener) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		radioButton.addActionListener(actionListener);
		radioButton.setActionCommand(name);
		
		box.add(radioButton);
		
		return box;
	}
	
	
	
}
