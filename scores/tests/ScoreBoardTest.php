<?php


require './ScoreBoard.php';

class ScoreBoardTest extends PHPUnit_Framework_TestCase
{
    // ...

    public function testAddPlayer()
    {
       $sb = new ScoreBoard;
       $res = $sb->addPlayer("foo");
       $this->assertEquals('foo', $res['id']);
       $this->assertEquals(0, $res['score']);
    }

    public function testUpdatePlayer()
    {
       $sb = new ScoreBoard;
       $sb->addPlayer("foo");
       $res = $sb->updatePlayer("foo", 100);
       $this->assertEquals('foo', $res['id']);
       $this->assertEquals(100, $res['score']);
    }

    public function testGetScores()
    {
       $sb = new ScoreBoard;
       $res = $sb->getScores();
       $this->assertEquals(strtolower(gettype($res)),'array');
    }

    // ...
}

?>
