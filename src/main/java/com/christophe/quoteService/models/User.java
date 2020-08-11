package com.christophe.quoteService.models;

import com.christophe.quoteService.component.BCryptManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @NonNull @Getter @Setter private String firstName;
    @NonNull @Getter @Setter private String lastName;
    @NonNull @Getter @Setter private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NonNull @Getter private String password;
    @Column(unique = true)
    @NonNull @Getter @Setter private String username;



    @OneToOne
    @Getter
    private User userManager;

    public void setUserManager(User userManager) {
        userManager.getRoles().add(Role.MANAGER);
        this.userManager = userManager;
    }

    @ElementCollection(targetClass=Role.class, fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Collection<Role> roles = new ArrayList<Role>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roles = StringUtils.collectionToCommaDelimitedString(getRoles().stream()
                .map(Enum::name).collect(Collectors.toList()));
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        return authorities;
    }

    @NonNull @Getter(onMethod = @__( @JsonIgnore )) @Setter private boolean isAccountNonExpired;
    @NonNull @Getter(onMethod = @__( @JsonIgnore )) @Setter private boolean isAccountNonLocked;
    @NonNull @Getter(onMethod = @__( @JsonIgnore )) @Setter private boolean isCredentialsNonExpired;
    @NonNull @Getter(onMethod = @__( @JsonIgnore )) @Setter private boolean isEnabled;

    public void setPassword(String password) {
        if (!password.isEmpty()) {
            this.password = BCryptManager.passwordEncoder().encode(password);
        }
    }
}
