package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.Buyer;
import com.example.repository.BuyerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    public Buyer getBuyerById(Integer id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Buyer con ID " + id + " no encontrado"));
    }

    public Buyer createBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }

    public Buyer updateBuyer(Integer id, Buyer updatedBuyer) {
        Buyer buyer = getBuyerById(id);
        buyer.setName(updatedBuyer.getName());
        buyer.setSurname(updatedBuyer.getSurname());
        buyer.setBirthDate(updatedBuyer.getBirthDate());
        buyer.setCc(updatedBuyer.getCc());
        buyer.setEmail(updatedBuyer.getEmail());
        buyer.setPassAccount(updatedBuyer.getPassAccount());
        return buyerRepository.save(buyer);
    }

    public void deleteBuyer(Integer id) {
        Buyer buyer = getBuyerById(id);
        buyerRepository.delete(buyer);
    }
}
