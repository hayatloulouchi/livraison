package com.livraison.livraison.controller;

import com.livraison.livraison.model.Utilisateur;
import com.livraison.livraison.repository.UtilisateurRepository;

import org.springframework.http.ResponseEntity;
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
public ResponseEntity<?> login(@RequestBody Utilisateur u){

    Utilisateur user = repo.findByUsernameAndPassword(
            u.getUsername(),
            u.getPassword()
    );

    if(user == null){
        return ResponseEntity.status(401).body("Login incorrect");
    }

    return ResponseEntity.ok(user);
}
}