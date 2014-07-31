/*
 * Copyright © 2014 René Bernhardsgrütter, Christoph Walter Senn at Zurich
 * University of Applied Sciences; 2014 Helmut Hauser at University of Zurich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.zhaw.iamp.rct;

import ch.zhaw.iamp.rct.weights.Weights;
import java.io.File;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The main class of this application.
 */
public class App {

    public final static String APP_NAME = "Reservoir Control Toolbox";
    public final static String APP_VERSION = "0.1.0";
    final static String[] GRAMMAR_OPTIONS = {"g", "grammar", "use grammar generator - this requires the options d, i, o"};
    final static String[] DEFINITION_OPTIONS = {"d", "definition", "grammar definition file"};
    final static String[] INIT_OPTIONS = {"i", "init", "grammar init file"};
    final static String[] OUTPUT_OPTIONS = {"o", "output", "grammar output file"};
    final static String[] WEIGHTS_OPTIONS = {"w", "weights", "calculate weights"};
    static String CONFIG_DIRECTORY = System.getProperty("user.home") + "/.rct/";
    static Options cliOptions;
    static Controller controller;

    public static void main(String[] args) {
        configureCommandLineInterface();
        ensureConfigDirectoryExists();
        controller = new Controller();

        handleArguments(args);
    }

    static void configureCommandLineInterface() {
        cliOptions = new Options();
        cliOptions.addOption(GRAMMAR_OPTIONS[0], GRAMMAR_OPTIONS[1], false, GRAMMAR_OPTIONS[2]);
        cliOptions.addOption(DEFINITION_OPTIONS[0], DEFINITION_OPTIONS[1], true, DEFINITION_OPTIONS[2]);
        cliOptions.addOption(INIT_OPTIONS[0], INIT_OPTIONS[1], true, INIT_OPTIONS[2]);
        cliOptions.addOption(OUTPUT_OPTIONS[0], OUTPUT_OPTIONS[1], true, OUTPUT_OPTIONS[2]);
        Option weights = OptionBuilder.withArgName(WEIGHTS_OPTIONS[0]).withLongOpt(WEIGHTS_OPTIONS[1]).withDescription(WEIGHTS_OPTIONS[2]).hasArgs(3).create(WEIGHTS_OPTIONS[0]);
        cliOptions.addOption(weights);
    }

    private static void ensureConfigDirectoryExists() {
        File configDirectory = new File(CONFIG_DIRECTORY);

        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }
    }

    private static void handleArguments(String[] args) {
        CommandLineParser parser = new BasicParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.out.println("The arguments could not be understood: " + ex.getMessage());
            printCliHelp();
            return;
        }

        if (commandLine.hasOption("g")) {
            System.out.println("The generation grammar is not implemented yet.");
            return;
        }

        if (commandLine.hasOption("w")) {
            String[] optionValues = commandLine.getOptionValues("w");
            Weights.calculateWeights(optionValues[0], optionValues[1], optionValues[2], 1000);
            return;
        }

        if (commandLine.getOptions() != null && commandLine.getOptions().length > 0) {
            System.out.println("The arguments could not be understood.\n");
            printCliHelp();
            return;
        }

        controller.showMainWindow();
    }

    private static void printCliHelp() {
        HelpFormatter help = new HelpFormatter();
        help.printHelp("rct [(-g|--grammar) "
                + "(-d|--definition) <definition-file>"
                + "(-i|--init) <init-file>"
                + "(-o|--output) <output-file>]", cliOptions);
    }

    /**
     * @return The path of the configuration directory, per default in the
     * user's home directory.
     */
    public static String getConfigDirectoryPath() {
        return CONFIG_DIRECTORY;
    }

}
