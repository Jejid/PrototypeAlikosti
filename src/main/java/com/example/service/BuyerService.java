package com.example.service;

import com.example.dao.BuyerDao;
import com.example.dto.BuyerDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.model.Buyer;
import com.example.repository.BuyerRepository;
import com.example.utility.DeletionValidator;
import com.example.utility.MapperObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;
    private final MapperObject mapperObject;
    private final DeletionValidator validator;

    public BuyerService(BuyerRepository buyerRepository, MapperObject mapperObject, DeletionValidator validator) {
        this.buyerRepository = buyerRepository;
        this.mapperObject = mapperObject;
        this.validator = validator;
    }

    public List<Buyer> getAllBuyers() {
        List<BuyerDao> buyerListDao = buyerRepository.findAll();
        return buyerListDao.stream().map(mapperObject::toModel).collect(Collectors.toList());
    }

    public Buyer getBuyerById(Integer id) {
        Optional<BuyerDao> optionalBuyer = buyerRepository.findById(id);
        BuyerDao buyerDao = optionalBuyer.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));
        return mapperObject.toModel(buyerDao);
    }

    public Buyer createBuyer(BuyerDto buyerDto) {
        return mapperObject.toModel(buyerRepository.save(mapperObject.toDao(mapperObject.toModel(buyerDto))));
    }

    public void deleteBuyer(Integer id) {
        if (!buyerRepository.existsById(id))
            throw new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado");
        validator.deletionValidatorBuyer(id);
        buyerRepository.deleteById(id);
    }

    public Buyer updateBuyer(Integer id, BuyerDto updatedBuyerDto) {
        buyerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));
        if (!Objects.equals(updatedBuyerDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return mapperObject.toModel(buyerRepository.save(mapperObject.toDao(mapperObject.toModel(updatedBuyerDto))));
    }

    public Buyer partialUpdateBuyer(Integer id, Map<String, Object> updates) {

        Optional<BuyerDao> optionalBuyerDao = buyerRepository.findById(id);
        if (optionalBuyerDao.isEmpty()) {
            throw new EntityNotFoundException("Comprador con ID: " + id + " no encontrado");
        }

        final BuyerDao buyerDao = optionalBuyerDao.get();

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name":
                        if (value == null || value instanceof String) buyerDao.setName((String) value);
                        break;
                    case "surname":
                        if (value == null || value instanceof String) buyerDao.setSurname((String) value);
                        break;
                    case "birth_date":
                        if (value == null || value instanceof String) buyerDao.setBirthDate(((String) value));
                        break;
                    case "cc":
                        if (value == null || value instanceof String) buyerDao.setCc(((String) value));
                        break;
                    case "email":
                        if (value == null || value instanceof String) buyerDao.setEmail((String) value);
                        break;
                    case "pass_account":
                        if (value == null || value instanceof String) buyerDao.setPassAccount(((String) value));
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        buyerRepository.save(buyerDao);

        return new Buyer(
                buyerDao.getId(),
                buyerDao.getName(),
                buyerDao.getSurname(),
                buyerDao.getBirthDate(),
                buyerDao.getCc(),
                buyerDao.getEmail(),
                buyerDao.getPassAccount());
    }

    public Buyer partialUpdateBuyer2(Integer id, Buyer updates) {

        Optional<BuyerDao> optionalBuyerDao = buyerRepository.findById(id);
        if (optionalBuyerDao.isEmpty()) {
            throw new EntityNotFoundException("Comprador con ID: " + id + " no encontrado");
        }

        final BuyerDao buyerDao = optionalBuyerDao.get();

        if (updates.getName() != null) buyerDao.setName(updates.getName());
        if (updates.getSurname() != null) buyerDao.setSurname(updates.getSurname());
        if (updates.getBirthDate() != null) buyerDao.setBirthDate(updates.getBirthDate());
        if (updates.getCc() != null) buyerDao.setCc(updates.getCc());
        if (updates.getEmail() != null) buyerDao.setEmail(updates.getEmail());
        if (updates.getPassAccount() != null) buyerDao.setPassAccount(updates.getPassAccount());

        buyerRepository.save(buyerDao);

        return new Buyer(
                buyerDao.getId(),
                buyerDao.getName(),
                buyerDao.getSurname(),
                buyerDao.getBirthDate(),
                buyerDao.getCc(),
                buyerDao.getEmail(),
                buyerDao.getPassAccount());
    }

}
