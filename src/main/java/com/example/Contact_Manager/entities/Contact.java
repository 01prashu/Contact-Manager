    package com.example.Contact_Manager.entities;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.*;
    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.stereotype.Controller;
    import java.util.Date;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class Contact {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Contact_Id")
        private int c_id;
        private String firstName;
        private String lastName;
        @Column(name = "Phone")
        private String mb_no;
        @Column(name = "email")
        private String email;
        @Column(name = "Address")
        private String address;
        @Column(name = "Work")
        private String work;
        @Column(name = "Relation")
        private String relation;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Temporal(TemporalType.DATE)
        private Date dob;
        @Column(name = "More Information")
        private String description;
        @JoinColumn(name = "UserId")
        @ManyToOne
        @JsonBackReference
        private Myuser user;
    }
