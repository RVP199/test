import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class TestAll {
  
    public static Git cloneRepository(String url, String directoryPath, String username, String password) {
        try {
            File directory = new File(directoryPath);
            if (directory.exists() && directory.listFiles().length > 0) {
                // If the directory is not empty, create a new directory with a unique name
                directory = new File(directoryPath + "_new");
                System.out.println("Directory is not empty. Creating a new directory: " + directory.getAbsolutePath());
            }
            
            Git git = Git.cloneRepository()
               .setURI(url)
               .setDirectory(directory)
               .call();
            System.out.println("Repository cloned successfully!");
            return git;
        } catch (GitAPIException e) {
            System.err.println("Error cloning repository: " + e.getMessage());
            return null;
        }
    }

    public static void addAllFiles(Git git) {
        try {
            AddCommand add = git.add();
            add.addFilepattern(".").call(); // Add all files in the current directory
            System.out.println("All files added to the index successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index: " + e.getMessage());
        }
    }

    public static void commitChanges(Git git, String commitMessage) {
        try {
            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage);
            commit.call();
            System.out.println("Changes committed successfully!");
        } catch (Exception e) {
            System.err.println("Error committing changes: " + e.getMessage());
        }
    }

    public static void pushChanges(Git git, String remoteRepoUrl, String username, String password) {
        try {
            URIish remoteURI = new URIish(remoteRepoUrl);
            PushCommand pushCommand = git.push();
            pushCommand.setRemote(remoteURI.toString())
                       .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
            pushCommand.call();
            System.out.println("Push successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String gitUrl = "https://github.com/RVP199/nvdemo.git";
        String dirPath = "C:/Users/bhuvan.ku/Desktop/bb";
        String commitMessage = "xsxs.txt";
        String remoteRepoUrl = "https://github.com/RVP199/nvdemo.git";
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please choose 1: clone / 2: push ");
        int buttonInput = input.nextInt();

        if (buttonInput == 1) {
            System.out.println("Enter your GitHub username: ");
            String username = input.next();
            System.out.println("Enter your GitHub password: ");
            String password = input.next();
            
            Git git = cloneRepository(gitUrl, dirPath, username, password);
            
            if (git != null) {
                addAllFiles(git);
                commitChanges(git, commitMessage);
                pushChanges(git, remoteRepoUrl, username, password);
            }
        } else if (buttonInput == 2) {
            System.out.println("Please provide your GitHub username and password used for cloning.");
            System.out.println("Username: ");
            String username = input.next();
            System.out.println("Password: ");
            String password = input.next();
            
            Git git = Git.open(new File(dirPath)); // Open existing repository
            if (git != null) {
                addAllFiles(git);
                commitChanges(git, commitMessage);
                pushChanges(git, remoteRepoUrl, username, password);
            }
        } else {
            System.out.println("Invalid input. Please choose 1 to clone or 2 to push.");
        }
    }
}
