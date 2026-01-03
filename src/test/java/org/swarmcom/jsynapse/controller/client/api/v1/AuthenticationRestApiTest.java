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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
package org.swarmcom.jsynapse.controller.client.api.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.swarmcom.jsynapse.TestBase;
import org.swarmcom.jsynapse.service.user.UserUtils;

import static org.mockito.Mockito.when;

class AuthenticationRestApiTest extends TestBase {

    @Value("classpath:auth/GetAuthSchemas.json")
    private Resource getLoginSchemas;

    @Value("classpath:auth/PostLogin.json")
    private Resource postLoginRequest;

    @Value("classpath:auth/PostLoginResponse.json")
    private Resource postLoginResponse;

    @Value("classpath:auth/PostMissingKeys.json")
    private Resource postMissingKeys;

    @Value("classpath:auth/PostUnknownSchema.json")
    private Resource postUnknownSchema;

    @MockBean
    UserUtils utils;

    @Override
    @BeforeEach
    public void setup() {
        super.setup();
        when(utils.generateUserId("user_id")).thenReturn("@user:swarmcom.org");
        when(utils.generateAccessToken()).thenReturn("abcdef0123456789");
    }

    @Test
    void testGetSchemas() throws Exception {
        getAndCompareResult("/_matrix/client/api/v1/login", getLoginSchemas);
        getAndCompareResult("/_matrix/client/api/v1/register", getLoginSchemas);
    }

    @Test
    void testUnknownSchema() throws Exception {
        postAndCheckStatus("/_matrix/client/api/v1/login", postUnknownSchema, HttpStatus.BAD_REQUEST);
        postAndCheckStatus("/_matrix/client/api/v1/register", postUnknownSchema, HttpStatus.BAD_REQUEST);
    }

    @Test
    void testMissingKeys() throws Exception {
        postAndCheckStatus("/_matrix/client/api/v1/register", postMissingKeys, HttpStatus.BAD_REQUEST);
        postAndCheckStatus("/_matrix/client/api/v1/login", postMissingKeys, HttpStatus.BAD_REQUEST);
    }

    @Test
    void testRegisterAndLoginPassword() throws Exception {
        // login with user not registered
        postAndCheckStatus("/_matrix/client/api/v1/login", postLoginRequest, HttpStatus.NOT_FOUND);

        // register
        postAndCompareResult("/_matrix/client/api/v1/register", postLoginRequest, postLoginResponse);
        // login
        postAndCompareResult("/_matrix/client/api/v1/login", postLoginRequest, postLoginResponse);

        // register with an existing user
        postAndCheckStatus("/_matrix/client/api/v1/register", postLoginRequest, HttpStatus.CONFLICT);
    }

}
