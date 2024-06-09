package com.light.backend.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentMemberGetter {
    public String getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (String) authentication.getPrincipal();
    }
}
