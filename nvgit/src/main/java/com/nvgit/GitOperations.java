package com.nvgit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GitOperations {

    private static final String REMOTE_URL = "https://github.com/ravindrapanthi/Conflict-Merge.git";
    private static final String LOCAL_PATH = "C:/Users/bhuvan.ku/Desktop/ram/Conflict-Merge";
    private static final String USERNAME = "RVP199";
    private static final String PASSWORD = "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";

    public static void main(String[] args) {
        Scanner check = new Scanner(System.in);
        Integer test=check.nextInt();
        try {
            // Clone repository
            if(test==1){

                cloneRepository();
                // makeAndCommitChanges();
                // 2makeConflict();
                makeAndCommitChanges();
            }
            else if(test== 2){



            

            // Make some changes and commit
           

            // Push changes
            pushChanges();

            // Simulate conflict by making changes on remote and local repository
            
            // makeConflict();

            // Merge changes
            mergeChanges();
            }

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void cloneRepository() throws GitAPIException {
        Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(new File(LOCAL_PATH))
                .call();
        System.out.println("Repository cloned successfully.");
    }

    private static void makeAndCommitChanges() throws GitAPIException, IOException {
        try (Git git = Git.open(new File(LOCAL_PATH))) {
            // Make some changes to files
            // For demonstration purposes, let's just create a new file
            File newFile = new File(LOCAL_PATH + File.separator + "newFile.txt");
            newFile.createNewFile();

            // Add and commit changes
            git.add().addFilepattern(".").call();
            git.commit().setMessage("Commit message").call();
            System.out.println("Changes committed successfully.");
        }
    }

    private static void pushChanges() throws GitAPIException, IOException {
        try (Git git = Git.open(new File(LOCAL_PATH))) {
            git.push()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(USERNAME, PASSWORD))
                    .call();
            System.out.println("Changes pushed successfully.");
        }
    }

    private static void makeConflict() throws GitAPIException, IOException {
        // Simulate conflict by making changes on both local and remote repository
        // For demonstration purposes, let's just append different content to the same file
        try (Git git = Git.open(new File(LOCAL_PATH))) {
            File conflictingFile = new File(LOCAL_PATH + File.separator + "conflictingFile.txt");
            if (conflictingFile.exists()) {
                // Append some content
                appendToFile(conflictingFile, "Content from local repository");
                System.out.println("Content added to local repository.");
            }
        }

        // Simulate change on remote repository (e.g., by another user)
        // For demonstration purposes, let's assume the remote user added different content
        System.out.println("Simulating change on remote repository.");
    }

    // private static void mergeChanges() throws GitAPIException, IOException {
    //     try (Git git = Git.open(new File(LOCAL_PATH))) {
    //         // Fetch changes from remote repository
    //         git.fetch().setCredentialsProvider(new UsernamePasswordCredentialsProvider(USERNAME, PASSWORD)).call();
            
    //         // Perform merge
    //         MergeResult mergeResult = git.merge().include(git.getRepository().findRef("FETCH_HEAD")).call();
            
    //         if (mergeResult.getMergeStatus().isSuccessful()) {
    //             System.out.println("Merge successful.");
    //         } else if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
    //             // If merge resulted in conflicts, handle conflicts
    //             System.out.println("Merge conflict detected.");
    //             handleMergeConflict(git);
    //         } else {
    //             System.out.println("Merge failed.");
    //         }
    //     }
    // }
    private static void mergeChanges() throws GitAPIException, IOException {
        try (Git git = Git.open(new File(LOCAL_PATH))) {
            // Fetch changes from remote repository
            git.fetch().setCredentialsProvider(new UsernamePasswordCredentialsProvider(USERNAME, PASSWORD)).call();
            
            // Specify the commit or branch to merge into the current branch
            // For example, merge with the origin/master branch
            Ref head = git.getRepository().exactRef("refs/heads/main");
            System.out.println(head);
            
            // Perform merge
            if (head != null) {
                // git.merge().setStrategy(MergeStrategy.OURS).setCommit(true).call();
                MergeResult mergeResult = git.merge().include(head.getObjectId()).call();
                System.out.println("-----------------------------------------"+mergeResult);
                // handleMergeConflict(git);
                if (mergeResult.getMergeStatus().isSuccessful()) {
                    

                    System.out.println("Merge successful.");
                } else if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
                    // If merge resulted in conflicts, handle conflicts
                    System.out.println("Merge conflict detected.");
                    handleMergeConflict(git);
                } else {
                    System.out.println("Merge failed.");
                }
            } else {
                System.out.println("No HEAD reference found.");
            //   git.merge().setStrategy(MergeStrategy.OURS).setCommit(true).call();
                // System.out.println("Merge conflict resolved by accepting local changes.");
        
            }
        }
    }

    private static void handleMergeConflict(Git git) throws GitAPIException {
        // Resolve the merge conflict by accepting local changes (ours)
        git.merge().setStrategy(MergeStrategy.THEIRS).setCommit(true).call();
        System.out.println("Merge conflict resolved by accepting local changes.");
    }

    private static void appendToFile(File file, String content) throws IOException {
        // Append content to the file
        java.nio.file.Files.write(file.toPath(), content.getBytes(), java.nio.file.StandardOpenOption.APPEND);
    }
}