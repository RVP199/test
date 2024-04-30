package com.nvgit;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class GitManager {
    // Constants for configuration
    public static String REPOSITORY_PATH = "C:/Users/bhuvan.ku/Desktop/gitnv/test";
    public static String COMMIT_MESSAGE = "mmm";
    public static URIish REMOTE_REPO_URL;
    public static Git git;
    public static String USERNAME = "RVP199";
    public static String PASSWORD = "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";

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

    public static void addFiles(Git git, Set<String> filePaths) {
        try {
            git.add().addFilepattern(String.join(".", filePaths)).call();
            System.out.println("Files added to the index successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index: " + e.getMessage());
        }
    }

    public static void commitChanges(Git git, Set<String> filePaths, String commitMessage) {
        try {
            git.commit().setMessage(commitMessage).call();
            System.out.println("Changes committed successfully!");
        } catch (Exception e) {
            System.err.println("Error committing changes: " + e.getMessage());
        }
    }

    public static void pushChanges(Git git, URIish rEMOTE_REPO_URL2, String username, String password) {
        try {
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
            System.out.println("Push successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getUntrackedFiles(Git git) {
        try {
            org.eclipse.jgit.api.Status status = git.status().call();
            return status.getUntracked();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<String> getTrackedFiles(Git git) {
        try {
            org.eclipse.jgit.api.Status status = git.status().call();
            return status.getAdded();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addRemote(String remoteName, URIish rEMOTE_REPO_URL2, Git git) {
        try {
            git.remoteAdd().setName(remoteName).setUri(rEMOTE_REPO_URL2).call();
            System.out.println("Remote '" + remoteName + "' added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentBranch(Git git) {
        try {
            return git.getRepository().getBranch();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void listBranches(Git git) {
        try {
            git.branchList().call().forEach(ref -> System.out.println(ref.getName()));
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> checkConflictedFiles(Git git) {
        try {
            org.eclipse.jgit.api.Status status = git.status().call();
            return status.getConflicting();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void acceptIncomingChanges(Git git, Set<String> conflictedFiles) {
    try {

        MergeResult mergeResult = git.merge().include(git.getRepository().resolve("refs/heads/branch-to-merge")).setStrategy(MergeStrategy.RESOLVE).call();
 
                
                    if (mergeResult.getMergeStatus().isSuccessful()) {
                        System.out.println("Merge successful!");
                    } else {
                        System.out.println("Merge failed: " + mergeResult.getMergeStatus().toString());
                    }
                
} catch (Exception e) {
    e.printStackTrace();
}
        
}
        

    public static void acceptCurrentChanges(Git git, Set<String> conflictedFiles) {
        try {
            MergeResult mergeResult = git.merge().include(git.getRepository().resolve("refs/heads/branch-to-merge")).setStrategy(MergeStrategy.OURS).call();
            
            if (mergeResult.getMergeStatus().isSuccessful()) {
                System.out.println("Merge successful!");
            } else {
                System.out.println("Merge failed: " + mergeResult.getMergeStatus().toString());
                
            }


        } catch (GitAPIException | RevisionSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void acceptBothChanges(Git git, Set<String> conflictedFiles) {
        try {
            
            MergeResult mergeResult = git.merge().include(git.getRepository().resolve("refs/heads/branch-to-merge")).setStrategy(MergeStrategy.RESOLVE).call();
 
            // Check merge status
            if (mergeResult.getMergeStatus().isSuccessful()) {
                System.out.println("Merge successful!");
            } else {
                System.out.println("Merge failed: " + mergeResult.getMergeStatus().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkoutFiles(Git git, Set<String> files, CheckoutCommand.Stage stage) throws GitAPIException {
        CheckoutCommand checkoutCommand = git.checkout();
        files.forEach(file -> checkoutCommand.addPath(file).setStage(stage));
        checkoutCommand.call();
    }

    public static void main(String[] args) {
        // Open the repository or initialize if it doesn't exist
        git = openRepository(REPOSITORY_PATH);
        if (git == null) {
            System.out.println("Initializing repository...");
            initializeRepository(REPOSITORY_PATH);
            git = openRepository(REPOSITORY_PATH);
        }
    
        if (git != null) {
            // Add remote repository
            addRemote("origin", REMOTE_REPO_URL, git);
            System.out.println("Current branch: " + getCurrentBranch(git));
            System.out.println("List of branches:");
            listBranches(git);
    
            // Pull changes from remote repository
        //   pullChanges(git);
    
           // Check for conflicted files
            Set<String> conflictedFiles = checkConflictedFiles(git);
            if (!conflictedFiles.isEmpty()) {
                System.out.println("Conflicts detected.");
                System.out.println("Conflicted files:");
                conflictedFiles.forEach(System.out::println);
    
                // Resolving conflicts based on user input
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose conflict resolution option:");
                System.out.println("1. Accept incoming changes");
                System.out.println("2. Accept current changes");
                System.out.println("3. Accept both changes");
                int choice = scanner.nextInt();
    
                switch (choice) {
                    case 1:
                        acceptIncomingChanges(git, conflictedFiles);
                        break;
                    case 2:
                        acceptCurrentChanges(git, conflictedFiles);
                        break;
                    case 3:
                        acceptBothChanges(git, conflictedFiles);
                        break;
                    default:
                        System.out.println("Invalid choice. No action taken.");
                        break;
                }
            } else {
                System.out.println("No conflicts detected.");
            }
    
            // Add untracked files, commit changes, and push to remote
               Set<String> untrackedFiles = getUntrackedFiles(git);
              addFiles(git, untrackedFiles);
            // commitChanges(git, getTrackedFiles(git), COMMIT_MESSAGE);
           // pushChanges(git, REMOTE_REPO_URL, USERNAME, PASSWORD);
        } else {
            System.err.println("Unable to proceed due to repository opening error.");
        }
    }
    
    public static void pullChanges(Git git) {
        try {
            git.pull().call();
            System.out.println("Pull successful");
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}