package com.branchflow;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Branch Flow! A new approach into git.");

        final String command = args.length > 0 ? args[0] : "help";

        switch (command) {
            case "init":
                getInitCmd(args);

                break;
            case "help":
                System.out.println("Available commands:");
                System.out.println("  init - Initialize a new Branch Flow repository");
                System.out.println("  help - Show this help message");
                System.out.println("  version - Show the version of Branch Flow");
                break;
            case "version":
                System.out.println("Branch Flow version 1.0.0");
                break;
            default:
                break;
        }
    }

    private static void getInitCmd(String[] args) throws IOException {
        System.out.println("Initializing a new Branch Flow repository...");

        String pathToInitialize = ".";
        if (args.length == 2) {
            pathToInitialize = args[1];
        }

        File workDirFile = new File(pathToInitialize);

        if (workDirFile.exists() && args.length == 2) {
            if (!workDirFile.isDirectory()) {
                throw new RuntimeException("The path you provided is not a Directory");
            } else if (workDirFile.list().length != 0) {
                throw new RuntimeException("Git directory is not empty");
            }
        } else {
            RepositoryHelper.repositoryDirectory(pathToInitialize, null, true);
        }

        RepositoryHelper.repositoryDirectory(pathToInitialize, ".git", true);
        RepositoryHelper.repositoryDirectory(pathToInitialize, ".git/objects", true);
        RepositoryHelper.repositoryDirectory(pathToInitialize, ".git/refs", true);

        RepositoryHelper.writeToFile(pathToInitialize + "/.git/description",
                "Unnamed repository; edit this file 'description' to name the repository.\n");
        RepositoryHelper.writeToFile(pathToInitialize + "/.git/HEAD", "ref: refs/heads/main\n");
        RepositoryHelper.writeToFile(pathToInitialize + "/.git/config",
                ConfigUtils.getDefaultRepoProperties().toString());

        System.out.println("Branch Flow repository initialized successfully.");
    }
}
