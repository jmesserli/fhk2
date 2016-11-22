package nu.peg.fhk2

import nu.peg.fhk2.cli.getOptions
import nu.peg.fhk2.internal.FileHaecksler
import nu.peg.fhk2.util.FileSizeParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.MissingOptionException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val options = getOptions()
    var commandLine: CommandLine
    try {
        commandLine = DefaultParser().parse(options, args)
    } catch (moe: MissingOptionException) {
        println(moe.message)
        HelpFormatter().printHelp("fhk2", options, true)
        // todo launch gui
        exitProcess(-1)
    }

    val file = Paths.get(commandLine.getOptionValue('f'))
    if (!Files.exists(file)) {
        println("Your file '$file' does not exist!")
        exitProcess(-1)
    }
    val partSize = FileSizeParser.parseFileSize(commandLine.getOptionValue('s', "5M"))
    val verify = commandLine.hasOption('v')
    val deleteSource = commandLine.hasOption('d')

    val haecksler = FileHaecksler()
    if (commandLine.hasOption('c')) {
        haecksler.cutFile(file, partSize, verify, deleteSource)
    } else if (commandLine.hasOption('m')) {
        haecksler.mergeFile(file, verify, deleteSource)
    }
}