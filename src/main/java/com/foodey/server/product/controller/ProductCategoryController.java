package com.foodey.server.product.controller;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.product.model.ProductCategory;
import com.foodey.server.product.service.ProductCategoryService;
import com.foodey.server.user.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {

  private final ProductCategoryService productCategoryService;

  @Operation(summary = "Get all product categories")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Return all product categories in a paginated format"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @GetMapping({"/", ""})
  public Slice<ProductCategory> getCategories(
      @PageableDefault(page = 0, size = 12, direction = Direction.ASC) Pageable pageable) {
    return productCategoryService.findAll(pageable);
  }

  @Operation(summary = "Add a new product category")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return the newly created product category"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(
        responseCode = "409",
        description = "Product Category with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping({"/", ""})
  @RolesAllowed(RoleType.Fields.ADMIN)
  public ProductCategory addCategory(@RequestBody @Valid ProductCategory productCategory) {
    return productCategoryService.createProductCategory(productCategory);
  }

  @Operation(summary = "Remove a product category")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Product Category removed"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @DeleteMapping("/{id}")
  @RolesAllowed(RoleType.Fields.ADMIN)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeCategory(@PathVariable(name = "id") String id) {
    productCategoryService.deleteById(id);
  }
}
