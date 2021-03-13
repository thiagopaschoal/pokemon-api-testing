package br.com.tspaschoal.serenity;

import br.com.tspaschoal.serenity.models.UserProfile;
import com.github.javafaker.Faker;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.*;
import static org.hamcrest.CoreMatchers.*;

import net.serenitybdd.screenplay.rest.interactions.Put;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class UserProfileTests {

    private Actor user;

    private static Faker faker;

    @BeforeClass
    public static void setUpClass() {
        faker = new Faker();
    }

    @Before
    public void setUp() {
        final String baseUrl = "https://reqres.in/api";
        user = Actor.named("user").whoCan(CallAnApi.at(baseUrl));
    }

    @Test
    @Title("deve criar um perfil com sucesso")
    public void shouldCreateAnProfileSuccessfully() {
        final String name = faker.name().name();
        final String job = faker.job().title();
        final UserProfile userProfile = new UserProfile(name, job);

        user.attemptsTo(Post.to("/users").with(request ->
                request.header("Content-Type", "application/json").body(userProfile)));

        user.should(seeThatResponse(response -> response.statusCode(201).body("id", notNullValue())));
    }

    @Test
    @Title("deve retornar todos os usuários da base")
    public void shouldReturnAllUsers() {
        user.attemptsTo(Get.resource("/users"));
        user.should(seeThatResponse(response -> response.statusCode(200)
                .body("total", is(12))
                .body("total_pages", is(2))
        ));
    }

    @Test
    @Title("deve retornar apenas um usuário em especifico")
    public void shouldReturnSingleUser() {
        final String emailExpected = "george.bluth@reqres.in";
        final String firstNameExpected = "George";
        final String lastNameExpected = "Bluth";
        final String avatarExpected = "https://reqres.in/img/faces/1-image.jpg";

        user.attemptsTo(Get.resource("/users/1"));

        user.should(seeThatResponse(response -> response
                .statusCode(200)
                .body("data.first_name", is(firstNameExpected))
                .body("data.last_name", is(lastNameExpected))
                .body("data.email", is(emailExpected))
                .body("data.avatar", is(avatarExpected))));
    }

    @Test
    @Title("deve retornar 404 quando não encontrar um usuário em especifico")
    public void shouldReturn404NotFoundUser() {
        user.attemptsTo(Get.resource("/users/13"));
        user.should(seeThatResponse(response -> response.statusCode(404)));
    }

    @Test
    @Title("deve alterar um perfil completo com sucesso")
    public void shouldUpdateAnUserSuccessfully() {
        UserProfile userProfileUpdate = new UserProfile("steve rogers", "avenger");
        user.attemptsTo(Put.to("/users/2").with(request ->
                request.header("Content-Type", "application/json")
                        .body(userProfileUpdate)));

        user.should(seeThatResponse(response -> response
                .statusCode(200)
                .body("name", is("steve rogers"))
                .body("job", is("avenger"))
                .body("updatedAt", notNullValue())));
    }

    @Test
    @Title("deve deletar um perfil com sucesso")
    public void shouldDeleteAnUserSuccessfully() {
        user.attemptsTo(Delete.from("/users/2"));
        user.should(seeThatResponse(response -> response.statusCode(204)));
    }
}
