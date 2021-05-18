package book.manager.entity;

import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

@Data
public class Book {
    String title;
    String author;
    String desc;
    int year;

    public String toString(){
        return title+" ["+author+","+year+"]";
    }
}
