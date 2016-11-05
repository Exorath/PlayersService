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

public interface Service {

    /**
     * Get player from uuid
     *
     * Exceptions:
     *   Database unreachable
     *
     * @param uuid UUID of player
     * @return Player object
     */
    Player getPlayer(String uuid);

    /**
     * Update player
     *
     * Exceptions:
     *   Setting online=false and joinTime at the same time - Success=false, error=Online set to false and join time set at same time!
     *   Setting online=true and leaveTime at the same time - Success=false, error=Online set to true and leave time set at same time!
     *
     * @param player Player object
     * @return Returns seccess object
     */
    Success updatePlayer(Player player);

}
