package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing {@link Member} entities, including creation, retrieval,
 * and adding/removing loans for a member.
 */
public class MemberService extends Service<Member> {

    /**
     * Constructs a new {@link MemberService} with the specified file handler.
     *
     * @param fileHandler the file handler to be used for saving and loading {@link Member} data
     */
    public MemberService(MapFileHandler<UUID, Member> fileHandler) {
        super(fileHandler);
    }

    /**
     * Checks if a member with the given email already exists.
     *
     * @param email the email address to check for existence
     * @throws MemberAlreadyExistsException if a member with the provided email already exists
     */
    public void checkMemberExistsByEmail(String email) throws MemberAlreadyExistsException {
        for (Member member : this.getItems().values()) {
            if (member.getEmail().equals(email)) {
                throw new MemberAlreadyExistsException("Member with email " + email + " already exists");
            }
        }
    }

    /**
     * Creates a new {@link Member} instance based on the provided {@link MemberCreationDTO}.
     *
     * @param memberDTO the data transfer object containing member information
     * @return the newly created {@link Member}
     */
    public Member create(MemberCreationDTO memberDTO) {
        return new Member(UUID.randomUUID(),
                memberDTO.name(),
                memberDTO.email(),
                new HashSet<>());
    }

    /**
     * Retrieves a {@link Member} by its ID.
     *
     * @param memberId the ID of the member to retrieve
     * @return the {@link Member} object associated with the provided ID
     * @throws MemberNotFoundException if no member is found for the specified ID
     */
    public Member getById(UUID memberId) throws MemberNotFoundException {
        Member member = this.getItems().get(memberId);
        if (Objects.isNull(member)) {
            throw new MemberNotFoundException("Member with id " + memberId + " not found");
        }
        return member;
    }

    /**
     * Adds a loan to a member's loan list.
     *
     * @param memberId the ID of the member to add the loan to
     * @param id the ID of the loan to add
     * @throws MemberNotFoundException if the member with the provided ID is not found
     */
    public void addLoanToMember(UUID memberId, UUID id) throws MemberNotFoundException {
        Member member = super.getItems().get(memberId);
        if (Objects.isNull(member)) {
            throw new MemberNotFoundException("Member with id " + memberId + " not found");
        }
        member.addLoan(id);
        super.items.put(member.getId(), member);
    }

    /**
     * Removes a loan from a member's loan list.
     *
     * @param memberId the ID of the member to remove the loan from
     * @param id the ID of the loan to remove
     */
    public void removeLoanFromMember(UUID memberId, UUID id) {
        Member member = super.getItems().get(memberId);
        if (Objects.nonNull(member)) {
            member.removeLoan(id);
            super.items.put(member.getId(), member);
        }
    }
}

