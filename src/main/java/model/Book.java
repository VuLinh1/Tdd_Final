package model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Book {
    private Long  id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Double price;
    private int stockQuantity;
    private String description;



}
