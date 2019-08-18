package com.github.stcarolas.enki.core.impl;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

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
                    defaultJSch.addIdentity(System.getProperty("user.home") + "/.ssh/id_rsa");
                    return defaultJSch;
                }
            }
        );
    }
}
