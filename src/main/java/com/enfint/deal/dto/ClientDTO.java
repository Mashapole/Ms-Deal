package com.enfint.deal.dto;


import com.enfint.deal.dto.enumm.Gender;
import com.enfint.deal.dto.enumm.MaritalStatus;
import com.enfint.deal.entity.model.Passport;
import lombok.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {

    private Long clientId;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentNumber;
    private Passport passport;
    private EmploymentDTO employment;
    private String account;

}
