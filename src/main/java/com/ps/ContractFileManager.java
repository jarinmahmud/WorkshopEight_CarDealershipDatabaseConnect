package com.ps;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ContractFileManager {
    private static final String FILE_NAME = "contracts.txt";

    public static void saveContract(Contract contract) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(contract.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

