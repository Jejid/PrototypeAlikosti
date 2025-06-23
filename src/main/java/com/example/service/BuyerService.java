package com.example.service;

import com.example.dao.BuyerDao;
import com.example.dto.BuyerDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.model.Buyer;
import com.example.repository.BuyerRepository;
import com.example.utility.BuyerMapper;
import com.example.utility.DeletionValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;
    private final DeletionValidator validator;

    public BuyerService(BuyerRepository buyerRepository, BuyerMapper buyerMapper, DeletionValidator validator) {
        this.buyerRepository = buyerRepository;
        this.buyerMapper = buyerMapper;
        this.validator = validator;
    }

    public List<Buyer> getAllBuyers() {
        List<BuyerDao> buyerListDao = buyerRepository.findAll();
        return buyerListDao.stream().map(buyerMapper::toModel).collect(Collectors.toList());
    }

    public Buyer getBuyerById(Integer id) {
        Optional<BuyerDao> optionalBuyer = buyerRepository.findById(id);
        BuyerDao buyerDao = optionalBuyer.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));
        return buyerMapper.toModel(buyerDao);
    }

    public Buyer createBuyer(BuyerDto buyerDto) {
        return buyerMapper.toModel(buyerRepository.save(buyerMapper.toDao(buyerMapper.toModel(buyerDto))));
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
        return buyerMapper.toModel(buyerRepository.save(buyerMapper.toDao(buyerMapper.toModel(updatedBuyerDto))));
    }

    public Buyer partialUpdateBuyer(Integer id, Map<String, Object> updates) {

        Optional<BuyerDao> optionalBuyer = buyerRepository.findById(id);
        BuyerDao buyerDaoOrigin = optionalBuyer.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));

        return buyerMapper.toModel(buyerRepository.save(buyerMapper.parcialUpdateToDao(buyerDaoOrigin, updates)));
    }

     /* public Buyer partialUpdateBuyer2(Integer id, BuyerDto updatesDto) {

        Optional<BuyerDao> optionalBuyer = buyerRepository.findById(id);
        BuyerDao buyerDaoOrigin = optionalBuyer.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));

        return buyerMapper.toModel(buyerRepository.save(buyerMapper.parcialUpdateToDao2(updatesDto, buyerDaoOrigin)));}
     */


}



