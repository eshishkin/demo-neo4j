package org.eshishkin.edu.demoneo4j.persistence;

import org.eshishkin.edu.demoneo4j.model.Account;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import java.util.UUID;

public interface AccountRepository extends ReactiveNeo4jRepository<Account, UUID> {
}
