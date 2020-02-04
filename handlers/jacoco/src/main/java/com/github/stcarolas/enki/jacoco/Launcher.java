package com.github.stcarolas.enki.jacoco;

import java.io.File;
import java.util.concurrent.Callable;

import com.github.stcarolas.enki.JacocoHandler;
import com.github.stcarolas.enki.WebhookHandler;
import com.github.stcarolas.enki.core.RepoHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.undertow.Undertow;
import io.vavr.collection.Seq;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true, version = {"checksum 0.1"}, description = {"generate files from git repo with template"})
public class Launcher extends Object implements Callable<Integer> {
  public static LauncherBuilder builder() { return new LauncherBuilder(); }
  
  Launcher(Seq<RepoHandler> handlers, boolean isServer) { this.handlers = handlers;
    this.isServer = isServer; }
  
  public static class LauncherBuilder {
    private Seq<RepoHandler> handlers;
    
    private boolean isServer;
    
    public LauncherBuilder handlers(Seq<RepoHandler> handlers) { this.handlers = handlers;
      return this; }
    
    public LauncherBuilder isServer(boolean isServer) { this.isServer = isServer;
      return this; }
    
    public Launcher build() { return new Launcher(this.handlers, this.isServer); }
    
    public String toString() { return "Launcher.LauncherBuilder(handlers=" + this.handlers + ", isServer=" + this.isServer + ")"; }
  }
  
  private static final Logger log = LogManager.getLogger(Launcher.class);
  
  private final Seq<RepoHandler> handlers;
  
  @Option(names = {"-s"}, description = {"run as server"})
  boolean isServer;
  
  public Integer call() throws Exception {
    Config config = ConfigFactory.load();
    if (this.isServer) {
      log.info("run as server");
      Undertow.builder()
        .addHttpListener(8080, "0.0.0.0")
        .setHandler(WebhookHandler.builder().handlers(this.handlers).build())
        .build()
        .start();
    } else {
      JacocoHandler handler = JacocoHandler.builder().build();
      handler.analyze(new File(""));
    } 
    return Integer.valueOf(0);
  }
}
