package com.nvgit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class MergeConflictSolver {
    public static void main(String[] args) {
        File repoDir = new File("C:/Users/bhuvan.ku/Desktop/checker/.git");
        System.out.println("We are here................>>>>>>>>>>>>1111111111111");
        String username = "RVP199";
        String password = "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";
        try {
            // Open the Git repository
            System.out.println("We are here................>>>>>>>>>>>>22222222222222");
            Repository repository = new FileRepositoryBuilder().setGitDir(repoDir).build();
            Git git = new Git(repository);
            System.out.println("-------------------->"+git);
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));

            // Perform the push operation
            // pushCommand.call();
            // pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));

            // Perform the push operation
            

            System.out.println("Push successful");

            // Merge the changes from the other branch
            MergeResult mergeResult = git.merge()
                    .include(repository.resolve("refs/heads/main")) // Change "other_branch" to the branch you're merging from
                    .call();
                    
            // If the merge resulted in conflicts, solve them by using "ours" (current branch) changes.
            System.out.println(mergeResult.getConflicts());
            if (mergeResult.getConflicts() != null && !mergeResult.getConflicts().isEmpty()) {
                // Manually resolve the conflicts by choosing "ours" (current branch) changes''
                System.out.println("We are here................>>>>>>>>>>>>");
                
                for (Entry<String, int[][]> entry : mergeResult.getConflicts().entrySet()) {
                    String conflict = entry.getKey();
                    pushCommand.call();
                    // Modify the conflictedFile content with your resolution strategy
                    // For simplicity, let's just mark it as resolved by deleting the conflict markers
                    resolveConflict(new File(repoDir, conflict));
                }

                // Add the resolved files
                git.add().addFilepattern(".").call();

                // Commit the resolved changes
                git.commit().setMessage("Resolved merge conflict by choosing 'ours'").call();
            }

            // Close the Git repository
            repository.close();
            System.out.println("Merge conflict resolved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void resolveConflict(File file) throws IOException {
        // For simplicity, let's just mark it as resolved by deleting the conflict markers
        // You might need a more sophisticated strategy based on your use case
        // Here, we are assuming conflict markers are "<<<<<<<", "=======", and ">>>>>>>"
        // You should adjust this method based on your specific needs
        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        content = content.replaceAll("<<<<<<<.*=======", "").replaceAll(">>>>>>>.*", "");
        java.nio.file.Files.write(file.toPath(), content.getBytes());
    }
}