// package com.nvgit;
// import org.eclipse.jgit.api.Git;
// import org.eclipse.jgit.api.MergeResult;
// import org.eclipse.jgit.lib.Repository;
// import org.eclipse.jgit.merge.MergeStrategy;

// // import org.eclipse.jgit.merge.getConflicts;

// import java.io.File;

// public class Conflict {

//     public static void main(String[] args) {
//         try {
//             // Open the repository
//             File repoDir = new File("");
//             Git git = Git.open(repoDir);
//             Repository repository = git.getRepository();
//             System.out.println("----------------------------------------------------"+repository);

//             // Merge the branch with itself to check for conflicts


//             MergeResult mergeResult = git.merge()
//                     .setStrategy(MergeStrategy.RECURSIVE)
//                     .include(repository.resolve("main"))
//                     .call();
//                 System.out.println("--------------------------------------------------------"+mergeResult.getConflicts());

//             // Check if conflicts occurred
//             if (mergeResult.getConflicts() != null && !mergeResult.getConflicts().isEmpty()) {
//                 System.out.println("Conflicts detected.");
//                 // Print out the conflicted files
//                 System.out.println("Conflicted files:");


                

//                 // for (String conflict : mergeResult.getConflicts() ) {
//                 //     System.out.println("conflicted file: " + conflict);
//                 // }
//             } else {
//                 System.out.println("No conflicts detected.");
//             }

//             // Close the repository
//             repository.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
