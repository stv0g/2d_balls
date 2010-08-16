<?php

require_once 'config.php';

$link = mysql_connect($config['db']['host'], $config['db']['user'], $config['db']['pw']);
if (!$link) {
    die('Not connected : ' . mysql_error());
}

$selected = mysql_select_db($config['db']['db'], $link);
if (!$selected) {
    die ('Can\'t use foo : ' . mysql_error());
}

?>