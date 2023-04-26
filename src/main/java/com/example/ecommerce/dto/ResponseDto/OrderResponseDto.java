package com.example.ecommerce.dto.ResponseDto;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Item;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {

    String orderNo;

    int totalValue;

    Date orderDate;

    String cardUsed;

    List<ItemResponseDto> items;

    String customerName;
}
