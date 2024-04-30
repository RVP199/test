package com.nvgit;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.*;
import java.io.*;
import java.util.*;
 
public class ConflictResolution {
    public static void main(String[] args) {
        String localRepoPath = "C:/Users/bhuvan.ku/Desktop/Conflict/Conflict-Merge";
        String branch = "main"; // Change this to your branch name
 
        try (Git git = Git.open(new File(localRepoPath))) {
            Repository repository = git.getRepository();
 
            // Get the status of the repository
            Status status = git.status().call();
 
            // Check if there are any unmerged paths (indicating conflicts)
            Set<String> conflicts = status.getConflicting();
 
            if (!conflicts.isEmpty()) {
                System.out.println("Conflicts detected.");
                System.out.println("Conflicted files:");
 
                // Print the conflicted files
                for (String conflictedFile : conflicts) {
                    System.out.println(conflictedFile);
                }
 
                // Resolving conflicts based on user input
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose conflict resolution option:");
                System.out.println("1. Keep current changes");
                System.out.println("2. Keep incoming changes");
                System.out.println("3. Keep both changes");
                int choice = scanner.nextInt();
 
                switch (choice) {
                    case 1:
                        // Keep current changes
                        checkoutFiles(git, conflicts, CheckoutCommand.Stage.THEIRS);
                        break;
                    case 2:
                        // Keep incoming changes
                        checkoutFiles(git, conflicts, CheckoutCommand.Stage.OURS);
                        break;
                    case 3:
                        // Keep both changes
                        checkoutFiles(git, conflicts, CheckoutCommand.Stage.THEIRS);
                        checkoutFiles(git, conflicts, CheckoutCommand.Stage.OURS);
                        break;
                    default:
                        System.out.println("Invalid choice. No action taken.");
                        break;
                }
 
                System.out.println("Conflicts resolved successfully.");
            } else {
                System.out.println("No conflicts detected.");
            }
 
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
 
    // Helper method to checkout conflicted files
    private static void checkoutFiles(Git git, Set<String> files, CheckoutCommand.Stage stage) throws GitAPIException {
        CheckoutCommand checkoutCommand = git.checkout();
        for (String file : files) {
            checkoutCommand.addPath(file).setStage(stage).call();
        }
    }
}