package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.payloads.ResponseTemplate;
import com.ecommerce.inventory.services.ProductOrchestrationService;
import com.ecommerce.inventory.services.ProductService;
import com.ecommerce.inventory.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Products in Inventory",
        description = "REST APIs to CREATE, UPDATE, FETCH, RESERVE, SELL and DELETE product details"
)
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductOrchestrationService productOrchestrationService;

    @Operation(
            summary = "Fetch All Products REST API",
            description = "REST API to fetch all products (accessible by Admin, Supplier, and Customer)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CUSTOMER')")
    public ResponseEntity<ResponseTemplate<List<Product>>> getProducts() {
        List<Product> products = productService.getProducts();
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Products retrieved successfully",
                products
        );
    }

    @Operation(
            summary = "Fetch Product by ID REST API",
            description = "REST API to fetch product details by product ID (Admin or Owner only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins or owner can access this"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN') or @productSecurity.isOwner(authentication, #productId)")
    public ResponseEntity<ResponseTemplate<Product>> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Product retrieved successfully",
                product
        );
    }

    @Operation(
            summary = "Create Product REST API",
            description = "REST API to create a new product (Supplier only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only suppliers can create products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ResponseTemplate<Product>> createProduct(@RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseBuilder.build(
                HttpStatus.CREATED,
                "Product created successfully",
                createdProduct
        );
    }

    @Operation(
            summary = "Update Product REST API",
            description = "REST API to update product details (Owner only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only owner can update product"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PatchMapping("/{productId}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #productId)")
    public ResponseEntity<ResponseTemplate<Product>> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Product updated successfully",
                updatedProduct
        );
    }

    @Operation(
            summary = "Delete Product REST API",
            description = "REST API to delete a product by ID (Owner only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only owner can delete product"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @DeleteMapping("/{productId}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #productId)")
    public ResponseEntity<ResponseTemplate<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Product deleted successfully",
                null
        );
    }

    @Operation(
            summary = "Reserve Product Quantity REST API",
            description = "REST API for customers to reserve a certain quantity of a product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantity reserved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only customers can reserve products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping("/{productId}/reserve")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseTemplate<Product>> reserve(
            @PathVariable Long productId,
            @RequestBody @Valid QuantityRequest quantity) {
        Product reservedProduct = productService.reserveQuantity(productId, quantity.getQuantity());
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Quantity reserved successfully",
                reservedProduct
        );
    }

    @Operation(
            summary = "Sell Product REST API",
            description = "REST API for customers to mark a product as sold"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantity sold successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only customers can sell products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping("/{productId}/sell")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseTemplate<Product>> sell(
            @PathVariable Long productId,
            @RequestBody @Valid QuantityRequest quantity) {
        Product soldProduct = productOrchestrationService.markSold(productId, quantity.getQuantity());
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Quantity sold successfully",
                soldProduct
        );
    }

    @Operation(
            summary = "Return Product to Stock REST API",
            description = "REST API for customers to return product quantity back to stock"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantity returned successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only customers can return products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping("/{productId}/stock")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseTemplate<Product>> stockReturn(
            @PathVariable Long productId,
            @RequestBody @Valid QuantityRequest quantity) {
        Product productToReturn = productService.stockReturn(productId, quantity.getQuantity());
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Quantity returned successfully",
                productToReturn
        );
    }
}
