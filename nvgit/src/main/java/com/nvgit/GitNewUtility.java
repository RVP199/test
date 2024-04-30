package com.nvgit;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GitNewUtility {
    // Constants for configuration
    public static String REPOSITORY_PATH = "C:/Users/bhuvan.ku/Desktop/conflict/Conflict-Merge";
    public static String COMMIT_MESSAGE = "mmm";
    public static String REMOTE_REPO_URL;
    public static Git git;

    public static Git openRepository(String repositoryPath) {
        try {
            return Git.open(new File(repositoryPath));
        } catch (Exception e) {
            System.err.println("Error opening repository: " + e.getMessage());
            return null;
        }
    }

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

    public static void addFiles(Git git, Set<String> filePaths) {
        try {
            AddCommand add = git.add();
            for (String filePath : filePaths) {
                System.out.println(filePath);
                add.addFilepattern(filePath);
            }
            add.call();
            System.out.println("Files added to the index successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index: " + e.getMessage());
        }
    }

    public static void commitChanges(Git git, Set<String> filePaths, String commitMessage) {
        try {
            AddCommand add = git.add();
            for (String filePath : filePaths) {
                add.addFilepattern(filePath);
            }
            add.call();
            System.out.println("Files added to the index successfully!");

            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage);
            commit.call();
            System.out.println("Changes committed successfully!");
        } catch (Exception e) {
            System.err.println("Error adding files to the index or committing changes: " + e.getMessage());
        }
    }

    public static void pushChanges(Git git, String remoteRepoUrl, String token) {
        try {
            URIish remoteURI = new URIish(remoteRepoUrl);
            PushCommand pushCommand = git.push();
            pushCommand.setRemote(remoteURI.toString())
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("", token)); // Use token for authentication
            pushCommand.call();
            System.out.println("Push successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getUntrackedFiles() {
        Status status;
        try {
            status = git.status().call();
            Set<String> untrackedFiles = status.getUntracked();
            System.out.println("Untracked files:");
            for (String untrackedFile : untrackedFiles) {
                System.out.println(untrackedFile);
            }
            return untrackedFiles;
        } catch (NoWorkTreeException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<String> getTrackedFiles() {
        Status status;
        try {
            status = git.status().call();
            System.out.println(git.status().call().getModified());
            Set<String> untrackedFiles = status.getModified();

            System.out.println("Tracked files:");
            for (String untrackedFile : untrackedFiles) {
                System.out.println(untrackedFile);
            }
            return untrackedFiles;
        } catch (NoWorkTreeException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // Open the repository or initialize if it doesn't exist
        git = openRepository(REPOSITORY_PATH);
        if (git == null) {
            initializeRepository(REPOSITORY_PATH);
            git = openRepository(REPOSITORY_PATH);
        }

        if (git != null) {
            String token = retrieveToken(); // Retrieve token from JSON file
            if (token == null) {
                try {
                    startHttpServer();
                    redirectToGitHub();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Token retrieved from JSON: " + token);
                commitAndPushChanges(token);
            }

            Set<String> untrackedFiles = getUntrackedFiles();
            if (untrackedFiles != null && !untrackedFiles.isEmpty()) {

            } else {
                System.out.println("No untracked files found.");
            }

            // Resolve conflicts
            resolveConflicts(git);
        } else {
            System.err.println("Unable to proceed due to repository opening error.");
        }
    }


    public static void resolveConflicts(Git git) {
        try {
            // Get the status of the repository
            Status status = git.status().call();

            // Check if there are any unmerged paths (indicating conflicts)
            Set<String> conflicts = status.getConflicting();

            if (!conflicts.isEmpty()) {
                System.out.println("Conflicts detected.");
                System.out.println("Conflicted files:");

                // Print the conflicted files
                for (String conflictedFile : conflicts) {
                    System.out.println(conflictedFile);
                }

                // Resolving conflicts based on user input
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose conflict resolution option:");
                System.out.println("1. Accept current changes");
                System.out.println("2. Accept incoming changes");
                System.out.println("3. Accept both changes");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        acceptCurrentChanges(git, conflicts);
                        break;
                    case 2:
                        acceptIncomingChanges(git, conflicts);
                        break;
                    case 3:
                        acceptBothChanges(git, conflicts);
                        break;
                    default:
                        System.out.println("Invalid choice. No action taken.");
                        break;
                }

                System.out.println("Conflicts resolved successfully.");
            } else {
                System.out.println("No conflicts detected.");
            }

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private static void acceptCurrentChanges(Git git, Set<String> files) throws GitAPIException {
        CheckoutCommand checkoutCommand = git.checkout();
        for (String file : files) {
            checkoutCommand.addPath(file).setStage(CheckoutCommand.Stage.OURS).call();
        }
    }

    private static void acceptIncomingChanges(Git git, Set<String> files) throws GitAPIException {
        CheckoutCommand checkoutCommand = git.checkout();
        for (String file : files) {
            checkoutCommand.addPath(file).setStage(CheckoutCommand.Stage.THEIRS).call();
        }
    }

    private static void acceptBothChanges(Git git, Set<String> files) throws GitAPIException {
        acceptCurrentChanges(git, files);
        acceptIncomingChanges(git, files);
    }

    private static void redirectToGitHub() throws IOException {
        String authorizationURL = "https://github.com/login/oauth/authorize" +
                "?client_id=" + URLEncoder.encode("d868f0e1b6aba6672a7e", "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/github/callback", "UTF-8") +
                "&scope=repo";

        java.awt.Desktop.getDesktop().browse(URI.create(authorizationURL));
    }

    private static String getAccessToken(String code) throws IOException {
        String tokenURL = "https://github.com/login/oauth/access_token" +
                "?client_id=" + URLEncoder.encode("d868f0e1b6aba6672a7e", "UTF-8") +
                "&client_secret=" + URLEncoder.encode("7710c2b3fb55d780e9bb0c11130a3399d38fc1bb", "UTF-8") +
                "&code=" + URLEncoder.encode(code, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/github/callback", "UTF-8");

        HttpURLConnection connection = (HttpURLConnection) new URL(tokenURL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                String jsonResponse = response.toString();
                return new JSONObject(jsonResponse).getString("access_token");
            }
        } else {
            System.out.println("Failed to get access token. Response code: " + responseCode);
            return null;
        }
    }

    private static void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/github/callback", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String code = query.substring(query.indexOf("code=") + 5);
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.close();
            handleGitHubCallback(code, null);
        });
        server.setExecutor(null);
        server.start();
    }

    public static void handleGitHubCallback(String code, String token) {
        try {
            if (token == null) {
                token = getAccessToken(code);
                if (token == null) {
                    System.out.println("Failed to retrieve access token.");
                    return;
                }
                storeTokenInJSON(token);
                System.out.println("Access token stored in JSON: " + token);
            }

            if (token != null) {
                commitAndPushChanges(token); // Push changes using the retrieved or stored token
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void commitAndPushChanges(String token) {
        Set<String> trackedFiles = getTrackedFiles();
        if (trackedFiles != null && !trackedFiles.isEmpty()) {
            commitChanges(git, trackedFiles, COMMIT_MESSAGE);
            pushChanges(git, REMOTE_REPO_URL, token);
        } else {
            System.out.println("No tracked files found.");
        }
    }

    private static void storeTokenInJSON(String accessToken) {
        JSONObject credentials = new JSONObject();
        credentials.put("token", accessToken);

        try (FileWriter file = new FileWriter("github_token.json")) {
            file.write(credentials.toString());
            System.out.println("GitHub token stored successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String retrieveToken() {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get("github_token.json")));
            JSONObject jsonObject = new JSONObject(jsonString);
            String value = jsonObject.getString("token");
            System.out.println("Token retrieved from JSON: " + value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
