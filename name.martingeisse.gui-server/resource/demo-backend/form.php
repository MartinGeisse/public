<?php 

header('Content-Type: application/json');
if (empty($_POST['foo'])) {
	echo '{"validation": {"foo": "please fill this field"}}';
} else {
	echo json_encode(array('redirectPage' => '/index'));
}
