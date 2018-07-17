package com.ur.urcap.sample.manipulateIO.impl;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class ManipulateProgramNodeView implements SwingProgramNodeView<ManipulateProgramContribution> {

	private final ViewAPIProvider apiProvider;
	
	public ManipulateProgramNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<ManipulateProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(createDescriptionLabel("This node can interact with a fictive gripper."));
		panel.add(createDescriptionLabel("The \"gripper\" grips, by toggling tool_out[0] high, and releases by setting tool_out[1] high."));
		panel.add(createDescriptionLabel("tool_in[0] reports, that the gripper is currently holding an item, and tool_in[1] reports an error"));
		panel.add(createDescriptionLabel("Gripper live position is expressed by tool analog in 0."));
		
		
	}
	
	private Box createDescriptionLabel(String text) {
		Box box = Box.createHorizontalBox();
		
		JLabel label = new JLabel(text);
		
		box.add(label);
		
		return box;
	}
	
}
