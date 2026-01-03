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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.swarmcom.jsynapse.TestBase;
import org.swarmcom.jsynapse.domain.Room;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

class RoomServiceTest extends TestBase {
    @MockBean
    RoomUtils utils;

    @Autowired
    RoomServiceImpl roomService;

    Room createdRoom;

    @Override
    @BeforeEach
    public void setup() {
        super.setup();
        when(utils.generateRoomId()).thenReturn("abcdef0123456789:swarmcom.org");
        Room room = new Room();
        room.setName("Test room");
        room.setAlias("Room alias");
        room.setTopic("Room topic");
        createdRoom = roomService.createRoom(room);
    }

    @Test
    void createRoom() {
        assertEquals("abcdef0123456789:swarmcom.org", createdRoom.getRoomId());
    }

    @Test
    void findRoomByAlias() {
        Room foundRoom = roomService.findRoomByAlias("Room alias");
        assertEquals("Room alias", foundRoom.getAlias());
    }


    @Test
    void deleteAlias() {
        roomService.deleteAlias("abcdef0123456789:swarmcom.org", "Room alias");
        Room room = roomService.findRoomById("abcdef0123456789:swarmcom.org");
        assertEquals("", room.getAlias());
    }

    @Test
    void findRoomById() {
        Room room = roomService.findRoomById("abcdef0123456789:swarmcom.org");
        assertEquals("abcdef0123456789:swarmcom.org", room.getRoomId());
    }

    @Test
    void saveTopic() {
        Room room = roomService.saveTopic("abcdef0123456789:swarmcom.org", "Room Topic");
        assertEquals("Room Topic", room.getTopic());
    }

    @Test
    void saveName() {
        Room room = roomService.saveName("abcdef0123456789:swarmcom.org", "New Test room");
        assertEquals("New Test room", room.getName());
    }

}
