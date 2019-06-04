package com.legalzoom.bank.service;

import com.legalzoom.bank.exception.CardNotFoundException;
import com.legalzoom.bank.exception.MissingCardInformationsException;
import com.legalzoom.bank.model.Card;
import com.legalzoom.bank.parser.CSVParser;
import com.legalzoom.bank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card save(Card card) throws CardNotFoundException, MissingCardInformationsException {
        return cardRepository.save(replaceCardNumbers(card));
    }

    public List<Card> getSortedCards() {
        return StreamSupport.stream(cardRepository.findAll().spliterator(), false)
                .sorted(Collections.reverseOrder()).collect(Collectors.toList());
    }

    public List<Card> saveFromCSV(MultipartFile file) throws IOException, CardNotFoundException, MissingCardInformationsException {
        List<Card> cards = CSVParser.parseCSV(file);
        List<Card> mutatedCards = new ArrayList<>();
        for (Card card : cards)
            mutatedCards.add(cardRepository.save(replaceCardNumbers(card)));
        return mutatedCards;
    }

    private Card replaceCardNumbers(Card card) throws CardNotFoundException, MissingCardInformationsException {
        isValidCard(card);
        Card newCard = new Card();
        String[] cardNumber = card.getCard().split("-");
        try{
            Integer.parseInt(cardNumber[0]);
        } catch (NumberFormatException e) {
            System.out.println("Cannot parse this type" + cardNumber[0]);
            throw new NumberFormatException();
        }
        StringBuilder sb = new StringBuilder(cardNumber[0]).append("-xxxx-xxxx-xxxx");
        newCard.setCard(sb.toString());
        newCard.setExpiryDate(card.getExpiryDate());
        newCard.setNameOfBank(card.getNameOfBank());
        return newCard;
    }

    private void isValidCard(Card card) throws CardNotFoundException, MissingCardInformationsException {
        if (card == null)
            throw new CardNotFoundException();

        if (card.getExpiryDate() == null || card.getCard().isEmpty() || card.getNameOfBank().isEmpty() || card.getCard().length() != 19)
            throw new MissingCardInformationsException();

    }


}
