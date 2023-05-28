package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Directory {
    File path;
    String name;
    List<Directory> subDirectories = new ArrayList<>();
    List<Image> images = new ArrayList<>();
    int depth;

    private boolean isImage(File file) // checks if given file is an image
    {
        String s = file.toString().toLowerCase();
        return s.contains(".jpg") || s.contains(".jpeg") || s.contains(".gif") || s.contains(".png");
    }

    private void acquireData(File currentDir) // fills up properties of this Directory
    {
        this.path = currentDir;
        this.name = path.toString().replace(path.getParentFile().toString(), "")
                .replace("/","");

        for(File file : Objects.requireNonNull(this.path.listFiles())) // goes through every sub-file
        {
            if(file.isDirectory()){
                this.subDirectories.add(new Directory(file, this.depth + 1));
            }
            else if(isImage(file)){
                this.images.add(new Image(file,depth));
            }
        }
    }

    private File generateHtmlFile() // generate file only, no content
    {
        File html = new File(this.path.toString() + "/index.html");

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

    private String getStartPage() // returns relative path to root index.html file
    {
        return "../".repeat(this.depth) + "index.html";
    }

    private String getIndexHtml(List<Directory> directories, int index) // returns rel. path to sub folder index.html
    {
        return directories.get(index).path.getName() + "/index.html";
    }

    private String getImageLink(int index) // returns rel. path to given image html
    {
        return images.get(index).path.getName().split("\\.")[0] + ".html";
    }

    private String getImageName(int index){ // returns name of given image
        return images.get(index).name;
    }

    public void createIndexHtml() // writes html content
    {
        File html = generateHtmlFile();
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head><style> img {width: 30%;}</style></head>\n");
        sb.append(String.format("<a href=\"%s\"><h1>Start Page</h1></a>\n", getStartPage()));
        sb.append("<hr>\n");

        sb.append("<h2>Directories</h2>\n<ul>\n");

        if(!this.path.equals(Main.startPage)){
            sb.append("<a href=\"../index.html\"><li><<</li></a>\n");
        }
        for(int i = 0; i < subDirectories.size(); i++){
            sb.append(String.format("<a href=\"%s\"><li>%s</li></a>\n", getIndexHtml(subDirectories,i), subDirectories.get(i).name));
        }

        sb.append("</ul><hr>\n");
        sb.append("<h2>Images</h2>\n<ul>\n");

        for(int i = 0; i < images.size(); i++)
        {
            sb.append(String.format("<li><a href=\"%s\">%s</li></a>\n", getImageLink(i), getImageName(i)));
        }
        sb.append("</ul>\n</body>\n</html>\n");

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

    public Directory(File currentDir, int depth)
    {
        this.depth = depth;
        acquireData(currentDir);

        this.createIndexHtml();
        for(int i = 0; i < images.size(); i++)
        {
            images.get(i).createHtml(images, i); // generate html for all images in current directory
        }
    }
}
