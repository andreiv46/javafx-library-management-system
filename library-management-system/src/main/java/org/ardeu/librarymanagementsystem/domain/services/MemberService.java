package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemberService extends Service<Member> {

    public MemberService(MapFileHandler<UUID, Member> fileHandler) {
        super(fileHandler);
    }

    public void checkMemberExistsByEmail(String email) throws MemberAlreadyExistsException {
        for (Member member : this.getItems().values()) {
            if (member.getEmail().equals(email)) {
                throw new MemberAlreadyExistsException("Member with email " + email + " already exists");
            }
        }
    }

    public Member create(MemberDTO memberDTO) {
        return new Member(UUID.randomUUID(),
                memberDTO.name(),
                memberDTO.email(),
                new HashSet<>());
    }

    public Member getById(UUID memberId) throws MemberNotFoundException {
        Member member = this.getItems().get(memberId);
        if (Objects.isNull(member)) {
            throw new MemberNotFoundException("Member with id " + memberId + " not found");
        }
        return member;
    }

    public void checkMemberExistsById(UUID uuid) throws MemberNotFoundException {
        if (!this.getItems().containsKey(uuid)) {
            throw new MemberNotFoundException("Member with id " + uuid + " not found");
        }
    }

    public void addLoanToMember(UUID memberId, UUID id) throws MemberNotFoundException {
        Member member = super.getItems().get(memberId);
        if (Objects.isNull(member)) {
            throw new MemberNotFoundException("Member with id " + memberId + " not found");
        }
        member.addLoan(id);
    }

    public void removeLoanFromMember(UUID memberId, UUID id) {
        Member member = super.getItems().get(memberId);
        if(Objects.nonNull(member)) {
            member.removeLoan(id);
        }
    }
}
