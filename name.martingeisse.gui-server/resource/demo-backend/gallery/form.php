<?php 

header('Content-Type: application/json');
if (empty($_POST['foo']) || strlen($_POST['foo']) < 5) {
	echo '{"validation": {"foo": "please type in at least 5 characters"}}';
} else {
	echo json_encode(array('redirectPage' => '/index'));
}
