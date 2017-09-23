package com.ftemotheos.hello;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactRestController {

    @Autowired
    private JdbcOperations jdbcOperations;

    @RequestMapping(value = "/hello/contacts", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String getContacts(@RequestParam(value="nameFilter") String nameFilter) {

        List<Contact> pojoContacts = jdbcOperations.query("SELECT * FROM contacts;", new BeanPropertyRowMapper<>(Contact.class));
        Pattern pattern = Pattern.compile(nameFilter);

        for (Iterator<Contact> iterator = pojoContacts.iterator(); iterator.hasNext(); ) {
            Matcher matcher = pattern.matcher(iterator.next().getName());
            if (matcher.matches()) {
                iterator.remove();
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String contacts = "";

        try {
            contacts = mapper.writeValueAsString(pojoContacts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return contacts;
    }

}
