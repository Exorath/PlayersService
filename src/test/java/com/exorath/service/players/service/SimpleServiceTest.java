/*
 * Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.players.service;

import com.exorath.service.players.Player;
import com.exorath.service.players.Success;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleServiceTest {

    private SimpleService service;
    private MemDataProvider memDataProvider;

    @Before
    public void setup() {
        memDataProvider = new MemDataProvider();
        service = new SimpleService(memDataProvider);
    }

    @Test
    public void testGetNoneExistOnlineFalse() {
        Player player = service.getPlayer("abc");
        assertFalse(player.isOnline());
    }

    @Test
    public void testGetPlayerReturnsOnlinePlayerIfDatabaseEntryOnlinePresent() {
        memDataProvider.getHashMap().put("uuid-fake", new Player(true, "uuid-fake", "username", "server", 0L, 0L, System.currentTimeMillis() + 100000L));

        Player player = service.getPlayer("uuid-fake");
        assertTrue(player.isOnline());
    }

    @Test
    public void testUpdateOnlineWithLeaveTimeReturnsErrorSuccess() {
        Player player = new Player(true, "uuid-fake", "username", "server", 10000L, 10000L, System.currentTimeMillis() + 100000);
        Success success = service.updatePlayer(player);
        assertFalse(success.getSuccess());
        assertNotNull(success.getError());
    }

    @Test
    public void testGetAfterPlayerExpireFromOnlineToOffline() {
        Player putPlayer = new Player(true, "uuid-fake", "username", "server", 10000L, null, System.currentTimeMillis() - 100000);
        memDataProvider.getHashMap().put("uuid-fake", putPlayer);

        Player getPlayer = service.getPlayer("uuid-fake");
        assertFalse(getPlayer.isOnline());
    }

    @Test
    public void testGetAfterPlayerExpireServerRemoved() {
        Player putPlayer = new Player(true, "uuid-fake", "username", "server", 10000L, null, System.currentTimeMillis() - 100000);
        memDataProvider.getHashMap().put("uuid-fake", putPlayer);

        Player getPlayer = service.getPlayer("uuid-fake");
        assertNull(getPlayer.getServerId());
    }

}
