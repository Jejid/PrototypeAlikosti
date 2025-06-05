package com.example.controller;

import com.example.dto.BuyerDto;
import com.example.model.Buyer;
import com.example.service.BuyerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buyers")
public class BuyerController {
    private static final Logger logger = LoggerFactory.getLogger(BuyerController.class);
    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping
    public ResponseEntity<List<BuyerDto>> getAllBuyers() {

        List<BuyerDto> buyerListDtos = new ArrayList<>();
        List<Buyer> buyerList = buyerService.getAllBuyers();
        for (Buyer buyer : buyerList) {
            BuyerDto buyerDto = new BuyerDto();
            //buyerDto.setId(buyer.getId());
            buyerDto.setName(buyer.getName());
            buyerDto.setSurname(buyer.getSurname());
            buyerDto.setBirthDate(buyer.getBirthDate());
            //buyerDto.setCc(buyer.getCc());
            buyerDto.setEmail(buyer.getEmail());
            buyerListDtos.add(buyerDto);
        }
        return new ResponseEntity<>(buyerListDtos, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BuyerDto> getBuyerById(@PathVariable Integer id) {

        Buyer buyer = buyerService.getBuyerById(id);

        BuyerDto buyerDto = new BuyerDto();
        //buyerDto.setId(buyer.getId());
        buyerDto.setName(buyer.getName());
        buyerDto.setSurname(buyer.getSurname());
        buyerDto.setBirthDate(buyer.getBirthDate());
        //buyerDto.setCc(buyer.getCc());
        buyerDto.setEmail(buyer.getEmail());

        return new ResponseEntity<>(buyerDto,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Map<String, String>> createBuyer(@Valid @RequestBody Buyer buyer) {

        Buyer buyerCreated = buyerService.createBuyer(buyer);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + buyerCreated.getName() + " creado exitosamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBuyer(@PathVariable Integer id) {

        String nameBuyer = buyerService.getBuyerById(id).getName();
        buyerService.deleteBuyer(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " +nameBuyer+ " eliminado exitosamente");
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateBuyer(@PathVariable Integer id,@Valid @RequestBody Buyer buyer) {
        //logger.info("Intentando actualizar el buyero con ID: {}", id);
        Buyer updatedBuyer = buyerService.updateBuyer(id, buyer);
        //logger.info("Buyer actualizado correctamente: {}", updatedBuyer);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + updatedBuyer.getName()+ ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity <Map<String, String>> partialUpdateBuyer(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Buyer updatedBuyer = buyerService.partialUpdateBuyer(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + updatedBuyer.getName()+ ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }


}
