package com.enfint.deal.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel {
    private Date timeStamp;
    private String error;
    private String details;
    private HttpStatus httpStatus;
}
