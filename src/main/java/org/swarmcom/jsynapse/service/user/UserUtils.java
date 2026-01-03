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
package org.swarmcom.jsynapse.service.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.RandomStringUtils.secureStrong;
import static org.apache.commons.lang3.StringUtils.join;

@Component
public class UserUtils {
    @Value("${jsynapse.domain}")
    private String domain;

    public String generateUserId(String userIdOrLocalPart) {
        String userIdRegular = String.format("^@\\S*:%s$", StringUtils.replace(domain, ".", "\\."));
        Pattern p = Pattern.compile(userIdRegular);
        Matcher m = p.matcher(userIdOrLocalPart);

        return m.matches() ? userIdOrLocalPart : join("@", userIdOrLocalPart, ":", domain);

    }

    public String generateAccessToken() {
        return secureStrong().next(16, true, false);
    }
}
