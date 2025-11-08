package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.PetType;
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
 * Integration tests for PetTypeController (Types)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class PetTypeControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllPetTypes() throws Exception {
        this.mockMvc.perform(get("/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

    @Test
    public void testFindPetTypeOK() throws Exception {
        String TYPE_NAME = "bird";

        this.mockMvc.perform(get("/types/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is(TYPE_NAME)));
    }

    @Test
    public void testFindPetTypeKO() throws Exception {
        mockMvc.perform(get("/types/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePetType() throws Exception {
        String TYPE_NAME = "rabbit";

        PetType newPetType = new PetType();
        newPetType.setName(TYPE_NAME);

        this.mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(TYPE_NAME)));
    }

    @Test
    public void testDeletePetType() throws Exception {
        String TYPE_NAME = "TypeToDelete";

        PetType newPetType = new PetType();
        newPetType.setName(TYPE_NAME);

        ResultActions mvcActions = mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/types/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePetTypeKO() throws Exception {
        mockMvc.perform(delete("/types/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePetType() throws Exception {
        String TYPE_NAME = "TypeToUpdate";
        String UP_TYPE_NAME = "UpdatedType";

        PetType newPetType = new PetType();
        newPetType.setName(TYPE_NAME);

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // UPDATE
        PetType upPetType = new PetType();
        upPetType.setId(id);
        upPetType.setName(UP_TYPE_NAME);

        mockMvc.perform(put("/types/" + id)
                        .content(om.writeValueAsString(upPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/types/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(UP_TYPE_NAME)));

        // DELETE
        mockMvc.perform(delete("/types/" + id))
                .andExpect(status().isOk());
    }
}
