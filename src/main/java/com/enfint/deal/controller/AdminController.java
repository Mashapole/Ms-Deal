package com.enfint.deal.controller;

import com.enfint.deal.dto.ApplicationDTO;
import com.enfint.deal.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/deal/admin/application")
public class AdminController {

    @Autowired
    protected AdminService adminService;
    
    @GetMapping("/{applicationId}")
    public ApplicationDTO getApplicationById(@PathVariable(value = "applicationId") Long id)
    {
     return adminService.getApplicationById(id);
    }

    @GetMapping("/{applicationId}/status")
    public void updateApplicationStatus(@PathVariable(value = "applicationId") Long id)
    {
     adminService.updateApplicationStatus(id);
    }
}
