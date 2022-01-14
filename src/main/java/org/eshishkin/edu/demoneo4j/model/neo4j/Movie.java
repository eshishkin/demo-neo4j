package org.eshishkin.edu.demoneo4j.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Movie")
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    private Long id;

    @Property
    private String name;
}
