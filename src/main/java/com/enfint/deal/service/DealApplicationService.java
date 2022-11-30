package com.enfint.deal.service;

import com.enfint.deal.dto.*;
import com.enfint.deal.dto.enumm.ChangeType;
import com.enfint.deal.entity.Application;
import com.enfint.deal.entity.Client;
import com.enfint.deal.entity.Credit;
import com.enfint.deal.entity.model.ApplicationStatus;
import com.enfint.deal.entity.model.Passport;
import com.enfint.deal.exception.EntityNotFoundException;
import com.enfint.deal.feign.FeignServiceUntil;
import com.enfint.deal.repository.ApplicationRepository;
import com.enfint.deal.repository.ClientRepository;
import com.enfint.deal.repository.CreditRepositoty;
import com.enfint.deal.service.implementations.Helper;
import com.enfint.deal.validation.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealApplicationService implements Helper {


    @Autowired
    CreditRepositoty creditRepositoty;

    @Autowired
    protected ClientRepository clientRepository;
    @Autowired
    protected ApplicationRepository applicationRepository;

    @Autowired
    protected FeignServiceUntil feignServiceUntil;

    private static final String LINE ="------------------------------";
    private static final String EXECUTED="Execution Completed";
    @Override
    public List<LoanOfferDTO> possibleCalculation(@RequestBody LoanApplicationRequestDTO reg)
    {


        //I always prefer to validate data
        validationData(reg);
        Client createClient=createClient(reg);

        //let's store data to database
        log.info(LINE);
        log.info("Client entity is created, and data is stored to database ");
        Client storeClient=clientRepository.save(createClient);
        log.info(EXECUTED);
        log.info("CLIENT TABLE WITH DATA:{}", storeClient);
        log.info(LINE);

        log.info(LINE);
        log.info("Application entity is created, and data is stored to database ");
        Application application=new Application();
        application.setClientId(storeClient);
        application.setCreationDate(LocalDate.now());

        Application store=applicationRepository.save(application);
        log.info("APPLICATION TABLE WITH DATA:{}", store);
        log.info(EXECUTED);
        log.info(LINE);


        log.info(LINE);
        log.info("Post Request is sent using Feign");
        List<LoanOfferDTO> loanOfferDTOS=feignServiceUntil.offers(reg);
        log.info(EXECUTED);
        log.info(LINE);

        log.info(LINE);
        log.info("Assign LoanOffer With ID");
        if(loanOfferDTOS!=null)
        {
            loanOfferDTOS.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));
            log.info("LoanOffer With ID:{}", loanOfferDTOS);
        }
        else
        {
            log.info(LINE);
            log.info("LoanOfferDTO is Empty");
            log.info(EXECUTED);
            log.info(LINE);
        }
        log.info(EXECUTED);
        log.info(LINE);

        return loanOfferDTOS;
    }

    @Override
    public void selectingOffer(LoanOfferDTO loanOfferDTO)
    {
        log.info(LINE);
        log.info("Selecting offer");

        Application application=applicationRepository.findById(loanOfferDTO.getApplicationId()).
                orElseThrow(()->new EntityNotFoundException("Request No Found"));

        List<ApplicationStatusHistoryDTO> stat= new ArrayList<>();
        stat.add(new ApplicationStatusHistoryDTO(ApplicationStatus.APPROVED, LocalDateTime.now(), ChangeType.APPROVED));


        application.setStatus(ApplicationStatus.APPROVED);
        application.setStatusHistory(stat);
        application.setAppliedOffer(loanOfferDTO);
        final Application app=applicationRepository.save(application);
        log.info("Application Information is updated and saved to DB");
        log.info("TABLE DATA IS:{}", app);
        log.info(LINE);

    }

    @Override
    public void registrationCompletion(Long applicationId,ScoringDataDTO scoringDataDTO)
    {
        log.info(LINE);
        log.info("Application retrieve by Id");
        Application getApplication=applicationRepository.findById(applicationId).orElseThrow(()->new EntityNotFoundException("Requested ID Not Found"));
        log.info("Application retrieved");
        log.info(LINE);
        log.info("Filling ScoringDTO with Data");

        Client getClient=getApplication.getClientId();
        LoanOfferDTO getAppliedOffer=getApplication.getAppliedOffer();

        ScoringDataDTO storeData=setData(getAppliedOffer,getClient);

        log.info("SCORING DATA:{}", storeData);
        log.info(LINE);

        log.info("POST REQUEST SENT TO /conveyor/calculation");
        CreditDTO credit=feignServiceUntil.calculationService(scoringDataDTO);
        log.info("REQUEST EXECUTED");
        log.info(LINE);

        log.info("creating credit entity");
        Credit createCredit=fillCredit(credit);
        creditRepositoty.save(createCredit);

        getApplication.setCredit(createCredit);
        applicationRepository.save(getApplication);
        log.info("CREDIT DATA:{}", createCredit);
        log.info("REQUEST EXECUTED");
        log.info(LINE);
    }

    private Credit fillCredit(CreditDTO credit) {
        Credit store= new Credit();
        store.setAmount(credit.getAmount());
        store.setTerm(credit.getTerm());
        store.setMonthlyPayment(credit.getMonthlyPayment());
        store.setRate(credit.getRate());
        store.setFlc(credit.getPsk());
        store.setIsInsuranceEnabled(credit.isInsuranceEnabled());
        store.setIsSalaryClient(credit.isSalaryClient());
        store.setPaymentScheduleElement(credit.getPaymentSchedule());
        return store;
    }
    private ScoringDataDTO setData(LoanOfferDTO getAppliedOffer, Client getClient) {
        ScoringDataDTO storeData= new ScoringDataDTO();
        storeData.setAmount(getAppliedOffer.getRequestAmount());
        storeData.setTerm(getAppliedOffer.getTerm());
        storeData.setFirstName(getClient.getFirstName());
        storeData.setLastName(getClient.getLastName());
        storeData.setMiddleName(getClient.getMiddleName());
        storeData.setGender(getClient.getGender());
        storeData.setBirthdate(getClient.getBirthDate());
        storeData.setPassportSeries(getClient.getPassport().getSeries());
        storeData.setPassportNumber(getClient.getPassport().getNumber());
        storeData.setPassportIssueDate(getClient.getPassport().getIssueDate());
        storeData.setPassportIssueBranch(getClient.getPassport().getIssueBranch());
        storeData.setMaritalStatus(getClient.getMaritalStatus());
        storeData.setDependentAmount(getClient.getDependentNumber());
        EmploymentDTO employmentDTO=getClient.getEmployement();
        storeData.setEmployment(employmentDTO);
        storeData.setInsuranceEnabled(getAppliedOffer.isInsuranceEnabled());
        storeData.setSalaryClient(getAppliedOffer.isSalaryClient());

        return storeData;
    }

    public Client createClient(LoanApplicationRequestDTO reg)
    {
        Client client= new Client();
        client.setLastName(reg.getLastName());
        client.setFirstName(reg.getFirstName());
        client.setMiddleName(reg.getMiddleName());
        client.setBirthDate(reg.getBirthdate());
        client.setEmail(reg.getEmail());
        Passport pass= new Passport();
        pass.setSeries(reg.getPassportSeries());
        pass.setNumber(reg.getPassportNumber());
        client.setPassport(pass);

        return client;
    }

    private void validationData(LoanApplicationRequestDTO reg)
    {
        log.info(LINE);
        log.info("Data Validation");
        if(!Validate.checkAmount(reg.getAmount())) return;

        if(!Validate.checkTerm(reg.getTerm())) return;

        if(!Validate.checkFirstName(reg.getFirstName())) return;

        if(!Validate.checkLastName(reg.getLastName())) return;

        if(!Validate.checkEmail(reg.getEmail())) return;

        if(!Validate.checkBirthdate(reg.getBirthdate())) return;

        if(!Validate.checkPassportSeries(reg.getPassportSeries())) return;

        if(!Validate.checkPassportNumber(reg.getPassportNumber())) return;
        log.info(LINE);
        log.info("Data Validated");
        log.info(LINE);
    }
}
