# NetBeans PHP Enhancements Plugin

Support for some small features.

## Features

- Smart delete (Ctrl + Shift + BACK_SPACE)
- Generate dummy text (Alt + Insert > Dummy Text)
- Generate dummy image
- Convert to PHP short array syntax
- Typing hooks
- Code completion
- Convert String to Html entities (name entities)
- Convert Html eltities to String

### Smart delete

This feature delete string between "" or '', variable name.

#### delete string

e.g.

```php
<?php echo "your message";?> -> <?php echo "";?>
```

#### delete variable name

e.g.

```php
<?php $somethingVariable;?> -> <?php $;?>
```

### Generate dummy text

We can use this feature on PHP and Html editors.
Please set base text on the Dialog. It will be looped with option.

#### options

- loop count
- text length

### Generate dummy image

We can generate a dummy image with this feature to specific folder.
Please right-click a folder > `Generate dummy image`

### Convert to PHP short array syntax

Convert array() to [].
Right-click (file|directory) > Convert to php short array syntax

### Typing hooks

If you want to enable this feature, Please check Options(Tools > Options > PHP > Enhancements).

This is avaible with the following operators:

- Object operator ->
- Double arrow operator =>

e.g.  type `$this-` => `$this->`, type `array('key' =)` => `array('key' =>)`

```php
$this->property;
$array = array('key' => 'value');
$a = ["foo" => "bar"];
$f = fn() => "arrow function";
$result = match ($condition) {
    1, 2 => foo(),
    default => bar(),
};
```

#### Note:

- If you want to type `==`, please type `=` after `=>`.
- If you want to type `=`, please delete `>`. (It's difficult to handle all cases correctly...)

### Code completion

#### Constant

Please check `Tools > Options > PHP > Enhancements`

- Change name for `define`, `defined` and `const` to uppercase name

#### Function and method parameter

- multibyte functions (e.g. mb_convert_encoding())
- header
- ini_set, ini_get, ini_alter, ini_restore
- date_default_timezone_set
- date, date_format
- Datetime::format, DateTimeImmutable::format
- htmlentities, htmlspecialchars
- session_cache_limiter

Please run code completion (<kbd>Ctrl</kbd> + <kbd>Space</kbd>) inside quotes.

e.g.
```php
<?php
ini_get('here');
date("here");
```

## Download

- https://github.com/junichi11/netbeans-php-enhancements/releases (github releases: contains old versions)
- https://plugins.netbeans.apache.org/catalogue/?id=29 (Plugin Portal)

## Donation

- https://github.com/sponsors/junichi11

## License

Apache License, Version 2.0
