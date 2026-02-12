package com.bootcodeperu.admision_academica.adapter.monitoring.health;

import org.bson.Document;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoHealthIndicator implements HealthIndicator {

    private final MongoTemplate mongoTemplate;

    public MongoHealthIndicator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Health health() {
        try {
            // Ejecutamos un comando simple de ping a la base de datos
            Document command = new Document("ping", 1);
            mongoTemplate.getDb().runCommand(command);

            return Health.up()
                    .withDetail("database", "MongoDB")
                    .withDetail("status", "Operacional")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "MongoDB")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}