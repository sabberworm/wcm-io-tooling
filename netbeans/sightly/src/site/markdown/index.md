## wcm.io Netbeans Sightly Extension

Plugin for [Netbeans](http://www.netbeans.org) to help developers using [Sightly](http://docs.adobe.com/content/docs/en/aem/6-0/develop/sightly.html).

### Documentation

* [Usage](usage.html)
* [Development](development.html)
* [Changelog](changes-report.html)

### Overview

#### Autocompletion

The Netbeans Sightly Extension provides autocompletion in html/jsp files for:

* data-sly-\* commands (including documentation)
* properties of **currentPage** and **wcmmode** (e.g. currentPage.language)
* class lookup if setting a data-sly-use variable
* variable-names defined by data-sly-use and data-sly-list (e.g. data-sly-use.foo)
* members of variables defined (e.g. lookup for foo.MyMethod), including documentation if available


See [Usage](usage.html) for details.

### Installation

You can find the Plugin in the NetBeans Plugin Portal Update Center. Navigate to **'Tools'** > **'Plugins'** and look for the Plugin.

![NetbeanUpdateCenter](images/installation.png)

### Acknowledgments

This plugin contains the documentation for data-sly-\* commands provided in the [HTL specification](https://github.com/Adobe-Marketing-Cloud/htl-spec/blob/master/SPECIFICATION.md)


