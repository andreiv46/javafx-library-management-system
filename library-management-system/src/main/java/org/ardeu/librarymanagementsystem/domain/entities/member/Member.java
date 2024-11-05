package org.ardeu.librarymanagementsystem.domain.entities.member;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;

import java.util.HashSet;
import java.util.UUID;

public class Member extends BaseEntity {
    private String name;
    private String email;
    private HashSet<UUID> loans;

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
