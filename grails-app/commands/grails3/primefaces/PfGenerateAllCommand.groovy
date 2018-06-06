package grails3.primefaces


import grails.dev.commands.*
import grails.util.GrailsNameUtils
import groovy.text.SimpleTemplateEngine

class PfGenerateAllCommand implements GrailsApplicationCommand {
    /** The syntax for this command */
    private static final String SYNTAX = "SYNTAX:  grails run-command pf-generate-all <domain>"

    /**
     * The directory for the template resources
     */
    private static final String TEMPLATE_DIR = 'primefaces-templates'

    /** The package name of the target domain class */
    private String packageName = ''
    /** The target domain class name */
    private String domainClassName = ''
    /** The domain class name in property format */
    private String propertyName = ''

    /** The attributes to use for the templates */
    private Map templateAttributes = [:]

    /** configure whether we should overwrite any existing files */
    private boolean overwriteAll = false
    /** The base application directory for outputted files */
    private String appDir = "grails-app"
    /** The engine used to generate the templates */
    private SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()

    /**
     * The base method of the Command.
     * @return <code>true</code> if the command succeeded, <code>false</code> if the command failed.
     */
    boolean handle() {
        if (args.size() < 1) {
            errorMessage("Missing domain class name")
            errorMessage(SYNTAX)
            return false
        }

        def fullDomainClassName = args[0]

        printMessage("Generating beans for domain '$fullDomainClassName'...")

        (packageName, domainClassName) = splitClassName(fullDomainClassName)
        propertyName = GrailsNameUtils.getPropertyName(domainClassName)

        templateAttributes = [
                packageName: packageName,
                domainClassName: domainClassName,
                propertyName: propertyName
        ]

        printMessage """
            packageName = ${packageName}
            domainClassName = ${domainClassName}
            propertyName = ${propertyName}
        """


        generateServices()
        generateBeans()

        return true
    }

    /**
     * Generate the service from a template
     */
    private void generateServices() {
        String dir = packageToDir(packageName)
        generateFile "$TEMPLATE_DIR/Service.groovy.template", "$appDir/services/${dir}${domainClassName}Service.groovy"
    }

    /**
     * Generate the beans from a template
     */
    private void generateBeans() {
        String dir = packageToDir(packageName)
        generateFile "$TEMPLATE_DIR/ManagedBean.groovy.template", "src/main/groovy/${dir}/beans/${domainClassName}ManagedBean.groovy"
        generateFile "$TEMPLATE_DIR/LazyDataModel.groovy.template", "src/main/groovy/${dir}/beans/${domainClassName}LazyDataModel.groovy"
    }

    /**
     * Convert a package name (i.e. 'com.company') to a file path (i.e. 'com/company').
     * The resulting path will not have leading or trailing '/' characters.
     * @param packageName  the package name
     * @return  the corresponding path
     */
    private static String  packageToDir (packageName) {
        String dir = ''
        if (packageName) {
            dir = packageName.replaceAll('\\.', '/') + '/'
        }
        return dir
    }

    /**
     * Check for permission to write to the target file.
     * Permission will be granted if the file does not exist or if {@link #overwriteAll} == true
     *
     * @param dest  the name and path of the file to check
     * @return <code>true</code> if the write is allowed, <code>false</code> otherwise.
     */
    private boolean  okToWrite (String dest) {
        File file = new File(dest)
        if (overwriteAll || !file.exists()) {
            return true
        }
        else {
            return false
        }


        // TODO:  prompt the user for permission?
//        String propertyName = "file.overwrite.$file.name"
//        ant.input(addProperty: propertyName, message: "$dest exists, ok to overwrite?",
//                validargs: 'y,n,a', defaultvalue: 'y')
//
//        if (ant.antProject.properties."$propertyName" == 'n') {
//            return false
//        }
//
//        if (ant.antProject.properties."$propertyName" == 'a') {
//            overwriteAll = true
//        }
//
//        true
    }

    /**
     * Generate the output file using the indicated template
     * @param templateName  the name of the template.  This will be loaded as a resource
     * @param outputPath  the output file path
     */
    private void generateFile (String templateName, String outputPath ) {
        if (!okToWrite(outputPath)) {
            errorMessage("ERROR:  $outputPath already exists, and overwriting is not allowed.")
            return
        }

        InputStream templateStream = this.class.getClassLoader().getResourceAsStream(templateName)
        if (!templateStream) {
            errorMessage "\nERROR: Template '$templateName' does not exist."
            return
        }

        File outFile = new File(outputPath)

        // in case it's in a package, parent directories
        outFile.parentFile.mkdirs()

        outFile.withWriter { writer ->
            templateEngine.createTemplate(templateStream.text).make(templateAttributes).writeTo(writer)
        }

        printMessage "Generated $outFile.absolutePath"
    }

    /**
     * Given a class name including the package (i.e. "com.domain.MyClass"), split it into the package ("com.domain")
     * and the class name ("MyClass").  If there is no package name, it will be returned as ""
     * @param fullName  the full class name and package.
     * @return (packageName, className)
     */
    private static splitClassName (String fullName) {

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

    /**
     * Write an informational message
     * @param message  the message to write
     */
    private void printMessage (String message) {
        println  message
    }

    /**
     * Write an error message.
     * @param message  the message to write
     */
    private void errorMessage (String message) {
        println message
    }
}

