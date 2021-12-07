package org.eshishkin.edu.demoneo4j.web;

import lombok.RequiredArgsConstructor;
import org.eshishkin.edu.demoneo4j.model.Person;
import org.eshishkin.edu.demoneo4j.persistence.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping
    public Flux<Person> test() {
        return personRepository.deleteAll()
                .thenMany(Flux.range(0, 10))
                .flatMap(i -> {
                    Person friend = new Person();
                    friend.setName("friend_" + i);

                    Person p = new Person();
                    p.setName("Name_" + i);
                    p.getFriends().add(friend);

                    return personRepository.save(p);
                });
    }
}
