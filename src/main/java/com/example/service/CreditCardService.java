package com.example.service;

import com.example.dao.CreditCardDao;
import com.example.dto.BuyerDto;
import com.example.dto.CreditCardDto;
import com.example.dto.payu.PayuTokenRequest;
import com.example.exception.EntityNotFoundException;
import com.example.exception.PayuTransactionException;
import com.example.mapper.BuyerMapper;
import com.example.mapper.CreditCardMapper;
import com.example.model.CreditCard;
import com.example.utility.DateValidator;
import com.example.utility.PayuRequestBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreditCardService {
    private final com.example.repository.CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final BuyerService buyerService;
    private final BuyerMapper buyerMapper;
    private final PayuService payuService;

    public CreditCardService(com.example.repository.CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper, BuyerService buyerService, BuyerMapper buyerMapper, PayuService payuService) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
        this.buyerService = buyerService;
        this.buyerMapper = buyerMapper;
        this.payuService = payuService;
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

        if (!DateValidator.isValidFormat(creditCardDto.getCardDate())) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa MM/aa (ej. 07/27).");
        }

        if (DateValidator.isExpired(creditCardDto.getCardDate())) {
            throw new IllegalArgumentException("La tarjeta está vencida.");
        }

        //Generamos Token de la tarjeta
        BuyerDto buyerdto = buyerMapper.toDto(buyerService.getBuyerById(creditCardDto.getBuyerId()));

        PayuTokenRequest tokenRequest = PayuRequestBuilder.buildTokenRequest(buyerdto, creditCardDto);
        Map<String, Object> payuResponse = payuService.sendTokenRequest(tokenRequest);

        @SuppressWarnings("unchecked")
        Map<String, Object> creditCardToken = (Map<String, Object>) payuResponse.get("creditCardToken");

        Object tokenId = creditCardToken.get("creditCardTokenId");
        if (tokenId == null) {
            throw new PayuTransactionException("Token no generado correctamente: no se recibió el ID del token.");
        }

        // Guardar el token en la base de datos
        creditCardDto.setTokenizedCode((String) tokenId);


        return creditCardMapper.toModel(creditCardRepository.save(creditCardMapper.toDao(creditCardMapper.toModel(creditCardDto))));
    }

    public void deleteCreditCard(Integer id) {
        if (!creditCardRepository.existsById(id))
            throw new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada");
        creditCardRepository.deleteById(id);
    }

    public CreditCard updateCreditCard(Integer id, CreditCardDto updatedCreditCardDto) {
        creditCardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada"));

        if (!DateValidator.isValidFormat(updatedCreditCardDto.getCardDate())) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa MM/aa (ej. 07/27).");
        }

        if (DateValidator.isExpired(updatedCreditCardDto.getCardDate())) {
            throw new IllegalArgumentException("La tarjeta está vencida.");
        }

        //aseguramos que sea el mismo id del endpoint
        updatedCreditCardDto.setId(id);

        return creditCardMapper.toModel(creditCardRepository.save(creditCardMapper.toDao(creditCardMapper.toModel(updatedCreditCardDto))));
    }

    public CreditCard partialUpdateCreditCard(Integer id, Map<String, Object> updates) {
        Optional<CreditCardDao> optionalCreditCard = creditCardRepository.findById(id);
        CreditCardDao creditCardDaoOrigin = optionalCreditCard.orElseThrow(() -> new EntityNotFoundException("Tarjeta con ID: " + id + ", no encontrada"));

        return creditCardMapper.toModel(creditCardRepository.save(creditCardMapper.parcialUpdateToDao(creditCardDaoOrigin, updates)));
    }
}
