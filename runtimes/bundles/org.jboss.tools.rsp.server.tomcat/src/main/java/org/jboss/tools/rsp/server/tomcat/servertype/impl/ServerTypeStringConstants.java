package org.jboss.tools.rsp.server.tomcat.servertype.impl;

import org.jboss.tools.rsp.server.tomcat.beans.impl.IServerConstants;

public interface ServerTypeStringConstants {
	public static final String TC9_ID = IServerConstants.RUNTIME_TOMCAT_90;
	public static final String TC9_NAME = "Tomcat 9.x";
	public static final String TC9_DESC = "A server adapter capable of discovering and controlling a Tomcat 9.x runtime instance.";
	
	public static final String RUNTIME_TC_9_ID = "org.jboss.tools.openshift.crc.server.runtime.type.crc.100";
}
