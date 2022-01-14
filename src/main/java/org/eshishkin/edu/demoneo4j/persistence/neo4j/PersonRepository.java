package org.eshishkin.edu.demoneo4j.persistence.neo4j;

import org.eshishkin.edu.demoneo4j.Profiles;
import org.eshishkin.edu.demoneo4j.model.neo4j.Person;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import reactor.core.publisher.Flux;

@Profile(Profiles.NEO4J)
public interface PersonRepository extends ReactiveNeo4jRepository<Person, Long> {

    @Query(value =
            "MATCH (p:Person)-[r:ACTED_IN]->(m:Movie)<-[r2:ACTED_IN]-(p2:Person) "
                    + "WHERE p.id = $id "
                    + "RETURN p2"
    )
    Flux<Person> findRelatedActors(Long id);
}
