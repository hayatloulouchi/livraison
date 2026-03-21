package com.livraison.livraison.repository;

import com.livraison.livraison.model.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    List<Livraison> findByLivreur(String livreur);

    List<Livraison> findByClientContainingIgnoreCase(String client);

    List<Livraison> findByNumeroBcContainingIgnoreCase(String numeroBc);

    // ✅ AJOUT IMPORTANT
    List<Livraison> findByLivreurAndStatut(String livreur, String statut);
}
    