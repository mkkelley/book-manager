package net.minthe.bookmanager.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** Created by Michael Kelley on 8/29/2020 */
@Service
public class AuthService {

  public String getUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  public boolean isAdmin() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
  }
}
