package com.github.stcarolas.enki;

import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.gitea.provider.OneRepoProvider;
import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.vavr.collection.Seq;
import java.io.InputStreamReader;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebhookHandler implements HttpHandler {
	WebhookHandler(Seq<RepoHandler> handlers) { this.handlers = handlers; }
  
	public static WebhookHandlerBuilder builder() { return new WebhookHandlerBuilder(); }
  
	public static class WebhookHandlerBuilder {
		private Seq<RepoHandler> handlers;
    
		public WebhookHandlerBuilder handlers(Seq<RepoHandler> handlers) { this.handlers = handlers;
			return this; }
    
		public WebhookHandler build() { return new WebhookHandler(this.handlers); }
    
		public String toString() { return "WebhookHandler.WebhookHandlerBuilder(handlers=" + this.handlers + ")"; }
	}
  
	private static final Logger log = LogManager.getLogger(WebhookHandler.class);
  
	private final Seq<RepoHandler> handlers;
  
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		exchange.startBlocking();
		exchange.dispatch(new HttpHandler() {
					public void handleRequest(HttpServerExchange exchange) throws Exception {
						Map request = (Map)(new Gson()).fromJson(new InputStreamReader(exchange
									.getInputStream()), Map.class);
						String url = (String)((Map)request.get("repository")).get("ssh_url");
						EnkiRunner.EnkiRunnerBuilder enki = EnkiRunner.builder().provider(new OneRepoProvider(url, CloneURLType.SSH));
						WebhookHandler.this.handlers.forEach(handler -> 
                
								enki.analyzer(handler));
						enki.build().analyze();
					}
				});
	}
}
