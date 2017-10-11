package com.ftemotheos.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteContactsRepository implements ContactsRepository {

    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    @Cacheable("contacts")
    public List<Contact> getAll() {
        return jdbcOperations.query("SELECT * FROM contacts;", new BeanPropertyRowMapper<>(Contact.class));
    }

}
