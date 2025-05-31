package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.CreditCard;
import com.example.repository.CreditCardRepository;
import com.example.dao.CreditCardDao;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public List<CreditCard> getAllCreditCards() {
        List<CreditCardDao> creditCardDaos = creditCardRepository.findAll();
        List<CreditCard> creditCards = new ArrayList<>();

        for (CreditCardDao creditCardDao : creditCardDaos) {
            creditCards.add(new CreditCard(
                    creditCardDao.getId(),
                    creditCardDao.getBuyerId(),
                    creditCardDao.getName(),
                    creditCardDao.getCardNumber(),
                    creditCardDao.getCardDate(),
                    creditCardDao.getCvcCode(),
                    creditCardDao.getTokenizedCode(),
                    creditCardDao.getBank()
            ));
        }

        return creditCards;
    }

    public CreditCard getCreditCardById(Integer id) {

        CreditCardDao creditCardDao = creditCardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarjeta de crédito con ID " + id + " no encontrada"));

        return new CreditCard(
                creditCardDao.getId(),
                creditCardDao.getBuyerId(),
                creditCardDao.getName(),
                creditCardDao.getCardNumber(),
                creditCardDao.getCardDate(),
                creditCardDao.getCvcCode(),
                creditCardDao.getTokenizedCode(),
                creditCardDao.getBank()
        );
    }

    public CreditCard createCreditCard(CreditCard creditCard) {

        CreditCardDao creditCardDao = new CreditCardDao();

        creditCardDao.setBuyerId(creditCard.getBuyerId());
        creditCardDao.setName(creditCard.getName());
        creditCardDao.setCardNumber(creditCard.getCardNumber());
        creditCardDao.setCardDate(creditCard.getCardDate());
        creditCardDao.setCvcCode(creditCard.getCvcCode());
        creditCardDao.setTokenizedCode(creditCard.getTokenizedCode());
        creditCardDao.setBank(creditCard.getBank());

        CreditCardDao creditCardCreated = creditCardRepository.save(creditCardDao);

        return new CreditCard(
                creditCardCreated.getId(),
                creditCardCreated.getBuyerId(),
                creditCardCreated.getName(),
                creditCardDao.getCardNumber(),
                creditCardCreated.getCardDate(),
                creditCardCreated.getCvcCode(),
                creditCardCreated.getTokenizedCode(),
                creditCardCreated.getBank()
        );
    }

    public CreditCard updateCreditCard(Integer id, CreditCard creditCard) {
        CreditCardDao creditCardDao = creditCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarjeta de crédito con ID " + id + " no encontrada"));

        creditCardDao.setBuyerId(creditCard.getBuyerId());
        creditCardDao.setName(creditCard.getName());
        creditCardDao.setCardNumber(creditCard.getCardNumber());
        creditCardDao.setCardDate(creditCard.getCardDate());
        creditCardDao.setCvcCode(creditCard.getCvcCode());
        creditCardDao.setTokenizedCode(creditCard.getTokenizedCode());
        creditCardDao.setBank(creditCard.getBank());

        creditCardRepository.save(creditCardDao);

        return new CreditCard(
                creditCardDao.getId(),
                creditCardDao.getBuyerId(),
                creditCardDao.getName(),
                creditCardDao.getCardNumber(),
                creditCardDao.getCardDate(),
                creditCardDao.getCvcCode(),
                creditCardDao.getTokenizedCode(),
                creditCardDao.getBank()
        );
    }

    public void deleteCreditCard(Integer id) {
        if (creditCardRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Tarjeta de crédito con ID " + id + " no encontrada");
        }
        creditCardRepository.deleteById(id);
    }

    public CreditCard partialUpdateCreditCard(Integer id, Map<String, Object> updates) {
        CreditCardDao creditCardDao = creditCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarjeta de crédito con ID " + id + " no encontrada"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "buyerId" -> creditCardDao.setBuyerId((Integer) value);
                case "name" -> creditCardDao.setName((String) value);
                case "cardNumber" -> creditCardDao.setCardNumber((String) value);
                case "cardDate" -> creditCardDao.setCardDate((String) value);
                case "cvcCode" -> creditCardDao.setCvcCode((Integer) value);
                case "tokenizedCode" -> creditCardDao.setTokenizedCode((String) value);
                case "bank" -> creditCardDao.setBank((String) value);
                default -> throw new IllegalArgumentException("Campo " + key + " no válido para actualización parcial.");
            }
        });

        creditCardRepository.save(creditCardDao);

        return new CreditCard(
                creditCardDao.getId(),
                creditCardDao.getBuyerId(),
                creditCardDao.getName(),
                creditCardDao.getCardNumber(),
                creditCardDao.getCardDate(),
                creditCardDao.getCvcCode(),
                creditCardDao.getTokenizedCode(),
                creditCardDao.getBank()
        );
    }
}
