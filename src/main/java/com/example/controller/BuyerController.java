package com.example.controller;

import com.example.dto.BuyerDto;
import com.example.model.Buyer;
import com.example.service.BuyerService;
import com.example.utility.MapperObject;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/buyers")
public class BuyerController {

    private final BuyerService buyerService;
    private final MapperObject mapperObject;

    public BuyerController(BuyerService buyerService, MapperObject mapperObject) {
        this.buyerService = buyerService;
        this.mapperObject = mapperObject;
    }

    @GetMapping
    public ResponseEntity<List<BuyerDto>> getAllBuyers() {
        return new ResponseEntity<>(buyerService.getAllBuyers().stream().map(mapperObject::toPublicDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerDto> getBuyerById(@PathVariable Integer id) {
        return new ResponseEntity<>(mapperObject.toPublicDto(buyerService.getBuyerById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createBuyer(@Valid @RequestBody BuyerDto buyerDto) {
        Buyer buyerCreated = buyerService.createBuyer(buyerDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + buyerCreated.getName() + "con ID: " + buyerCreated.getId() + ", creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBuyer(@PathVariable Integer id) {

        String nameBuyer = buyerService.getBuyerById(id).getName();
        buyerService.deleteBuyer(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + nameBuyer + "de ID: " + id + ", fue eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateBuyer(@PathVariable Integer id, @Valid @RequestBody BuyerDto buyerdto) {
        Buyer updatedBuyer = buyerService.updateBuyer(id, buyerdto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + updatedBuyer.getName() + " de ID: " + id + ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateBuyer(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Buyer updatedBuyer = buyerService.partialUpdateBuyer(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + updatedBuyer.getName() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }

    /*@PatchMapping("/a/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateBuyer2(@PathVariable Integer id, @RequestBody BuyerDto updatesDto) {
        Buyer updatedBuyer = buyerService.partialUpdateBuyer2(id, updatesDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador: " + updatedBuyer.getName() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }*/

}
