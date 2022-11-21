package com.enfint.deal.service.implementations;

import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;

import java.util.List;

public interface Helper {

    List<LoanOfferDTO> possibleCalculation(LoanApplicationRequestDTO reg);

    void selectingOffer(LoanOfferDTO loanOfferDTO);

    void registrationCompletion(Long applicationId,ScoringDataDTO scoringDataDTO);
}
