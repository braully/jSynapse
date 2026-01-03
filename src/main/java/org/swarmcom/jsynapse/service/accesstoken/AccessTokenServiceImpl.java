/*
 * (C) Copyright 2015 eZuce Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
package org.swarmcom.jsynapse.service.accesstoken;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.swarmcom.jsynapse.dao.AccessTokenRepository;
import org.swarmcom.jsynapse.domain.AccessToken;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class AccessTokenServiceImpl implements AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;

    @Value("${token.expire.seconds:300}")
    private long expire;

    @Inject
    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * Limpa tokens expirados periodicamente (executa a cada 5 minutos)
     * Substitui o índice TTL do MongoDB
     */
    @Scheduled(fixedDelay = 300000) // 5 minutos
    public void cleanExpiredTokens() {
        long expirationTime = System.currentTimeMillis() - (expire * 1000);
        Date expirationDate = new Date(expirationTime);
        List<AccessToken> tokens = accessTokenRepository.findAll();
        tokens.stream()
                .filter(token -> token.getLastUsed().before(expirationDate))
                .forEach(accessTokenRepository::delete);
    }

    @Override
    public AccessToken createOrUpdateToken(@NotNull @Valid AccessToken accessToken) {
        AccessToken existingToken = accessTokenRepository.findByUserId(accessToken.getUserId());
        if (existingToken == null) {
            return accessTokenRepository.save(accessToken);
        } else {
            existingToken.setToken(accessToken.getToken());
            existingToken.setLastUsed(accessToken.getLastUsed());
            return accessTokenRepository.save(existingToken);
        }
    }

    @Override
    public boolean isTokenAssigned(@NotNull String userId, @NotNull String token) {
        AccessToken accessToken = accessTokenRepository.findByToken(token);
        return accessToken != null ? StringUtils.equals(userId, accessToken.getUserId()) : false;
    }
}
