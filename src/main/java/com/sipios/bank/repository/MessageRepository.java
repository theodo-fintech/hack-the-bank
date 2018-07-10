package com.sipios.bank.repository;

import com.sipios.bank.model.Message;
import com.sipios.bank.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}