package com.web.tech.Repository;

import com.web.tech.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
    // Custom query methods can be added here if needed
}
