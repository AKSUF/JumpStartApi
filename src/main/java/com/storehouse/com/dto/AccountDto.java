package com.storehouse.com.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto {
private Long account_id;
@NotNull
@Email
private String email;
private String provider;

private UserDto user;
private List<UserRoleDto> userRoles;
}
