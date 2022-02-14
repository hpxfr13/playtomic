package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.OrderStatus;
import com.playtomic.tests.wallet.model.http.Product;
import com.playtomic.tests.wallet.model.http.ShoppingObject;
import com.playtomic.tests.wallet.model.http.WalletRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class WalletService {

    public static final String WALLET = "wallet-collection";
    private final MongoTemplate mongoTemplate;
    private final ProductService productService;
    private final ShoppingService shoppingService;

    public WalletService(MongoTemplate mongoTemplate, ProductService productService, ShoppingService shoppingService) {
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
        this.shoppingService = shoppingService;
    }

    public WalletRequest findById(String creditCardNumber){
        return mongoTemplate.findOne(new Query(Criteria.where("creditCardNumber").is(creditCardNumber)),WalletRequest.class, WALLET);
    }

    public WalletRequest save(WalletRequest walletRequest){
        return mongoTemplate.save(walletRequest, WALLET);
    }
    public WalletRequest charge(WalletRequest walletRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("creditCardNumber").is(walletRequest.getCreditCardNumber()));
        WalletRequest currentWallet = mongoTemplate.findOne(query,WalletRequest.class, WALLET);

        BigDecimal amount = null;

        if(currentWallet != null){
            amount = currentWallet.getAmount().add(walletRequest.getAmount());
            walletRequest.setAmount(amount);
            walletRequest.setId(currentWallet.getId());
        }

        return  save(walletRequest);

    }

    public boolean refund(ShoppingObject shoppingObject) {
        ShoppingObject currentShoppingInfo = shoppingService.findById(shoppingObject.getId());
        Long dateBefore14 = new Date().getTime() - 14 * 60 * 24 * 3600 * 1000;
        if(currentShoppingInfo.getEventDate().before(new Date(dateBefore14))){
            return false;
        }

        WalletRequest currentWallet = findById(currentShoppingInfo.getCreditCardNumber());
        currentWallet.getAmount().add(currentShoppingInfo.getProduct().getPrice());
        shoppingObject.setOrderStatus(OrderStatus.ORDER_CANCELED);
        shoppingService.save(shoppingObject);
        save(currentWallet);

        return true;
    }

    public boolean shopping(ShoppingObject shoppingObject){
        Query query = new Query();
        query.addCriteria(Criteria.where("creditCardNumber").is(shoppingObject.getCreditCardNumber()));
        WalletRequest currentWallet = mongoTemplate.findOne(query,WalletRequest.class, WALLET);

        if(currentWallet != null){
            Product product = productService.findByProductName(shoppingObject.getProduct().getName());
            if(product != null){
                BigDecimal currentAmount = null;
                currentAmount = currentWallet.getAmount().subtract(product.getPrice());
                currentWallet.setAmount(currentAmount);
                shoppingObject.getProduct().setPrice(product.getPrice());
                shoppingObject.setOrderStatus(OrderStatus.ORDER_COMPLETED);
                shoppingObject.setEventDate(new Date());

                mongoTemplate.save(currentWallet, WALLET);
                shoppingService.save(shoppingObject);

                return true;
            }
        }

        return false;

    }

}
