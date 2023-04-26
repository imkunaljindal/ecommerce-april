package com.example.ecommerce.service;

import com.example.ecommerce.dto.RequestDto.CustomerRequestDto;
import com.example.ecommerce.dto.ResponseDto.CustomerResponseDto;
import com.example.ecommerce.exception.MobileNoAlreadyPresentException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.transformer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) throws MobileNoAlreadyPresentException {

        if(customerRepository.findByMobNo(customerRequestDto.getMobNo())!=null)
            throw new MobileNoAlreadyPresentException("Sorry! Customer already exists!");

        // request dto -> customer
        Customer customer = CustomerTransformer.CustomerRequestDtoToCustomer(customerRequestDto);
        Cart cart = Cart.builder()
                .cartTotal(0)
                .numberOfItems(0)
                .customer(customer)
                .build();
        customer.setCart(cart);



        Customer savedCustomer = customerRepository.save(customer);  // customer and cart

        // prepare response dto
        return CustomerTransformer.CustomerToCustomerResponseDto(savedCustomer);
    }
}
