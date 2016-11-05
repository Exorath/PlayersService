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

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.exorath.service.commons.dynamoDBProvider.DynamoDBProvider;
import com.exorath.service.commons.tableNameProvider.TableNameProvider;
import com.exorath.service.players.res.Player;
import com.exorath.service.players.res.Success;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamoDatabaseProvider implements DatabaseProvider {

    private static final String ONLINE = "online", USERNAME = "username", SERVER_ID = "sid", JOIN_TIME = "joinTime", LEAVE_TIME = "leaveTime", EXPIRE = "expiry";

    private static final String PRIM_KEY = "uuid";
    private static final Logger logger = LoggerFactory.getLogger(DynamoDatabaseProvider.class);

    private DynamoDB database;
    private Table table;
    private String tableName;

    public DynamoDatabaseProvider(DynamoDBProvider dbProvider, TableNameProvider tableNameProvider) {
        this.database = dbProvider.getDB();
        this.tableName = tableNameProvider.getTableName();
        this.table = setupTables();
    }

    private Table setupTables() {
        Table table;
        try {
            table = database.createTable(new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(new KeySchemaElement(PRIM_KEY, KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition(PRIM_KEY, ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
            );
            logger.info("Created DynamoDB table " + tableName + " with 1r/1w provisioning. Waiting for it to activate.");
        } catch (ResourceInUseException ex) {
            table = database.getTable(tableName);
            logger.info("DynamoDB table " + tableName + " already existed. Waiting for it to activate.");
        }

        try {
            table.waitForActive();
        } catch (InterruptedException ex) {
            logger.error("DynamoDB table " + tableName + " could not activate!\n" + ex.getMessage());
            System.exit(1);
        }
        logger.info("DynamoDB table " + tableName + " active.");
        return table;
    }

    @Override
    public Player getPlayer(String uuid) {
        Item item = table.getItem(new GetItemSpec()
        .withPrimaryKey(new KeyAttribute(PRIM_KEY, uuid)));
        if(item == null) {
            return null;
        }
        Boolean online = item.hasAttribute(ONLINE) ? item.getBOOL(ONLINE) : null;
        String username = item.hasAttribute(USERNAME) ? item.getString(USERNAME) : null;
        String serverId = item.hasAttribute(SERVER_ID) ? item.getString(SERVER_ID) : null;
        Long joinTime = item.hasAttribute(JOIN_TIME) ? item.getLong(JOIN_TIME) : null;
        Long leaveTime = item.hasAttribute(LEAVE_TIME) ? item.getLong(LEAVE_TIME) : null;
        Long expiry = item.hasAttribute(EXPIRE) ? item.getLong(EXPIRE) : null;
        return new Player(online, uuid, username, serverId, joinTime, leaveTime, expiry);
    }

    @Override
    public Success updatePlayer(Player player) {
        UpdateItemSpec update = getUpdateItemSpec(player);
        table.updateItem(update);
        return new Success(true);
    }

    private UpdateItemSpec getUpdateItemSpec(Player player) {
        UpdateItemSpec update = new UpdateItemSpec();
        update.withPrimaryKey(new KeyAttribute(PRIM_KEY, player.getUuid()));
        update.addAttributeUpdate(new AttributeUpdate(ONLINE).put(player.isOnline()));
        if(player.getServerId() != null) {
            update.addAttributeUpdate(new AttributeUpdate(SERVER_ID).put(player.getServerId()));
        }
        if(player.getJoinTime() != null) {
            update.addAttributeUpdate(new AttributeUpdate(JOIN_TIME).put(player.getJoinTime()));
        }
        if(player.getLeaveTime() != null) {
            update.addAttributeUpdate(new AttributeUpdate(LEAVE_TIME).put(player.getJoinTime()));
        }
        if(player.getExpire() != null) {
            update.addAttributeUpdate(new AttributeUpdate(EXPIRE).put(player.getExpire()));
        }
        return update;
    }

}
