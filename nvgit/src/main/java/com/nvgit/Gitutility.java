package com.nvgit;
import java.io.File;
import java.util.Set;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
 
public class Gitutility {
    // Constants for configuration
    public static  String REPOSITORY_PATH = "C:/Users/bhuvan.ku/Desktop/abc/test";
    public static  String COMMIT_MESSAGE ="mmm";
    public static  String REMOTE_REPO_URL;
    public static  Git git;
    public static  String USERNAME = "RVP199";
    public static  String PASSWORD = "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";
 
    public static Git openRepository(String repositoryPath) {
        try {
            return Git.open(new File(repositoryPath));
        } catch (Exception e) {
            System.err.println("Error opening repository: " + e.getMessage());
            return null;
        }
    }
 
    public static void initializeRepository(String localRepoPath) {
        try {
            InitCommand initCommand = Git.init();
            initCommand.setDirectory(new File(localRepoPath));
            initCommand.call();
            System.out.println("Initialized empty Git repository at " + localRepoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void addFiles(Git git, Set<String> filePaths) {
        try {
            AddCommand add = git.add();
            for (String filePath : filePaths) {
                System.out.println(filePath);
                add.addFilepattern(filePath);
            }
            add.call();
            System.out.println("Files added to the index successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index: " + e.getMessage());
        }
    }
 
    public static void commitChanges(Git git, Set<String> filePaths, String commitMessage) {
        try {
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
 
    public static void pushChanges(Git git, String remoteRepoUrl, String username, String password) {
        try {
            URIish remoteURI = new URIish(remoteRepoUrl);
            PushCommand pushCommand = git.push();
            pushCommand.setRemote(remoteURI.toString())
                       .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
            pushCommand.call();
            System.out.println("Push successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public static Set<String> getUntrackedFiles(){
        Status status;
        try {
            status = git.status().call();
            Set<String> untrackedFiles = status.getUntracked();
            System.out.println("Untracked files:");
            for (String untrackedFile : untrackedFiles) {
                System.out.println(untrackedFile);
            }
            return untrackedFiles;
        } catch (NoWorkTreeException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;  
    }
 
    public static Set<String> gettrackedFiles(){
        Status status;
        try {
            status = git.status().call();
            System.out.println(git.status().call().getChanged());
            Set<String> untrackedFiles = status.getChanged();
            System.out.println("Tracked files:");
            for (String untrackedFile : untrackedFiles) {
                System.out.println(untrackedFile);
            }
            return untrackedFiles;
        } catch (NoWorkTreeException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;  
    }
 
    public static void main(String[] args) {
        // Open the repository or initialize if it doesn't exist
        // String dirPath = "";
        git = openRepository(REPOSITORY_PATH);
        if (git == null) {
        System.out.println("Im here");
            initializeRepository(REPOSITORY_PATH);
            git = openRepository(REPOSITORY_PATH);
        }
 
        if (git != null) {
          // addFiles(git, gettrackedFiles());
            //commitChanges(git, gettrackedFiles(), COMMIT_MESSAGE);
            // pushChanges(git, REMOTE_REPO_URL, USERNAME, PASSWORD);
        } else {
            System.err.println("Unable to proceed due to repository opening error.");
        }
    }
}