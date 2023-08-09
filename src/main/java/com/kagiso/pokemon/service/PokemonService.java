package com.kagiso.pokemon.service;

import org.springframework.http.ResponseEntity;

public interface PokemonService {
    public ResponseEntity<?> getPokemonByName(String pokemonName);
    public ResponseEntity<?> getListOfPokemon(int limit, int offset);
}
