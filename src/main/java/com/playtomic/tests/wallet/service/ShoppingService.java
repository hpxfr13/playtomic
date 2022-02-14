package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.model.http.ShoppingObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingService {
    private static final String SHOPPING_COLLECTION = "shopping-collection";
    private final MongoTemplate mongoTemplate;

    public ShoppingService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ShoppingObject> findAllShoppingHistory(){
        return mongoTemplate.findAll(ShoppingObject.class,SHOPPING_COLLECTION);
    }

    public ShoppingObject findById(String shoppingId){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(shoppingId));
        return mongoTemplate.findOne(query, ShoppingObject.class,SHOPPING_COLLECTION);
    }

    public ShoppingObject save(ShoppingObject shoppingObject){
        return mongoTemplate.save(shoppingObject,SHOPPING_COLLECTION);
    }


}
