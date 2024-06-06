// package com.foodey.server.user.service.impl;

// import com.foodey.server.user.model.User;
// import com.foodey.server.user.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsPasswordService;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

//   private final UserRepository userRepository;

//   @Override
//   public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
//     assert userDetails instanceof User;

//     User user = (User) userDetails;
//     user.setPassword(newPassword);
//     userRepository.save(user);
//     return user;
//   }
// }
