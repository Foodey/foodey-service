package com.foodey.server.user.exceptions;

import com.foodey.server.exceptions.BaseException;
import com.foodey.server.user.enums.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NewRoleRequestAlreadySentException extends BaseException {

  private RoleType role;

  public NewRoleRequestAlreadySentException(RoleType role) {
    super(HttpStatus.ALREADY_REPORTED, "New " + role.name() + " request already sent");
    this.role = role;
  }
}
