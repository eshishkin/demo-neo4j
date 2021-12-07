package org.eshishkin.edu.demoneo4j.persistence;

import org.eshishkin.edu.demoneo4j.model.Person;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import java.util.UUID;

public interface PersonRepository extends ReactiveNeo4jRepository<Person, UUID> {
}
