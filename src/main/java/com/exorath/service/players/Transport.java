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

import com.exorath.service.commons.portProvider.PortProvider;
import com.google.gson.Gson;
import spark.Route;

import static spark.Spark.*;

/**
 * Created by toonsev on 11/3/2016.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());
        get("/players/:uuid", Transport.getGetPlayerRoute(service), GSON::toJson);
        put("/players/:uuid", Transport.getUpdatePlayerRoute(service), GSON::toJson);
    }

    public static Route getGetPlayerRoute(Service service) {
        return (req, res) -> service.getPlayer(req.params("uuid"));
    }

    public static Route getUpdatePlayerRoute(Service service) {
        return (req, res) -> {
            Player player = GSON.fromJson(req.body(), Player.class);
            player.setUuid(req.params("uuid"));
            return service.updatePlayer(player);
        };
    }
}
