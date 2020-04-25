package com.github.stcarolas.enki.core.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DefaultTransportConfigCallback implements TransportConfigCallback {

	@Override
	public void configure(Transport transport) {
		SshTransport sshTransport = (SshTransport) transport;
		sshTransport.setSshSessionFactory(
			new JschConfigSessionFactory() {

				@Override
				protected void configure(Host arg0, Session arg1) {}

				@Override
				protected JSch createDefaultJSch(FS fs) throws JSchException {
					JSch defaultJSch = super.createDefaultJSch(fs);
					JSch.setConfig("StrictHostKeyChecking", "no");
					val key = System.getProperty("user.home") + "/.ssh/id_rsa";
					log.info("using key: {}", key);
					defaultJSch.addIdentity(key);
					return defaultJSch;
				}
			}
		);
	}
}
