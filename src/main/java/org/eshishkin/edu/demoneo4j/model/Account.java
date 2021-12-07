package org.eshishkin.edu.demoneo4j.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.UUID;

@Node("Account")
public class Account {

    @Id
    @GeneratedValue(value = GeneratedValue.UUIDGenerator.class)
    private UUID number;

    @Property
    private String currency;

    @Property
    private String type;
}
