package org.ardeu.librarymanagementsystem.domain.filerepository.handlers;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.mappers.CSVToLoanMapper;
import org.ardeu.librarymanagementsystem.domain.entities.loan.mappers.LoanToCSVMapper;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LoanMapFileHandler extends MapFileHandler<UUID, Loan> {

    private final LoanToCSVMapper loanToCSVMapper;
    private final CSVToLoanMapper csvToLoanMapper;

    public LoanMapFileHandler(String fileName) {
        super(fileName);
        this.loanToCSVMapper = new LoanToCSVMapper();
        this.csvToLoanMapper = new CSVToLoanMapper();
    }

    @Override
    public Map<UUID, Loan> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToLoanMapper)
                    .collect(Collectors.toMap(Loan::getId, Function.identity()));
        }
        catch (EOFException e){
            return Collections.emptyMap();
        }
    }

    @Override
    public void writeToFile(Map<UUID, Loan> map) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            for (String line : map.values().stream().map(this.loanToCSVMapper).toList()) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
