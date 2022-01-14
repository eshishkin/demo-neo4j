package org.eshishkin.edu.demoneo4j.web;

import lombok.RequiredArgsConstructor;
import org.eshishkin.edu.demoneo4j.Profiles;
import org.eshishkin.edu.demoneo4j.model.neo4j.Movie;
import org.eshishkin.edu.demoneo4j.model.neo4j.Person;
import org.eshishkin.edu.demoneo4j.persistence.neo4j.PersonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@Profile(Profiles.NEO4J)
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping("/generate")
    public Mono<Void> generate(@RequestParam(name = "size", defaultValue = "500") Integer size) {
        return Flux.range(0, size)
                .map(number -> {
                    Person actor = new Person();
                    actor.setId(number.longValue());
                    actor.setName("Actor " + number);
                    actor.setMovies(LongStream.range(0, (number % 50) + 1)
                            .boxed()
                            .map(id -> new Movie(id, "Movie " + id))
                            .collect(Collectors.toSet())
                    );
                    return actor;
                })
                .collectList()
                .flatMap(data -> personRepository.saveAll(data).then())
                .then();
    }

    @GetMapping("/linked")
    public Flux<Person> getRelatedPersons(@RequestParam(name = "userId") Long userId) {
        return personRepository.findRelatedActors(userId);
    }
}
