package uz.pdp.appatm.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.appatm.entity.User;

import java.util.Optional;

public class SecurityAudit implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser"))) {
            return Optional.of(((User)authentication.getPrincipal()).getId());
        }
        return Optional.empty();
    }
}
