package nu.peg.fhk2.cli

import org.apache.commons.cli.Option
import org.apache.commons.cli.OptionGroup
import org.apache.commons.cli.Options

fun getOptions(): Options {
    val options = Options()

    val cut = Option.builder("c")
            .longOpt("cut")
            .desc("Puts the program into 'cut' mode")
            .build()

    val merge = Option.builder("m")
            .longOpt("merge")
            .desc("Puts the program into 'merge' mode")
            .build()

    val listDigests = Option.builder("l")
            .longOpt("list-algorithms")
            .desc("Lists all available hashing algorithms")
            .build()

    val mainGroup = OptionGroup()
    mainGroup.isRequired = true
    mainGroup.addOption(cut)
    mainGroup.addOption(merge)
    mainGroup.addOption(listDigests)

    val deleteSource = Option.builder("d")
            .longOpt("delete-source")
            .desc("Deletes the source files that are used for the operation")
            .build()

    val verify = Option.builder("v")
            .longOpt("verify")
            .desc("Generates a file containing a hash which allows to verify the merged file. In merge mode specifies that the file should be verified")
            .build()

    val algorithm = Option.builder("h")
            .longOpt("hashing-algorithm")
            .hasArg()
            .argName("hashing algorithm")
            .desc("Specified which algorithm should be used when hashing a file for verification. Only used with -c and -v. Get a list of algorithms with -l")
            .build()

    val file = Option.builder("f")
            .longOpt("file")
            .hasArg()
            .argName("file path")
            .desc("Specifies the file to be cut or a part of a cut file to be merged")
            .build()

    val size = Option.builder("s")
            .longOpt("size")
            .hasArg()
            .argName("part size")
            .desc("Specifies the file size of the parts. You can specify suffixes like K, M or G. Defaults to 5M. Not used for merge mode")
            .build()


    options.addOptionGroup(mainGroup)
    options.addOption(deleteSource)
    options.addOption(verify)
    options.addOption(algorithm)
    options.addOption(file)
    options.addOption(size)

    return options
}