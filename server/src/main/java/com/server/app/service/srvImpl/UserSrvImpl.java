package com.server.app.service.srvImpl;

import com.server.app.repository.UserRepository;
import com.server.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSrvImpl implements UserService {
    private final UserRepository repository;

    @Override
    public void deleteUserByUsername(String username) {
        repository.deleteUserByUsername(username);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
