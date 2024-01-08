package com.leglone;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main {
    public static void main(String[] args) throws IOException {
        String request = "hello world";
        String search = Request2Search(request);
        System.out.println("Search: "+request);
        Elements elem = getAnsLinks(search);
        Write2File(elem);
    }
    static String Request2Search(String string){
        String ans = "https://www.google.com/search?q=";
        return ans.concat(string.replace(' ', '+'));
    }
    static void Write2File(Elements elem){
        for (int i=0; i!= elem.size(); i++){
            SearchTrhread th = new SearchTrhread(elem, i);
            th.start();
        }
    }
    static Elements getAnsLinks(String search){
        Document doc = null;
        Elements links = null;
        try {
            doc = Jsoup.connect(search).get();
            links = doc.select("a[jsname=UWckNb]");
        } catch (IOException e) {
            System.out.println("Exception in getAnsLinks: " + e);
        }
        return links;
    }
}

class SearchTrhread extends Thread{
    public SearchTrhread(Elements elem, int i){
        this.elem = elem;
        this.i = i;
    }
    
    @Override
    public void run() {
        // Download page:
            String pageHtml;
            try {
                String url = elem.get(i).absUrl("href");
                pageHtml = Jsoup.connect(url).get().toString();
            } catch (Exception e) {
                pageHtml ="<html> <h1>Page not found<h1></html>";
            }
            // Write to file
            try {
                File file = new File(path+"/File_"+i+".html");
                if (!file.exists())
                    file.createNewFile();
                PrintWriter pw = new PrintWriter(file);                
                pw.println(pageHtml);
                pw.close();
            } catch (Exception e) {
                System.out.println("Exception in Write2File: File_"+i);
            }
    }
    Elements elem;
    int i;
    String path = "src/main/resources/result";
}