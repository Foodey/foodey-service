package com.foodey.server.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.foodey.server.product.model.Product;
import com.foodey.server.utils.ConsoleUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@NoArgsConstructor
public class Menu {

  /** Wrapper class for managing products and their categories. */
  @NoArgsConstructor
  private class ProductWrapper {
    @Schema(description = "The set of category names displayed on menu.")
    private Set<String> categoryNames = new LinkedHashSet<>();

    private Set<String> excludedProductIds;

    @Schema(description = "The order sorted set of category names displayed on menu.")
    private LinkedHashSet<String> categorySortOrder;

    @Schema(description = "The list of productIds mapped to the category name displayed on menu.")
    private Map<String, Set<String>> categoryToProductIds = new HashMap<>();

    /**
     * Returns the set of excluded product IDs.
     *
     * @return a set of excluded product IDs.
     */
    private Set<String> getExcludedProductIds() {
      return Optional.ofNullable(excludedProductIds).orElseGet(HashSet::new);
    }

    /**
     * Excludes a product by its ID.
     *
     * @param productId the ID of the product to exclude.
     */
    public void excludeProduct(String productId) {
      if (excludedProductIds == null) excludedProductIds = new HashSet<>();
      excludedProductIds.add(productId);
    }

    /**
     * Excludes a product.
     *
     * @param product the product to exclude.
     */
    public void excludeProduct(Product product) {
      excludeProduct(product.getId());
    }

    /**
     * Excludes a list of products by their IDs.
     *
     * @param productIds the list of product IDs to exclude.
     */
    public void excludeProducts(List<String> productIds) {
      if (excludedProductIds == null) excludedProductIds = new HashSet<>();
      excludedProductIds.addAll(productIds);
    }

    /**
     * Checks if a category exists.
     *
     * @param categoryName the name of the category.
     * @return true if the category exists, false otherwise.
     */
    public boolean hasCategory(String categoryName) {
      return categoryNames.contains(categoryName);
    }

    public Set<String> getCategoryNames() {
      return categoryNames;
    }

    /**
     * Gets the category names order.
     *
     * @return the category sort order.
     */
    public Set<String> getCategorySortOrder() {
      if (categorySortOrder == null) {
        return categoryNames;
      } else {
        LinkedHashSet<String> categorySortOrderCopy = new LinkedHashSet<>(categorySortOrder);
        categorySortOrderCopy.addAll(categoryNames);
        return categorySortOrderCopy;
      }
    }

    public void setCategorySortOrder(List<String> categorySortOrder) {
      this.categorySortOrder = new LinkedHashSet<>(categorySortOrder);
    }

    /**
     * Gets the existing category names sorted by the category sort order if it exists.
     *
     * @return the existing category sort order.
     */
    public Set<String> getSortedCategoryNames() {
      if (categorySortOrder == null) {
        return categoryNames;
      } else {
        LinkedHashSet<String> categorySortOrderCopy =
            categorySortOrder.stream()
                .filter(categoryNames::contains)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        categorySortOrderCopy.addAll(categoryNames);
        return categorySortOrderCopy;
      }
    }

    /**
     * Gets the product IDs for a given category.
     *
     * @param categoryName the name of the category.
     * @return a set of product IDs.
     */
    public Set<String> getProductIds(String categoryName) {
      Set<String> productIds = categoryToProductIds.get(categoryName);
      return productIds != null
          ? productIds.stream()
              .filter(id -> !getExcludedProductIds().contains(id))
              .collect(Collectors.toSet())
          : new HashSet<>();
    }

    /**
     * Gets all product IDs.
     *
     * @return a set of all product IDs.
     */
    public Set<String> getProductIds() {
      return categoryToProductIds.values().stream()
          .flatMap(Set::stream)
          .filter(id -> !getExcludedProductIds().contains(id))
          .collect(Collectors.toSet());
    }

    /**
     * Gets the total number of products.
     *
     * @return the total number of products.
     */
    public int getNumberOfProducts() {
      return categoryToProductIds.values().stream().mapToInt(Set::size).sum();
    }

