package book.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
    String id;
    String name;

    public String toString(){
        return name;
    }
}
