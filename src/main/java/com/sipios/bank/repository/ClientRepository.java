package com.sipios.bank.repository;

import com.sipios.bank.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class ClientRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<User> getClients(String userid) {
        List<User> clients = manager.createNativeQuery("SELECT * FROM user WHERE  advisor_id = " + userid, User.class)
            .getResultList();
        return clients;
    }
}
