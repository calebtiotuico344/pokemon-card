package ph.edu.dlsu.lbycpei.pokemoncardapp.model;

import ph.edu.dlsu.lbycpei.pokemoncardapp.config.AppConfig;
import ph.edu.dlsu.lbycpei.pokemoncardapp.utils.CSVFileLoader;

import java.util.*;

public class PokemonModel {
    private final List<Pokemon> pokemonList;
    private final Random random;

    public PokemonModel() {
        this.pokemonList = new ArrayList<>();
        this.random = new Random();
        loadPokemonFromCSV();
    }

    private void loadPokemonFromCSV() {

        String[] csvData = CSVFileLoader.loadCSVFromResources(AppConfig.DATA_PATH);

        // Sample data - in real implementation, this would read from actual CSV file
        String[] sampleData = {
                "Squirtle,9.0kg,0.5m,0.55,0.35,0.56,Water",
                "Wartortle,22.5kg,1.0m,0.50,0.37,0.60,Water",
                "Blastoise,85.5kg,1.6m,0.83,0.78,0.79,Water",
                "Charmander,8.5kg,0.6m,0.52,0.43,0.39,Fire",
                "Charmeleon,19.0kg,1.1m,0.64,0.58,0.58,Fire",
                "Charizard,90.5kg,1.7m,0.84,0.78,0.78,Fire-Flying",
                "Bulbasaur,6.9kg,0.7m,0.49,0.49,0.45,Grass-Poison",
                "Ivysaur,13.0kg,1.0m,0.62,0.63,0.60,Grass-Poison",
                "Venusaur,100.0kg,2.0m,0.82,0.83,0.80,Grass-Poison",
                "Pikachu,6.0kg,0.4m,0.55,0.40,0.35,Electric",
                "Raichu,30.0kg,0.8m,0.90,0.55,0.60,Electric",
                "Geodude,20.0kg,0.4m,0.80,0.100,0.40,Rock-Ground",
                "Graveler,105.0kg,1.0m,0.95,0.115,0.55,Rock-Ground",
                "Golem,300.0kg,1.4m,1.00,1.00,0.80,Rock-Ground",
                "Magikarp,10.0kg,0.9m,0.29,0.85,0.20,Water",
                "Gyarados,235.0kg,6.5m,1.0,0.79,0.95,Water-Flying"
        };

        createPokemons(concatenate(csvData, sampleData));
    }

    public String[] concatenate(String[] array1, String[] array2) {
        if (array1 == null) return array2 == null ? new String[0] : array2.clone();
        if (array2 == null) return array1.clone();

        String[] result = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    private void createPokemons(String[] lines){
        for (String line : lines) {
            try {
                Pokemon pokemon = PokemonFactory.createPokemonFromCSV(line);
                pokemonList.add(pokemon);
            } catch (Exception e) {
                System.err.println("Error parsing Pokemon data: " + line);
            }
        }
    }

    public List<Pokemon> getAllPokemon() {
        return new ArrayList<>(pokemonList);
    }

    public Pokemon getRandomPokemon() {
        if (pokemonList.isEmpty()) return null;
        return pokemonList.get(random.nextInt(pokemonList.size()));
    }

    public Pokemon searchPokemon(String name) {
        return pokemonList.stream()
                .filter(pokemon -> pokemon.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean removePokemon(String name) {
        return pokemonList.removeIf(pokemon -> pokemon.getName().equalsIgnoreCase(name));
    }

    public int getPokemonCount() {
        return pokemonList.size();
    }
}
