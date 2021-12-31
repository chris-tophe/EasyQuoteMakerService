package com.christophe.quoteService.models;

import com.christophe.quoteService.component.BCryptManager;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    private Long id;

    @NotBlank
    @NonNull
    @Getter
    @Setter
    private String firstName;
    @NotBlank
    @NonNull
    @Getter
    @Setter
    private String lastName;
    @Email
    @NonNull
    @Getter
    @Setter
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NonNull
    @Getter
    private String password;
    @Column(unique = true)
    @NotBlank
    @NonNull
    @Getter
    @Setter
    private String username;

    @ManyToOne()
    @Getter
    private User userManager;

    public void setUserManager(User userManager) {
        userManager.getRoles().add(Role.MANAGER);
        this.userManager = userManager;
    }

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
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

    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private boolean isAccountNonExpired;
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private boolean isAccountNonLocked;
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private boolean isCredentialsNonExpired;
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private boolean isEnabled;

    public void setPassword(String password) {
        if (!password.isEmpty()) {
            this.password = BCryptManager.passwordEncoder().encode(password);
        }
    }
}