    /**
     * Adds a product to the specified category. If the category does not exist, it will be created.
     *
     * @param categoryName the name of the category to which the product will be added
     * @param productId the ID of the product to be added
     */
    public void addProduct(String categoryName, String productId) {
      categoryToProductIds
          .computeIfAbsent(
              categoryName,
              k -> {
                // create a new category if it does not exist
                categoryNames.add(categoryName);
                return new HashSet<>();
              })
          .add(productId);
    }

    public void addProduct(Product product) {
      addProduct(product.getCategoryNameDisplayedOnMenu(), product.getId());
    }

    public void addProducts(List<Product> products) {
      products.stream()
          .collect(
              Collectors.groupingBy(
                  Product::getCategoryNameDisplayedOnMenu,
                  Collectors.mapping(Product::getId, Collectors.toSet())))
          .forEach(
              (categoryName, productIds) -> {
                categoryToProductIds
                    .computeIfAbsent(
                        categoryName,
                        k -> {
                          categoryNames.add(categoryName);
                          return new HashSet<>();
                        })
                    .addAll(productIds);
              });
    }
  }

  @Schema(
      description =
          "The shop with id containing in this field will not be able to access this menu.")
  private Set<String> excludedShopIds;

  private ProductWrapper productWrapper = new ProductWrapper(); // the product wrapper for all shops

  private Map<String, ProductWrapper> specificShops;

  private Map<String, ProductWrapper> getSpecificShops() {
    ConsoleUtils.prettyPrint(specificShops);
    if (specificShops == null) specificShops = new HashMap<>();
    return specificShops;
  }

  private Optional<ProductWrapper> getProductWrapperInShop(String shopId) {
    if (specificShops == null) return Optional.empty();
    return Optional.ofNullable(specificShops.get(shopId));
  }

  @JsonIgnore
  public boolean isExcludedFromShop(String shopId) {
    return excludedShopIds != null && excludedShopIds.contains(shopId);
  }

  public Set<String> getExcludedShopIds() {
    if (excludedShopIds == null) excludedShopIds = new HashSet<>();
    return excludedShopIds;
  }

  public boolean hasCategory(String categoryName) {
    return productWrapper.hasCategory(categoryName);
  }

