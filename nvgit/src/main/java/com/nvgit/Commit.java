package com.nvgit;
import java.io.File;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
 
public class Commit {
 
 
    public static void addFilesAndCommit(String repositoryPath, String[] filePaths, String commitMessage) {
        try (Git git = Git.open(new File(repositoryPath))) {
           
            AddCommand add = git.add();
            for (String filePath : filePaths) {
                add.addFilepattern(filePath);
            }
            add.call();
            System.out.println("Files added to the index successfully!");
           
         
            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage);
            commit.call();
            System.out.println("Changes committed successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index or committing changes: " + e.getMessage());
        }
    }
 
   public static void main(String[] args) {
        String repositoryPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo.git";
        String[] filesToAdd = {"xyz"};
        String commitMessage = "Added clone.txt";
       
        addFilesAndCommit(repositoryPath, filesToAdd, commitMessage);
    }
}