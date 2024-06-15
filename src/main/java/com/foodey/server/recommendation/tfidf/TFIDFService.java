// package com.foodey.server.recommendation.tfidf;

// import com.foodey.server.shop.model.Shop;
// import com.foodey.server.shop.service.ShopService;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import org.apache.lucene.analysis.Analyzer;
// import org.apache.lucene.analysis.standard.StandardAnalyzer;
// import org.apache.lucene.document.Document;
// import org.apache.lucene.document.Field;
// import org.apache.lucene.document.TextField;
// import org.apache.lucene.index.DirectoryReader;
// import org.apache.lucene.index.IndexWriter;
// import org.apache.lucene.index.IndexWriterConfig;
// import org.apache.lucene.queryparser.classic.QueryParser;
// import org.apache.lucene.search.*;
// import org.apache.lucene.store.Directory;
// import org.apache.lucene.store.RAMDirectory;
// import org.springframework.stereotype.Service;

// @Service
// // @RequiredArgsConstructor
// public class TFIDFService {

//   private ShopService shopService; // Inject ProductService để lấy dữ liệu sản phẩm

//   private Directory directory;
//   private IndexSearcher indexSearcher;

//   public TFIDFService(ShopService shopService) {
//     this.shopService = shopService;
//     // FSDirectory nếu muốn lưu vào ổ cứng
//     try {

//       createIndex(); // Tạo index cho dữ liệu sản phẩm khi khởi tạo service
//     } catch (Exception e) {
//       e.printStackTrace();
//       // TODO: handle exception
//     }
//   }

//   private void createIndex() throws IOException {
//     this.directory =
//         new RAMDirectory(); // Sử dụng RAMDirectory cho mục đích minh họa, có thể thay bằng
//     Analyzer analyzer = new StandardAnalyzer();
//     IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//     IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

//     List<Shop> shops = shopService.findAll();

//     for (Shop shop : shops) {
//       Document doc = new Document();
//       doc.add(new TextField("productId", shop.getId(), Field.Store.YES));
//       doc.add(new TextField("name", shop.getName(), Field.Store.YES));
//       doc.add(new TextField("addresss", shop.getAddress().getDetailsAddress(), Field.Store.YES));

//       String categoryIdsString =
//           shop.getCategoryIds().stream().map(Object::toString).reduce("", String::concat);
//       doc.add(new TextField("categoryIds", categoryIdsString, Field.Store.YES));

//       indexWriter.addDocument(doc);
//     }

//     indexWriter.close();
//     indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
//   }

//   public List<Shop> recommendShops(String query) throws Exception {
//     List<Shop> recommmendShops = new ArrayList<>();

//     QueryParser parser = new QueryParser("description", new StandardAnalyzer());
//     Query luceneQuery = parser.parse(QueryParser.escape(query));

//     TopDocs topDocs = indexSearcher.search(luceneQuery, 10);
//     ScoreDoc[] scoreDocs = topDocs.scoreDocs;

//     for (ScoreDoc scoreDoc : scoreDocs) {
//       Document doc = indexSearcher.doc(scoreDoc.doc);
//       String shopId = doc.get("shopId");
//       Shop shop = shopService.findById(shopId);
//       if (shop != null) {
//         recommmendShops.add(shop);
//       }
//     }
//     return recommmendShops;
//   }
// }
