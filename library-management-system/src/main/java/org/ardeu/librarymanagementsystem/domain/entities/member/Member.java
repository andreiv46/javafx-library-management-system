package org.ardeu.librarymanagementsystem.domain.entities.member;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;

import java.util.HashSet;
import java.util.UUID;

/**
 * Represents a member in the library system.
 * <p>
 * A member has a unique identifier, a name, an email, and a set of loan records
 * that track the books they have borrowed.
 */
public class Member extends BaseEntity {
    /**
     * The name of the member.
     */
    private String name;

    /**
     * The email address of the member.
     */
    private String email;

    /**
     * The set of loan records associated with the member.
     * This field stores a collection of loan UUIDs that represent the books
     * borrowed by the member.
     */
    private HashSet<UUID> loans;

    /**
     * Constructs a Member instance.
     *
     * @param id the unique identifier for the member
     * @param name the name of the member
     * @param email the email address of the member
     * @param loans a set of UUIDs representing the loans the member has made
     */
    public Member(UUID id, String name, String email, HashSet<UUID> loans) {
        super(id);
        this.name = name;
        this.email = email;
        this.loans = loans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashSet<UUID> getLoans() {
        return loans;
    }

    public void setLoans(HashSet<UUID> loans) {
        this.loans = loans;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", loans=" + loans +
                '}';
    }

    public void addLoan(UUID id) {
        this.loans.add(id);
    }

    public void removeLoan(UUID id) {
        this.loans.remove(id);
    }
}
