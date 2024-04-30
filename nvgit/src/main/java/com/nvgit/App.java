package com.nvgit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) 
    {
        String gitUrl = "https://github.com/RVP199/nvdemo.git";

        String dirPath =  "C:\\Users\\bhuvan.ku\\Desktop\\xyz";

        cloneRepositoryUsingHttsUrl(gitUrl, dirPath);
       
    
    }

public static void cloneRepositoryUsingHttsUrl(String url, String directoryPath){

    try {
        Git.cloneRepository()
        .setURI(url)
        .setDirectory(new File(directoryPath))
        .call();
    } catch (GitAPIException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}


