package com.nvgit;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

public class Tag {
    public static void createTag(String localRepoPath, String tagName, String commitId) {
        try {
            Git git = Git.open(new File(localRepoPath));

            // RevCommit commit = git.getRepository().parseCommit(ObjectId.fromString(commitId));

            TagCommand tagCommand = git.tag();

            tagCommand.setName(tagName).setMessage("test");
                    

            tagCommand.call();

            System.out.println("Tag '" + tagName + "' created successfully for commit " + commitId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String localRepoPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo";
        String tagName = "v1.0.2";
        String commitId = "9a0084f"; 

        createTag(localRepoPath, tagName, commitId);
    }
}
