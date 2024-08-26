package com.krstudy.kapi.global.Security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class SecurityUser(
    val id: Long,
    userid:String,
    username:String,
    password: String,
    authorities: Collection<out GrantedAuthority>,
    enabled: Boolean = true,
    accountNonExpired: Boolean = true,
    credentialsNonExpired: Boolean = true,
    accountNonLocked: Boolean = true
) : User(userid,password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities) { }
