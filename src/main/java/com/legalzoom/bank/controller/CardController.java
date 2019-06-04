package com.legalzoom.bank.controller;

import com.legalzoom.bank.exception.CardNotFoundException;
import com.legalzoom.bank.exception.MissingCardInformationsException;
import com.legalzoom.bank.model.Card;
import com.legalzoom.bank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/cards/add")
    public Card addCard(@RequestBody Card card) throws CardNotFoundException, MissingCardInformationsException {
        return cardService.save(card);
    }

    @PostMapping(value = "/cards/csv")
    @ResponseBody
    public List<Card> addCard(@RequestParam("file") MultipartFile file) throws IOException, CardNotFoundException, MissingCardInformationsException {
        return cardService.saveFromCSV(file);
    }

    @GetMapping("/cards/sort")
    public List<Card> getCards() {
        return cardService.getSortedCards();
    }
}
