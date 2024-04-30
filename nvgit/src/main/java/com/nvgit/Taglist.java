package com.nvgit;
import java.io.File;
import java.util.Collection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

public class Taglist {
    public static void listTags(String localRepoPath) {
        try {
           
            Git git = Git.open(new File(localRepoPath));

           
            Collection<Ref> tagRefs = git.tagList().call();

           
            System.out.println("List of Tags:");
            for (Ref tagRef : tagRefs) {
                System.out.println(tagRef.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        String localRepoPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo";

        
        listTags(localRepoPath);
    }
}
