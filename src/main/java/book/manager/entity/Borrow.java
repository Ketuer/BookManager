package book.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Borrow {
    String name;
    String title;
    String author;
    String desc;
    Date start;
    Date end;
}
