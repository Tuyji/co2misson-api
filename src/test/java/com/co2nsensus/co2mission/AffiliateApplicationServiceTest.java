package com.co2nsensus.co2mission;

import com.co2nsensus.co2mission.model.request.AffiliateApplicationRequest;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModel;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModelList;
import com.co2nsensus.co2mission.model.entity.AffiliateApplication;
import com.co2nsensus.co2mission.model.enums.ApplicationStatus;
import com.co2nsensus.co2mission.repo.AffiliateApplicationRepository;
import com.co2nsensus.co2mission.service.impl.AffiliateApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AffiliateApplicationServiceTest {

    @Mock
    AffiliateApplicationRepository repository;

    @InjectMocks
    AffiliateApplicationServiceImpl service;

    @Test
    public void getAffiliateApplication() throws Exception {

        AffiliateApplication entity = new AffiliateApplication();
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");
        entity.setStatus(ApplicationStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        AffiliateApplicationModel applicationModel = service.getApplication("1");

        assertEquals(applicationModel.getName(), entity.getName());
        assertEquals(applicationModel.getEmail(), entity.getEmail());
        assertEquals(applicationModel.getStatus(), entity.getStatus());
    }


    @Test
    public void getApplications() throws Exception {

        AffiliateApplication entity = new AffiliateApplication();
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");
        entity.setStatus(ApplicationStatus.PENDING);

        List<AffiliateApplication> affiliateApplications = singletonList(entity);

        when(repository.findAll()).thenReturn(affiliateApplications);

        AffiliateApplicationModelList applications = service.getApplications();

        assertEquals(affiliateApplications.size(), applications.getAffiliateApplicationModels().size());
        assertEquals(affiliateApplications.get(0).getName(), applications.getAffiliateApplicationModels().get(0).getName());

    }

    @Test
    public void createApplication(){
        AffiliateApplication entity = new AffiliateApplication();
        entity.setId(1L);
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");
        entity.setStatus(ApplicationStatus.PENDING);

        when(repository.save(any(AffiliateApplication.class))).thenReturn(entity);

        AffiliateApplicationRequest request = new AffiliateApplicationRequest();
        request.setAffiliateApplicationModel(new AffiliateApplicationModel());
        request.getAffiliateApplicationModel().setId(1L);
        request.getAffiliateApplicationModel().setName("Name");
        request.getAffiliateApplicationModel().setSurname("Surname");
        request.getAffiliateApplicationModel().setEmail("mail@mail.com");
        request.getAffiliateApplicationModel().setStatus(ApplicationStatus.PENDING);


        AffiliateApplicationModel applicationModel = service.createApplication(request);

        assertEquals(applicationModel.getId(), String.valueOf(entity.getId()));

    }


    @Test
    public void updateApplication(){
        final AffiliateApplication entity = new AffiliateApplication();
        entity.setId(1L);
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");
        entity.setStatus(ApplicationStatus.PENDING);


        given(repository.save(entity)).willReturn(entity);

        AffiliateApplicationRequest rq = new AffiliateApplicationRequest();

        AffiliateApplicationModel model = new AffiliateApplicationModel();

        BeanUtils.copyProperties(entity, model);

        model.setId(entity.getId());

        rq.setAffiliateApplicationModel(model);
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        final AffiliateApplicationModel expected = service.updateApplication(rq);
        assertThat(expected).isNotNull();

        AffiliateApplication newEntity = new AffiliateApplication();

        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setStatus(ApplicationStatus.SUCCESS);

        entity.setStatus(ApplicationStatus.SUCCESS);

        verify(repository).findById(entity.getId());
    }


    @Test
    public void deleteApplication() {
        AffiliateApplication entity = new AffiliateApplication();
        entity.setId(1L);
        entity.setName("Name");
        entity.setSurname("Surname");
        entity.setEmail("mail@mail.com");
        entity.setStatus(ApplicationStatus.PENDING);

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        service.deleteApplication(String.valueOf(entity.getId()));
        verify(repository).deleteById(entity.getId());
    }


}
