package com.legalzoom.bank.parser;

import com.legalzoom.bank.exception.DateNotValidException;
import com.legalzoom.bank.model.Card;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParser {

    public static List<Card> parseCSV(MultipartFile csv) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(csv.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
        List<Card> lenders = br.lines().skip(1).map(line -> {
            String[] args = line.split(",");
            Date date = null;
            try {
                date = new SimpleDateFormat("MMM-yyyy").parse(args[2]);
            } catch (ParseException e) {
                System.out.println("Cannot parse this date");
            }
            return new Card(args[1], args[0], date);
        }).collect(Collectors.toList());
        br.close();
        return lenders;
    }
}
