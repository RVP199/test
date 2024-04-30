// import java.io.File;
// import java.util.Scanner;

// import org.eclipse.jgit.api.AddCommand;
// import org.eclipse.jgit.api.CommitCommand;
// import org.eclipse.jgit.api.Git;
// import org.eclipse.jgit.api.PushCommand;
// import org.eclipse.jgit.api.errors.GitAPIException;
// import org.eclipse.jgit.transport.URIish;
// import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

// public class Test {
//     public static Git cloneRepository(String url, String directoryPath) {
//         try {
//             File directory = new File(directoryPath);
//             if (directory.exists() && directory.listFiles().length > 0) {
//                 // If the directory is not empty, create a new directory with a unique name
//                 directory = new File(directoryPath + "_new");
//                 System.out.println("Directory is not empty. Creating a new directory: " + directory.getAbsolutePath());
//             }
            
//             Git git = Git.cloneRepository()
//                .setURI(url)
//                .setDirectory(directory)
//                .call();
//             System.out.println("Repository cloned successfully!");
//             return git;
//         } catch (GitAPIException e) {
//             System.err.println("Error cloning repository: " + e.getMessage());
//             return null;
//         }
//     }

//     public static void addFiles(Git git, String... filePaths) {
//         try {
//             AddCommand add = git.add();
//             for (String filePath : filePaths) {
//                 add.addFilepattern(filePath);
//             }
//             add.call();
//             System.out.println("Files added to the index successfully!");
//         } catch (Exception e) {
//             System.err.println("Error adding files to the index: " + e.getMessage());
//         }
//     }

//     public static void commitChanges(Git git, String[] filePaths, String commitMessage) {
//         try {
//             AddCommand add = git.add();
//             for (String filePath : filePaths) {
//                 add.addFilepattern(filePath);
//             }
//             add.call();
//             System.out.println("Files added to the index successfully!");

//             CommitCommand commit = git.commit();
//             commit.setMessage(commitMessage);
//             commit.call();
//             System.out.println("Changes committed successfully!");
//         } catch (Exception e) {
//             System.err.println("Error adding files to the index or committing changes: " + e.getMessage());
//         }
//     }

//     public static void pushChanges(Git git, String remoteRepoUrl, String username, String password) {
//         try {
//             URIish remoteURI = new URIish(remoteRepoUrl);
//             PushCommand pushCommand = git.push();
//             pushCommand.setRemote(remoteURI.toString())
//                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
//             pushCommand.call();
//             System.out.println("Push successful");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         String gitUrl = "https://github.com/RVP199/nvdemo.git";
//         String dirPath = "C:/Users/bhuvan.ku/Desktop/Conflict";//remove
//         String[] filesToAdd = {"xsxs.txt"};
//         String commitMessage = "xsxs.txt";
//         String remoteRepoUrl = "https://github.com/RVP199/nvdemo.git";//remove
//         String username = "RVP199";// to change it so that it can take automaticaly
//         String password = "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";// to change it so that it can take automaticaly
    
//         Git git = cloneRepository(gitUrl, dirPath);

//         Integer buttonInput;
//         Scanner input = new Scanner(System.in);
//         System.out.println("Please choose 1 : clone / 2 : push ");
//         buttonInput=input.nextInt();



//         if (buttonInput == 1){
//             addFiles(git, filesToAdd);
//         }

//         else if (buttonInput == 2){
//         commitChanges(git, filesToAdd, commitMessage);
//         pushChanges(git, remoteRepoUrl, username, password);
//         }
//     }
// }
