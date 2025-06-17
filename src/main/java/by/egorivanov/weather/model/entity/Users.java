package by.egorivanov.weather.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@ToString(exclude = "locations")
@Table(name = "users",schema = "weather")
public class Users implements BaseEntity<Integer> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;



    private String username;

    @Column(nullable = false)
    private String password;


    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Locations> locations = new ArrayList<>();
}
