package com.github.stcarolas.enki.gitlab.provider;

import com.github.stcarolas.enki.core.repo.StrategiesAsRepo;

import lombok.Getter;
import lombok.Setter;

public class GitlabRepo extends StrategiesAsRepo {
	// TODO optional
	@Getter
	@Setter
	String sshUrl;
	// TODO make httpUrl for GitlabRepo as Optional
	@Getter
	@Setter
	String httpUrl;
	@Getter
	@Setter
	String path;
	@Getter
	@Setter
	String namespace;
}
