import Indexer.DirNode;
import Indexer.IndexManager;
import Indexer.IndexPrinter;
import SearchManager.SearchManager;

import java.nio.file.Path;
import java.util.List;
import Serializer.SerializationManager;


public class Main {
    public static void main(String[] args) throws Exception {
//        if(args.length < 1) {
//            throw new Exception("Pass proper arguments");
//        }

//        String searchTerm = args[0];
        long startTime = System.currentTimeMillis();
        IndexManager im = new IndexManager(Path.of("C:/Users/Administrator/Documents/GitHub/MultiThreadedFileSearch").toAbsolutePath().toString());


        long difference = System.currentTimeMillis() - startTime;
        double diff = (difference/1000);
        System.out.println("Indexing done in "+diff+" seconds");


        //serialize the root node after indexing.
        SerializationManager sm = new SerializationManager();
        sm.serialize(im.getHead(),"save.txt");



        im.setHead(null);

        IndexPrinter ip = new IndexPrinter();

        im.setHead(sm.deserialize("save.txt"));
//        ip.printIndex(im.getHead());



//        System.out.println("Single Threaded DFS");
//        SearchManager.singleThreadedDFS(im,"hello.txt");



        List<String> res = SearchManager.multiThreadedBFS(im, "hello.txt");
        System.out.println(res);
    }

}