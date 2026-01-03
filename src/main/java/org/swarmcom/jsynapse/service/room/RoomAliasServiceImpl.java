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
package org.swarmcom.jsynapse.service.room;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.swarmcom.jsynapse.dao.RoomAliasRepository;
import org.swarmcom.jsynapse.domain.RoomAlias;
import org.swarmcom.jsynapse.service.exception.EntityAlreadyExistsException;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Service
@Validated
public class RoomAliasServiceImpl implements RoomAliasService {
    @Value("${jsynapse.domain}")
    private String domain;

    private final RoomAliasRepository roomAliasRepository;

    public RoomAlias createAlias(String roomId, String alias) {
        RoomAlias roomAlias = new RoomAlias(roomId, alias, domain);
        RoomAlias existingRoomAlias = roomAliasRepository.findByAliasAndServer(alias, domain);
        if (existingRoomAlias == null) {
            return roomAliasRepository.save(roomAlias);
        } else {
            throw new EntityAlreadyExistsException("Room alias already taken");
        }
    }

    public void deleteAlias(String alias) {
        RoomAlias roomAlias = roomAliasRepository.findByAliasAndServer(alias, domain);
        roomAliasRepository.delete(roomAlias);
    }

    public RoomAlias.AliasServers findByAlias(String alias) {
        List<RoomAlias> roomAliases = roomAliasRepository.findByAlias(alias);
        String roomId = StringUtils.EMPTY;
        if(!CollectionUtils.isEmpty(roomAliases)) {
            roomId = roomAliases.getFirst().getRoomId();
        }

        return new RoomAlias.AliasServers(roomId, roomAliases);
    }

    public RoomAlias.RoomAliases findByRoomId(String roomId) {
        List<RoomAlias> aliases = roomAliasRepository.findByRoomId(roomId);
        return new RoomAlias.RoomAliases(aliases);
    }
}
