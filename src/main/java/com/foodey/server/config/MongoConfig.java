package com.foodey.server.config;

import com.foodey.server.user.model.converter.AAGUIDReadingConverter;
import com.foodey.server.user.model.converter.AAGUIDWritingConverter;
import com.foodey.server.user.model.converter.AttestationStatementReadingConverter;
import com.foodey.server.user.model.converter.AttestationStatementWritingConverter;
import com.foodey.server.user.model.converter.AuthenticatorExtensionsReadingConverter;
import com.foodey.server.user.model.converter.AuthenticatorExtensionsWritingConverter;
import com.foodey.server.user.model.converter.AuthenticatorTransportReadingConverter;
import com.foodey.server.user.model.converter.AuthenticatorTransportWritingConverter;
import com.foodey.server.user.model.converter.COSEKeyReadingConverter;
import com.foodey.server.user.model.converter.COSEKeyWritingConverter;
import com.foodey.server.user.model.converter.ClientExtensionsReadingConverter;
import com.foodey.server.user.model.converter.ClientExtensionsWritingConverter;
import com.foodey.server.user.model.converter.CollectedClientDataReadingConverter;
import com.foodey.server.user.model.converter.CollectedClientDataWritingConverter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfig {
  private final AAGUIDReadingConverter aaguidReadingConverter;
  private final AAGUIDWritingConverter aaguidWritingConverter;
  private final AttestationStatementWritingConverter attestationStatementWritingConverter;
  private final AttestationStatementReadingConverter attestationStatementReadingConverter;
  private final AuthenticatorExtensionsReadingConverter authenticatorExtensionsReadingConverter;
  private final AuthenticatorExtensionsWritingConverter authenticatorExtensionsWritingConverter;
  private final AuthenticatorTransportReadingConverter authenticatorTransportReadingConverter;
  private final AuthenticatorTransportWritingConverter authenticatorTransportWritingConverter;
  private final COSEKeyReadingConverter coseKeyReadingConverter;
  private final COSEKeyWritingConverter coseKeyWritingConverter;
  private final ClientExtensionsReadingConverter clientExtensionsReadingConverter;
  private final ClientExtensionsWritingConverter clientExtensionsWritingConverter;
  private final CollectedClientDataReadingConverter collectedClientDataReadingConverter;
  private final CollectedClientDataWritingConverter collectedClientDataWritingConverter;

  /*   private final CredentialPropertiesExtensionClientOutputReadingConverter */
  /*       credentialPropertiesExtensionClientOutputReadingConverter; */
  // private final CredentialPropertiesExtensionClientOutputWritingConverter
  //     credentialPropertiesExtensionClientOutputWritingConverter;

  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(
        Arrays.asList(
            aaguidReadingConverter,
            aaguidWritingConverter,
            attestationStatementWritingConverter,
            attestationStatementReadingConverter,
            authenticatorExtensionsReadingConverter,
            authenticatorExtensionsWritingConverter,
            authenticatorTransportReadingConverter,
            authenticatorTransportWritingConverter,
            coseKeyReadingConverter,
            coseKeyWritingConverter,
            clientExtensionsReadingConverter,
            clientExtensionsWritingConverter,
            collectedClientDataReadingConverter,
            collectedClientDataWritingConverter
            // credentialPropertiesExtensionClientOutputReadingConverter,
            // credentialPropertiesExtensionClientOutputWritingConverter));
            ));
  }

  @Bean
  public MappingMongoConverter mongoConverter(
      MongoDatabaseFactory mongoFactory, MongoMappingContext mongoMappingContext) throws Exception {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
    MappingMongoConverter mongoConverter =
        new MappingMongoConverter(dbRefResolver, mongoMappingContext);
    mongoConverter.setMapKeyDotReplacement(".");

    return mongoConverter;
  }
}
