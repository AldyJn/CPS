package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Owner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for OwnerController
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllOwners() throws Exception {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    @Test
    public void testFindOwnerOK() throws Exception {
        String FIRST_NAME = "George";
        String LAST_NAME = "Franklin";

        this.mockMvc.perform(get("/owners/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)));
    }

    @Test
    public void testFindOwnerKO() throws Exception {
        mockMvc.perform(get("/owners/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOwner() throws Exception {
        String FIRST_NAME = "John";
        String LAST_NAME = "Doe";
        String ADDRESS = "123 Main St";
        String CITY = "Lima";
        String TELEPHONE = "987654321";

        Owner newOwner = new Owner();
        newOwner.setFirstName(FIRST_NAME);
        newOwner.setLastName(LAST_NAME);
        newOwner.setAddress(ADDRESS);
        newOwner.setCity(CITY);
        newOwner.setTelephone(TELEPHONE);

        this.mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.address", is(ADDRESS)))
                .andExpect(jsonPath("$.city", is(CITY)))
                .andExpect(jsonPath("$.telephone", is(TELEPHONE)));
    }

    @Test
    public void testDeleteOwner() throws Exception {
        String FIRST_NAME = "OwnerToDelete";
        String LAST_NAME = "Test";
        String ADDRESS = "456 Test St";
        String CITY = "Arequipa";
        String TELEPHONE = "123456789";

        Owner newOwner = new Owner();
        newOwner.setFirstName(FIRST_NAME);
        newOwner.setLastName(LAST_NAME);
        newOwner.setAddress(ADDRESS);
        newOwner.setCity(CITY);
        newOwner.setTelephone(TELEPHONE);

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwnerKO() throws Exception {
        mockMvc.perform(delete("/owners/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateOwner() throws Exception {
        String FIRST_NAME = "OwnerToUpdate";
        String LAST_NAME = "Test";
        String ADDRESS = "789 Update St";
        String CITY = "Cusco";
        String TELEPHONE = "111222333";

        String UP_FIRST_NAME = "UpdatedOwner";
        String UP_LAST_NAME = "Updated";
        String UP_ADDRESS = "999 Updated Ave";
        String UP_CITY = "Trujillo";
        String UP_TELEPHONE = "444555666";

        Owner newOwner = new Owner();
        newOwner.setFirstName(FIRST_NAME);
        newOwner.setLastName(LAST_NAME);
        newOwner.setAddress(ADDRESS);
        newOwner.setCity(CITY);
        newOwner.setTelephone(TELEPHONE);

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        // UPDATE
        Owner upOwner = new Owner();
        upOwner.setId(id);
        upOwner.setFirstName(UP_FIRST_NAME);
        upOwner.setLastName(UP_LAST_NAME);
        upOwner.setAddress(UP_ADDRESS);
        upOwner.setCity(UP_CITY);
        upOwner.setTelephone(UP_TELEPHONE);

        mockMvc.perform(put("/owners/" + id)
                        .content(om.writeValueAsString(upOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/owners/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
                .andExpect(jsonPath("$.address", is(UP_ADDRESS)))
                .andExpect(jsonPath("$.city", is(UP_CITY)))
                .andExpect(jsonPath("$.telephone", is(UP_TELEPHONE)));

        // DELETE
        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }
}
