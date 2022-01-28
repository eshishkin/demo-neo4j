package org.eshishkin.edu.demoneo4j.config.sql;

import org.eshishkin.edu.demoneo4j.Profiles;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@Profile(Profiles.SQL)
@EnableR2dbcRepositories("org.eshishkin.edu.demoneo4j")
public class ServiceConfig {
}
