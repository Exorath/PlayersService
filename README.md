# PlayersService
Service that keeps track of online players and the server they are connected to.
##Endpoints
###/players/{uuid} [GET]:
####Gets information about the provided player

**Arguments**:
- uuid (string): The players uuid

**Response**: 
```json
{"online": true, "username": "Toonsev", "sid": "a1e99827-0c05-45d6-a822-911b425a4027", "joinTime": 1478190051447, "leaveTime": 1478190151447, "expiry": 1478190151447}
```
- online (boolean): Whether or not the player is currently (or very recently) playing on Exorath
- username (string)[OPTIONAL]: The last username this player was known as
- sid (string)[OPTIONAL]: The unique id of the server that the player is currently playing on, only returned when online=*true*
- joinTime(num)[OPTIONAL]: The time (in UNIX millis) of when this player joined, only returned if the player is currently online
- leaveTime(num)[OPTIONAL]: The time (in UNIX millis) of when this player leaved, only returned if the player is currently offline
- expiry(num)[OPTIONAL]: The time (in UNIX millis) of when the online validity expires, when this is expired, the player is considered offline. Only returned if the player has played before.

###/players/{uuid} [PUT]:
####Updates information about the player
**Body**:
```json
{"online": true, "username": "Toonsev", "sid": "a1e99827-0c05-45d6-a822-911b425a4027", "joinTime": 1478190051447, "expiry": 1478190151447}
```

**Arguments**:
- online (boolean): Whether or not the player is currently (or very recently) playing on Exorath
- username (string)[OPTIONAL]: The last username this player was known as, does not always have to be provided
- sid (string)[OPTIONAL]: The unique id of the server that the player is currently playing on, should only be send when the player changes server.
- joinTime(num)[OPTIONAL]: The time (in UNIX millis) of when this player joined, should only be send onjoin (online=*true* is required as well)
- leaveTime(num)[OPTIONAL]: The time (in UNIX millis) of when this player leaved, should only be send onleave (online=*false* is required as well)

**Response**: 
```json
{"success": true}
```
- success (boolean): Whether or not the record was updated successfully.
- err (string)[OPTIONAL]: Error message only responded when the put was not successful.

##Environment
| Name | Value |
| --------- | --- |
| AWS_REGION | EU_CENTRAL_1 |
| AWS_ACCESS_KEY_ID	| {acces_key_id} |
| AWS_SECRET_KEY	| {secret_key} |
| PORT	| {PORT} |
