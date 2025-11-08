package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Specialty;
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

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Vet-Specialty relationship
 * Tests the many-to-many relationship between Vet and Specialty entities
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetSpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateVetWithoutSpecialties() throws Exception {
        // Create vet without specialty
        String VET_FIRST_NAME = "TestVet";
        String VET_LAST_NAME = "WithoutSpecialty";

        Vet vet = new Vet();
        vet.setFirstName(VET_FIRST_NAME);
        vet.setLastName(VET_LAST_NAME);

        ResultActions vetActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(vet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(VET_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(VET_LAST_NAME));

        String vetResponse = vetActions.andReturn().getResponse().getContentAsString();
        Integer vetId = JsonPath.parse(vetResponse).read("$.id");

        // Verify vet was created
        mockMvc.perform(get("/vets/" + vetId))
                .andExpect(status().isOk())
                .andDo(print());

        // Cleanup
        mockMvc.perform(delete("/vets/" + vetId)).andExpect(status().isOk());
    }

    @Test
    public void testCreateAndUpdateVet() throws Exception {
        // Create vet
        String VET_FIRST_NAME = "TestVet2";
        String VET_LAST_NAME = "UpdateTest";

        Vet vet = new Vet();
        vet.setFirstName(VET_FIRST_NAME);
        vet.setLastName(VET_LAST_NAME);

        ResultActions vetActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(vet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String vetResponse = vetActions.andReturn().getResponse().getContentAsString();
        Integer vetId = JsonPath.parse(vetResponse).read("$.id");

        // Update vet
        String UPDATED_FIRST_NAME = "UpdatedVet";
        Vet updateVet = new Vet();
        updateVet.setId(vetId);
        updateVet.setFirstName(UPDATED_FIRST_NAME);
        updateVet.setLastName(VET_LAST_NAME);

        mockMvc.perform(put("/vets/" + vetId)
                        .content(om.writeValueAsString(updateVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify vet was updated
        mockMvc.perform(get("/vets/" + vetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(UPDATED_FIRST_NAME))
                .andDo(print());

        // Cleanup
        mockMvc.perform(delete("/vets/" + vetId)).andExpect(status().isOk());
    }

    @Test
    public void testMultipleVetsAndSpecialties() throws Exception {
        // Create specialty
        String SPECIALTY_NAME = "VetSpecialtyTest4";
        Specialty specialty = new Specialty();
        specialty.setName(SPECIALTY_NAME);

        ResultActions specialtyActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(specialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String specialtyResponse = specialtyActions.andReturn().getResponse().getContentAsString();
        Integer specialtyId = JsonPath.parse(specialtyResponse).read("$.id");

        // Create first vet
        Vet vet1 = new Vet();
        vet1.setFirstName("Vet1");
        vet1.setLastName("Relationship");

        ResultActions vet1Actions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(vet1))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String vet1Response = vet1Actions.andReturn().getResponse().getContentAsString();
        Integer vet1Id = JsonPath.parse(vet1Response).read("$.id");

        // Create second vet
        Vet vet2 = new Vet();
        vet2.setFirstName("Vet2");
        vet2.setLastName("Relationship");

        ResultActions vet2Actions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(vet2))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String vet2Response = vet2Actions.andReturn().getResponse().getContentAsString();
        Integer vet2Id = JsonPath.parse(vet2Response).read("$.id");

        // Verify both vets exist
        mockMvc.perform(get("/vets/" + vet1Id))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/vets/" + vet2Id))
                .andExpect(status().isOk())
                .andDo(print());

        // Cleanup
        mockMvc.perform(delete("/vets/" + vet1Id)).andExpect(status().isOk());
        mockMvc.perform(delete("/vets/" + vet2Id)).andExpect(status().isOk());
        mockMvc.perform(delete("/specialties/" + specialtyId)).andExpect(status().isOk());
    }

    @Test
    public void testDeleteVetAndSpecialty() throws Exception {
        // Create specialty
        String SPECIALTY_NAME = "VetSpecialtyTest5";
        Specialty specialty = new Specialty();
        specialty.setName(SPECIALTY_NAME);

        ResultActions specialtyActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(specialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String specialtyResponse = specialtyActions.andReturn().getResponse().getContentAsString();
        Integer specialtyId = JsonPath.parse(specialtyResponse).read("$.id");

        // Create vet
        Vet vet = new Vet();
        vet.setFirstName("VetDelete");
        vet.setLastName("Test");

        ResultActions vetActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(vet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String vetResponse = vetActions.andReturn().getResponse().getContentAsString();
        Integer vetId = JsonPath.parse(vetResponse).read("$.id");

        // Verify vet exists
        mockMvc.perform(get("/vets/" + vetId))
                .andExpect(status().isOk())
                .andDo(print());

        // Delete vet
        mockMvc.perform(delete("/vets/" + vetId))
                .andExpect(status().isOk());

        // Verify vet is deleted
        mockMvc.perform(get("/vets/" + vetId))
                .andExpect(status().isNotFound());

        // Cleanup specialty
        mockMvc.perform(delete("/specialties/" + specialtyId)).andExpect(status().isOk());
    }
}
