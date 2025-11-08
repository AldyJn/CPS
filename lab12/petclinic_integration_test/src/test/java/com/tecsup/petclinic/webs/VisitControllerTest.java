package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VisitDTO;
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
 * Integration tests for VisitController
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VisitControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllVisits() throws Exception {
        this.mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

    @Test
    public void testFindVisitOK() throws Exception {
        String VISIT_DATE = "2010-03-04";

        this.mockMvc.perform(get("/visits/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.visitDate", is(VISIT_DATE)));
    }

    @Test
    public void testFindVisitKO() throws Exception {
        mockMvc.perform(get("/visits/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVisit() throws Exception {
        String VISIT_DATE = "2024-01-15";
        String DESCRIPTION = "Regular checkup";
        int PET_ID = 1;

        VisitDTO newVisit = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .petId(PET_ID)
                .build();

        this.mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.petId", is(PET_ID)));
    }

    @Test
    public void testDeleteVisit() throws Exception {
        String VISIT_DATE = "2024-02-20";
        String DESCRIPTION = "VisitToDelete";
        int PET_ID = 1;

        VisitDTO newVisit = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .petId(PET_ID)
                .build();

        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVisitKO() throws Exception {
        mockMvc.perform(delete("/visits/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVisit() throws Exception {
        String VISIT_DATE = "2024-03-10";
        String DESCRIPTION = "VisitToUpdate";
        int PET_ID = 1;

        String UP_VISIT_DATE = "2024-03-15";
        String UP_DESCRIPTION = "UpdatedVisit";

        VisitDTO newVisit = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .petId(PET_ID)
                .build();

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        // UPDATE
        VisitDTO upVisit = VisitDTO.builder()
                .id(id)
                .visitDate(UP_VISIT_DATE)
                .description(UP_DESCRIPTION)
                .petId(PET_ID)
                .build();

        mockMvc.perform(put("/visits/" + id)
                        .content(om.writeValueAsString(upVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/visits/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.visitDate", is(UP_VISIT_DATE)))
                .andExpect(jsonPath("$.description", is(UP_DESCRIPTION)));

        // DELETE
        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());
    }
}
