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

        Optional<BuyerDao> optionalBuyer = buyerRepository.findById(id);
        BuyerDao buyerDaoOrigin = optionalBuyer.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));

        return mapperObject.toModel(buyerRepository.save(mapperObject.parcialUpdateToDao(buyerDaoOrigin, updates)));
    }

     /* public Buyer partialUpdateBuyer2(Integer id, BuyerDto updatesDto) {

        Optional<BuyerDao> optionalBuyer = buyerRepository.findById(id);
        BuyerDao buyerDaoOrigin = optionalBuyer.orElseThrow(() -> new EntityNotFoundException("Comprador con ID: " + id + ", no encontrado"));

        return mapperObject.toModel(buyerRepository.save(mapperObject.parcialUpdateToDao2(updatesDto, buyerDaoOrigin)));}
     */


}



