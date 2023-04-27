package com.example.ecommerce.controller;

import com.example.ecommerce.dto.RequestDto.CheckoutCartRequestDto;
import com.example.ecommerce.dto.RequestDto.ItemRequestDto;
import com.example.ecommerce.dto.ResponseDto.CartResponseDto;
import com.example.ecommerce.dto.ResponseDto.OrderResponseDto;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public ResponseEntity addToCart(@RequestBody ItemRequestDto itemRequestDto) throws Exception {

        try{
          Item savedItem = itemService.addItem(itemRequestDto);
          CartResponseDto cartResponseDto = cartService.saveCart(itemRequestDto.getCustomerId(),savedItem);
          return new ResponseEntity(cartResponseDto,HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/checkout")
    public OrderResponseDto checkOutCart(@RequestBody CheckoutCartRequestDto checkoutCartRequestDto) throws Exception {

        return cartService.checkOutCart(checkoutCartRequestDto);
    }


    // remove from cart

    // view all items in cart

    // email sending

    // my email - kunaljindal995@gmail.com


}
