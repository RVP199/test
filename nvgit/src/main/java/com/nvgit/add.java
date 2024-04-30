package com.nvgit;

import java.io.File;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;


public class add {
    public static void addFiles(String repositoryPath, String... filePaths) {
        try (Git git = Git.open(new File(repositoryPath))) {
            AddCommand add = git.add();
            for (String filePath : filePaths) {
                add.addFilepattern(filePath);
            }
            add.call();
            System.out.println("Files added to the index successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index: " + e.getMessage());
        }
    }
 
    public static void main(String[] args) {
        
        String repositoryPath = "C:/Users/bhuvan.ku/Desktop/gitnv/Gelloi";
        String[] filesToAdd = {"New Text Document.txt"}; 
 
        addFiles(repositoryPath, filesToAdd);
    }
}



