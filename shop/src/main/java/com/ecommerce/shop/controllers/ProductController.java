package com.ecommerce.shop.controllers;

import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ResponseTemplate;
import com.ecommerce.shop.services.ProductService;
import com.ecommerce.shop.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Product Management APIs",
        description = "Endpoints for managing and retrieving products in the Shop microservice"
)
@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Fetch all products",
            description = "Retrieves the complete list of available products from the shop"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error while fetching products")
    })
    @GetMapping("/products")
    public ResponseEntity<ResponseTemplate<List<Product>>> getProducts() {
        List<Product> products = productService.getProducts();
        return ResponseBuilder.build(HttpStatus.OK, "Products fetched successfully", products);
    }
}
