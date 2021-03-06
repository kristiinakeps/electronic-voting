package com.web_application_development.evoting.services;

import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.entities.VoteResult;
import com.web_application_development.evoting.entities.VoteResultForParty;
import com.web_application_development.evoting.repositories.VoteRepository;
import com.web_application_development.evoting.repositories.VoteResultsForPartyRepository;
import com.web_application_development.evoting.repositories.VoteResultsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VoteService {

    private static final Logger logger = LogManager.getLogger(CandidateService.class);

    private final VoteRepository voteRepository;
    private final VoteResultsRepository voteResultsRepository;
    private final VoteResultsForPartyRepository voteResultsForPartyRepository;

    @Autowired
    VoteService(VoteRepository voteRepository, VoteResultsRepository voteResultsRepository, VoteResultsForPartyRepository voteResultsForPartyRepository) {
        this.voteRepository = voteRepository;
        this.voteResultsRepository = voteResultsRepository;
        this.voteResultsForPartyRepository = voteResultsForPartyRepository;
    }

    public List<VoteResult> findAllVotes() {
        logger.debug("Request: voteResultsRepository.findAllVotes()");
        return voteResultsRepository.findAllVotes();
    }

    public List<VoteResultForParty> findAllVotesForEachParty() {
        logger.debug("Request: voteResultsRepository.findAllVotesForEachParty()");
        return voteResultsForPartyRepository.findAllVotesForEachParty();
    }

    public void saveVote(VoteDTO voteDTO, String voterId) {
        Vote entity = mapDTOToEntity(voteDTO, voterId);
        logger.debug("Request: voteRepository.save(entity): voter " + entity.getVoterIdentityCode() + " candidateID " + entity.getCandidateId());
        voteRepository.save(entity);
    }

    private Vote mapDTOToEntity(VoteDTO voteDTO, String voterId) {
        Vote voteEntity = new Vote();
        voteEntity.setVoterIdentityCode(voterId);
        voteEntity.setCandidateId(voteDTO.getCandidateId().longValue());
        voteEntity.setIsWithdrawn(0L);
        voteEntity.setVotingTime(LocalDateTime.now());
        return voteEntity;
    }

    public boolean hasVoted(String identityCode) {
        return voteRepository.hasVoted(identityCode);
    }

    public void takeBackVote(String identityCode) {
        voteRepository.takeBackCandidacy(identityCode);
    }
}
