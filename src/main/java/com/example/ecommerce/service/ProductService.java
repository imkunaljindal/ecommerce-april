package com.example.ecommerce.service;

import com.example.ecommerce.Enum.ProductCategory;
import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dto.RequestDto.ProductRequestDto;
import com.example.ecommerce.dto.ResponseDto.ProductResponseDto;
import com.example.ecommerce.exception.InvalidSellerException;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.model.Ordered;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.transformer.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws InvalidSellerException {

        Seller seller;
        try{
           seller =  sellerRepository.findById(productRequestDto.getSellerId()).get();
        }
        catch (Exception e){
            throw new InvalidSellerException("Seller doesn't exist");
        }

        Product product = ProductTransformer.ProductRequestDtoToProduct(productRequestDto);
        product.setSeller(seller);

        // add product to current products of seller
        seller.getProducts().add(product);
        sellerRepository.save(seller);  // saves both seller and product

        // prepare Response Dto
        return ProductTransformer.ProductToProductResponseDto(product);
    }

    public List<ProductResponseDto> getAllProductsByCategory(ProductCategory category){

        List<Product> products = productRepository.findByProductCategory(category);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product: products){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }

        return productResponseDtos;
    }

    public List<ProductResponseDto> getAllProductsByPriceAndCategory(int price, String category){

        List<Product> products = productRepository.getAllProductsByPriceAndCategory(price,category);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product: products){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }

        return productResponseDtos;
    }

    public void decreaseProductQuantity(Item item) throws Exception {

            Product product = item.getProduct();
            int quantity = item.getRequiredQuantity();
            int currentQuantity = product.getQuantity();
            if(quantity>currentQuantity){
                throw new Exception("Out of stock");
            }
            product.setQuantity(currentQuantity-quantity);
            if(product.getQuantity()==0){
                product.setProductStatus(ProductStatus.OUT_OF_STOCK);
            }
    }
}
