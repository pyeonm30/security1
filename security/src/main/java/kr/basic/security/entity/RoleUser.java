package kr.basic.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleUser {
    USER("user"),ADMIN("admin"),MANAGER("manager");
    private final String role;

}
