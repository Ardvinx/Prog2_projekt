package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Image {
    File path;
    String name;
    int depth;

    private File generateHtmlFile() // generate file only, no content
    {
        File html = new File(this.path.toString().split("\\.")[0] + ".html");

        try {
            if(html.createNewFile()){
                System.out.println("HTML FILE CREATED: " + html);
            }
            else{
                System.out.println("HTML FILE ALREADY EXISTS, OVERWRITING: " + html);
            }
        }
        catch (IOException e) {
            System.err.println("An error occurred");
            e.printStackTrace();
            System.exit(1);
        }

        return html;
    }

    private String getNextPage(List<Image> images, int index) // return rel. path to next image
    {
        if(images.size()-1 == index){
            return images.get(index).path.getName().split("\\.")[0] + ".html";
        }
        //else
        return images.get(index+1).path.getName().split("\\.")[0] + ".html";
    }

    private String getPreviousPage(List<Image> images, int index) // returns rel. path to previous image
    {
        if(index == 0){
            return images.get(0).path.getName().split("\\.")[0] + ".html";
        }
        //else
        return images.get(index-1).path.getName().split("\\.")[0] + ".html";
    }

    private String getStartPage() // returns rel. path to root html
    {
        return "../".repeat(depth) + "index.html";
    }

    private String getIndexHtml() // returns rel. path to current index html
    {
        return "index.html";
    }

    public void createHtml(List<Image> images, int index) // generates html content
    {
        File html = generateHtmlFile();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head><style> img {width: 30%;}</style></head>\n");
        sb.append(String.format("<a href=\"%s\"><h1>Start Page</h1></a>\n", getStartPage()));
        sb.append("<hr>\n");
        sb.append(String.format("<a href=\"%s\">^^</a>\n", getIndexHtml()));
        sb.append(String.format("<p><a href=\"%s\"><<</a>", getPreviousPage(images,index)));
        sb.append(images.get(index).name);
        sb.append(String.format("<a href=\"%s\">>></a></p>", getNextPage(images,index)));
        sb.append("<body>\n");
        sb.append(String.format("<a href=\"%s\"><img src=\"%s\"></a>\n", getNextPage(images,index),this.path));
        sb.append("</body>\n</html>\n");
        try {
            FileWriter writer = new FileWriter(html);
            writer.write(sb.toString());
            writer.close();

        } catch (IOException e) {
            System.err.println("An error occurred");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Image(File path, int depth)
    {
        this.depth = depth;
        this.path = path;
        this.name = path.toString().replace(path.getParentFile().toString(), "")
                .replace("/","");
    }
}
