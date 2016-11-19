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

    val cutMergeGroup = OptionGroup()
    cutMergeGroup.isRequired = true
    cutMergeGroup.addOption(cut)
    cutMergeGroup.addOption(merge)

    val file = Option.builder("f")
            .longOpt("file")
            .hasArg()
            .argName("file path")
            .required()
            .desc("Specifies the file to be cut or a part of a cut file to be merged")
            .type(String::class.java)
            .build()

    val size = Option.builder("s")
            .longOpt("size")
            .hasArg()
            .argName("part size")
            .desc("Specifies the file size of the parts. You can specify suffixes like K, M or G. Defaults to 5M. Not used for merge mode")
            .build()

    val verify = Option.builder("v")
            .longOpt("verify")
            .desc("Generates a file containing a hash which allows to verify the merged file. In merge mode specifies that the file should be verified")
            .build()

    options.addOptionGroup(cutMergeGroup)
    options.addOption(file)
    options.addOption(size)
    options.addOption(verify)

    return options
}