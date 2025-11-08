package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Vet;
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

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindVetOK() throws Exception {
        this.mockMvc.perform(get("/vets/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testFindVetKO() throws Exception {
        mockMvc.perform(get("/vets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVet() throws Exception {
        String FIRST_NAME = "John";
        String LAST_NAME = "Doe";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        this.mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)));
    }

    @Test
    public void testDeleteVet() throws Exception {
        String FIRST_NAME = "VetToDelete";
        String LAST_NAME = "Test";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVetKO() throws Exception {
        mockMvc.perform(delete("/vets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVet() throws Exception {
        String FIRST_NAME = "VetToUpdate";
        String LAST_NAME = "Test";
        String UP_FIRST_NAME = "UpdatedVet";
        String UP_LAST_NAME = "Updated";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // UPDATE
        Vet upVet = new Vet();
        upVet.setId(id);
        upVet.setFirstName(UP_FIRST_NAME);
        upVet.setLastName(UP_LAST_NAME);

        mockMvc.perform(put("/vets/" + id)
                        .content(om.writeValueAsString(upVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/vets/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)));

        // DELETE
        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }
}
