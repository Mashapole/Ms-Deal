package com.enfint.deal.feign;

import com.enfint.deal.dto.CreditDTO;
import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeignService {

    @Autowired
    FeignServiceUntil until;
    public List<LoanOfferDTO> offerDTOList(LoanApplicationRequestDTO loanApplicationRequestDTO)
    {
        return until.offers(loanApplicationRequestDTO);
    }
    public CreditDTO calculateService(ScoringDataDTO scoringDataDTO)
    {
        return  until.calculationService(scoringDataDTO);
    }
}
