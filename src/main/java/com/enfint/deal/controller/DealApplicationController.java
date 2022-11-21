package com.enfint.deal.controller;

import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import com.enfint.deal.service.DealApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
@Slf4j
public class DealApplicationController {
    @Autowired
    private final DealApplicationService dealApplicationService=new DealApplicationService();

    private static final String LINE ="---------------------------------------";
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> possibleCalculation(@RequestBody LoanApplicationRequestDTO loan)
    {
        log.info(LINE);
        log.info("deal/application is running");
        return ResponseEntity.ok(dealApplicationService.possibleCalculation(loan));
    }
    @PutMapping("/offer")
    public void selectOffers(@RequestBody LoanOfferDTO loanOfferDTO)
    {
        log.info(LINE);
        log.info("deal/offer is running");
        dealApplicationService.selectingOffer(loanOfferDTO);
    }
    @PutMapping("/calculate/{applicationId}")
    public void registrationCompletion(@PathVariable(value = "applicationId") Long applicationId,@RequestBody ScoringDataDTO scoringDataDTO)
    {
        log.info(LINE);
        log.info("calculate is running");
        dealApplicationService.registrationCompletion(applicationId,scoringDataDTO);
    }
}
