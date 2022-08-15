package uz.pdp.appatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatm.entity.Banknote;
import uz.pdp.appatm.payload.ApiResponse;
import uz.pdp.appatm.service.MoneyTransactionService;

import java.util.Set;

@RestController
@RequestMapping("/api/transaction")
public class MoneyTransactionController {
    @Autowired
    MoneyTransactionService moneyTransactionService;


    /**
     * GIVE CASH FROM ATM MACHINE
     *
     * @param amount
     * @param id
     * @return ApiResponse in ResponseEntity
     */
    @PutMapping("/giveCash/{id}")
    public ResponseEntity<?> giveCash(@RequestBody Integer amount, @PathVariable Integer id) {
        ApiResponse apiResponse = moneyTransactionService.getCash(amount, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    /**
     * ADD MONEY TO CARD
     *
     * @param banknote
     * @param id
     * @return ApiResponse in ResponseEntity
     */
    @PutMapping("/addMoney/{id}")
    public ResponseEntity<?> addMoney(@RequestBody Set<Banknote> banknote, @PathVariable Integer id) {
        ApiResponse apiResponse = moneyTransactionService.addMoney(banknote, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
