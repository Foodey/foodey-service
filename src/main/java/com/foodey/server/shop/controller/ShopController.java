package com.foodey.server.shop.controller;

import com.foodey.server.annotation.CurrentUser;
import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.shop.model.Shop;
import com.foodey.server.shop.service.ShopService;
import com.foodey.server.user.enums.RoleType;
import com.foodey.server.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

  private final ShopService shopService;

  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Shop created successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description =
            "User does not have permission to access this resource. Only sellers can create shops"),
    @ApiResponse(responseCode = "404", description = "Brand not found"),
    @ApiResponse(responseCode = "409", description = "Shop with the same name already exists"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @RolesAllowed(RoleType.Fields.SELLER)
  public Shop addNewShopOfBrand(@Valid @RequestBody Shop shop, @CurrentUser User user) {
    return shopService.createShop(shop, user);
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shop found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public Shop findById(@PathVariable(required = true, name = "id") String id) {
    return shopService.findById(id);
  }

  @Operation(
      summary = "Get all shops",
      description = "Get all shops. Optionally filter by location")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shops found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping({"/", ""})
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public Slice<Shop> findAll(
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false, defaultValue = "5") long maxDistance,
      @PageableDefault(page = 0, size = 12) Pageable pageable) {
    if (longitude != null && latitude != null) {
      return shopService.findAllNear(longitude, latitude, maxDistance, pageable);
    }
    return shopService.findAll(pageable);
  }

  @Operation(
      summary = "Get shops by category",
      description = "Get shops by category. Optionally filter by location")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shops found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/categories/{categoryId}")
  @PublicEndpoint
  @ResponseStatus(HttpStatus.OK)
  public Slice<Shop> findByCategoryId(
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false, defaultValue = "5") long maxDistance,
      @PathVariable("categoryId") String categoryId,
      @PageableDefault(page = 0, size = 12, sort = "rating", direction = Direction.ASC)
          Pageable pageable) {
    if (longitude != null && latitude != null) {
      return shopService.findByCategoryIdNear(
          categoryId, longitude, latitude, maxDistance, pageable);
    }
    return shopService.findByCategoryId(categoryId, pageable);
  }

  @Operation(
      summary = "Search shops by name",
      description = "Search shops by name. Optionally filter by location")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shops found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/search")
  @PublicEndpoint
  public Slice<Shop> searchByName(
      @RequestParam String q,
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false, defaultValue = "5") long maxDistance,
      @PageableDefault(page = 0, size = 9, sort = "rating", direction = Direction.ASC)
          Pageable pageable) {
    if (longitude != null && latitude != null) {
      return shopService.searchByName(q, longitude, latitude, maxDistance, pageable);
    }
    return shopService.searchByName(q, pageable);
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shops found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/me/all")
  @RolesAllowed(RoleType.Fields.SELLER)
  public Slice<Shop> getShopsOfCurrentUser(
      @CurrentUser User user,
      @PageableDefault(page = 0, size = 9, sort = "rating", direction = Direction.ASC)
          Pageable pageable) {
    return shopService.findByOwnerId(user.getId(), pageable);
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Shops found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/brands/{brandId}")
  @RolesAllowed(RoleType.Fields.SELLER)
  public Slice<Shop> getAllShopsOfBrand(
      @PathVariable("brandId") String brandId,
      @PageableDefault(page = 0, size = 9, sort = "rating", direction = Direction.ASC)
          Pageable pageable) {
    return shopService.findByBrandId(brandId, pageable);
  }

  @Operation(
      summary = "Get upload options for shop logo",
      description = "Get upload options for shop logo")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Upload options found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have permission to access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}/logo-upload-options")
  @RolesAllowed(RoleType.Fields.SELLER)
  public Map<String, Object> getLogoUploadOptions(
      @PathVariable("id") String shopId, @CurrentUser User user) {
    return shopService.getLogoUploadApiOptions(shopId, user.getId());
  }

  @Operation(
      summary = "Get upload options for shop wallpaper",
      description = "Get upload options for shop wallpaper")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Upload options found successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    @ApiResponse(
        responseCode = "403",
        description = "User does not have permission to access this resource"),
    @ApiResponse(responseCode = "404", description = "Shop not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{id}/wallpaper-upload-options")
  @RolesAllowed(RoleType.Fields.SELLER)
  public Map<String, Object> getWallpaperUploadOptions(
      @PathVariable("id") String shopId, @CurrentUser User user) {
    return shopService.getWallpaperUploadApiOptions(shopId, user.getId());
  }
}
