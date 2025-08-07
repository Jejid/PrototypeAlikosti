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
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testGetAllBuyers() {
        //Arrange
        ArrayList<Buyer> buyers = new ArrayList<>();
        buyers.add(new Buyer(1, "name", "apellido", "2 agosto", null, null,
                null, null, null));
        when(buyerService.getAllBuyers()).thenReturn(buyers);

        ArrayList<BuyerDto> buyerDtos = new ArrayList<>();
        buyerDtos.add(new BuyerDto(1, "nombre", "apellido", "2 agosto", null,
                "sdsd", "null", null));
        when(buyerMapper.toPublicDto(buyers.get(0))).thenReturn(buyerDtos.get(0));


        //Act
        ResponseEntity<List<BuyerDto>> response = buyerController.getAllBuyers();

        //Assert
        assertEquals(buyerDtos, response.getBody());
        assertEquals(buyerDtos.get(0).getName(), response.getBody().get(0).getName());

    }

    @Test
    public void testGetBuyerById() {

        //Arrange
        Buyer buyer = new Buyer();
        when(buyerService.getBuyerById(1)).thenReturn(buyer);

        BuyerDto buyerDto = new BuyerDto();
        when(buyerMapper.toPublicDto(buyer)).thenReturn(buyerDto);

        //Act
        ResponseEntity<BuyerDto> response = buyerController.getBuyerById(1);

        //Assert
        assertEquals(buyerDto, response.getBody());
        verify(buyerService, times(1)).getBuyerById(1);
        verify(buyerMapper, atLeast(1)).toPublicDto(buyer);
    }

}