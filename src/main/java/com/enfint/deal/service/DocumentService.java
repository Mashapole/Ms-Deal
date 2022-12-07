package com.enfint.deal.service;

import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.EmailMessage;
import com.enfint.deal.dto.enumm.ChangeType;
import com.enfint.deal.dto.enumm.Theme;
import com.enfint.deal.entity.Application;
import com.enfint.deal.entity.model.ApplicationStatus;
import com.enfint.deal.exception.EntityNotFoundException;
import com.enfint.deal.service.Kafka.Producer;
import com.enfint.deal.service.implementations.Documents;
import com.enfint.deal.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class DocumentService implements Documents {

    protected final static String LINE="--------------------------------------";
    protected final static String ERROR="The Application does not exist!";

    @Autowired
    protected ApplicationRepository applicationRepository;

    @Autowired
    protected Producer producer;



    @Override
    public void prepareDocument(Long id)
    {
        Application application=applicationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(ERROR));
        if(application.getStatus()== ApplicationStatus.CC_APPROVED)
        {
            log.info(LINE);
            log.info("Creating application{}", application.getClientId().getEmail());
            EmailMessage emailMessage=new EmailMessage();
            emailMessage.setTheme(Theme.CREATE_DOCUMENTS);
            emailMessage.setAddress(application.getClientId().getEmail());
            emailMessage.setApplicationId(application.getApplicationId());
            producer.message(emailMessage);

        }
        else
        {
         throw new EntityNotFoundException("Application not met");
        }
    }
    @Override
    public void sendDocument(Long id) {

      log.info(LINE);
        Application application=applicationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(ERROR));
        if(application.getStatus()== ApplicationStatus.CC_APPROVED)
        {
            log.info(LINE);
            log.info("Creating application for user{}", application.getClientId().getEmail());
            EmailMessage emailMessage=new EmailMessage();
            emailMessage.setTheme(Theme.SEND_DOCUMENTS);
            emailMessage.setAddress(application.getClientId().getEmail());
            emailMessage.setApplicationId(application.getApplicationId());
            producer.message(emailMessage);

        }
        else
        {
            throw new EntityNotFoundException("Error");
        }
    }
    @Override
    public void signDocument(Long id)
    {
        Long code=getCode();
        Application application=applicationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(ERROR));
        if(application.getStatus()== ApplicationStatus.CC_APPROVED)
        {
            log.info(LINE);
            log.info("application for user{}", application.getClientId().getEmail());

            application.setSesCode(code);
            applicationRepository.save(application);
            EmailMessage emailMessage=new EmailMessage();
            emailMessage.setTheme(Theme.SEND_SES);
            emailMessage.setAddress(application.getClientId().getEmail());
            emailMessage.setApplicationId(application.getApplicationId());
            producer.message(emailMessage);

        }
        else
        {
            throw new EntityNotFoundException("Application requirements not met");
        }

    }
    @Override
    public void verifyCode(Long id, Long code)
    {
        Application application=applicationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(ERROR));
        if(application.getStatus()== ApplicationStatus.DOCUMENT_CREATED)
        {
            log.info(LINE);
            log.info("application for user{}", application.getClientId().getEmail());

            List<ApplicationStatusHistoryDTO> stat;
            if(code.equals(application.getSesCode()))
            {
                application.setStatus(ApplicationStatus.DOCUMENT_SIGNED);
                stat=application.getStatusHistory();
                stat.add(new ApplicationStatusHistoryDTO(ApplicationStatus.DOCUMENT_SIGNED, LocalDateTime.now(), ChangeType.MANUAL));
                application.setStatusHistory(stat);

                application.setStatus(ApplicationStatus.CREDIT_ISSUED);
                stat.add(new ApplicationStatusHistoryDTO(ApplicationStatus.CREDIT_ISSUED, LocalDateTime.now(), ChangeType.MANUAL));
                applicationRepository.save(application);

                EmailMessage emailMessage=new EmailMessage();
                emailMessage.setTheme(Theme.CREDIT_ISSUED);
                emailMessage.setAddress(application.getClientId().getEmail());
                emailMessage.setApplicationId(application.getApplicationId());
                producer.message(emailMessage);
            }

        }
        else
        {
            throw new EntityNotFoundException("Application requirements not met");
        }
    }
    private Long getCode() {

        int min=999;
        int max=2000;
        int code;

        code=(int) (Math.random()*(max-min)+min);

        return (long) code;
    }
}
