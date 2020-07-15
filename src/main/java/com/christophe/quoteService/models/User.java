package com.christophe.quoteService.models;

import lombok.*;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @NonNull @Getter @Setter private  String firstName;
    @NonNull @Getter @Setter private String lastName;
    @NonNull @Getter @Setter private String email;

    @OneToOne
    @Getter
    @Setter
    private User userManager;

}
