package com.enfint.deal.controller;

import com.enfint.deal.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deal/document")
public class DocumentController {

    @Autowired
    protected DocumentService documentService;


    @PostMapping("/{applicationId}/send")
    public void sendDocuments(@PathVariable(value="applicationId") Long id)
    {

        documentService.sendDocument(id);
    }
    @PostMapping("/{applicationId}/sign")
    public void signDocuments(@PathVariable(value="applicationId") Long id)
    {
        documentService.signDocument(id);
    }
    @PostMapping("/{applicationId}/code")
    public void codeDocuments(@PathVariable(value="applicationId") Long id, @RequestBody Long code)
    {
        documentService.verifyCode(id, code);
    }

}
