package com.branchflow;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import objects.GitBlob;
import objects.GitCommit;
import objects.GitObject;

public class App {

    public static void main(String[] args) throws IOException, DataFormatException, NoSuchAlgorithmException {
        System.out.println("Welcome to Branch Flow! A new approach into git.");

        final String command = args.length > 0 ? args[0] : "help";

        switch (command) {
            case "init":
                getInitCmd(args);
                break;
            case "cat-file":
                getCatFileCmd(args);
                break;
            case "hash-object":
                getHashObjectCmd(args);
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
            case "teste":
                RepositoryHelper.findRepositoryRoot(args[1]);
            default:
                break;
        }
    }

    private static void getHashObjectCmd(String[] args) throws NoSuchAlgorithmException {
        final boolean writeFile = args.length > 1 && args[1].equals("-w");
        String file = args[writeFile ? 2 : 1];
        if (args.length < 2) {
            System.out.println("Usage: git hash-object <file>");
            return;
        }

        GitBlob gitBlob = new GitBlob();
        System.out.println(gitBlob.serialize(file.getBytes()));

    }

    private static void getCatFileCmd(String[] args) throws DataFormatException, IOException {
        if (args.length < 3) {
            System.out.println("See help to know how to use command.");
            return;
        }
        if (args.length > 3) {
            throw new RuntimeException("Invalid command");
        }

        String type = args[1];
        String file = args[2];

        System.out.println("Type: " + type);

        switch (type.toLowerCase()) {
            case "blob":
                GitBlob gitBlob = new GitBlob(file);
                System.out.println(gitBlob.toString());
                break;
            case "commit":
                GitCommit gitCommit = new GitCommit(file);
                System.out.println(gitCommit.toString());
                break;

            default:
                throw new RuntimeException("Invalid type for cat-file");
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
            File gitDir = new File(workDirFile + "/.git");
            if (!workDirFile.isDirectory()) {
                throw new RuntimeException("The path you provided is not a Directory");
            } else if (gitDir.exists() && gitDir.list().length != 0) {
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
