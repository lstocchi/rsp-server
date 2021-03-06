/*******************************************************************************
 * Copyright (c) 2015-2019 Red Hat 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     JBoss by Red Hat
 *******************************************************************************/
package org.jboss.tools.rsp.runtime.core.model.installer.internal;

import java.io.InputStream;
import java.net.URL;

import org.jboss.tools.rsp.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.rsp.eclipse.core.runtime.IStatus;
import org.jboss.tools.rsp.eclipse.core.runtime.SubProgressMonitor;
import org.jboss.tools.rsp.foundation.core.tasks.TaskModel;
import org.jboss.tools.rsp.runtime.core.model.DownloadRuntime;
import org.jboss.tools.rsp.runtime.core.model.IDownloadRuntimeConnectionFactory;
import org.jboss.tools.rsp.runtime.core.model.IDownloadRuntimeWorkflowConstants;
import org.jboss.tools.rsp.runtime.core.model.IRuntimeInstaller;
import org.jboss.tools.rsp.runtime.core.util.internal.DownloadRuntimeOperationUtility;

public class ExtractionRuntimeInstaller implements IRuntimeInstaller {

	public static final String ID = IRuntimeInstaller.EXTRACT_INSTALLER;
	
	@Override
	public IStatus installRuntime(DownloadRuntime downloadRuntime, 
			String unzipDirectory, String downloadDirectory,
			boolean deleteOnExit, TaskModel taskModel, IProgressMonitor monitor) {
		
		String user = (String)taskModel.getObject(IDownloadRuntimeWorkflowConstants.USERNAME_KEY);
		String pass = (String)taskModel.getObject(IDownloadRuntimeWorkflowConstants.PASSWORD_KEY);
		
		monitor.beginTask("Download '" + downloadRuntime.getName() + "' ...", 100);//$NON-NLS-1$ //$NON-NLS-2$
		monitor.worked(1);
		return createDownloadRuntimeOperationUtility(taskModel).downloadAndUnzip(unzipDirectory, downloadDirectory, 
				getDownloadUrl(downloadRuntime, taskModel), deleteOnExit, user, pass, taskModel, new SubProgressMonitor(monitor, 99));
	}

	protected DownloadRuntimeOperationUtility createDownloadRuntimeOperationUtility(TaskModel tm) {
		IDownloadRuntimeConnectionFactory fact = (IDownloadRuntimeConnectionFactory)tm.getObject(
				IDownloadRuntimeWorkflowConstants.CONNECTION_FACTORY);
		if (fact == null) {
			return new DownloadRuntimeOperationUtility();
		} else {
			return new DownloadRuntimeOperationUtility() {
				
				@Override
				protected InputStream createDownloadInputStream(URL url, String user, String pass) {
					return fact.createConnection(url, user, pass);
				}

				@Override
				protected int getContentLength(URL url, String user, String pass) {
					return fact.getContentLength(url, user, pass);
				}
			};
		}
	}
	
	private String getDownloadUrl(DownloadRuntime downloadRuntime, TaskModel taskModel) {
		if( downloadRuntime != null ) {
			String dlUrl = downloadRuntime.getUrl();
			if( dlUrl == null ) {
				return (String)taskModel.getObject(IDownloadRuntimeWorkflowConstants.DL_RUNTIME_URL);
			}
			return dlUrl;
		}
		return null;
	}
}
