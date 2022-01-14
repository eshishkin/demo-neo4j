package org.eshishkin.edu.demoneo4j.model.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@Node("Person")
public class Person {

    @Id
    private Long id;

    @Property("name")
    private String name;

    @Relationship(type = "ACTED_IN")
    private Set<Movie> movies = new HashSet<>();
}
