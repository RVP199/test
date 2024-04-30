package com.nvgit;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Push {
    public static void pushChanges(String localRepoPath, String username, String password) {
        try {
            // Open the local repository
            Git git = Git.open(new File(localRepoPath));

            // Create push command
            PushCommand pushCommand = git.push();

            // Set up credentials
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));

            // Perform the push operation
            pushCommand.call();

            System.out.println("Push successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Local repository path
        String localRepoPath = "C:/Users/bhuvan.ku/Desktop/trust";

        // GitHub username and personal access token
        String username = "RVP199";
        String password = "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";

        // Call pushChanges method
        pushChanges(localRepoPath, username, password);
    }
}