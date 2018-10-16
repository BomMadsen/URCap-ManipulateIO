package com.ur.urcap.sample.manipulateIO.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		bundleContext.registerService(SwingProgramNodeService.class, new ManipulateSwingProgramNodeService(), null);
		bundleContext.registerService(SwingToolbarService.class, new ManipulateToolbarService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

