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
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Getter
@Setter
public class Room {
    @Id
    private String id;

    @JsonProperty("room_id")
    @JsonView({CreateSummary.class, DirectorySummary.class})
    @Indexed
    private String roomId;

    @NotNull
    @JsonView(NameSummary.class)
    private String name;

    @JsonProperty
    private String visibility = "private";

    @JsonProperty("room_alias_name")
    @JsonView(CreateSummary.class)
    private String alias;

    @JsonProperty
    @JsonView(TopicSummary.class)
    private String topic;

    @JsonProperty
    private List<String> invite;

    public interface CreateSummary {}

    public interface DirectorySummary {}

    public interface TopicSummary {}

    public interface NameSummary {}
}
