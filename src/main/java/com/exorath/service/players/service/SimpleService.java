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
import com.exorath.service.players.Service;

/**
 * Created by minez on 04/11/2016.
 */
public class SimpleService implements Service {

    private DatabaseProvider provider;

    public SimpleService(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public Player getPlayer(String uuid) {
        Player player = provider.getPlayer(uuid);
        if(player == null) {
            player = new Player();
            player.setOnline(false);
        }
        if (player.isOnline() == null)
            player.setOnline(false);
        if(player.getExpire() != null && player.getExpire() < System.currentTimeMillis()) {
            player.setOnline(false);
            player.setLeaveTime(player.getExpire());
            player.setJoinTime(null);
            player.setServerId(null);
        }
        return player;
    }

    @Override
    public Success updatePlayer(Player player) {
        if(player.isOnline() == null) {
            return new Success(false, "online is a required field");
        }
        if(player.getUuid() == null) {
            return new Success(false, "uuid is a required field");
        }
        if(player.getExpire() == null) {
            return new Success(false, "expire is a required field");
        }

        if(player.isOnline() && player.getLeaveTime() != null) {
            return new Success(false, "online=true, leaveTime set");
        }
        if(!player.isOnline() && player.getJoinTime() != null) {
            return new Success(false, "online=false, joinTime set");
        }
        return provider.updatePlayer(player);
    }
}
