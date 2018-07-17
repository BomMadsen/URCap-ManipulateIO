package com.ur.urcap.sample.manipulateIO.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ManipulateSwingProgramNodeService manipulateProgramService = new ManipulateSwingProgramNodeService();
		bundleContext.registerService(SwingProgramNodeService.class, manipulateProgramService, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

