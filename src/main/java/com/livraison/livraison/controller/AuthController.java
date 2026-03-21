package com.livraison.livraison.controller;

import com.livraison.livraison.model.Utilisateur;
import com.livraison.livraison.repository.UtilisateurRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UtilisateurRepository repo;

    public AuthController(UtilisateurRepository repo){
        this.repo = repo;
    }

    @PostMapping("/login")
public Utilisateur login(@RequestBody Utilisateur u){

    Utilisateur user = repo.findByUsername(u.getUsername());

    if(user != null && user.getPassword().equals(u.getPassword())){
        return user;
    }

    return null;
}
}