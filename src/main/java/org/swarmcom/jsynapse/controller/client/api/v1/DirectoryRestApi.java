package org.swarmcom.jsynapse.controller.client.api.v1;
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

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.swarmcom.jsynapse.controller.JsynapseApi;
import org.swarmcom.jsynapse.domain.Room;
import org.swarmcom.jsynapse.domain.RoomAlias;
import org.swarmcom.jsynapse.service.room.RoomAliasService;
import org.swarmcom.jsynapse.service.room.RoomService;

import static org.swarmcom.jsynapse.controller.JsynapseApi.CLIENT_V1_API;
import static org.swarmcom.jsynapse.domain.RoomAlias.AliasServers;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@RestController
@RequestMapping(value = CLIENT_V1_API + "/directory/room/{roomAlias}")
public class DirectoryRestApi extends JsynapseApi {
    private final RoomService roomService;
    private final RoomAliasService roomAliasService;

    @GetMapping
    public AliasServers getRoomByAlias(@PathVariable String roomAlias) {
        log.debug("Get room with alias {}", roomAlias);
        return roomAliasService.findByAlias(roomAlias);
    }

    @PutMapping
    public RoomAlias saveRoomAlias(@PathVariable String roomAlias, @RequestBody final Room room) {
        log.debug("PUT alias {} for room id {}", roomAlias, room.getRoomId());
        return roomAliasService.createAlias(room.getRoomId(), roomAlias);
    }

    @DeleteMapping
    public void deleteRoomAlias(@PathVariable String roomAlias, @RequestBody final Room room) {
        log.debug("DELETE alias {} for room id {}", roomAlias, room.getRoomId());
        roomService.deleteAlias(room.getRoomId(), roomAlias);
    }
}