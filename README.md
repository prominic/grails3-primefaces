# Summary

Run JSF and PrimeFaces from a Grails 3 application

## Getting Started

This plugin has not yet been submitted to the Grails repository.  For now you will need to install the plugin to your local repository.

    ./gradlew install

Then add this dependency to your project:
```
    compile "net.prominic.grails.plugins:grails3-primefaces:0.2"
```

You can see an example project and some demo instructions in [this project](https://github.com/prominic/grails3-primefaces-demo)

## Dependencies

This plugin was written and tested on Grails 3.3.3.

## Usage

To generate the beans, use this command:

    ./grailsw run-command pf-generate-all mypackage.MyDomain
    
This will generate the following files:
* grails-app/services/mypackage/MyDomainService.groovy
* src/main/groovy/mypackage/beans/MyDomainManageBean.groovy (the ManagedBean is called "myDomainMB")
* src/main/groovy/mypackage/beans/MyDomainLazyDataModel.groovy

To allow the new beans to work, you will need to add the new package to the grails.plugins.primefaces.beans.packages property in grails-app/conf/application.yml.

```
grails:
    plugins:
        primefaces:
                beans:
                   packages: mypackage.beans
```

You can then define .xhtml files to reference these beans in:

    src/main/webapp

## Built With

* [Grails](http://grails.org/download.html)
* [Gradle](https://gradle.com/)

## Authors

* [mibesoft](https://github.com/mibesoft/primefaces) - Original code 
* [feather812002](https://github.com/feather812002) - Grails 3 Conversion
* [JoelProminic](https://github.com/JoelProminic) - Cleanup and Documentation

## License

Moonshine-IDE is licensed under the Apache License 2.0 - see the [LICENSE.md](TODO) file for details

## Acknowledgments

* This code was adapted from [this Grails 2.x plugin](https://github.com/mibesoft/primefaces)
