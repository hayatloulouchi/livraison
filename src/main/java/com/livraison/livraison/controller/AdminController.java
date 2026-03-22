package com.livraison.livraison.controller;

import com.livraison.livraison.model.Livraison;
import com.livraison.livraison.repository.LivraisonRepository;
import com.livraison.livraison.service.ExcelImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class AdminController {

    private final LivraisonRepository repo;

    public AdminController(LivraisonRepository repo){
        this.repo = repo;
    }

    // 📋 PAGE ADMIN + RECHERCHE + FILTRE + STATS
   @GetMapping("/admin")
public String admin(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String livreur,
        @RequestParam(required = false) String tiers,
        @RequestParam(required = false) String ville,
        Model model){

    List<Livraison> livraisons = repo.findAll();

    // 🔍 Recherche (client + BC)
    if(keyword != null && !keyword.isEmpty()){
        livraisons = livraisons.stream()
                .filter(l ->
                        (l.getClient() != null && l.getClient().toLowerCase().contains(keyword.toLowerCase())) ||
                        (l.getNumeroBc() != null && l.getNumeroBc().toLowerCase().contains(keyword.toLowerCase()))
                )
                .toList();
    }

    // 👤 Filtre livreur
    if(livreur != null && !livreur.isEmpty()){
        livraisons = livraisons.stream()
                .filter(l -> l.getLivreur() != null &&
                        l.getLivreur().equalsIgnoreCase(livreur))
                .toList();
    }

    // 🏢 Filtre tiers
    if(tiers != null && !tiers.isEmpty()){
        livraisons = livraisons.stream()
                .filter(l -> l.getTiers() != null &&
                        l.getTiers().equalsIgnoreCase(tiers))
                .toList();
    }

    // 📍 Filtre ville
    if(ville != null && !ville.isEmpty()){
        livraisons = livraisons.stream()
                .filter(l -> l.getVille() != null &&
                        l.getVille().equalsIgnoreCase(ville))
                .toList();
    }

    // 📊 Stats
    long total = livraisons.size();

    long enAttente = livraisons.stream()
            .filter(l -> "EN_ATTENTE".equals(l.getStatut()))
            .count();

    long terminee = livraisons.stream()
            .filter(l -> "TERMINEE".equals(l.getStatut()))
            .count();

    model.addAttribute("livraisons", livraisons);

    model.addAttribute("keyword", keyword);
    model.addAttribute("livreur", livreur);
    model.addAttribute("tiers", tiers);
    model.addAttribute("ville", ville);

    model.addAttribute("total", total);
    model.addAttribute("enAttente", enAttente);
    model.addAttribute("terminee", terminee);

    return "admin";
}

    // ➕ AJOUTER
    @PostMapping("/admin/ajouter")
    public String ajouter(Livraison livraison){

        livraison.setStatut("EN_ATTENTE");
        repo.save(livraison);

        return "redirect:/admin";
    }

    // ❌ SUPPRIMER
    @GetMapping("/admin/supprimer/{id}")
    public String supprimer(@PathVariable Long id){

        repo.deleteById(id);

        return "redirect:/admin";
    }

    // ✏ EDIT
    @GetMapping("/admin/edit/{id}")
    public String editLivraison(@PathVariable Long id, Model model){

        Livraison livraison = repo.findById(id).orElse(null);
        model.addAttribute("livraison", livraison);

        return "modifier";
    }

    // 💾 UPDATE
   @PostMapping("/admin/update")
public String updateLivraison(Livraison livraison){

    Livraison existing = repo.findById(livraison.getId()).orElse(null);

    if(existing != null){
        existing.setTiers(livraison.getTiers());
        existing.setNumeroBc(livraison.getNumeroBc());
        existing.setClient(livraison.getClient());
        existing.setTelephone(livraison.getTelephone());
        existing.setVille(livraison.getVille());
        existing.setLivreur(livraison.getLivreur());
        existing.setDateLivraison(livraison.getDateLivraison());

        repo.save(existing);
    }

    return "redirect:/admin";
}


    // ✔ LIVRÉ
    @PostMapping("/admin/livrer/{id}")
    public String livrer(@PathVariable Long id){

        Livraison l = repo.findById(id).orElse(null);

        if(l != null){
            l.setStatut("TERMINEE");
            repo.save(l);
        }

        return "redirect:/admin";
    }

    // 📥 IMPORT EXCEL
    
    @PostMapping("/admin/import")
public String importExcel(@RequestParam("file") MultipartFile file){

    try {
        List<Livraison> list = ExcelImportService.importExcel(file.getInputStream());

        System.out.println("AVANT SAVE = " + list.size());

        repo.saveAll(list);

        System.out.println("APRES SAVE = " + repo.count());

    } catch (Exception e) {
        e.printStackTrace();
    }

    return "redirect:/admin";
}
}