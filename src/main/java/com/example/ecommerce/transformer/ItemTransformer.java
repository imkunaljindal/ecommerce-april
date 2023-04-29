package com.example.ecommerce.transformer;

import com.example.ecommerce.dto.RequestDto.ItemRequestDto;
import com.example.ecommerce.dto.ResponseDto.ItemResponseDto;
import com.example.ecommerce.model.Item;

public class ItemTransformer {

    public static Item ItemRequestDtoToItem(ItemRequestDto itemRequestDto){
        return Item.builder()
                .requiredQuantity(itemRequestDto.getRequiredQuantity())
                .build();
    }

    public static ItemResponseDto ItemToItemResponseDto(Item item){

        return ItemResponseDto.builder()
                .priceOfOneItem(item.getProduct().getPrice())
                .productName(item.getProduct().getName())
                .quantity(item.getRequiredQuantity())
                .totalPrice(item.getRequiredQuantity()*item.getProduct().getPrice())
                .build();
    }
}
