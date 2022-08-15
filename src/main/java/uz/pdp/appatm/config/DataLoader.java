package uz.pdp.appatm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.appatm.entity.Banknote;
import uz.pdp.appatm.entity.CardType;
import uz.pdp.appatm.entity.Currency;
import uz.pdp.appatm.entity.Role;
import uz.pdp.appatm.entity.enums.CardTypeName;
import uz.pdp.appatm.entity.enums.CurrencyName;
import uz.pdp.appatm.entity.enums.RoleName;
import uz.pdp.appatm.repositpry.BanknoteRepository;
import uz.pdp.appatm.repositpry.CardTypeRepository;
import uz.pdp.appatm.repositpry.CurrencyRepository;
import uz.pdp.appatm.repositpry.RoleRepository;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    CardTypeRepository cardTypeRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    BanknoteRepository banknoteRepository;
    @Autowired
    RoleRepository roleRepository;

    @Value(value = "${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            CardType humo = new CardType(CardTypeName.HUMO);
            cardTypeRepository.save(humo);

            CardType uzcard = new CardType(CardTypeName.UZCARD);
            cardTypeRepository.save(uzcard);

            CardType visa = new CardType(CardTypeName.VISA);
            cardTypeRepository.save(visa);



            Currency uzs = new Currency(CurrencyName.UZS);
            currencyRepository.save(uzs);

            Currency usd = new Currency(CurrencyName.USD);
            currencyRepository.save(usd);



            Role director = new Role(RoleName.DIRECTOR);
            roleRepository.save(director);

            Role moneySupplier = new Role(RoleName.MONEY_SUPPLIER);
            roleRepository.save(moneySupplier);

            Role client= new Role(RoleName.CLIENT);
            roleRepository.save(client);
        }
    }
}
