package gov.samhsa.c2s.c2suiapi.service;

import gov.samhsa.c2s.c2suiapi.config.C2sUiApiProperties;
import gov.samhsa.c2s.c2suiapi.infrastructure.UaaClient;
import gov.samhsa.c2s.c2suiapi.service.dto.LoginRequestDto;
import gov.samhsa.c2s.c2suiapi.service.exception.AccountLockedException;
import gov.samhsa.c2s.c2suiapi.service.exception.BadCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UaaServiceImpl implements UaaService {
    private static final String OAUTH2_GRAND_TYPE = "password";
    private static final String OAUTH2_RESPONSE_TYPE = "token";
    private static final String BAD_CREDENTIAL_ERROR_MESSAGE = "Bad credentials";
    private static final String ACCOUNT_LOCKED_ERROR_MESSAGE = "Your account has been locked because of too many failed attempts to login";


    private final C2sUiApiProperties c2sUiApiProperties;
    private final UaaClient uaaClient;

    @Autowired
    public UaaServiceImpl(C2sUiApiProperties c2sUiApiProperties, UaaClient uaaClient) {
        this.c2sUiApiProperties = c2sUiApiProperties;
        this.uaaClient = uaaClient;
    }

    @Override
    public Object login(LoginRequestDto loginRequestDto) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("client_id", c2sUiApiProperties.getOauth2().getClient().getClientId());
        requestParams.put("client_secret", c2sUiApiProperties.getOauth2().getClient().getClientSecret());
        requestParams.put("grant_type", OAUTH2_GRAND_TYPE);
        requestParams.put("response_type", OAUTH2_RESPONSE_TYPE);
        requestParams.put("username", loginRequestDto.getUsername());
        requestParams.put("password", loginRequestDto.getPassword());

        try {
            return uaaClient.getTokenUsingPasswordGrant(requestParams);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String errorMessage = e.getCause().getMessage();

            if(errorMessage.contains(BAD_CREDENTIAL_ERROR_MESSAGE)){
                throw new BadCredentialsException();
            }else if(errorMessage.contains(ACCOUNT_LOCKED_ERROR_MESSAGE)){
                throw new AccountLockedException();
            }
        }
        return null;
    }
}
