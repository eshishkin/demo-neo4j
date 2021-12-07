package org.eshishkin.edu.demoneo4j.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Data
@Node("Person")
public class Person {

    @Id
    @Property("userId")
    @GeneratedValue(value = GeneratedValue.UUIDGenerator.class)
    private UUID userId;

    @Property("name")
    private String name;

    @Relationship(type = "OWNS", direction = OUTGOING)
    private Set<Account> accounts = new HashSet<>();

    @Relationship(type = "FRIEND_WITH")
    private Set<Person> friends = new HashSet<>();
}
