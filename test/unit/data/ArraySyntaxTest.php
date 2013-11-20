<?php
$array = array(
	array("test" => array(
		getMessage(array("test" => "array"))),
		"test" => array("test",
			array(getMessage(getMessage2()), array("array"))
		)
	),
);
?>

// expected
<?php
$array = [
	["test" => [
		getMessage(["test" => "array"])],
		"test" => ["test",
			[getMessage(getMessage2()), ["array"]]
		]
	],
];
?>