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

package com.exorath.service.players;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toonsev on 11/3/2016.
 */
public class Player {

    // Required
    private Boolean online;
    private String uuid;

    // Optional
    @SerializedName("sid")
    private String serverId;
    private Long joinTime;
    private Long leaveTime;
    private String username;

    // GET - Optional, PUT - Required
    private Long expire;

    public Player(Boolean online, String uuid, String username, String serverId, Long joinTime, Long leaveTime, Long expire) {
        this.online = online;
        this.uuid = uuid;
        this.username = username;
        this.serverId = serverId;
        this.joinTime = joinTime;
        this.leaveTime = leaveTime;
        this.expire = expire;
    }

    public Player() {
    }

    public Boolean isOnline() {
        return online;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Long joinTime) {
        this.joinTime = joinTime;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
