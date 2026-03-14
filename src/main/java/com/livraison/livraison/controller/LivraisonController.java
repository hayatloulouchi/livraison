package com.livraison.livraison.controller;

import com.livraison.livraison.model.Livraison;
import com.livraison.livraison.repository.LivraisonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livraisons")
@CrossOrigin
public class LivraisonController {

    private final LivraisonRepository repo;

    public LivraisonController(LivraisonRepository repo) {
        this.repo = repo;
    }

    // 📦 Livraisons d'un livreur (EN_ATTENTE)
    @GetMapping("/livreur/{livreur}")
    public List<Livraison> getByLivreur(@PathVariable String livreur) {

        return repo.findByLivreurAndStatut(livreur,"EN_ATTENTE");

    }

    // 📦 Toutes les livraisons (Admin)
    @GetMapping
    public List<Livraison> getAll() {

        return repo.findAll();

    }

    // ✅ Marquer livraison TERMINÉE
    @PostMapping("/livrer/{id}")
    public Livraison livrer(@PathVariable Long id) {

        Livraison l = repo.findById(id).orElse(null);

        if (l != null) {
            l.setStatut("TERMINEE");
            repo.save(l);
        }

        return l;
    }

}