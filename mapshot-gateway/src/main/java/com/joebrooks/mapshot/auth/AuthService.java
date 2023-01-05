package com.joebrooks.mapshot.auth;

//@Service
//@Transactional
//@RequiredArgsConstructor
//public class AuthService implements UserDetailsService {
//
//    private static final String NO_EXIST_USER = "[ERROR] 존재하지 않는 유저: ";
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        return userRepository.findByUserName(username).orElseThrow(() -> {
//            throw new AuthException(NO_EXIST_USER + username);
//        });
//    }
//}