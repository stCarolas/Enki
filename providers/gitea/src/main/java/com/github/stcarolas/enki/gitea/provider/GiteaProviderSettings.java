package com.github.stcarolas.enki.gitea.provider;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GiteaProviderSettings {

	private Option<String> baseUrl = none();
	private Option<String> organization = none();
	private Option<String> username = none();
	private Option<String> password = none();
	
	public static GiteaProviderSettings defaultSettings(){
		return new GiteaProviderSettings();
	}

	public GiteaProviderSettings withBaseUrl(String url){
		return new GiteaProviderSettings(of(url), organization, username, password);
	}

	public GiteaProviderSettings withOrganization(String organization){
		return new GiteaProviderSettings(baseUrl, of(organization), username, password);
	}

	public GiteaProviderSettings withUsername(String username){
		return new GiteaProviderSettings(baseUrl, organization, of(username), password);
	}

	public GiteaProviderSettings withPassword(String password){
		return new GiteaProviderSettings(baseUrl, organization, username, of(password));
	}
}
