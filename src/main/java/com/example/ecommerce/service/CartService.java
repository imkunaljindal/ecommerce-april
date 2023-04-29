package com.example.ecommerce.service;

import com.example.ecommerce.dto.RequestDto.CheckoutCartRequestDto;
import com.example.ecommerce.dto.ResponseDto.CartResponseDto;
import com.example.ecommerce.dto.ResponseDto.ItemResponseDto;
import com.example.ecommerce.dto.ResponseDto.OrderResponseDto;
import com.example.ecommerce.exception.InvalidCardException;
import com.example.ecommerce.exception.InvalidCustomerException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CardRespository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.OrderedRepository;
import com.example.ecommerce.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired OrderService orderService;
    @Autowired
    OrderedRepository orderedRepository;

    @Autowired
    CardRespository cardRespository;

    public CartResponseDto saveCart(Integer customerId, Item item){

        Customer customer = customerRepository.findById(customerId).get();
        Cart cart = customer.getCart();

        int newTotal = cart.getCartTotal() + item.getRequiredQuantity()*item.getProduct().getPrice();
        cart.setCartTotal(newTotal);
        cart.getItems().add(item);
        cart.setNumberOfItems(cart.getItems().size());
        item.setCart(cart);
        Cart savedCart = cartRepository.save(cart);

        CartResponseDto cartResponseDto = CartResponseDto.builder()
                .cartTotal(savedCart.getCartTotal())
                .customerName(customer.getName())
                .numberOfItems(savedCart.getNumberOfItems())
                .build();

        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for(Item itemEntity: savedCart.getItems()){
            ItemResponseDto itemResponseDto = ItemTransformer.ItemToItemResponseDto(itemEntity);
            itemResponseDtoList.add(itemResponseDto);
        }

        cartResponseDto.setItems(itemResponseDtoList);
        return cartResponseDto;
    }

    public OrderResponseDto checkOutCart(CheckoutCartRequestDto checkoutCartRequestDto) throws Exception {

        Customer customer;
        try{
            customer = customerRepository.findById(checkoutCartRequestDto.getCustomerId()).get();
        }
        catch (Exception e){
            throw new InvalidCustomerException("Customer id is invalid!!!");
        }

        Card card = cardRespository.findByCardNo(checkoutCartRequestDto.getCardNo());
        if(card==null || card.getCvv()!=checkoutCartRequestDto.getCvv() || card.getCustomer()!=customer){
            throw new InvalidCardException("Your card is not valid!!");
        }

       Cart cart = customer.getCart();
        if(cart.getNumberOfItems()==0){
            throw new Exception("Cart is empty!!");
        }

        try{
            Ordered order = orderService.placeOrder(customer,card);  // throw exception if product goes out of stock
            customer.getOrderList().add(order);
            resetCart(cart);
            Ordered savedOrder = orderedRepository.save(order);

            // prepare response dto
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setOrderDate(savedOrder.getOrderDate());
            orderResponseDto.setCardUsed(savedOrder.getCardUsed());
            orderResponseDto.setCustomerName(customer.getName());
            orderResponseDto.setOrderNo(savedOrder.getOrderNo());
            orderResponseDto.setTotalValue(savedOrder.getTotalValue());

            List<ItemResponseDto> items = new ArrayList<>();
            for(Item itemEntity: savedOrder.getItems()){
               ItemResponseDto itemResponseDto = ItemTransformer.ItemToItemResponseDto(itemEntity);
                items.add(itemResponseDto);
            }
            orderResponseDto.setItems(items);
            return orderResponseDto;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void resetCart(Cart cart){

        cart.setCartTotal(0);
        for(Item item: cart.getItems()){
            item.setCart(null);
        }
        cart.setNumberOfItems(0);
        cart.getItems().clear();

    }
}
