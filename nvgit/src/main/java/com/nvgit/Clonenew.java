package com.nvgit;

import java.io.File;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class Clonenew {
    public static void main(String[] args) {
        String gitUrl = "https://github.com/RVP199/nvdemo.git";

        // Get the current working directory path
        String defaultDirPath = System.getProperty("user.dir");

        cloneRepositoryUsingHttpsUrl(gitUrl, defaultDirPath);
    }

    public static void cloneRepositoryUsingHttpsUrl(String url, String directoryPath) {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists() || directory.listFiles().length == 0) {
                // If the directory is empty or doesn't exist, clone directly
                Git.cloneRepository()
                   .setURI(url)
                   .setDirectory(directory)
                   .call();
                System.out.println("Repository cloned successfully!");
            } else {
                Scanner input = new Scanner(System.in);
                System.out.println("Directory is not empty. Enter a new directory name: ");
                String newDirectoryName = input.nextLine();
                File newDirectory = new File(directory.getParent(), newDirectoryName);
                if (!newDirectory.exists()) {
                    newDirectory.mkdir(); // Create new directory
                    Git.cloneRepository()
                       .setURI(url)
                       .setDirectory(newDirectory)
                       .call();
                    System.out.println("Repository cloned successfully into " + newDirectory.getAbsolutePath());
                } else {
                    System.out.println("Directory already exists. Cloning aborted.");
                }
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
