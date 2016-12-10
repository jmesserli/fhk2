package nu.peg.fhk2

import nu.peg.fhk2.cli.getOptions
import nu.peg.fhk2.digest.Digest
import nu.peg.fhk2.internal.FileHaecksler
import nu.peg.fhk2.util.FileSizeParser
import org.apache.commons.cli.*
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    fun printHelp(options: Options) {
        HelpFormatter().printHelp("fhk2", options, true)
    }

    val options = getOptions()
    var commandLine: CommandLine
    try {
        commandLine = DefaultParser().parse(options, args)
    } catch (moe: MissingOptionException) {
        println(moe.message)
        printHelp(options)
        // todo launch gui
        exitProcess(-1)
    }

    if (commandLine.hasOption('l')) {
        println("All available hashing algorithms:")
        Digest.values().sortedBy { it.name }.forEach {
            println(" - ${it.name} (${it.ordinal})")
        }
        exitProcess(0)
    }

    val fileString = commandLine.getOptionValue('f')
    if (fileString == null) {
        println("You must specify a file (-f)")
        printHelp(options)
        exitProcess(-1)
    }

    val file = Paths.get(fileString)
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