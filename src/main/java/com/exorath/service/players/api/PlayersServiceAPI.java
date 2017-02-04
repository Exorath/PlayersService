package com.exorath.service.players.api;

import com.exorath.service.players.res.Player;
import com.exorath.service.players.res.Success;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;


/**
 * Created by toonsev on 2/4/2017.
 */
public class PlayersServiceAPI {
    private static final Gson GSON = new Gson();

    private String address;

    public PlayersServiceAPI(String address) {
        this.address = address;
    }

    /**
     * Get player from uuid
     * <p>
     * Exceptions:
     * Database unreachable
     *
     * @param uuid UUID of player
     * @return Player object
     */
    public Player getPlayer(String uuid) {
        try {
            String res = Unirest.get(url("/players/{uuid}"))
                    .routeParam("uuid", uuid).asString().getBody();
            return GSON.fromJson(res, Player.class);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update player
     * <p>
     * Exceptions:
     * Setting online=false and joinTime at the same time - Success=false, error=Online set to false and join time set at same time!
     * Setting online=true and leaveTime at the same time - Success=false, error=Online set to true and leave time set at same time!
     *
     * @param player Player object
     * @return Returns seccess object
     */
    public Success updatePlayer(Player player) {
        try {
            HttpRequestWithBody req = Unirest.put(url("/players/{uuid}"))
                    .routeParam("uuid", player.getUuid());
            player.setUuid(null);
            req.body(GSON.toJson(player));
            String res = req.asString().getBody();
            return GSON.fromJson(res, Success.class);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private String url(String endpoint) {
        return address + endpoint;
    }
}
