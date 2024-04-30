import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.List;

public class Gson {
    public static void main(String[] args) {
        // Path to the local Git repository
        String repositoryPath = "C:/Users/bhuvan.ku/Desktop/hj/test";

        // Open the Git repository
        try (Git git = Git.open(new File(repositoryPath))) {
            // List branches
            List<Ref> branches = git.branchList().call();
            System.out.println("List of branches:");
            for (Ref branch : branches) {
                System.out.println(branch.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
