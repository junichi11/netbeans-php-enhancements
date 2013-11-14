# NetBeans PHP Enhancements Plugin

Support for some small features.

## Features

- Smart delete (Ctrl + Shift + BACK_SPACE)
- Generate dummy text (Alt + Insert > Dummy Text)
- Generate dummy image
- Convert to PHP short array syntax

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

## License

[Common Development and Distribution License (CDDL) v1.0 and GNU General Public License (GPL) v2](http://netbeans.org/cddl-gplv2.html)
