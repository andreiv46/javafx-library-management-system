package org.ardeu.librarymanagementsystem.fileio.handlers;

import org.ardeu.librarymanagementsystem.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.entities.loan.mappers.CSVToLoanMapper;
import org.ardeu.librarymanagementsystem.entities.loan.mappers.LoanToCSVMapper;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class LoanFileHandler extends FileHandler<Loan> {

    private final LoanToCSVMapper loanToCSVMapper;
    private final CSVToLoanMapper csvToLoanMapper;

    public LoanFileHandler(String fileName) {
        super(fileName);
        this.loanToCSVMapper = new LoanToCSVMapper();
        this.csvToLoanMapper = new CSVToLoanMapper();
    }

    @Override
    public List<Loan> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToLoanMapper)
                    .toList();
        }
        catch (EOFException e){
            return Collections.emptyList();
        }
    }

    @Override
    public void writeToFile(List<Loan> list) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            list.stream()
                    .map(this.loanToCSVMapper)
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
