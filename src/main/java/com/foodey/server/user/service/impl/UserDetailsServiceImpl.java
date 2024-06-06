package com.foodey.server.user.service.impl;

import com.foodey.server.user.model.User;
import com.foodey.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    User account =
        userRepository
            .findByPhoneNumber(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + username));

    log.info("Fetch account by phone number: {}", account);

    return account;
  }
}
