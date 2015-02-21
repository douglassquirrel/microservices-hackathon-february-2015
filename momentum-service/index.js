var _ = require('lodash');
var api = require('./api');

var players = {};

function getPlayer (id) {
    return players[id];
}

function onJoin (event) {
    var player = {};
    player.id = event.id;
    player.direction = [0, 0];
    player.lastCoordinates = event.coordinates || [0, 0];
    players[player.id] = player;
}

function onPlayerMove (event) {
    var player = players[event.id];
    if (player) {
        player.direction = event.direction;
    }
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
    var newState = _.indexBy(state.moves, 'id');

    players = _.chain(players)
    .filter(function (player) {
        return newState[player.id];
    })
    .map(function (player) {
        player.lastCoordinates = newState[player.id].coordinates;
        return player;
    })
    .indexBy('id')
    .value();
}

function updatePlayerCoordinates (player) {
    player.lastCoordinates[0] += player.direction[0];
    player.lastCoordinates[1] += player.direction[1];
}

api.subscribe('ArenaClock', onTick);
api.subscribe('playerMove', onPlayerMove);
api.subscribe('playerJoin', onJoin);
// api.subscribe('validPlayerCoordinates', onState);
