package com.nvgit;
import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.CheckoutCommand;

public class Checkoutnew {
    public static void checkoutNewBranch(String localRepoPath, String newBranchName) {
        try {
            Git git = Git.open(new File(localRepoPath));

            git.branchCreate()
               .setName(newBranchName)
               .call();

            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(newBranchName);
            // checkoutCommand.setCreateBranch(true);
            checkoutCommand.call();

            System.out.println("Checked out new branch: " + newBranchName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String localRepoPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo";

        String newBranchName = "feature-2"; 

        checkoutNewBranch(localRepoPath, newBranchName);
    }
}
