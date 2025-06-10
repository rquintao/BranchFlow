package com.branchflow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RepositoryHelper {

    public static void writeToFile(String path, String stringValue) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(stringValue);
        writer.close();

    }

    public static String repositoryDirectory(String workdir, String path, boolean toCreate) {
        String fullPath = path != null ? new File(workdir, path).getPath() : workdir;
        System.out.println(fullPath);
        File file = new File(fullPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                System.out.println("Returning path since " + file + " is a directory.");
                return fullPath;
            } else {
                throw new RuntimeException("The path you provided is not a directory!");
            }
        }

        if (toCreate) {
            if (file.mkdirs()) {
                System.out.println("Created directory!");
            } else {
                System.out.println("Some error occured!");
            }

            return fullPath;
        } else {
            return null;
        }
    }

    public static String findRepositoryRoot(String dir) {
        System.out.println(dir);
        File directory = new File(dir);

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter is not a directory: " + dir);
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().equals(".git")) {
                    return file.getPath();
                }
            }
        }

        if (directory.getParent() == null) {
            System.out.println("Could not find .git repository folder");
            return null;
        }

        // Recursively check the parent directory
        return findRepositoryRoot(directory.getParent());
    }
}
