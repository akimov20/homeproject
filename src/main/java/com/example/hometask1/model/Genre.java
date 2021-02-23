package com.example.hometask1.model;

import com.example.hometask1.model.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dim_genre")
@Data
@ToString(of = {"id", "name"})
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"books","count"})
public class Genre extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;
    @Column(name = "genre_name", nullable = false)
    private String name;

    @Formula(value = "(SELECT coalesce(count(link.genre_id), 0) from dim_genre as g\n" +
            "    left join book_genre_link as link on link.genre_id = g.genre_id\n" +
            "  where g.genre_id = genre_id  group by link.genre_id)")
    private int count;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "genres")
    private Set<Book> books = new HashSet<>();
}
