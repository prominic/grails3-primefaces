package grails3.primefaces
import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware

import javax.servlet.DispatcherType
import javax.servlet.ServletContext
import javax.servlet.ServletException
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean

import com.sun.faces.config.ConfigureListener
import com.sun.faces.config.FacesInitializer


/**
 * Created by prominic2 on 2018/4/23.
 */
class Grails3PrimefacesConfig  implements GrailsApplicationAware{
    GrailsApplication grailsApplication

    public void setGrailsApplication(GrailsApplication grailsApplication){
        this.grailsApplication=grailsApplication
    }


    @Bean
    public ServletContextInitializer grails3PrimefacesInitializer() {
        return new ServletContextInitializer() {
            @Override
            void onStartup(ServletContext servletContext) throws ServletException {

                //servletContext.setInitParameter("primefaces.THEME", "bootstrap");
                servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
                servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
                servletContext.setInitParameter("contextConfigLocation", "/WEB-INF/applicationContext.xml");
                //servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Production");
                servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");

                servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");


                FacesInitializer facesInitializer = new FacesInitializer();
                Set<Class<?>> clazz = new HashSet<Class<?>>();
                clazz.add(Grails3PrimefacesConfig.class);
                facesInitializer.onStartup(clazz, servletContext);

                servletContext.addListener(new ConfigureListener())
            }
        }
    }


}
