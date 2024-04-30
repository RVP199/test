package com.nvgit;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.StashCreateCommand;
import org.eclipse.jgit.lib.Repository;

public class Stash {
    public static void stashChanges(String localRepoPath) {
        try {
            Git git = Git.open(new File(localRepoPath));
            Repository repository = git.getRepository();

            StashCreateCommand stashCommand = git.stashCreate();

            
            stashCommand.call();

            System.out.println("Stash successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        String localRepoPath = "";
        stashChanges(localRepoPath);
    }
}
