package com.ftemotheos.hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretProjectOnSpringBootApplication.class)
@ContextConfiguration(classes = JdbcOperationsConfig.class)
@WebAppConfiguration
public class RestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void setUpTable() {
        jdbcOperations.execute(
                "CREATE TABLE IF NOT EXISTS contacts (" +
                        "id INT(10) UNSIGNED NOT NULL PRIMARY KEY, " +
                        "name VARCHAR(45) NOT NULL UNIQUE KEY" +
                ");"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (1, 'Alice Johnson');"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (2, 'Bill Gates');"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (3, 'John Silver');"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (4, 'Donald Trump');"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (5, 'Monica Bellucci' );"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (6, 'Silvia Peters' );"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (7, 'Joshua Bloch' );"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (8, 'Britney Spears' );"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (9, 'Sara Connor' );"
        );
        jdbcOperations.update(
                "INSERT IGNORE INTO contacts (id, name) " +
                        "VALUES (10, 'Peter Parker' );"
        );
    }

    @Test
    public void getWithoutParameter() throws Exception {
        mockMvc.perform(get("/hello/contacts"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/hello/contacts").param("nameFilter", "$"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"Alice Johnson\"}," +
                        "{\"id\":2,\"name\":\"Bill Gates\"}," +
                        "{\"id\":3,\"name\":\"John Silver\"}," +
                        "{\"id\":4,\"name\":\"Donald Trump\"}," +
                        "{\"id\":5,\"name\":\"Monica Bellucci\"}," +
                        "{\"id\":6,\"name\":\"Silvia Peters\"}," +
                        "{\"id\":7,\"name\":\"Joshua Bloch\"}," +
                        "{\"id\":8,\"name\":\"Britney Spears\"}," +
                        "{\"id\":9,\"name\":\"Sara Connor\"}," +
                        "{\"id\":10,\"name\":\"Peter Parker\"}]"
                ));
    }

    @Test
    public void getNotStart_A() throws Exception {
        mockMvc.perform(get("/hello/contacts").param("nameFilter", "^A.*$"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().json(
                        "[{\"id\":2,\"name\":\"Bill Gates\"}," +
                        "{\"id\":3,\"name\":\"John Silver\"}," +
                        "{\"id\":4,\"name\":\"Donald Trump\"}," +
                        "{\"id\":5,\"name\":\"Monica Bellucci\"}," +
                        "{\"id\":6,\"name\":\"Silvia Peters\"}," +
                        "{\"id\":7,\"name\":\"Joshua Bloch\"}," +
                        "{\"id\":8,\"name\":\"Britney Spears\"}," +
                        "{\"id\":9,\"name\":\"Sara Connor\"}," +
                        "{\"id\":10,\"name\":\"Peter Parker\"}]"
                ));
    }

    @Test
    public void getNotEnd_rs() throws Exception {
        mockMvc.perform(get("/hello/contacts").param("nameFilter", "^.*rs$"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"Alice Johnson\"}," +
                        "{\"id\":2,\"name\":\"Bill Gates\"}," +
                        "{\"id\":3,\"name\":\"John Silver\"}," +
                        "{\"id\":4,\"name\":\"Donald Trump\"}," +
                        "{\"id\":5,\"name\":\"Monica Bellucci\"}," +
                        "{\"id\":7,\"name\":\"Joshua Bloch\"}," +
                        "{\"id\":9,\"name\":\"Sara Connor\"}," +
                        "{\"id\":10,\"name\":\"Peter Parker\"}]"
                ));
    }

    @Test
    public void getContain_BCJSaehilnoprstvy_() throws Exception {
        mockMvc.perform(get("/hello/contacts").param("nameFilter", "^.*[^BCJSaehilnoprstvy ].*$"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().json(
                        "[{\"id\":3,\"name\":\"John Silver\"}," +
                        "{\"id\":8,\"name\":\"Britney Spears\"}," +
                        "{\"id\":9,\"name\":\"Sara Connor\"}]"
                ));
    }

    @Test
    public void getNotContain_tvr() throws Exception {
        mockMvc.perform(get("/hello/contacts").param("nameFilter", "^.*[tvr].*$"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"Alice Johnson\"}," +
                        "{\"id\":5,\"name\":\"Monica Bellucci\"}," +
                        "{\"id\":7,\"name\":\"Joshua Bloch\"}]"
                ));
    }

}
