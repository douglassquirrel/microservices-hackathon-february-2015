var _ = require('lodash');
var api = require('./api');

var players = {};

function getPlayer (id) {
    return players[id];
}

function addPlayer (event) {
    var player = {};
    player.id = event.id;
    player.direction = [0, 0];
    player.lastCoordinates = event.coordinates || [0, 0];
    players[player.id] = player;
    return player;
}

function onPlayerMove (event) {
    var player = players[event.id];
    if (!player) {
        player = addPlayer(event);
    }
    player.direction = event.direction;
}

function onTick (event) {
    var positions = _.chain(players)
    .each(updatePlayerCoordinates)
    .map(function (player) {
        return {
            id: player.id,
            coordinates: player.lastCoordinates
        };
    })
    .value();

    api.publish('playerCoordinates', {
        positions: positions
    });
}

function onState (state) {
    _.each(state.positions, function (newPlayer) {
        var player = getPlayer(newPlayer.id);
        if (player) {
            // player.lastCoordinates = newPlayer.coordinates;
        } else {
            addPlayer(newPlayer);
        }
    });
}

function updatePlayerCoordinates (player) {
    player.lastCoordinates[0] += player.direction[0];
    player.lastCoordinates[1] += player.direction[1];
}

api.subscribe('ArenaClock', onTick);
api.subscribe('playerMove', onPlayerMove);
api.subscribe('playerJoin', addPlayer);
api.subscribe('validPlayerCoordinates', onState);
