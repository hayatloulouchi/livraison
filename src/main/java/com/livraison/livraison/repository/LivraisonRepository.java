package com.livraison.livraison.repository;

import com.livraison.livraison.model.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    List<Livraison> findByLivreurAndStatut(String livreur, String statut);

}