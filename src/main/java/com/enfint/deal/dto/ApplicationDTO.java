package com.enfint.deal.dto;

import com.enfint.deal.entity.Client;
import com.enfint.deal.entity.Credit;
import com.enfint.deal.entity.model.ApplicationStatus;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDTO {

    private Long applicationId;
    private Client clientId;
    private Credit credit;
    private ApplicationStatus status;
    private LocalDate creationDate;
    private LoanOfferDTO appliedOffer;
    private LocalDate signDate;
    private Long sesCode;
    private List<ApplicationStatusHistoryDTO> statusHistory;
}
