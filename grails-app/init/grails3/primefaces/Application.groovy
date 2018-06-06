package grails3.primefaces

import grails.boot.*
import grails.boot.config.GrailsAutoConfiguration
import grails.plugins.metadata.*

@PluginSource
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        System.out.println ("application starting ")
        GrailsApp.run(Application, args)
    }
}
