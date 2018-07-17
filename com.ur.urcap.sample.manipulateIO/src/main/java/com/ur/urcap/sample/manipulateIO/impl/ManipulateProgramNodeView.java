package com.ur.urcap.sample.manipulateIO.impl;

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
		// TODO Auto-generated method stub
		
	}

}
