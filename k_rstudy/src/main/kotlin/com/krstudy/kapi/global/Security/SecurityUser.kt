package com.krstudy.kapi.com.krstudy.kapi.global.Security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.Collection

class SecurityUser(
    val id: Long,
    username: String,
    password: String,
    authorities: Collection<out GrantedAuthority>
) : User(username, password, authorities) {

    constructor(
        id: Long,
        username: String,
        password: String,
        enabled: Boolean,
        accountNonExpired: Boolean,
        credentialsNonExpired: Boolean,
        accountNonLocked: Boolean,
        authorities: Collection<out GrantedAuthority>
    ) : this(id, username, password, authorities) {
        // This constructor calls the primary constructor
        // and uses the `User` class's properties directly.
        super.setEnabled(enabled)
        super.setAccountNonExpired(accountNonExpired)
        super.setCredentialsNonExpired(credentialsNonExpired)
        super.setAccountNonLocked(accountNonLocked)
    }
}
