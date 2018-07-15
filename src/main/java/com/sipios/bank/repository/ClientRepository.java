package com.sipios.bank.repository;

import com.sipios.bank.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ClientRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<User> getClients(String userid) {
        List<User> clients = manager.createNativeQuery("SELECT * FROM user WHERE  advisor_id = " + userid, User.class)
            .getResultList();

        return clients.stream().filter(user -> user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_SUPER_ADMIN"))).collect(Collectors.toList());
    }
}
