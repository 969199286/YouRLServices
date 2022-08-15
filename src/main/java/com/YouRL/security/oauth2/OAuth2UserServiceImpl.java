package com.YouRL.security.oauth2;

import com.YouRL.enums.AuthProvider;
import com.YouRL.entity.Role;
import com.YouRL.entity.RoleName;
import com.YouRL.entity.User;
import com.YouRL.repository.RoleRepository;
import com.YouRL.repository.UserRepository;
import com.YouRL.security.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.naming.AuthenticationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class OAuth2UserServiceImpl extends OidcUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("load Oidc User");
        OidcUser oidcUser = super.loadUser(userRequest);
        log.info(oidcUser.getAttributes().toString());
        try{
            return process(userRequest, oidcUser);
        } catch (AuthenticationException authenticationException) {
            throw new InternalAuthenticationServiceException(
                    authenticationException.getMessage(),
                    authenticationException.getCause()
            );
        }
    }

    public OidcUser process(OidcUserRequest request, OidcUser oidcUser) throws AuthenticationException {
        log.info("process");
        if(!StringUtils.hasText(oidcUser.getEmail())){
            throw new AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmailAndProvider(
                oidcUser.getEmail(),
                AuthProvider.valueOf(request.getClientRegistration().getRegistrationId().toUpperCase()));
        User user = userOptional.orElseGet(() -> register(request, oidcUser));
        log.info("ggss");
        return UserDetailsImpl.create(user, oidcUser);
    }

    public User register(OidcUserRequest request, OidcUser oidcUser) {
        log.info("register");
        User user = new User();

        user.setProvider(
                AuthProvider.valueOf(request.getClientRegistration().getRegistrationId().toUpperCase())
        );
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setUsername(oidcUser.getName());
        user.setEmail(oidcUser.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        return userRepository.save(user);
    }
}