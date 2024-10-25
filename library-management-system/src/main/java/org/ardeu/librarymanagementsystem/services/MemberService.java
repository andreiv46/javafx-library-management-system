package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.member.Member;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;
import org.ardeu.librarymanagementsystem.services.base.Service;

public class MemberService extends Service<Member> {

    public MemberService(FileHandler<Member> fileHandler) {
        super(fileHandler);
    }
}
