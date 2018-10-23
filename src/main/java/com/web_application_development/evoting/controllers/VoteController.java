package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.CandidateService;
import com.web_application_development.evoting.services.VoteService;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/voting")
public class VoteController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final VoteService voteService;
    private final CandidateService candidateService;

    @Autowired
    VoteController(SimpMessageSendingOperations messagingTemplate, VoteService voteService, CandidateService candidateService) {
        this.messagingTemplate = messagingTemplate;
        this.voteService = voteService;
        this.candidateService = candidateService;
    }

    @PostMapping(value = "/vote")
    public void sendVote(@RequestBody VoteDTO voteDTO) {
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        // save new entity
        voteService.saveVote(voteDTO, authIdentity.getIdentityCode());

        Candidate candidate = candidateService.findCandidateById(voteDTO.getCandidateId());
        messagingTemplate.convertAndSend("/topic/votes", candidate);
    }
}
