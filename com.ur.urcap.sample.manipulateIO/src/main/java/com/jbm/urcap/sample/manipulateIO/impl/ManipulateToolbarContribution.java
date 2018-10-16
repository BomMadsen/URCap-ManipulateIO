package com.ur.urcap.sample.manipulateIO.impl;

import javax.swing.JPanel;

import com.ur.urcap.api.contribution.toolbar.ToolbarAPIProvider;
import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;

public class ManipulateToolbarContribution implements SwingToolbarContribution {

	private final ToolbarAPIProvider apiProvider;
	private GripperLiveControl liveControl;
	
	public ManipulateToolbarContribution(ToolbarContext context) {
		this.apiProvider = context.getAPIProvider();
	}
	
	@Override
	public void buildUI(JPanel panel) {
		liveControl = new GripperLiveControl(this.apiProvider.getApplicationAPI());
		liveControl.createUI();
		
		panel.add(liveControl);
	}

	@Override
	public void openView() {
		liveControl.openView();
	}

	@Override
	public void closeView() {
		liveControl.closeView();
	}

}
