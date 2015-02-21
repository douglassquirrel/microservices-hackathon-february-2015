<?php

class ScoreBoard
{
    private $scores;

    public function __construct()
    {
        $this->scores = [];
    }

    public function addPlayer($id)
    {
        $this->scores[$id] = 0;
        return ['id' => $id, 'score' => 0];
    }

    public function updatePlayer($id, $value)
    {
        $this->scores[$id] += $value;
        return ['id' => $id, 'score' => $this->scores[$id]];
    }

    public function getScores()
    {
        uasort($this->scores, function($a, $b) {
            if ($a['score'] == $b['score']){
                return 0;
            }
            return $a['score'] < $b['score'] ? -1 : 1;
        });

        // sort returned scores on score
        return $this->scores;
    }
}
