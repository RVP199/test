package com.nvgit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import com.sun.net.httpserver.HttpServer;

public class user {
    // Constants for configuration
    public static String REPOSITORY_PATH = "C:\\Users\\bhuvan.ku\\Desktop\\gitnv\\test";
    public static String COMMIT_MESSAGE = "mmm";
    public static String REMOTE_REPO_URL;
    public static Git git;
    private static final String CLIENT_ID = "4580827ea65fca1e182c";
    private static final String CLIENT_SECRET = "6b9759d5dfc033b737b3ee2f5b8ab17e9627ea3c";
    private static final String REDIRECT_URI = "http://localhost:8080/github/callback";
    private static final String AUTHORIZATION_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";

    public static void main(String[] args) {
        // Start the HTTP server
        try {
            startHttpServer();
            redirectToGitHub();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open the repository or initialize if it doesn't exist
        git = openRepository(REPOSITORY_PATH);
        if (git == null) {
            System.out.println("Im here");
            initializeRepository(REPOSITORY_PATH);
            git = openRepository(REPOSITORY_PATH);
        }

        if (git != null) {
            addRemote("origin", REMOTE_REPO_URL);
            System.out.println("Current branch: " + getCurrentBranch());
            System.out.println("List of branches:");
            List<Ref> branches = getBranches();
            if (branches != null) {
                for (Ref branch : branches) {
                    System.out.println(branch);
                }
            }
            // Uncomment the following lines to add files, commit changes, and push changes
         addFiles(git, getUntrackedFiles());
            // commitChanges(git, getTrackedFiles(), COMMIT_MESSAGE);
            // pushChanges(git, REMOTE_REPO_URL, USERNAME, PASSWORD);
        } else {
            System.err.println("Unable to proceed due to repository opening error.");
        }
    }

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
            Set<String> trackedFiles = status.getAdded();
            System.out.println("Tracked files:");
            for (String trackedFile : trackedFiles) {
                System.out.println(trackedFile);
            }
            return trackedFiles;
        } catch (NoWorkTreeException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addRemote(String remoteName, String remoteUrl) {
        try {
            RemoteAddCommand remoteAddCommand = git.remoteAdd();
            remoteAddCommand.setName(remoteName);
            remoteAddCommand.setUri(new URIish(remoteUrl));
            remoteAddCommand.call();
            System.out.println("Remote '" + remoteName + "' added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentBranch() {
        try {
            return git.getRepository().getBranch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Ref> getBranches() {
        try {
            return git.branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/github/callback", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String code = query.substring(query.indexOf("code=") + 5);
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.close();
            handleGitHubCallback(code);
        });
        server.setExecutor(null);
        server.start();
    }

    public static void handleGitHubCallback(String code) {
        try {
            String accessToken = getAccessToken(code);
            if (accessToken != null) {
                System.out.println("Access Token: " + accessToken);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void redirectToGitHub() throws IOException {
        String authorizationURL = AUTHORIZATION_URL +
                "?client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                "&scope=repo"; 

     
        java.awt.Desktop.getDesktop().browse(URI.create(authorizationURL));
    }

    private static String getAccessToken(String code) throws IOException {
        String tokenURL = TOKEN_URL +
                "?client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") +
                "&code=" + URLEncoder.encode(code, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8");

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
                return jsonResponse.split(":")[1].replace("\"", "").trim();
            }
        } else {
            System.out.println("Failed to get access token. Response code: " + responseCode);
            return null;
        }
    }
}
