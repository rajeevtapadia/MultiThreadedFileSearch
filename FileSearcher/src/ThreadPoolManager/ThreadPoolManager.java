package ThreadPoolManager;

import Indexer.DirNode;
import Indexer.FileNode;
import Indexer.FileType;
import Indexer.Node;
import ThreadSafeQueue.ThreadSafeQueue;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ThreadPoolManager {
    private final ThreadSafeQueue<DirNode> q;
    private final int maxThreads;

    public ThreadPoolManager(int n) {
        this.maxThreads = n;
        this.q = new ThreadSafeQueue<DirNode>();
    }

    public void startThreadPool(DirNode root) {
        q.offer(root,100);
        ArrayList<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < maxThreads; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    DirNode curr = q.poll(100);
                    if (curr == null) {
                        return; // Exit the thread if the queue is empty after waiting for a while
                    }

                    File currDir = new File(curr.absolutePath);
                    try {
                        for (File file : Objects.requireNonNull(currDir.listFiles())) {
                            if (file.isDirectory()) {
                                DirNode subdirectory = new DirNode(file.getName(), FileType.DIR, file.getAbsolutePath(), new ArrayList<>());
                                curr.addChild(subdirectory);
                                q.offer(subdirectory,100);
                            } else if (file.isFile()) {
                                FileNode subfile = new FileNode(file.getName(), FileType.FILE, file.getAbsolutePath());
                                curr.addChild(subfile);
                            }
                        }


                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            threadList.add(t);
            t.start();
        }
        System.out.println(threadList.size());
        // Wait for all threads to finish
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("All threads have finished execution");
    }
}
