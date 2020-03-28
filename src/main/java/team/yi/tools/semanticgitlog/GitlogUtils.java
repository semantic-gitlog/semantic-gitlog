package team.yi.tools.semanticgitlog;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class GitlogUtils {
    public static void forceMkdir(final File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                final String message = "File " + directory + " exists and is not a directory. Unable to create directory.";

                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs() && !directory.isDirectory()) {
                final String message = "Unable to create directory " + directory;

                throw new IOException(message);
            }
        }
    }
}
