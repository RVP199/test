package com.nvgit;
import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Pull {
    public static void pullChanges(String localRepoPath, String remoteRepoUrl, String username, String password) {
        try {
            Git git = Git.open(new File(localRepoPath));

            if (git.getRepository().getConfig().getString("remote", "origin", "url") == null) {
                git.remoteAdd()
                   .setName("origin")
                   .setUri(new URIish(remoteRepoUrl))
                   .call();
            }

            PullCommand pullCommand = git.pull();

            pullCommand.setRemoteBranchName("main")
                       .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));

            pullCommand.call();

            System.out.println("Pull successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String localRepoPath = "C:\\Users\\bhuvan.ku\\Desktop\\gittest\\nvdemo";

        String remoteRepoUrl = "https://github.com/RVP199/localrepo.git";
        String username = "RVP199";
        String password = "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";

        pullChanges(localRepoPath, remoteRepoUrl, username, password);
    }
}
