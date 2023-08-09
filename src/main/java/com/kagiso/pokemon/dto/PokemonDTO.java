package com.kagiso.pokemon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PokemonDTO {
    private final Long id;
    private final String name;
    private String imageUrl;
    private List<String> types;
    private Double weight;
    private Double height;
    private Long baseExperience;
    private List <PokemonStatDTO> stats;

    public PokemonDTO(JsonNode jsonNode) {
        this.id = jsonNode.get("id").asLong();
        this.name = jsonNode.get("name").asText();
        this.weight = jsonNode.get("weight").asDouble();
        this.height = jsonNode.get("height").asDouble();
        this.baseExperience = jsonNode.get("base_experience").asLong();
        this.imageUrl = jsonNode.get("sprites").get("other")
                .get("dream_world")
                .get("front_default").asText();

        setStats(jsonNode.get("stats"));
        setTypes(jsonNode.get("types"));
    }

    public PokemonDTO(Long id, String name, String baseImageUrl) {
        this.id = id;
        this.name = name;

        setImageUrl(baseImageUrl, id);
    }

    private void setStats(JsonNode statsNode) {
        this.stats = new ArrayList<>();

        statsNode.forEach(node -> {
            if (isRequiredStat(node.get("stat").get("name").asText())) {
                PokemonStatDTO stat = new PokemonStatDTO();
                stat.setName(node.get("stat").get("name").asText());
                stat.setBaseStat(node.get("base_stat").asLong());

                this.stats.add(stat);
            }
        });
    }

    public boolean isRequiredStat(String statName) {
        return Objects.equals(statName, "hp") || Objects.equals(statName, "attack") ||
                Objects.equals(statName, "defense") || Objects.equals(statName, "speed");
    }

    public void setTypes(JsonNode typesNode) {
        this.types = new ArrayList<>();
        typesNode.forEach(node -> types.add(node.get("type").get("name").asText()));
    }

    public void setImageUrl(String baseImageUrl, Long pokemonId) {
        this.imageUrl = baseImageUrl+"/"+pokemonId.toString()+".svg";
    }
}
