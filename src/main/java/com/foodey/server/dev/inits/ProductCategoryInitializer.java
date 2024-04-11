package com.foodey.server.dev.inits;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** RoleInitial */
@Component
@RequiredArgsConstructor
@Profile("dev")
public class ProductCategoryInitializer implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'run'");
  }

  // private final ProductCategoryRepository productCategoryRepository;

  // private static String[] names = {
  //   "Beverages",
  //   "Bread/Bakery",
  //   "Canned/Jarred Goods",
  //   "Dairy",
  //   "Dry/Baking Goods",
  //   "Frozen Foods",
  //   "Meat",
  //   "Produce",
  //   "Cleaners",
  //   "Paper Goods",
  //   "Salad",
  //   "Other"
  // };

  // @Override
  // @Transactional
  // public void run(String... args) throws Exception {
  //   for (String name : names) {
  //     if (!productCategoryRepository.existsByName(name)) {
  //       productCategoryRepository.insert(ProductCategory.builder().name(name).build());
  //     }
  //   }
  // }
}
