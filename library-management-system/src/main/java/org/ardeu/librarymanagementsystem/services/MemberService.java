package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.member.Member;
import org.ardeu.librarymanagementsystem.fileio.baseio.FileHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemberService implements DataService{

    private Map<UUID, Member> members;
    private final FileHandler<Member> fileHandler;

    public MemberService(FileHandler<Member> fileHandler) {
        this.fileHandler = fileHandler;
        this.members = new HashMap<>();
    }

    @Override
    public void save() throws IOException {
        this.fileHandler.writeToFile(members.values().stream().toList());
    }

    @Override
    public void load() throws IOException {
        this.members = this.fileHandler
                .readFromFile()
                .stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));
    }
}
