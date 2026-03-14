package com.livraison.livraison.controller;

import com.livraison.livraison.model.Utilisateur;
import com.livraison.livraison.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    UtilisateurRepository repo;

    // LOGIN pour Android (POST)
    @PostMapping("/login")
    public Utilisateur login(@RequestBody Utilisateur user) {

        return repo.findByUsernameAndPassword(
                user.getUsername(),
                user.getPassword()
        );

    }

    // LOGIN pour tester dans le navigateur (GET)
    @GetMapping("/login")
    public Utilisateur loginTest(
            @RequestParam String username,
            @RequestParam String password
    ) {

        return repo.findByUsernameAndPassword(username, password);

    }

}