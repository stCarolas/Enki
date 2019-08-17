package com.github.stcarolas.enki.model;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

import io.vavr.control.Try;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

@Getter
@Builder
public class Repo {
    private final RepoProvider repoProvider;
    private final String sshUrl;
    private final String name;
    private final UUID id = UUID.randomUUID();

    public Optional<File> getDirectory(){
        val directory = new File("/tmp/enki/"+id.toString());
        directory.mkdirs();
        return Try.of(() -> {
            SshSessionFactory sshSessionFactory = new JschConfigSessionFactory(){
                @Override
                protected void configure(Host arg0, Session arg1) {}
                @Override
                protected JSch createDefaultJSch( FS fs ) throws JSchException {
                  JSch defaultJSch = super.createDefaultJSch( fs );
                  defaultJSch.addIdentity( System.getProperty("user.home") + "/.ssh/id_rsa" );
                  return defaultJSch;
                }
            };
            Git.cloneRepository()
                .setURI(sshUrl)
                .setDirectory(directory)
                .setTransportConfigCallback( new TransportConfigCallback() {
                    @Override
                    public void configure( Transport transport ) {
                        SshTransport sshTransport = ( SshTransport )transport;
                        sshTransport.setSshSessionFactory( sshSessionFactory );
                    }
                })
                .call();
            return directory;
        })
        .onFailure( error -> System.out.println("error:"+error))
        .toOptional();
    }
}
