package com.livraison.livraison.repository;

import com.livraison.livraison.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long>{

    Utilisateur findByUsernameAndPassword(String username,String password);
    Utilisateur findByUsername(String username);

}