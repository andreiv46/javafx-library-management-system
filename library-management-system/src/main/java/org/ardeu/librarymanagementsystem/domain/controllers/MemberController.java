package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberExportField;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.MemberService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.member.MemberDTOValidator;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * MemberController is responsible for handling member-related operations.
 */
public class MemberController {
    private final MemberService memberService = ServiceRegistry.getInstance().getService(MemberService.class);
    private final Validator<MemberCreationDTO> memberValidator = new MemberDTOValidator();

    /**
     * Retrieves all members from the system.
     *
     * @return a Result containing an ObservableMap of members, or a failure message if an error occurs
     */
    public Result<ObservableMap<UUID, Member>> getAllMembers() {
        return Result.success(memberService.getItems());
    }

    /**
     * Adds a new member to the system.
     *
     * @param memberDTO the data transfer object containing member creation details
     * @return a Result containing the created Member, or a failure message if validation fails or the member already exists
     */
    public Result<Member> addMember(MemberCreationDTO memberDTO) {
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

    /**
     * Retrieves a member by its ID.
     *
     * @param memberId the UUID of the member
     * @return a Result containing the Member object if found, or a failure message if not found
     */
    public Result<Member> getMemberById(UUID memberId) {
        try {
            Member member = this.memberService.getById(memberId);
            return Result.success(member);
        } catch (MemberNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Exports the member data to a CSV file with a specified set of fields.
     *
     * @param file the file to export to
     * @param memberList the list of members to export
     * @param checkModel the fields to include in the export
     * @return a Result indicating success or failure of the export operation
     */
    public Result<Void> exportMembersToCSV(File file, List<Member> memberList, List<MemberExportField> checkModel) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(checkModel.stream().map(MemberExportField::getDisplayName).toArray(String[]::new))
                .build();

        try(CSVPrinter printer =
                    new CSVPrinter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            new FileOutputStream(file))), csvFormat)) {

            for (Member member : memberList) {

                printer.printRecord(
                        checkModel.stream().map(field -> switch (field) {
                            case ID -> member.getId();
                            case NAME -> member.getName();
                            case EMAIL -> member.getEmail();
                            case TOTAL_LOANS -> member.getLoans().size();
                        }).toArray());
            }
            return Result.success();
        } catch (IOException e) {
            return Result.failure("Error exporting genres to CSV");
        }
    }
}