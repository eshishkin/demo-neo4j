package org.eshishkin.edu.demoneo4j.model.sql;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Person {
    @Id
    private Long id;
    private String name;
}
