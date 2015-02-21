<?php
require __DIR__.'/vendor/autoload.php';
require __DIR__.'/ComboClient.php';
require __DIR__.'/ScoreBoard.php';

// set up scores data
$scores = [];
$score_board = new ScoreBoard;

// subscribe to topics
$topics = ['ArenaClock', 'playerJoin', 'scoreEvent'];

$client = new ComboClient;

foreach ($topics as $topic) {
    $client->subscribe($topic);
}

function updateScore($client, $topic, $score) {
    $client->send('PlayerScore',
        ['status' => 'playing', 
        'score' => $score['score'],
        'id' => $score['id']
        ]
    );
}

// set up polling loop
while (true) {
    foreach ($topics as $topic) {
        echo "Polling for $topic\n";

        // poll topic
        $response = $client->poll($topic);
        if (!$response) {
            continue;
        }
        var_dump($response);

        switch ($topic) {
            case 'playerJoin':
                // add player to score board
                $score = $score_board->addPlayer($response['id']);
                updateScore($client, 'PlayerScore', $score);
                break;
            
            case 'ArenaClock':
                // send out leader board each tick
                var_dump($score_board->getScores());
                $fact = ['scores' => $score_board->getScores()];
                $client->send('LeaderBoard', $fact);
                break;

            case 'scoreEvent':
                // increase player score
                if ($response['type'] == 'collision') {
                    // set score
                    $score = $score_board->updatePlayer($response['id'], -10);
                    updateScore($client, 'PlayerScore', $score);
                } else if ($response['type'] == 'hit') {
                    $score = $score_board->updatePlayer($response['id'], 30);
                    updateScore($client, 'PlayerScore', $score);
                }
                break;
            default:
                //nothing
        }
    }
}
