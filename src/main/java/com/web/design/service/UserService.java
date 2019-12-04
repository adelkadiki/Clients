package com.web.design.service;

import com.web.design.model.Client;
import com.web.design.model.User;
import com.web.design.repository.ClientRepository;
import com.web.design.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ClientRepository repository;


    public Client saveClient(Client client){

        Client update= repository.save(client);
        return  update;
    }
}
