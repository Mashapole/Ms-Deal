package com.enfint.deal.service;

import com.enfint.deal.dto.ApplicationDTO;
import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.ClientDTO;
import com.enfint.deal.dto.enumm.ChangeType;
import com.enfint.deal.entity.Application;
import com.enfint.deal.entity.Client;
import com.enfint.deal.entity.model.ApplicationStatus;
import com.enfint.deal.exception.EntityNotFoundException;
import com.enfint.deal.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminService {

    @Autowired
    protected ApplicationRepository applicationRepository;
    private final static String LINE="--------------------------------------";

    public ApplicationDTO getApplicationById(Long id)
    {
        log.info(LINE);
        log.info("Getting Application by Id");

        Application application=applicationRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Request No Found"));

        log.info("Application{}:", id);
        Client client=application.getClientId();

        ClientDTO.builder()
                .clientId(client.getClientId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .birthDate(client.getBirthDate())
                .email(client.getEmail())
                .gender(client.getGender())
                .maritalStatus(client.getMaritalStatus())
                .dependentNumber(client.getDependentNumber())
                .passport(client.getPassport())
                .employment(client.getEmployement())
                .account(client.getAccount())
                .build();
        return ApplicationDTO.builder().applicationId(application.getApplicationId()).clientId(client).status(application.getStatus()).creationDate(application.getCreationDate()).appliedOffer(application.getAppliedOffer()).signDate(application.getSignDate())
                .sesCode(application.getSesCode()).statusHistory(application.getStatusHistory()).build();
    }

    public void updateApplicationStatus(Long id)
    {
        log.info(LINE);
        log.info("updating application by status");

        List<ApplicationStatusHistoryDTO> historyDTOList;
        Application application=applicationRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Request No Found"));

        log.info("Application{}:", id);
        Client client= application.getClientId();
        log.info("client{}", client);
        historyDTOList=application.getStatusHistory();
        application.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        historyDTOList.add(new ApplicationStatusHistoryDTO(ApplicationStatus.DOCUMENT_CREATED, LocalDateTime.now(), ChangeType.MANUAL));
        application.setStatusHistory(historyDTOList);
        applicationRepository.save(application);
    }
}
