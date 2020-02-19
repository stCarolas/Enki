package com.github.stcarolas.enki.bitbucket.provider;

import com.github.stcarolas.enki.core.repo.StrategiesAsRepo;

import lombok.Getter;
import lombok.Setter;

public class BitbucketRepo extends StrategiesAsRepo {
	@Getter
	@Setter
	private String sshUrl;
}
