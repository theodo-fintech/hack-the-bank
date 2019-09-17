package com.sipios.bank.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class User implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String pinCode;

    private String message;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
        name = "users_chats",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "chat_id", referencedColumnName = "id"))
    private Collection<Chat> chats;

    @OneToMany(mappedBy="advisor")
    private Collection<User> clients;

    @ManyToOne
    private User advisor;

    private Double money;

    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    //standard getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Chat> getChats() {
        return chats;
    }

    public void setChats(Collection<Chat> chats) {
        this.chats = chats;
    }

    public Collection<User> getClients() {
        return clients;
    }

    public void setClients(Collection<User> clients) {
        this.clients = clients;
    }

    public User getAdvisor() {
        return advisor;
    }

    public void setAdvisor(User advisor) {
        this.advisor = advisor;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
