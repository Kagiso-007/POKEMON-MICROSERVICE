package com.kagiso.pokemon.controller;

import com.kagiso.pokemon.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"https://pokemon-web-c1e2a.web.app",
        "https://pokemon-web-c1e2a.firebaseapp.com"})
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/pokemons")
    public ResponseEntity<?> getListOfPokemon(@RequestParam int limit, @RequestParam int offset) {
        return pokemonService.getListOfPokemon(limit, offset);
    }

    @GetMapping("/pokemons/{pokemonName}")
    public ResponseEntity<?> getPokemonByName(@PathVariable String pokemonName) {
        return pokemonService.getPokemonByName(pokemonName);
    }
}
