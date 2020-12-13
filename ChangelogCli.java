package net.avdw.cli;

import org.tinylog.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

@Command(name = "changelog", description = "Show last changelog", mixinStandardHelpOptions = true, hidden = true)
public class ChangelogCli implements Runnable {
    @Spec
    private CommandSpec spec;

    @Override
    public void run() {
        boolean started = false;
        StringBuilder extract = new StringBuilder();
        URL inputUrl = ChangelogCli.class.getResource("/CHANGELOG.md");
        try {
            Scanner scanner = new Scanner(new File(inputUrl.getFile()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.matches("^## \\[v\\S+].*$")) {
                    if (started) {
                        break;
                    }
                    started = true;
                }

                if (started) {
                    extract.append(line).append("\n");
                }
            }
            if (extract.toString().isEmpty()) {
                spec.commandLine().getOut().println("No changelog");
            } else {
                spec.commandLine().getOut().println(extract);
            }
        } catch (FileNotFoundException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
