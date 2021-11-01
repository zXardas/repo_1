package dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString
@Getter
@Setter
public class Product {
    Integer id;
    String title;
    Integer price;
    String categoryTitle;
}
