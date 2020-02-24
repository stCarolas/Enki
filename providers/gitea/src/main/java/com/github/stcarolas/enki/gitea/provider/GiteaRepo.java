package com.github.stcarolas.enki.gitea.provider;

import com.github.stcarolas.enki.core.repo.StrategiesAsRepo;

import lombok.Getter;
import lombok.Setter;

public class GiteaRepo extends StrategiesAsRepo {
	@Getter
	@Setter
	String sshUrl;
}
