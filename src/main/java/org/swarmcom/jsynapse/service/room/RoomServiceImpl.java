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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.swarmcom.jsynapse.dao.RoomRepository;
import org.swarmcom.jsynapse.domain.Room;
import org.swarmcom.jsynapse.domain.RoomAlias;
import org.swarmcom.jsynapse.service.exception.EntityNotFoundException;
import org.swarmcom.jsynapse.service.exception.InvalidRequestException;

import static java.lang.String.format;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Service
@Validated
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomAliasService roomAliasService;
    private final RoomUtils utils;

    @Override
    public Room createRoom(@NotNull @Valid final Room room) {
        String roomId = utils.generateRoomId();
        room.setRoomId(roomId);
        String initialAlias = room.getAlias();
        if (!StringUtils.isEmpty(initialAlias)) {
            roomAliasService.createAlias(roomId, room.getAlias());
        }
        return roomRepository.save(room);
    }

    @Override
    public Room findRoomByAlias(String alias) {
        RoomAlias.AliasServers roomAlias = roomAliasService.findByAlias(alias);
        Room room = roomRepository.findRoomByRoomId(roomAlias.getRoomId());
        if (null == room) {
            throw new EntityNotFoundException(format("Room with alias %s not found on this homeserver", alias));
        }
        return room;
    }

    @Override
    public void deleteAlias(String roomId, String alias) {
        roomAliasService.deleteAlias(alias);
        Room room = findRoomById(roomId);
        if (StringUtils.equals(room.getAlias(), alias)) {
            room.setAlias("");
            roomRepository.save(room);
        }
    }

    @Override
    public Room findRoomById(String roomId) {
        Room room = roomRepository.findRoomByRoomId(roomId);
        if (null == room) {
            throw new EntityNotFoundException(format("Room with id %s not found on this homeserver", roomId));
        }
        return room;
    }

    @Override
    public Room saveTopic(String roomId, String topic) {
        if (null == topic) {
            throw new InvalidRequestException("Topic to set is null");
        }
        Room room = findRoomById(roomId);
        room.setTopic(topic);
        return roomRepository.save(room);
    }

    @Override
    public Room saveName(String roomId, String name) {
        if (null == name) {
            throw new InvalidRequestException("Name to set is null");
        }
        Room room = findRoomById(roomId);
        room.setName(name);
        return roomRepository.save(room);
    }
}
