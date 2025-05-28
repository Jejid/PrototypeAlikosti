package com.example.service;

import com.example.dao.BuyerDao;
import com.example.dao.ProductDao;
import com.example.exception.EntityNotFoundException;
import com.example.model.Buyer;
import com.example.model.Product;
import com.example.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
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

    public Buyer createBuyer(Buyer buyer) {
        BuyerDao buyerToUpload = new BuyerDao();
        buyerToUpload.setName(buyer.getName());
        buyerToUpload.setSurname(buyer.getSurname());
        buyerToUpload.setBirthDate(buyer.getBirthDate());
        buyerToUpload.setCc(buyer.getCc());
        buyerToUpload.setEmail(buyer.getEmail());
        buyerToUpload.setPassAccount(buyer.getPassAccount());

        BuyerDao createdBuyer = buyerRepository.save(buyerToUpload);

        return new Buyer(createdBuyer.getId(),
                createdBuyer.getName(),
                createdBuyer.getSurname(),
                createdBuyer.getBirthDate(),
                createdBuyer.getCc(),
                createdBuyer.getEmail(),
                createdBuyer.getPassAccount());
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

    public Buyer partialUpdateBuyer(Integer id, Map<String, Object> updates){

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

}
