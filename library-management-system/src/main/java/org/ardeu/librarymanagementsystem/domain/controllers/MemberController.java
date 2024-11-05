package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.MemberService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.member.MemberDTOValidator;

import java.util.UUID;

public class MemberController {
    private final MemberService memberService = ServiceRegistry.getInstance().getService(MemberService.class);
    private final Validator<MemberDTO> memberValidator = new MemberDTOValidator();

    public Result<ObservableMap<UUID, Member>> getAllMembers() {
        ObservableMap<UUID, Member> members = memberService.getItems();
        return Result.success(members);
    }

    public Result<Member> addMember(MemberDTO memberDTO) {
        try {
            this.memberValidator.validate(memberDTO);
            this.memberService.checkMemberExistsByEmail(memberDTO.email());
            Member member = this.memberService.create(memberDTO);
            this.memberService.add(member);
            return Result.success(member);
        } catch (ValidationException e) {
            return Result.failure("Validation failed: \n" + e.getMessage());
        } catch (MemberAlreadyExistsException | DuplicateItemException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<Member> getMemberById(UUID memberId) {
        try {
            Member member = this.memberService.getById(memberId);
            return Result.success(member);
        } catch (MemberNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}
