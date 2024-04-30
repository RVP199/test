package com.nvgit;
import java.io.File;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;

public class Checkout {
    public static void checkoutBranch(String localRepoPath, String branchName) {
        try {
      
            Git git = Git.open(new File(localRepoPath));

            CheckoutCommand checkoutCommand = git.checkout();

            checkoutCommand.setName(branchName);

            checkoutCommand.call();

            System.out.println("Checkout to branch '" + branchName + "' successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String localRepoPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo";
        String branchName = "main";
        checkoutBranch(localRepoPath, branchName);
    }
}
