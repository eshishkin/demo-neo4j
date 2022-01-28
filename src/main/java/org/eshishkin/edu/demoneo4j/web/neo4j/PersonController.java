package org.eshishkin.edu.demoneo4j.web.neo4j;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eshishkin.edu.demoneo4j.Profiles;
import org.eshishkin.edu.demoneo4j.model.neo4j.Person;
import org.eshishkin.edu.demoneo4j.persistence.neo4j.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@Profile(Profiles.NEO4J)
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final ReactiveNeo4jClient reactiveNeo4jClient;

    @GetMapping("/generate")
    public Mono<Void> generate(@RequestParam(name = "actors", defaultValue = "500") Integer actors,
                               @RequestParam(name = "movies", defaultValue = "50") Integer movies) {
        var act = reactiveNeo4jClient
                .query(String.format("UNWIND range(1,%s) as id create (p:Person {id:id, name: \"Actor_\"+ id}) return p;", actors))
                .run()
                .doOnNext(x -> log.info(x.counters().toString()))
                .then();

        var mov = reactiveNeo4jClient
                .query(String.format("UNWIND range(1,%s) as id create (m:Movie {id:id, name: \"Movie \" + id}) return m;", movies))
                .run()
                .doOnNext(x -> log.info(x.counters().toString()))
                .then();

        return act
                .then(Mono.defer(() -> mov))
                .then(Mono.defer(() -> {
                    var generator = String.format("UNWIND range(1,%s) as id RETURN id", actors);
                    var query = String.format(
                            "MATCH (p:Person) WHERE p.id=id " +
                            "UNWIND range(1,id%%%s + 1) as m_id MATCH (m:Movie) WHERE m.id=m_id " +
                            "CREATE (p)-[:ACTED_IN]->(m) ",
                            movies
                    );
                    return reactiveNeo4jClient
                            .query(String.format(
                                    "call apoc.periodic.iterate(\"%s\", \"%s\", {batchSize:5, parallel:true})",
                                    generator, query
                            ))
                            .run()
                            .doOnNext(x -> log.info(x.counters().toString()))
                            .then();
                }));
    }

    @GetMapping("/linked")
    public Flux<Person> getRelatedPersons(@RequestParam(name = "userId") Long userId,
                                          @RequestParam(name = "limit") Long limit) {
        return personRepository.findRelatedActors(userId, limit);
    }
}
