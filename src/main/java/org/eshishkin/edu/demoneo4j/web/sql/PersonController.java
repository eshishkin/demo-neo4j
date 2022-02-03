package org.eshishkin.edu.demoneo4j.web.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eshishkin.edu.demoneo4j.Profiles;
import org.eshishkin.edu.demoneo4j.model.sql.Person;
import org.eshishkin.edu.demoneo4j.persistence.sql.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@Profile(Profiles.SQL)
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final R2dbcEntityTemplate rd2dbcTemplate;

    @GetMapping("/generate")
    public Mono<Void> generate(@RequestParam(name = "actors", defaultValue = "500") Integer actors,
                               @RequestParam(name = "movies", defaultValue = "50") Integer movies) {
        var act = rd2dbcTemplate.getDatabaseClient()
                .sql(() -> {
                    var insert = "INSERT INTO person (id, name) VALUES ";
                    var values = IntStream.range(1, actors)
                            .mapToObj(id -> String.format("(%s, '%s')", id, "Actor_" + id))
                            .collect(Collectors.joining(", "));
                    return insert + values;
                })
                .fetch()
                .rowsUpdated()
                .doOnNext(updated -> log.info("Actors created: {}", updated))
                .then();

        var mov = rd2dbcTemplate.getDatabaseClient()
                .sql(() -> {
                    var insert = "INSERT INTO movie (id, name) VALUES ";
                    var values = IntStream.range(1, movies)
                            .mapToObj(id -> String.format("(%s, '%s')", id, "Movie_" + id))
                            .collect(Collectors.joining(", "));
                    return insert + values;
                })
                .fetch()
                .rowsUpdated()
                .doOnNext(updated -> log.info("Movies created: {}", updated))
                .then();


        return act
                .then(Mono.defer(() -> mov))
                .then(Mono.defer(() -> {
                    var insert = "INSERT INTO roles(person_id, movie_id) VALUES ";
                    return Flux.range(1, actors)
                            .map(personId -> {
                                return IntStream.range(1, personId % movies + 1)
                                        .mapToObj(id -> String.format("(%s, %s)", personId, id))
                                        .collect(Collectors.joining(", "));
                            })
                            .filter(StringUtils::hasText)
                            .buffer(20)
                            .flatMap(values -> {
                                var value = String.join(", ", values);
                                    return rd2dbcTemplate.getDatabaseClient()
                                            .sql(insert + value)
                                            .fetch()
                                            .rowsUpdated()
                                            .doOnNext(updated -> log.info("Relationships created: {}", updated));
                            })
                            .then();
                }));
    }

    @GetMapping("/linked")
    public Flux<Person> getRelatedPersons(@RequestParam(name = "userId") Long userId,
                                          @RequestParam(name = "limit") Long limit) {
        return personRepository
                .findRelatedActors(userId, limit)
                .filter(p -> !Objects.equals(userId, p.getId()));
    }
}
