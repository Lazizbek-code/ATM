package uz.pdp.appatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.appatm.entity.*;
import uz.pdp.appatm.entity.enums.RoleName;
import uz.pdp.appatm.payload.ApiResponse;
import uz.pdp.appatm.repositpry.*;

import java.util.*;

@Transactional
@Service
public class MoneyTransactionService {
    @Autowired
    AtmMachineRepository atmMachineRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AtmMachineHistoryRepository atmMachineHistoryRepository;


    /**
     * GIVE CASH FROM ATM MACHINE
     *
     * @param amount
     * @param id
     * @return ApiResponse in ResponseEntity
     */
    public ApiResponse getCash(Integer amount, Integer id) {
        // sistamadagi cardni ushlab oldim
        Card cardInSystem = (Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Card> optionalCard = cardRepository.findById(cardInSystem.getId());
        if (!optionalCard.isPresent()) {
            return new ApiResponse("Card not found", false);
        }
        Card card = optionalCard.get();

        // bankomatni id bo'yicha topib keldim
        Optional<AtmMachine> optionalAtmMachine = atmMachineRepository.findById(id);
        if (optionalAtmMachine.isEmpty()) {
            return new ApiResponse("Atm machine not found", false);
        }
        AtmMachine atmMachine = optionalAtmMachine.get();

        // card type va currency tog'riligiga tekshirdim
        if (card.getCardType().equals(atmMachine.getCardType()) &&
                card.getCurrency().equals(atmMachine.getCurrency())) {

            // comissiya qoydim
            double commission = 0.01;
            // agar card va bankomat bir bankga tegishli bo'lsa commissiya yoq
            if (card.getBank().equals(atmMachine.getBank())) {
                commission = 0.0;
            }

            // card ni mablag'ini tekchirdim
            if (!(card.getBalance() >= amount + (amount * commission))) {
                return new ApiResponse("Crad da yetarlicha mablag' yoq", false);
            }

            // bankomatni mablag'ini tekshirdim
            if (!(atmMachine.getMoneyAmount() >= amount)) {
                return new ApiResponse("Bankomatda yetarlicha mablag' yoq", false);
            }

            // cardni muddatini tekshirdim
            if (!card.getExpireDate().after(new Date())) {
                card.setEnabled(false);
                cardRepository.save(card);
                return new ApiResponse("Card is expired", false);
            }

            // maximal pul yechib olish summasidan o'tmaganligigga tekshirdim
            if (!(amount <= atmMachine.getMaxCashTakeOff())) {
                return new ApiResponse("Maximal qiymatdan o'tgan", false);
            }

            // pulni bankomatdan banknota bo'yicha yechib oldim
            int money = 0;

            while (amount != 0) {
                for (Banknote banknote : atmMachine.getBanknoteAmount()) {
                    if (banknote.getBanknoteValue().equals(amount)) {
                        banknote.setAmount(banknote.getAmount() - amount);
                        amount = money;
                        money = 0;
                    }
                }

                amount--;
                money++;
            }

            AtmMachine savedAtmMachine = atmMachineRepository.save(atmMachine);

            // transaksiyani amalga oshirdim
            card.setBalance(card.getBalance() - amount + amount * commission);
            cardRepository.save(card);

            // save amt history
            AtmMachineHistory atmMachineHistory = new AtmMachineHistory();
            atmMachineHistory.setMoneyOut(true);
            atmMachineHistory.setAmount(amount);
            atmMachineHistory.setCurrency(card.getCurrency());
            atmMachineHistory.setAtmMachine(atmMachine);
            atmMachineHistoryRepository.save(atmMachineHistory);

            // agar bankomatda emailga habar yuboradi
            if (savedAtmMachine.getMoneyAmount() <= atmMachine.getMinCash()) {
                for (User user : userRepository.findAll()) {
                    for (Role role : user.getRole()) {
                        if (role.getRoleName().equals(RoleName.MONEY_SUPPLIER)) {
                            sendEmail(user.getEmail(), atmMachine.getId());
                        }
                    }
                }
            }
            return new ApiResponse("Success", true);
        } else {
            return new ApiResponse("Error", false);
        }
    }


    /**
     * ADD MONEY TO CARD
     *
     * @param banknote
     * @param id
     * @return ApiResponse in ResponseEntity
     */
    public ApiResponse addMoney(Set<Banknote> banknote, Integer id) {
        try {
            Card cardInSystem = (Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Card> optionalCard = cardRepository.findById(cardInSystem.getId());
            if (!optionalCard.isPresent()) {
                return new ApiResponse("Card not found", false);
            }
            Card card = optionalCard.get();

            Optional<AtmMachine> optionalAtmMachine = atmMachineRepository.findById(id);
            if (optionalAtmMachine.isEmpty()) {
                return new ApiResponse("Atm machine not found", false);
            }
            AtmMachine atmMachine = optionalAtmMachine.get();
            int money = 0;
            boolean banknoteNotFound = true;

            for (Banknote banknotes : banknote) {
                money = money + banknotes.getBanknoteValue() * banknotes.getAmount();
                for (Banknote banknoteAmount : atmMachine.getBanknoteAmount()) {
                    if (banknotes.getBanknoteValue().equals(banknoteAmount.getBanknoteValue())) {
                        banknoteAmount.setAmount(banknotes.getAmount());
                        banknoteNotFound = false;
                    }

                    if (banknoteNotFound) {
                        return new ApiResponse("Banknote not found", false);
                    }
                }
            }

            card.setBalance(cardInSystem.getBalance() + money);
            cardRepository.save(card);

            // save atm history
            AtmMachineHistory atmMachineHistory = new AtmMachineHistory();
            atmMachineHistory.setMoneyOut(false);
            atmMachineHistory.setAmount(money);
            atmMachineHistory.setCurrency(card.getCurrency());
            atmMachineHistory.setAtmMachine(atmMachine);
            atmMachineHistoryRepository.save(atmMachineHistory);

            return new ApiResponse("Success", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }


    // send message to user email
    public void sendEmail(String sendingEmail, Integer id) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("email.sender.hr@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Pul kam qoldi");
            simpleMailMessage.setText(id + " - bankomatda");
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
