package grails3.primefaces

import grails.util.Holders
//import org.apache.myfaces.webapp.StartupServletContextListener

class BootStrap {

    def init = { servletContext ->

        //def ctx=Holders.getServletContext()

        log.info "PrimefacesBootStrap start"
        grails.plugins.primefaces.PrimefacesBeans.init()
    }
    def destroy = {
    }
}
