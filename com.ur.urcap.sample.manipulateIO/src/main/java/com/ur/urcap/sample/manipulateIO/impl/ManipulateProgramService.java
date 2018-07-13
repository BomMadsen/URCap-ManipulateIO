package com.ur.urcap.sample.manipulateIO.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import java.io.InputStream;

public class ManipulateProgramService implements ProgramNodeService {

	@Override
	public String getId() {
		return "manipulatorIO";
	}

	@Override
	public String getTitle() {
		return "IO Toggle";
	}

	@Override
	public InputStream getHTML() {
		return this.getClass().getResourceAsStream("/com/ur/urcap/sample/manipulateIO/impl/programnode.html");
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isChildrenAllowed() {
		return false;
	}

	@Override
	public ProgramNodeContribution createNode(URCapAPI api, DataModel model) {
		return new ManipulateProgramContribution(api, model);
	}
}

