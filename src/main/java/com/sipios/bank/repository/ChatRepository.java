package com.sipios.bank.repository;

import com.sipios.bank.model.Chat;
import com.sipios.bank.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}