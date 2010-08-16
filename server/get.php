<?php

require_once 'init.php';

if (!in_array($_REQUEST['game'], $config['games'])) {
	die('Unknown game!');
}
	

$result = mysql_query('SELECT * FROM ' . $config['db']['table'] . ' WHERE game = \'' . $_REQUEST['game'] . '\' ORDER BY score DESC', $link);

$doc = new DOMDocument($config['xml']['version'], $config['xml']['encoding']);

$highScores = $doc->createElement('highscores');
$highScores->setAttribute('game', $_REQUEST['game']);
$doc->appendChild($highScores);

$rank = 1;
while ($row = mysql_fetch_assoc($result)) {
	$highScore = $doc->createElement('highscore');
	$highScore->setAttribute('id', $row['id']);
	$highScore->setAttribute('rank', $rank++);
	$highScores->appendChild($highScore);
	
	foreach (array('date', 'nick', 'score', 'time') as $tagName) {
		$highScore->appendChild($doc->createElement($tagName, $row[$tagName]));
	}
}

?>