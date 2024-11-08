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

/**
 * A concrete implementation of {@link MapFileHandler} for handling loan-related file operations.
 * This class provides functionality to read and write a map of loan IDs to {@link Loan} objects
 * from and to a CSV file. It uses mappers for converting between {@link Loan} objects and their
 * CSV representations.
 */
public class LoanMapFileHandler extends MapFileHandler<UUID, Loan> {

    private final LoanToCSVMapper loanToCSVMapper;
    private final CSVToLoanMapper csvToLoanMapper;

    /**
     * Constructs a new {@link LoanMapFileHandler} with the specified file name.
     *
     * @param fileName the name of the file where loan data will be read from or written to
     */
    public LoanMapFileHandler(String fileName) {
        super(fileName);
        this.loanToCSVMapper = new LoanToCSVMapper();
        this.csvToLoanMapper = new CSVToLoanMapper();
    }

    /**
     * Reads a map of loan IDs to {@link Loan} objects from the CSV file specified by {@code fileName}.
     * Each line of the file is mapped to a {@link Loan} object.
     *
     * @return a map of loan IDs to {@link Loan} objects read from the file
     * @throws IOException if an I/O error occurs while reading the file
     */
    @Override
    public Map<UUID, Loan> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToLoanMapper)
                    .collect(Collectors.toMap(Loan::getId, Function.identity()));
        }
        catch (EOFException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * Writes a map of loan IDs to {@link Loan} objects to the CSV file specified by {@code fileName}.
     * Each loan object in the map is converted to its string representation using the {@link LoanToCSVMapper}.
     *
     * @param map the map of loan IDs to {@link Loan} objects to write to the file
     * @throws IOException if an I/O error occurs while writing the file
     */
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
