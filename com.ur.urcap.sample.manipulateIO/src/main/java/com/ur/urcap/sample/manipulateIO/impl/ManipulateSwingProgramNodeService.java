package com.ur.urcap.sample.manipulateIO.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class ManipulateSwingProgramNodeService implements SwingProgramNodeService<ManipulateProgramContribution, ManipulateProgramNodeView> {

	@Override
	public String getId() {
		return "manipulateNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
		configuration.setUserInsertable(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "IO Toggle";
	}

	@Override
	public ManipulateProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new ManipulateProgramNodeView(apiProvider);
	}

	@Override
	public ManipulateProgramContribution createNode(ProgramAPIProvider apiProvider, ManipulateProgramNodeView view,
			DataModel model, CreationContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
