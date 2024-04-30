package com.nvgit;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;

public class Init {
    public static void initializeRepository(String localRepoPath) {
        try {
            
            InitCommand initCommand = Git.init();

            
            initCommand.setDirectory(new File(localRepoPath));

           
            initCommand.call();

            System.out.println("Initialized empty Git repository at " + localRepoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String localRepoPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo.git";

        initializeRepository(localRepoPath);
    }
}
