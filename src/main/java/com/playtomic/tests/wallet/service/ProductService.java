package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.model.http.Product;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private static final String PRODUCT = "product-collection";
    private final MongoTemplate mongoTemplate;

    public ProductService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Product findByProductName(String productName){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(productName));
        return mongoTemplate.findOne(query,Product.class, PRODUCT);
    }
}
