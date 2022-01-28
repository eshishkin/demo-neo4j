package org.eshishkin.edu.demoneo4j.persistence.sql;

import org.eshishkin.edu.demoneo4j.Profiles;
import org.eshishkin.edu.demoneo4j.model.sql.Person;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

@Profile(Profiles.SQL)
public interface PersonRepository extends R2dbcRepository<Person, Long> {

    @Query(value =
            "SELECT * from person as p " +
            "INNER JOIN roles as r ON r.person_id = p.id " +
            "WHERE p.id <> :id AND r.movie_id in (" +
            "   SELECT rr.movie_id FROM roles as rr WHERE rr.person_id = :id" +
            ") " +
            "LIMIT :limit"
    )
    Flux<Person> findRelatedActors(Long id, Long limit);
}
