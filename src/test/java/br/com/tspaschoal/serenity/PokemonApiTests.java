package br.com.tspaschoal.serenity;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.thucydides.core.annotations.Title;
import net.thucydides.core.annotations.UserStoryCode;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.junit.annotations.Concurrent;
import net.thucydides.junit.annotations.TestData;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(SerenityParameterizedRunner.class)
@Concurrent
@UserStoryCode("POK01")
public class PokemonApiTests {

    private final String name;

    private Actor pokemon;

    private EnvironmentVariables environmentVariables;

    public PokemonApiTests(String name) {
        this.name = name;
    }

    @TestData
    public static Collection<Object[]> testData() {
        Object[][] data = new Object[][]{
                {"charmander"},
                {"pikachu"},
                {"squirtle"},
                {"bulbasaur"},
                {"articuno"},
                {"lugia"},
                {"zapdos"},
                {"moltres"},
                {"mewtwo"},
                {"charizard"},
                {"blastoise"},
                {"venusaur"}
        };
        return Arrays.asList(data);
    }

    @Before
    public void setUp() {
        final String baseUrl = environmentVariables.optionalProperty("restapi.baseurl").get();
        pokemon = Actor.named("pokemon").whoCan(CallAnApi.at(baseUrl));
    }

    @Test
    public void shouldReturnValidPokemon() {
        pokemon.attemptsTo(Get.resource("pokemon/" + name));
        SerenityRest.lastResponse().then().statusCode(200).body("name", CoreMatchers.is(name));
    }

}
