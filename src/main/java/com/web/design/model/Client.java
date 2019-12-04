package com.web.design.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "owner")
    private String owner;

    @Column(name = "email")
    private String email;

    @Column(name = "notices")
    private String notices;

    @Column(name = "depDate")
    private Date depDate;

    @Column(name = "expDate")
    private Date expDate;

    @Column(name = "hostComp")
    private String hostComp;

    @Column(name = "cpUsername")
    private String cpUsername;

    @Column(name = "cpPassword")
    private String cpPassword;

    @Column(name = "cpUrl")
    private String cpUrl;
}
