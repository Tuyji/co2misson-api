package com.co2nsensus.co2mission;

import com.co2nsensus.co2mission.controller.AffiliateApplicationController;
import com.co2nsensus.co2mission.model.request.AffiliateApplicationRequest;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModel;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModelList;
import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import com.co2nsensus.co2mission.model.enums.ApplicationStatus;
import com.co2nsensus.co2mission.service.AffiliateApplicationService;
import com.co2nsensus.co2mission.utils.ListUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AffiliateApplicationController.class)
public class AffiliateApplicationControllerTest {

    @Autowired
    private MockMvc mvc;


    @MockBean
    private AffiliateApplicationService affiliateApplicationService;

    @Test
    public void getAllApplicationTest() throws Exception {

        AffiliateApplication entity = new AffiliateApplication();
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");
        entity.setStatus(ApplicationStatus.PENDING);

        List<AffiliateApplication> applications = singletonList(entity);

        ListUtils<AffiliateApplicationModel> utils = BeanUtils.instantiate(ListUtils.class);
        List<AffiliateApplicationModel> applicationModels = new ArrayList<>();
        utils.copyList(applications, applicationModels, AffiliateApplicationModel.class);

        AffiliateApplicationModelList affiliateApplicationModelList = AffiliateApplicationModelList.builder()
                .affiliateApplicationModels(applicationModels)
                .build();

        given(affiliateApplicationService.getApplications()).willReturn(affiliateApplicationModelList);

        mvc.perform(get("/application")).andExpect(status().isOk()).andDo(print());

    }


    @Test
    public void getApplicationById() throws Exception {

        AffiliateApplication entity = new AffiliateApplication();
        entity.setId(1L);
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");

        AffiliateApplicationModel affiliateApplicationModel = new AffiliateApplicationModel();

        BeanUtils.copyProperties(entity, affiliateApplicationModel);

        given(affiliateApplicationService.getApplication(String.valueOf(entity.getId())))
                .willReturn(affiliateApplicationModel);

        mvc.perform(get("/application/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void createApplication() throws Exception {

        AffiliateApplicationRequest request = new AffiliateApplicationRequest();

        AffiliateApplicationModel affiliateApplicationModel = new AffiliateApplicationModel();
        affiliateApplicationModel.setName("Name");
        affiliateApplicationModel.setSurname("Surname");
        affiliateApplicationModel.setEmail("mail@mail.com");

        request.setAffiliateApplicationModel(affiliateApplicationModel);

        given(affiliateApplicationService.createApplication(request)).willReturn(affiliateApplicationModel);

        mvc.perform(post("/application").content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }


    @Test
    public void updateApplication() throws Exception {

        AffiliateApplicationRequest request = new AffiliateApplicationRequest();

        AffiliateApplicationModel affiliateApplicationModel = new AffiliateApplicationModel();
        affiliateApplicationModel.setName("Name");
        affiliateApplicationModel.setSurname("Surname");
        affiliateApplicationModel.setEmail("mail@mail.com");

        request.setAffiliateApplicationModel(affiliateApplicationModel);

        given(affiliateApplicationService.updateApplication(request)).willReturn(affiliateApplicationModel);

        mvc.perform(put("/application").content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    public void deleteApplication() throws Exception {

        AffiliateApplication entity = new AffiliateApplication();
        entity.setId(1L);
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");

        given(affiliateApplicationService.deleteApplication("1L")).willReturn("success");

        mvc.perform(delete("/application/1"))
                .andExpect(status().isOk())
                .andReturn();

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
