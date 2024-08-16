    package com.example.Contact_Manager.entities;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.validator.constraints.UniqueElements;

    import java.util.Date;
    import java.util.List;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class Myuser{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "UserId")
        private long Id;

        @Column(name = "FirstName")
        private String firstName;

        @Column(name="LastName")
        private String lastName;


        @Column(name = "Email",unique = true)
        private String email;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Temporal(TemporalType.DATE)
        @Column(name="DOB")
        private Date dob;

        @Column(name = "Gender")
        private String gender;

        @Column(name = "Address")
        private String address;

        @Column(name = "About")
        private String about;

        @Column(name = "Password")
        @NotNull
        private String password;

        @NotNull
        @Column(name = "Repassword")
        private String repassword;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Contact> contacts;

    }
