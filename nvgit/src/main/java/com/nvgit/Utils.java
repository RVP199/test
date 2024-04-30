package com.nvgit;
import java.io.File;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;

public class Utils {
    // Constants for configuration
    public static final String REPOSITORY_PATH = "C:/Users/bhuvan.ku/Desktop/hj/test";
    public static final String REMOTE_REPO_URL = "https://github.com/RVP199/test.git";

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
            Git.init().setDirectory(new File(localRepoPath)).call();
            System.out.println("Initialized empty Git repository at " + localRepoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getUntrackedFiles(Git git) {
        try {
            Status status = git.status().call();
            return status.getUntracked();
        } catch (NoWorkTreeException | GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addFiles(Git git, Set<String> filePaths) {
        try {
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

    public static List<Ref> getBranches(Git git) {
        try {
            return git.branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addRemote(String remoteName, String remoteUrl, Git git) {
        try {
            RemoteAddCommand remoteAddCommand = git.remoteAdd();
            remoteAddCommand.setName(remoteName);
            remoteAddCommand.setUri(new URIish(remoteUrl));
            remoteAddCommand.call();
            System.out.println("Remote '" + remoteName + "' added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Open the repository or initialize if it doesn't exist
        Git git = openRepository(REPOSITORY_PATH);
        if (git == null) {
            System.out.println("Initializing repository...");
            initializeRepository(REPOSITORY_PATH);
            git = openRepository(REPOSITORY_PATH);
        }

        if (git != null) {
            // Add remote repository
            addRemote("origin", REMOTE_REPO_URL, git);

            // Pull changes from remote repository
            pullChanges(git);

            // Get untracked files
            Set<String> untrackedFiles = getUntrackedFiles(git);
            if (untrackedFiles != null && !untrackedFiles.isEmpty()) {
                // Add untracked files to the index
                addFiles(git, untrackedFiles);
            } else {
                System.out.println("No untracked files to add.");
            }

            // Display branches
            System.out.println("List of branches:");
            List<Ref> branches = getBranches(git);
            if (branches != null) {
                for (Ref branch : branches) {
                    System.out.println(branch);
                }
            }
        } else {
            System.err.println("Unable to proceed due to repository opening error.");
        }
    }

    public static void pullChanges(Git git) {
        try {
            PullCommand pullCommand = git.pull();
            pullCommand.call();
            System.out.println("Pull successful");
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
