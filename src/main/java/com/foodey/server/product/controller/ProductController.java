package com.foodey.server.product.controller;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.product.model.Product;
import com.foodey.server.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Operation(
      summary = "Get all products",
      description = "Get all products in a paginated format. This endpoint is public.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return all products in a paginated format"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping({"/", ""})
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public Slice<Product> getProducts(
      @PageableDefault(page = 0, size = 12, sort = "name", direction = Direction.ASC)
          Pageable pageable) {
    return productService.findAll(pageable);
  }

  @Operation(
      summary = "Get product by id",
      description = "Get product by id. This endpoint is public.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return product"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}")
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public Product findById(@PathVariable String id) {
    return productService.findById(id);
  }
}
