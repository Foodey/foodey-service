// package com.foodey.foodeyservice.v1.initials;

// import com.foodey.foodeyservice.v1.models.Product;
// import com.foodey.foodeyservice.v1.models.ProductCategory;
// import com.foodey.foodeyservice.v1.repositories.ProductCategoryRepository;
// import com.foodey.foodeyservice.v1.repositories.ProductRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// /** ProductInitializer */
// @Component
// @lombok.RequiredArgsConstructor
// public class ProductInitializer implements CommandLineRunner {

//   private final ProductRepository productRepository;
//   private final ProductCategoryRepository productCategoryRepository;

//   private String[] productCategories = {
//     "Food", "Drink", "Dessert", "Snack", "Beverage", "Alcohol", "Coffee", "Tea"
//   };

//   @Override
//   @Transactional
//   // create all roles if not exists
//   public void run(String... args) throws Exception {
//     for (String name : productCategories) {

//       if (!productCategoryRepository.existsByName(name)) {
//         ProductCategory category =
//             productCategoryRepository.insert(ProductCategory.builder().name(name).build());
//         productRepository.save(Product.builder().name(name).category(category).price(1).build());
//       }
//     }
//   }
// }
