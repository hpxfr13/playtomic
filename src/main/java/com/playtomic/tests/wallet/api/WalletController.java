package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.http.Product;
import com.playtomic.tests.wallet.model.http.ShoppingObject;
import com.playtomic.tests.wallet.model.http.WalletRequest;
import com.playtomic.tests.wallet.model.http.ResponseErrorObject;
import com.playtomic.tests.wallet.service.ShoppingService;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);
    private final StripeService stripeService;
    private final WalletService walletService;
    private final ShoppingService shoppingService;

    public WalletController(StripeService stripeService, WalletService walletService, ShoppingService shoppingService) {
        this.stripeService = stripeService;
        this.walletService = walletService;
        this.shoppingService = shoppingService;
    }


    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }


    @PostMapping("/charge")
    ResponseEntity charge(@RequestBody WalletRequest walletRequest) {
        try {
            stripeService.charge(walletRequest.getCreditCardNumber(), walletRequest.getAmount());
            walletService.charge(walletRequest);
            return new ResponseEntity(walletRequest,HttpStatus.OK);
        }catch (Exception e){
            if (e instanceof StripeAmountTooSmallException){
                return new ResponseEntity(ResponseErrorObject.builder()
                        .message("You can not charge amount above 10€.")
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()),HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        return new ResponseEntity(null,HttpStatus.OK);
    }

    @PostMapping("/shopping")
    ResponseEntity shopping(@RequestBody ShoppingObject shoppingObject){
        return new ResponseEntity(walletService.shopping(shoppingObject),HttpStatus.OK);
    }

    @PostMapping("/refund")
    ResponseEntity refund(@RequestBody ShoppingObject shoppingObject){
        try{
            stripeService.refund(shoppingObject.getId());
            walletService.refund(shoppingObject);
            return new ResponseEntity(shoppingObject,HttpStatus.OK);

        }catch (Exception e){
        }
        return new ResponseEntity(null,HttpStatus.OK);

    }

    @GetMapping("/findShoppingHistory")
    ResponseEntity findShoppingHistory(){
        return new ResponseEntity(shoppingService.findAllShoppingHistory(), HttpStatus.OK);
    }


}
