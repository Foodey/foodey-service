package com.foodey.server.recommendation.tfidf;

import com.foodey.server.annotation.PublicEndpoint;
import com.foodey.server.shop.model.Shop;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tfidf")
@RequiredArgsConstructor
public class TFIDFController {

  private final TFIDFService tfidfService;

  @RequestMapping("/recommend")
  @PublicEndpoint
  public List<Shop> recommend() throws Exception {
    return tfidfService.recommendShops("mini");
  }
}
