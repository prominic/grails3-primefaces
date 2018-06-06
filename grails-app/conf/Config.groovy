// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.grails.web.servlet',  //  controllers
           'org.grails.web.pages', //  GSP
           'org.grails.web.sitemesh', //  layouts
           'org.grails.web.mapping.filter', // URL mapping
           'org.grails.web.mapping', // URL mapping
           'org.grails.commons', // core / classloading
           'org.grails.plugins', // plugins
           'org.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}
