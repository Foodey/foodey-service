package com.foodey.server.product.controller;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.product.model.Product;
import com.foodey.server.product.service.ProductService;
import com.foodey.server.user.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
      @PageableDefault(page = 0, size = 12, direction = Direction.ASC) Pageable pageable) {
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

  @Operation(
      summary = "Get products by ids",
      description = "Get products by ids. This endpoint is public.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return products"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping("/multiple")
  @ResponseStatus(HttpStatus.OK)
  public List<Product> findMutipleProductById(@RequestBody List<String> ids) {
    return productService.findAllById(ids);
  }

  @Operation(
      summary = "Get image upload options",
      description = "Get image upload options for a product")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return image upload options"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @RolesAllowed({RoleType.Fields.SELLER})
  @GetMapping("/{id}/image-upload-options")
  public Map<String, Object> getImageUploadOptions(@PathVariable("id") String id) {
    return productService.getImageUploadApiOptions(id);
  }
}
