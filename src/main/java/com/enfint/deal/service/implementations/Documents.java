package com.enfint.deal.service.implementations;

public interface Documents {

    void prepareDocument(Long id);
    void sendDocument(Long id);
    void signDocument(Long id);

    void verifyCode(Long id, Long code);
}
