# NetBeans PHP Enhancements Plugin

Support for some small features.

## Features

- Smart delete (Ctrl + Shift + BACK_SPACE)
- Generate dummy text (Alt + Insert > Dummy Text)
- Generate dummy image
- Convert to PHP short array syntax
- Typing hooks

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
array('key' => 'value');
```

## License

[Common Development and Distribution License (CDDL) v1.0 and GNU General Public License (GPL) v2](http://netbeans.org/cddl-gplv2.html)
