package com.web_application_development.evoting.services;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.repositories.CandidateRepository;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> findAllCandidates() {
        return candidateRepository.findAllCandidates();
    }

    public Candidate findCandidateById(Integer candidateId) {
        return candidateRepository.findById(candidateId.longValue()).orElse(null);
    }

    public Candidate findCandidateUserVotedFor(String idenCode) {
        return candidateRepository.findCandidateUserVotedFor(idenCode).orElse(null);
    }

    public void saveCandidate(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
        // map DTO to entity
        Candidate entity = mapDTOToEntity(candidateDTO, authIdentity);
        // save new entity
        candidateRepository.save(entity);
    }

    private Candidate mapDTOToEntity(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
        Candidate candidateEntity = new Candidate();
        String givenName = authIdentity.getGivenName();
        String lastName = authIdentity.getSurName();
        candidateEntity.setFirstName(
                givenName.substring(0, 1).toUpperCase() + givenName.substring(1).toLowerCase());
        candidateEntity.setLastName(
                lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase());
        candidateEntity.setIdentityCode(authIdentity.getIdentityCode());
        candidateEntity.setRegion(candidateDTO.getRegion());
        candidateEntity.setParty(candidateDTO.getParty());
        candidateEntity.setHasWithdrawn(0L);
        candidateEntity.setCandidacyAnnounced(LocalDateTime.now());
        return candidateEntity;
    }
}
