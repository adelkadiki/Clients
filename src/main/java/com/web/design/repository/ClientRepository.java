package com.web.design.repository;

import com.web.design.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

   Client findClientById(Long id);

}
