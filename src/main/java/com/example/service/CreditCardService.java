package com.example.service;

import com.example.dao.CreditCardDao;
import com.example.dto.CreditCardDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.model.CreditCard;
import com.example.utility.CreditCardMapper;
import com.example.utility.DeletionValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreditCardService {
    private final com.example.repository.CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final DeletionValidator validator;

    public CreditCardService(com.example.repository.CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper, DeletionValidator validator) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
        this.validator = validator;
    }

    public List<CreditCard> getAllCreditCards() {
        List<CreditCardDao> creditCardListDao = creditCardRepository.findAll();
        return creditCardListDao.stream().map(creditCardMapper::toModel).collect(Collectors.toList());
    }

    public CreditCard getCreditCardById(Integer id) {
        Optional<CreditCardDao> optionalCreditCard = creditCardRepository.findById(id);
        CreditCardDao creditCardDao = optionalCreditCard.orElseThrow(() -> new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada"));
        return creditCardMapper.toModel(creditCardDao);
    }

    public CreditCard createCreditCard(CreditCardDto creditCardDto) {
        return creditCardMapper.toModel(creditCardRepository.save(creditCardMapper.toDao(creditCardMapper.toModel(creditCardDto))));
    }

    public void deleteCreditCard(Integer id) {
        if (!creditCardRepository.existsById(id))
            throw new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada");
        //validator.deletionValidatorCreditCard(id);
        creditCardRepository.deleteById(id);
    }

    public CreditCard updateCreditCard(Integer id, CreditCardDto updatedCreditCardDto) {
        creditCardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada"));
        if (!Objects.equals(updatedCreditCardDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return creditCardMapper.toModel(creditCardRepository.save(creditCardMapper.toDao(creditCardMapper.toModel(updatedCreditCardDto))));
    }

    public CreditCard partialUpdateCreditCard(Integer id, Map<String, Object> updates) {
        Optional<CreditCardDao> optionalCreditCard = creditCardRepository.findById(id);
        CreditCardDao creditCardDaoOrigin = optionalCreditCard.orElseThrow(() -> new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada"));
        return creditCardMapper.toModel(creditCardRepository.save(creditCardMapper.parcialUpdateToDao(creditCardDaoOrigin, updates)));
    }
}
