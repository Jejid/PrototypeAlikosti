package com.example.service;

import com.example.dao.BuyerDao;
import com.example.dto.BuyerDto;
import com.example.exception.EntityNotFoundException;
import com.example.model.Buyer;
import com.example.repository.BuyerRepository;
import com.example.utility.MapperObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;
    private final MapperObject mapperObject;

    public BuyerService(BuyerRepository buyerRepository, MapperObject mapperObject) {
        this.buyerRepository = buyerRepository;
        this.mapperObject = mapperObject;
    }

    public List<Buyer> getAllBuyers() {
        List<BuyerDao> buyerListDao = buyerRepository.findAll();
        List<Buyer> buyerList = new ArrayList<>();

        for (BuyerDao buyerDao : buyerListDao) {
            Buyer buyer = new Buyer();
            buyer.setId(buyerDao.getId());
            buyer.setName(buyerDao.getName());
            buyer.setSurname(buyerDao.getSurname());
            buyer.setBirthDate(buyerDao.getBirthDate());
            buyer.setCc(buyerDao.getCc());
            buyer.setEmail(buyerDao.getEmail());
            buyer.setPassAccount(buyerDao.getPassAccount());
            buyerList.add(buyer);
        }

        return buyerList;
    }

    public Buyer getBuyerById(Integer id) {
        Optional<BuyerDao> buyerDao = buyerRepository.findById(id);

        BuyerDao buyerDao1 = buyerDao.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));

        Buyer buyer = new Buyer();
        buyer.setId(buyerDao1.getId());
        buyer.setName(buyerDao1.getName());
        buyer.setSurname(buyerDao1.getSurname());
        buyer.setBirthDate(buyerDao1.getBirthDate());
        buyer.setCc(buyerDao1.getCc());
        buyer.setEmail(buyerDao1.getEmail());
        buyer.setPassAccount(buyerDao1.getPassAccount());

        return buyer;
    }

    public Buyer createBuyer(BuyerDto buyerDto) {
        return mapperObject.toModel(buyerRepository.save(mapperObject.toDao(mapperObject.toModel(buyerDto))));
    }

    public Buyer updateBuyer(Integer id, Buyer updatedBuyer) {
        Optional<BuyerDao> optionalBuyerDao = buyerRepository.findById(id);

        if (optionalBuyerDao.isEmpty()) {
            throw new EntityNotFoundException("Comprador con ID: " + id + " no encontrado");
        }

        BuyerDao buyerDao = optionalBuyerDao.get();

        buyerDao.setName(updatedBuyer.getName());
        buyerDao.setSurname(updatedBuyer.getSurname());
        buyerDao.setBirthDate(updatedBuyer.getBirthDate());
        buyerDao.setCc(updatedBuyer.getCc());
        buyerDao.setEmail(updatedBuyer.getEmail());
        buyerDao.setPassAccount(updatedBuyer.getPassAccount());

        buyerRepository.save(buyerDao);

        return new Buyer(buyerDao.getId(),
                buyerDao.getName(),
                buyerDao.getSurname(),
                buyerDao.getBirthDate(),
                buyerDao.getCc(),
                buyerDao.getEmail(),
                buyerDao.getPassAccount());
    }

    public void deleteBuyer(Integer id) {
        if (buyerRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Comprador con ID " + id + ", no encontrado");
        }
        buyerRepository.deleteById(id);
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
