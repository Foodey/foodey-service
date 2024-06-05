package com.foodey.server.user.model.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import java.util.Objects;

public class AttestationStatementSerializationContainer {

  @JsonProperty("attStmt")
  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
      property = "fmt")
  private final AttestationStatement attestationStatement;

  @JsonCreator
  public AttestationStatementSerializationContainer(
      @JsonProperty("attStmt") AttestationStatement attestationStatement) {
    this.attestationStatement = attestationStatement;
  }

  @JsonProperty("fmt")
  public String getFormat() {
    return attestationStatement.getFormat();
  }

  public AttestationStatement getAttestationStatement() {
    return attestationStatement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AttestationStatementSerializationContainer that =
        (AttestationStatementSerializationContainer) o;
    return Objects.equals(attestationStatement, that.attestationStatement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attestationStatement);
  }
}
