var request = require('request');
var config = require('./config.json');
var urlBase = config.api;

function getNextMessage (topic, subscriptionId, handler) {
    request({
        url: urlBase + '/topics/' + topic + '/subscriptions/' + subscriptionId + '/next',
        json: true
    }, function (err, resp, body) {
        if (body) {
            console.log('Message received on topic ' + topic);
            console.log(body);
            console.log('');
            handler(body);
        }
        getNextMessage(topic, subscriptionId, handler);
    });
}

exports.publish = function send (topic, data) {
    console.log('sending to topic ' + topic);
    console.log(JSON.stringify(data));
    console.log('');
    request({
        method: 'POST',
        url: urlBase + '/topics/' + topic + '/facts',
        body: data,
        json: true
    });
};

exports.subscribe = function subscribe (topic, handler) {
    console.log('subscribing to ' + topic);
    request({
        method: 'POST',
        url: urlBase + '/topics/' + topic + '/subscriptions',
        json: true
    }, function (err, resp, body) {
        if (err) {
            console.log('Error: can\'t subsribe to ' + topic);
            console.log(err);
            return;
        }
        var subscriptionId = body.subscription_id;
        getNextMessage(topic, subscriptionId, handler);
    });
};
