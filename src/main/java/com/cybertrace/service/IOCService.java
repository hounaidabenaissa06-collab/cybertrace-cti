package com.cybertrace.service;

import com.cybertrace.model.ioc.*;
import com.cybertrace.exception.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class IOCService {
    private final List<IOC> repo = new ArrayList<>();

    public IOC addIOC(IOC ioc)
            throws DuplicateIOCException, InvalidIOCFormatException {
        for (IOC existing : repo) {
            if (existing.getValue().equals(ioc.getValue()))
                throw new DuplicateIOCException(ioc.getValue());
        }
        if (!ioc.validate())
            throw new InvalidIOCFormatException(
                ioc.getValue(), ioc.getType().toString());
        repo.add(ioc);
        return ioc;
    }

    public IOC createFromInput(String type, String value, int conf)
            throws DuplicateIOCException, InvalidIOCFormatException {
        String id = "IOC-" + System.currentTimeMillis();
        IOC ioc = switch (type.toUpperCase()) {
            case "IP"     -> new IPIndicator(id, value, conf);
            case "DOMAIN" -> new DomainIndicator(id, value, conf);
            case "HASH"   -> new HashIndicator(id, value, conf);
            case "EMAIL"  -> new EmailIndicator(id, value, conf);
            default -> throw new IllegalArgumentException("Type inconnu: "+type);
        };
        return addIOC(ioc);
    }

    public int bulkImportFromCSV(String path) {
        int count = 0;
        try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 3) {
                    try {
                        createFromInput(p[0].trim(), p[1].trim(),
                            Integer.parseInt(p[2].trim()));
                        count++;
                    } catch (Exception e) { /* ligne ignoree */ }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur CSV: " + e.getMessage());
        }
        return count;
    }

    public List<IOC> getAll() { return repo; }
}
