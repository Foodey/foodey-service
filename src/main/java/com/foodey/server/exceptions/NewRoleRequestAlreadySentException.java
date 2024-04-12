package com.foodey.server.exceptions;

import com.foodey.server.user.enums.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class NewRoleRequestAlreadySentException extends HttpException {

  private RoleType role;

  public NewRoleRequestAlreadySentException(RoleType role) {
    super(HttpStatus.ALREADY_REPORTED, "New " + role.name() + " request already sent");
    this.role = role;
  }
}
