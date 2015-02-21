<?php


$SERVER = "http://52.16.7.112:8000"; 
$SCORE_TOPIC="playerScores";
$LEADERBOARD_TOPIC = "gameLeaderBoard";
$JOIN_TOPIC="playerJoin";
$SCOREUP_TOPIC="playerHits";

require './vendor/autoload.php';
require './ComboClient.php';


$client = new ComboClient; 
$client->subscribe($SCOREUP_TOPIC);; 
$client->subscribe($JOIN_TOPIC);
while(true) {
 $client->poll($JOIN_TOPIC);  



}
?>
