package com.example.controller;

import com.example.dto.BuyerDto;
import com.example.mapper.BuyerMapper;
import com.example.model.Buyer;
import com.example.service.BuyerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyerControllerTest {

    @Mock
    private BuyerService buyerService;

    @Mock
    private BuyerMapper buyerMapper;

    @InjectMocks
    private BuyerController buyerController;


    @Test
    void testGetAllBuyers() {
        //Arrange
        List<Buyer> buyers = List.of(new Buyer(1, "Carlos", "Pérez", "2 agosto", null,
                null, null, null, null));
        when(buyerService.getAllBuyers()).thenReturn(buyers);

        BuyerDto dto = new BuyerDto(1, "Carlos", "Pérez", "2 agosto", null, null,
                null, null);
        when(buyerMapper.toPublicDto(buyers.get(0))).thenReturn(dto);

        //Act
        ResponseEntity<List<BuyerDto>> response = buyerController.getAllBuyers();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Carlos", response.getBody().get(0).getName());
    }

    @Test
    void testGetAllBuyers_EmptyList() {
        //Arrange
        when(buyerService.getAllBuyers()).thenReturn(new ArrayList<>());

        //Act
        ResponseEntity<List<BuyerDto>> response = buyerController.getAllBuyers();

        //Assert
        assertTrue(response.getBody().isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBuyerById() {
        //Arrange
        Buyer buyer = new Buyer(1, "Ana", "López", "1 enero", null, null, null, null, null);
        when(buyerService.getBuyerById(1)).thenReturn(buyer);

        BuyerDto dto = new BuyerDto(1, "Ana", "López", "1 enero", null, null, null, null);
        when(buyerMapper.toPublicDto(buyer)).thenReturn(dto);

        //Act
        ResponseEntity<BuyerDto> response = buyerController.getBuyerById(1);

        //Assert
        assertEquals(dto, response.getBody());
        verify(buyerService, times(1)).getBuyerById(1);
    }

    @Test
    void testGetBuyerById_NotFound() {
        //Arrange
        when(buyerService.getBuyerById(99)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Act & Assert
        assertThrows(ResponseStatusException.class, () -> buyerController.getBuyerById(99));
    }

    @Test
    void testGetLogin_Success() {
        //Arrange
        Map<String, Object> credentials = Map.of("email", "test@test.com", "password", "123");
        when(buyerService.getLoginAccess(credentials)).thenReturn("Acceso Aprobado");

        //Act
        ResponseEntity<Map<String, String>> response = buyerController.getLogin(credentials);

        //Assert
        assertEquals("Acceso Aprobado", response.getBody().get("message"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetLogin_Failure() {
        //Arrange
        Map<String, Object> credentials = Map.of("email", "test@test.com", "password", "wrong");
        when(buyerService.getLoginAccess(credentials)).thenReturn("Credenciales inválidas");

        //Act
        ResponseEntity<Map<String, String>> response = buyerController.getLogin(credentials);

        //Assert
        assertEquals("Credenciales inválidas", response.getBody().get("message"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateBuyer() {
        // Arrange
        BuyerDto buyerDto = new BuyerDto(1, "Mario", "Suárez", "10 marzo", null, null, null, null);
        Buyer buyer = new Buyer(1, "Mario", "Suárez", "10 marzo", null, null, null, null, null);

        when(buyerService.createBuyer(buyerDto)).thenReturn(buyer);

        // Act
        ResponseEntity<Map<String, String>> response = buyerController.createBuyer(buyerDto);

        //Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("Mario"));
        assertTrue(response.getBody().get("message").contains("1"));
    }

    @Test
    void testDeleteBuyer() {
        //Arrange
        Buyer buyer = new Buyer(1, "Laura", "Gómez", "5 abril", null, null, null, null, null);
        when(buyerService.getBuyerById(1)).thenReturn(buyer);
        doNothing().when(buyerService).deleteBuyer(1);

        //Act
        ResponseEntity<Map<String, String>> response = buyerController.deleteBuyer(1);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("Laura"));
        assertTrue(response.getBody().get("message").contains("1"));
    }

    @Test
    void testUpdateBuyer() {
        //Arrange
        BuyerDto dto = new BuyerDto(1, "Pedro", "Ramírez", "7 julio", null, null, null, null);
        Buyer buyer = new Buyer(1, "Pedro", "Ramírez", "7 julio", null, null, null, null, null);
        when(buyerService.updateBuyer(1, dto)).thenReturn(buyer);

        //Act
        ResponseEntity<Map<String, String>> response = buyerController.updateBuyer(1, dto);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("Pedro"));
    }

    @Test
    void testPartialUpdateBuyer() {
        //Arrange
        Map<String, Object> updates = Map.of("name", "Carolina");
        Buyer updated = new Buyer(1, "Carolina", "Ríos", "20 mayo", null, null, null, null, null);
        when(buyerService.partialUpdateBuyer(1, updates)).thenReturn(updated);

        //Act
        ResponseEntity<Map<String, String>> response = buyerController.partialUpdateBuyer(1, updates);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comprador: Carolina, campo/s actualizado/s exitosamente", response.getBody().get("message"));
    }

}
