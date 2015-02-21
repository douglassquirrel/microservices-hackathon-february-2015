<?php

class LeaderBoard
{
   private $score_map = array();

   public function addPlayer($player_id)
   {
      $this->score_map[$player_id] = 0;
   }

   public function scoreUpPlayer($player_id, $points)
   {
      if (array_key_exists($player_id, $this->score_map))
      {
         $this->score_map[$player_id] += $points;
      }
   }

   public function sortedScores()
   {
      return arsort($this->score_map);
   }
}
