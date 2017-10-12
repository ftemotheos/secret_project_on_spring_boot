package com.ftemotheos.hello;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ContactRestController {

    @Autowired
    private ContactsRepository contactsRepository;

    @RequestMapping(value = "/hello/contacts", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String getContacts(@RequestParam(value="nameFilter") String nameFilter) {

        List<Contact> pojoContacts = contactsRepository.getAll();
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

    @ExceptionHandler(PatternSyntaxException.class)
    public ModelAndView handlePatternSyntaxException(HttpServletRequest req, PatternSyntaxException e, HttpServletResponse response) throws IOException {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        return mav;
    }

    @Bean
    public ContactsRepository contactsRepository() {
        return new ConcreteContactsRepository();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("contacts");
    }

}