  public boolean hasCategory(String shopId, String categoryName) {
    // this shop don't use the brand menu
    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId)
          .map((productWrapper) -> productWrapper.hasCategory(categoryName))
          .orElse(false);
    }
    return getProductWrapperInShop(shopId)
        .map(
            (productWrapper) ->
                productWrapper.hasCategory(categoryName) || hasCategory(categoryName))
        .orElseGet(() -> hasCategory(categoryName));
  }

  public void addProduct(String categoryName, String productId) {
    productWrapper.addProduct(categoryName, productId);
  }

  public void addProduct(String shopId, String categoryName, String productId) {
    getSpecificShops()
        .computeIfAbsent(shopId, k -> new ProductWrapper())
        .addProduct(categoryName, productId);
  }

  public void addProduct(Product product) {
    productWrapper.addProduct(product);
  }

  public void addProduct(String shopId, Product product) {
    getSpecificShops().computeIfAbsent(shopId, k -> new ProductWrapper()).addProduct(product);
  }

  public void addProducts(List<Product> products) {
    productWrapper.addProducts(products);
  }

  public void addProducts(String shopId, List<Product> products) {
    getSpecificShops().computeIfAbsent(shopId, k -> new ProductWrapper()).addProducts(products);
  }

  // Set the order of category names displayed on menu in all shops of this brand
  public void setCategorySortOrder(List<String> categorySortOrder) {
    productWrapper.setCategorySortOrder(categorySortOrder);
  }

  public void setCategorySortOrder(String shopId, List<String> categorySortOrder) {
    getSpecificShops()
        .computeIfAbsent(shopId, k -> new ProductWrapper())
        .setCategorySortOrder(categorySortOrder);
  }

  public Set<String> getCategoryNames() {
    return productWrapper.getCategoryNames();
  }

  public Set<String> getCategoryNames(String shopId) {
    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId)
          .map(ProductWrapper::getCategoryNames)
          .orElseGet(LinkedHashSet::new);
    }

    return getProductWrapperInShop(shopId)
        .map(
            (productWrapper) -> {
              Set<String> categoryNames = new LinkedHashSet<>(productWrapper.getCategoryNames());
              categoryNames.addAll(getCategoryNames());
              return categoryNames;
            })
        .orElseGet(this::getCategoryNames);
  }

  public Set<String> getCategorySortOrder() {
    return productWrapper.getCategorySortOrder();
  }

  public Set<String> getCategorySortOrder(String shopId) {
    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId)
          .map(ProductWrapper::getCategorySortOrder)
          .orElseGet(LinkedHashSet::new);
    }

    return getProductWrapperInShop(shopId)
        .map(
            (productWrapper) -> {
              Set<String> categorySortOrder =
                  new LinkedHashSet<>(productWrapper.getCategorySortOrder());
              categorySortOrder.addAll(getCategorySortOrder());
              return categorySortOrder;
            })
        .orElseGet(this::getCategorySortOrder);
  }

  public Set<String> getProductIds(String categoryName) {
    return productWrapper.getProductIds(categoryName);
  }

  public Set<String> getProductIds(String shopId, String categoryName) {

    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId)
          .map((productWrapper) -> productWrapper.getProductIds(categoryName))
          .orElseGet(HashSet::new);
    }

    return getProductWrapperInShop(shopId)
        .map(
            (productWrapper) -> {
              Set<String> productIds = productWrapper.getProductIds(categoryName);
              productIds.addAll(getProductIds(categoryName));
              return productIds;
            })
        .orElseGet(() -> getProductIds(categoryName));
  }

  public Set<String> getProductIds() {
    return productWrapper.getProductIds();
  }

  public Set<String> getProductIdsInShop(String shopId) {
    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId)
          .map(ProductWrapper::getProductIds)
          .orElseGet(HashSet::new);
    }

    return getProductWrapperInShop(shopId)
        .map(
            (productWrapper) -> {
              Set<String> productIds = new HashSet<>(productWrapper.getProductIds());
              productIds.addAll(getProductIds());
              return productIds;
            })
        .orElseGet(this::getProductIds);
  }

  public int getNumberOfProducts() {
    return productWrapper.getNumberOfProducts();
  }

  public int getNumberOfProducts(String shopId) {
    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId).map(ProductWrapper::getNumberOfProducts).orElse(0);
    }
    return getProductWrapperInShop(shopId)
        .map((productWrapper) -> productWrapper.getNumberOfProducts() + getNumberOfProducts())
        .orElseGet(this::getNumberOfProducts);
  }

  public Set<String> getSortedCategoryNames() {
    return productWrapper.getSortedCategoryNames();
  }

  public Set<String> getSortedCategoryNames(String shopId) {
    if (isExcludedFromShop(shopId)) {
      return getProductWrapperInShop(shopId)
          .map(ProductWrapper::getSortedCategoryNames)
          .orElseGet(LinkedHashSet::new);
    }

    return getProductWrapperInShop(shopId)
        .map(
            (shopProductWrapper) -> {
              Set<String> categorySortOrder =
                  new LinkedHashSet<>(shopProductWrapper.getCategorySortOrder());
              categorySortOrder.addAll(getCategorySortOrder());
              return categorySortOrder.stream()
                  .filter(
                      (categoryName) ->
                          shopProductWrapper.hasCategory(categoryName) || hasCategory(categoryName))
                  .collect(Collectors.toSet());
            })
        .orElseGet(this::getSortedCategoryNames);
  }

  public void excludeProduct(String shopId, String productId) {
    getSpecificShops().computeIfAbsent(shopId, k -> new ProductWrapper()).excludeProduct(productId);
  }

  public void excludeProduct(String shopId, Product product) {
    getSpecificShops().computeIfAbsent(shopId, k -> new ProductWrapper()).excludeProduct(product);
  }

  public void excludeProducts(String shopId, List<String> productIds) {
    getSpecificShops()
        .computeIfAbsent(shopId, k -> new ProductWrapper())
        .excludeProducts(productIds);
  }

  public void excludeShop(String shopId) {
    getExcludedShopIds().add(shopId);
  }

  public void excludeShops(List<String> shopIds) {
    getExcludedShopIds().addAll(shopIds);
  }

  @JsonSerialize(using = InstantSerializer.class)
  @CreatedDate
  private Instant createdAt;

  @JsonSerialize(using = InstantSerializer.class)
  @LastModifiedDate
  private Instant updatedAt;
}
