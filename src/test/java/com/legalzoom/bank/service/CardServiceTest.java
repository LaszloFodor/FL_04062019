package com.legalzoom.bank.service;

import com.legalzoom.bank.exception.CardNotFoundException;
import com.legalzoom.bank.exception.MissingCardInformationsException;
import com.legalzoom.bank.model.Card;
import com.legalzoom.bank.repository.CardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CardServiceTest {

    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;

    List<Card> cards;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        cards= parse("test.csv");
        for (Card card : cards) {
            System.out.println(card.getNameOfBank() +"\n"+ card.getCard() +"\n"+ card.getExpiryDate());
        }
    }

    @Test
    public void testSave() throws CardNotFoundException, MissingCardInformationsException {
        when(cardRepository.save(any())).thenReturn(cards.get(0));
        assertEquals(cards.get(0).getNameOfBank(), cardService.save(cards.get(0)).getNameOfBank());
        assertEquals(cards.get(0).getExpiryDate(), cardService.save(cards.get(0)).getExpiryDate());
        assertEquals(cards.get(0).getCard(), cardService.save(cards.get(0)).getCard());
    }

    @Test(expected = CardNotFoundException.class)
    public void testSave_withNullCard() throws CardNotFoundException, MissingCardInformationsException {
        cardService.save(null);
    }

    @Test(expected = MissingCardInformationsException.class)
    public void testSave_withEmptyCard() throws CardNotFoundException, MissingCardInformationsException {
        cardService.save(new Card());
    }

    @Test(expected = NumberFormatException.class)
    public void testSave_wrongCardData() throws CardNotFoundException, MissingCardInformationsException {
        cardService.save(new Card("aaaa-aaaa-aaaa-aaaa", "a", new Date()));
    }

    @Test(expected = MissingCardInformationsException.class)
    public void testSave_emptyExpiry() throws CardNotFoundException, MissingCardInformationsException {
        cardService.save(new Card("1111-1111-1111-1111", "a", null));
    }

    @Test(expected = MissingCardInformationsException.class)
    public void testSave_emptyCardName() throws CardNotFoundException, MissingCardInformationsException {
        cardService.save(new Card("1111-1111-1111-1111", "", new Date()));
    }

    @Test(expected = MissingCardInformationsException.class)
    public void testSave_wrongCardLength() throws CardNotFoundException, MissingCardInformationsException {
        cardService.save(new Card("11-1111-1111-1111", "asdf", new Date()));
    }

    @Test
    public void testSort() throws ParseException {
        when(cardRepository.findAll()).thenReturn(cards);
        assertEquals("American Express", cardService.getSortedCards().get(0).getNameOfBank());
        assertEquals("4519-4532-4524-2456", cardService.getSortedCards().get(0).getCard());
        assertEquals(new SimpleDateFormat("MMM-yyyy").parse("Dec-2018"), cardService.getSortedCards().get(0).getExpiryDate());

        assertEquals("HSBC CANADA", cardService.getSortedCards().get(1).getNameOfBank());
        assertEquals("5601-2345-3446-5678", cardService.getSortedCards().get(1).getCard());
        assertEquals(new SimpleDateFormat("MMM-yyyy").parse("Nov-2017"), cardService.getSortedCards().get(1).getExpiryDate());

        assertEquals("Royal Bank of Canada", cardService.getSortedCards().get(2).getNameOfBank());
        assertEquals("4519-4532-4524-2456", cardService.getSortedCards().get(2).getCard());
        assertEquals(new SimpleDateFormat("MMM-yyyy").parse("Oct-2017"), cardService.getSortedCards().get(2).getExpiryDate());
    }

    private List<Card> parse(String csv) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(csv)));
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
