package com.web.design.controller;



import com.web.design.jwt.AuthenticationRequest;
import com.web.design.jwt.AuthenticationResponse;
import com.web.design.jwt.JwtUtil;
import com.web.design.jwt.SysUserDetailsService;
import com.web.design.model.Client;
import com.web.design.model.Role;
import com.web.design.model.User;
import com.web.design.repository.ClientRepository;
import com.web.design.repository.UserRepository;
import com.web.design.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private SysUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;





    @GetMapping("/index")
    public String welcome(){
        return "home page";
    }



    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user){

        if(repository.findByUsername(user.getUsername())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

       BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        return new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/addclient")
    public ResponseEntity<?> addClient(@RequestBody Client client){

        if(clientRepository.findClientById(client.getId())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }

        return new ResponseEntity<>(clientRepository.save(client), HttpStatus.CREATED);

    }

    @GetMapping("/allclients")
    public ResponseEntity<?> findAllUsers(){
        return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findclient/{id}")
    public ResponseEntity<Client> getClient(@PathVariable(value = "id") Long cid){

        Optional<Client> client = clientRepository.findById(cid);
            Client clt = client.get();

        //return new ResponseEntity<>(client, HttpStatus.OK);
        return ResponseEntity.ok().body(clt);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateClient(@RequestBody Client client){

        Client updatedClient = clientRepository.findClientById(client.getId());

        if(updatedClient == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


            return new ResponseEntity<>(userService.saveClient(client) , HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id){

        //Client client = clientRepository.findClientById(id);

        clientRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody User user){

        User update = repository.findUserById(user.getId());

        return new ResponseEntity<>(repository.save(update) , HttpStatus.CREATED);

    }

}


