package com.kagiso.pokemon.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kagiso.pokemon.controller.PokemonController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.kagiso.pokemon.dto.PokemonDTO;
import com.kagiso.pokemon.service.PokemonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PokemonServiceImplementation implements PokemonService {
    private static final Logger logger = LogManager.getLogger(PokemonController.class);
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${pokemon.api.base.url}")
    private String pokemonApiUrl;

    @Value("${pokemon.image.url}")
    private String baseImageUrl;

    @Override
    public ResponseEntity<?> getPokemonByName(String pokemonName) {

        logger.info("Beginning execution of API: getPokemonByName(..)");

        PokemonDTO pokemonDTO;
        Request request = new Request.Builder()
                .url(pokemonApiUrl+"/"+pokemonName)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();
                if (responseBody.equals("Not Found")) { return ResponseEntity.notFound().build(); }

                JsonNode jsonNode = objectMapper.readTree(responseBody);
                pokemonDTO = new PokemonDTO(jsonNode);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(pokemonDTO);
    }

    @Override
    public ResponseEntity<?> getListOfPokemon(int limit, int offset) {

        logger.info("Beginning execution of API: getPokemonByName(..)");

        List<PokemonDTO> pokemonDTOList;
        Request request = new Request.Builder()
                .url(pokemonApiUrl+"?limit="+limit+"&offset="+offset)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();

                JsonNode jsonNode = objectMapper.readTree(responseBody);
                pokemonDTOList = mapListOfPokemon(jsonNode.get("results"), (long) offset);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(pokemonDTOList);
    }

    public List<PokemonDTO> mapListOfPokemon(JsonNode resultsNode, Long offsetId) {
        List<PokemonDTO> pokemonDTOList = new ArrayList<>();
        AtomicReference<Long> pokemonId = new AtomicReference<>(offsetId+1);

        resultsNode.forEach(node -> {
            PokemonDTO pokemonDTO = new PokemonDTO(pokemonId.get(), node.get("name").asText(), baseImageUrl);
            pokemonDTOList.add(pokemonDTO);
            pokemonId.set(pokemonId.get() + 1L);
        });
        return pokemonDTOList;
    }
}
