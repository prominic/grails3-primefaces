package grails.plugins.primefaces;

import grails.util.Holders;
import groovy.util.ConfigObject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import grails.config.Config

import java.util.logging.Logger;
import grails.core.GrailsApplication
import grails.util.Holders
public class PrimefacesBeans {
   // private static Logger log = Logger.getLogger("PrimefacesBeans");
    GrailsApplication grailsApplication=Holders.getGrailsApplication()
    public static void init(String... packagesForScanning) {
        try {
            registryPrimefacesBeans("grails.plugins.primefaces");
            def list=[]
            def conf=Holders.getGrailsApplication().config.getProperty('grails.plugins.primefaces.beans.packages')
            if(conf.contains(",")){
                conf.split(",").each{
                    if(it){
                        list.push(it)
                    }
                }
            }else{
                list.push(conf)
            }

            if (list.size() > 0) {
                for (String packageName : list) {
                    registryPrimefacesBeans(packageName);
                }
            } else {
                println("No package fonund for 40 'grails.plugins.primefaces.beans.packages'");
            }
        } catch (Exception ex) {
            println("No config found for 'grails.plugins.primefaces.beans.packages'");
            println(ex.getMessage());
        }
    }

    public  void init() {
        println("Start");

        try {
            registryPrimefacesBeans("grails.plugins.primefaces");
            def list=[]
            def conf=Holders.getGrailsApplication().config.getProperty('grails.plugins.primefaces.beans.packages')
            if(conf.contains(",")){
                conf.split(",").each{
                    if(it){
                        list.push(it)
                    }
                }
            }else{
                list.push(conf)
            }

            if (list.size() > 0) {
                for (String packageName : list) {
                    registryPrimefacesBeans(packageName);
                }
            } else {
                println("No package fonund for 40 'grails.plugins.primefaces.beans.packages'");
            }
        } catch (ClassCastException ex) {
            println("No config found for 'grails.plugins.primefaces.beans.packages'");
        } catch (Exception ex) {
            println(ex.getMessage());
        }
    }

    private static BeanDefinitionRegistry getRegistry() {
        ApplicationContext appContext = Holders.getApplicationContext();
        BeanFactory factory = appContext.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry registry = ((BeanDefinitionRegistry ) factory);
        return registry;
    }

    private static void registryPrimefacesBeans(String packageName) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ManagedBean.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
            println("beanClassName = " + bd.getBeanClassName());
            try {
                Class clazz = Class.forName(bd.getBeanClassName());
                ManagedBean mb = (ManagedBean) clazz.getAnnotation(ManagedBean.class);

                ApplicationContext appContext = Holders.getApplicationContext();
                BeanFactory factory = appContext.getAutowireCapableBeanFactory();

                BeanDefinitionRegistry registry = ((BeanDefinitionRegistry ) factory);

                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(clazz);
                beanDefinition.setLazyInit(false);
                beanDefinition.setAbstract(false);
                beanDefinition.setAutowireCandidate(true);
                for (Annotation a1 : clazz.getAnnotations()) {
                    println("\t annotation: " + a1.annotationType());
                    if (a1.annotationType() == SessionScoped.class) {
                        beanDefinition.setScope("session");
                    } else if (a1.annotationType() == ViewScoped.class) {
                        beanDefinition.setScope("view");
                    } else if (a1.annotationType() == RequestScoped.class) {
                        beanDefinition.setScope("request");
                    }
                }

                String beanName = mb.name();
                if (beanName.equals("")) {
                    beanName = clazz.getSimpleName();
                    beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                }

                MutablePropertyValues propertyValues = new MutablePropertyValues();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    for (Annotation annotation : field.getAnnotations()) {
                        if (annotation.annotationType() == GrailsService.class) {
                            try {
                                GrailsService gs = (GrailsService) annotation;
                                propertyValues.addPropertyValue(gs.name(), appContext.getBean(gs.name()));
                                println("\t GrailsService annotation named '" + gs.name() + "' [" + field.getType() + "] is created");
                            } catch (NoSuchBeanDefinitionException ex) {
                                println(ex.getMessage());
                            }
                        } else if (annotation.annotationType() == ManagedProperty.class) {
                            try {
                                ManagedProperty mp = (ManagedProperty) annotation;
                                String propName = mp.value().replace("#{", "").replace("}", "");
                                propertyValues.addPropertyValue(propName, appContext.getBean(propName));
                                println("\t ManagedProperty annotation named '" + propName + "' [" + field.getType() + "] is created");
                            } catch (NoSuchBeanDefinitionException ex) {
                                println(ex.getMessage());
                            }
                        }
                    }
                }
                println("beanName '" + beanName + "' is created");
                registry.registerBeanDefinition(beanName, beanDefinition);
                if (!propertyValues.isEmpty())
                    beanDefinition.setPropertyValues(propertyValues);
            } catch (Exception ex) {
                println(ex.getMessage());
            }
        }
    }
}