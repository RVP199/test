package com.nvgit;

import java.io.File;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.Ref;

public class Listofbranches {
    public static void listBranches(String localRepoPath) {
        try {
            Git git = Git.open(new File(localRepoPath));

            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

            System.out.println("Branches:");
            for (Ref branch : branches) {
                String branchName = branch.getName();
                System.out.println(branchName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String localRepoPath = "C:/Users/bhuvan.ku/Desktop/hj/test";

        listBranches(localRepoPath);
    }
}
