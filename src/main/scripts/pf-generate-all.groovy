import grails.util.GrailsNameUtils
import groovy.text.SimpleTemplateEngine

description( "Generates a CRUD interface (service + beans + primefaces-view) for a domain class" ) {
    usage "grails pf-generate-all [NAME]"
    argument name:'Domain Class', description:'The name of the domain class'
}

// Tell the user to use the command instead.
println ("OBSOLETE:   Use './grailsw run-commmand pf-generate-all [NAME]' instead.")

//println "Generates a CRUD interface (service + beans + primefaces-view) for a domain class"

//overwriteAll = false
//templateAttributes = [:]
//templateDir = "src/main/templates"
//appDir = "grails-app"
//templateEngine = new SimpleTemplateEngine()
//def fullDomainClassName = args[0]
//def model = model(fullDomainClassName)
//
//packageName = ''
//domainClassName = ''
//propertyName = ''
//(packageName, domainClassName) = splitClassName(fullDomainClassName)
//propertyName = GrailsNameUtils.getPropertyName(domainClassName)
//
//printMessage """
//            packageName = ${packageName}
//            domainClassName = ${domainClassName}
//            propertyName = ${propertyName}
//    """
//
//templateAttributes = [packageName: packageName,
//                      domainClassName: domainClassName,
//                      propertyName: propertyName]
//
//generateServices()
//generateBeans()


private void generateServices() {
    String dir = packageToDir(packageName)
    generateFile "$templateDir/Service.groovy.template", "$appDir/services/${dir}${domainClassName}Service.groovy"
}

private void generateBeans() {
    String dir = packageToDir(packageName)
    generateFile "$templateDir/ManagedBean.groovy.template", "src/main/groovy/${dir}/beans/${domainClassName}ManagedBean.groovy"
    generateFile "$templateDir/LazyDataModel.groovy.template", "src/main/groovy/${dir}/beans/${domainClassName}LazyDataModel.groovy"
}




private String  packageToDir (packageName) {
    String dir = ''
    if (packageName) {
        dir = packageName.replaceAll('\\.', '/') + '/'
    }
    return dir
}

private boolean  okToWrite (String dest) {
    File file = new File(dest)
    if (overwriteAll || !file.exists()) {
        return true
    }

    String propertyName = "file.overwrite.$file.name"
    ant.input(addProperty: propertyName, message: "$dest exists, ok to overwrite?",
            validargs: 'y,n,a', defaultvalue: 'y')

    if (ant.antProject.properties."$propertyName" == 'n') {
        return false
    }

    if (ant.antProject.properties."$propertyName" == 'a') {
        overwriteAll = true
    }

    true
}

private void generateFile (String templatePath, String outputPath ) {
    if (!okToWrite(outputPath)) {
        return
    }

    File templateFile = new File(templatePath)
    if (!templateFile.exists()) {
        errorMessage "\nERROR: $templatePath doesn't exist"
        return
    }

    File outFile = new File(outputPath)

    // in case it's in a package, create dirs
    ant.mkdir dir: outFile.parentFile

    outFile.withWriter { writer ->
        templateEngine.createTemplate(templateFile.text).make(templateAttributes).writeTo(writer)
    }

    printMessage "generated $outFile.absolutePath"
}

private splitClassName (String fullName) {

    int index = fullName.lastIndexOf('.')
    String packageName = ''
    String className = ''
    if (index > -1) {
        packageName = fullName[0..index-1]
        className = fullName[index+1..-1]
    }
    else {
        packageName = ''
        className = fullName
    }

    [packageName, className]
}

private copyFile (String from, String to ) {
    if (!okToWrite(to)) {
        return
    }

    ant.copy file: from, tofile: to, overwrite: true
}

private void printMessage (String message) {  println  message}
private void errorMessage (String message) {println message }