package book.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Account {
    int id;
    String name;
    String sex;
    String role;
    String header;
    String password;
    String note;
}
