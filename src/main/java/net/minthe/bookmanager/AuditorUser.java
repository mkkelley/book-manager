package net.minthe.bookmanager;

import java.util.Optional;
import net.minthe.bookmanager.models.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorUser implements AuditorAware<User> {
  @Override
  public Optional<User> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var springUser =
        (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    var user = new User();
    user.setUsername(springUser.getUsername());
    return Optional.of(user);
  }
}
