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

    // 📦 Livraisons EN_ATTENTE d'un livreur
    @GetMapping("/livreur/{livreur}")
    public List<Livraison> getByLivreur(@PathVariable String livreur) {
        return repo.findByLivreurAndStatut(livreur, "EN_ATTENTE");
    }

    // 📦 Toutes les livraisons
    @GetMapping
    public List<Livraison> getAll() {
        return repo.findAll();
    }

    // 🔍 Une livraison par ID
    @GetMapping("/{id}")
    public Livraison getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // ➕ Ajouter (API mobile)
    @PostMapping
    public Livraison ajouter(@RequestBody Livraison livraison) {

        livraison.setStatut("EN_ATTENTE");

        return repo.save(livraison);
    }

    // ✏ Modifier
    @PutMapping("/{id}")
    public Livraison update(@PathVariable Long id, @RequestBody Livraison updated) {

        Livraison l = repo.findById(id).orElse(null);

        if (l != null) {
            l.setClient(updated.getClient());
            l.setVille(updated.getVille());
            l.setLivreur(updated.getLivreur());
            l.setTelephone(updated.getTelephone());
            l.setNumeroBc(updated.getNumeroBc());
            l.setDateLivraison(updated.getDateLivraison());
            repo.save(l);
        }

        return l;
    }

    // ❌ Supprimer
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }

    // ✅ Marquer TERMINÉE
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