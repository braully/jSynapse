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
package org.swarmcom.jsynapse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.Indexed;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity

public class Message {

    @Id
    private String id;

    @Setter
    @Getter
    @NotNull
    @JsonProperty
    private String msgtype;

    @Setter
    @Getter
//    @Indexed
    private String roomId;

    @Setter
    @Getter
    private String body;

    @Getter
    public static class Messages {

        private final List<MessageSummary> chunk = new LinkedList<>();

        public Messages(List<Message> messages) {
            for (Message message : messages) {
                chunk.add(new MessageSummary(message));
            }
        }

    }

    @Getter
    public static class MessageSummary {

        public static final String MSG_TYPE = "msgtype";
        public static final String BODY = "body";

        @JsonProperty
        private final Map<String, String> content = new LinkedHashMap<>();

        public MessageSummary(Message message) {
            content.put(MSG_TYPE, message.getMsgtype());
            if (StringUtils.isNotBlank(message.getBody())) {
                content.put(BODY, message.getBody());
            }
        }

    }
}
