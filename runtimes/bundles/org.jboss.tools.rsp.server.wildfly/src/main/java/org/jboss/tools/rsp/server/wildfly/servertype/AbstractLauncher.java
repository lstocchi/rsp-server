/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors: Red Hat, Inc.
 ******************************************************************************/
package org.jboss.tools.rsp.server.wildfly.servertype;

import org.jboss.tools.rsp.eclipse.debug.core.DebugException;
import org.jboss.tools.rsp.eclipse.debug.core.ILaunch;
import org.jboss.tools.rsp.eclipse.debug.core.model.IProcess;
import org.jboss.tools.rsp.eclipse.jdt.launching.IVMInstall;
import org.jboss.tools.rsp.eclipse.jdt.launching.IVMInstallRegistry;
import org.jboss.tools.rsp.server.spi.launchers.AbstractJavaLauncher;
import org.jboss.tools.rsp.server.spi.launchers.IServerStartLauncher;
import org.jboss.tools.rsp.server.spi.servertype.IServerDelegate;
import org.jboss.tools.rsp.server.wildfly.servertype.capabilities.ExtendedServerPropertiesAdapterFactory;
import org.jboss.tools.rsp.server.wildfly.servertype.capabilities.JBossExtendedProperties;
import org.jboss.tools.rsp.server.wildfly.servertype.capabilities.ServerExtendedProperties;
import org.jboss.tools.rsp.server.wildfly.servertype.launch.IDefaultLaunchArguments;

public abstract class AbstractLauncher extends AbstractJavaLauncher implements IServerStartLauncher {

	public AbstractLauncher(IServerDelegate serverDelegate) {
		super(serverDelegate);
	}

	protected abstract String getWorkingDirectory();

	protected abstract String getMainTypeName();

	protected abstract String getVMArguments();

	protected abstract String getProgramArguments();

	protected abstract String[] getClasspath();

	protected JBossExtendedProperties getProperties() {
		ServerExtendedProperties props = new ExtendedServerPropertiesAdapterFactory()
				.getExtendedProperties(getDelegate().getServer());
		if (props instanceof JBossExtendedProperties) {
			return (JBossExtendedProperties) props;
		}
		return null;
	}

	protected IDefaultLaunchArguments getLaunchArgs() {
		JBossExtendedProperties prop = getProperties();
		if (prop != null) {
			IDefaultLaunchArguments largs = prop.getDefaultLaunchArguments();
			if (largs != null) {
				return largs;
			}
		}
		return null;
	}

	protected boolean terminateProcesses(ILaunch launch) {
		if( launch == null )
			return false;
		
		IProcess[] all = launch.getProcesses();
		for( int i = 0; i < all.length; i++ ) {
			try {
				all[i].terminate();
			} catch(DebugException de) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected IVMInstallRegistry getDefaultRegistry() {
		return new JBossVMRegistryDiscovery().getDefaultRegistry();
	}
	
	@Override
	protected IVMInstall getVMInstall(IServerDelegate delegate) {
		return new JBossVMRegistryDiscovery().findVMInstall(delegate);
	}
}
