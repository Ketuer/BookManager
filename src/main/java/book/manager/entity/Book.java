package book.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

@Data
@Getter
@AllArgsConstructor
public class Book {
    String title;
    String author;
    String desc;
    int year;

    public String toString(){
        return title;
    }
}
