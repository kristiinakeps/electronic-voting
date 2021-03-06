package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {

    private Integer candidateId;

    public VoteDTO() {
    }

    public VoteDTO(Integer candidateId) {
        this.candidateId = candidateId;
    }
}
