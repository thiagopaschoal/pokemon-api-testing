package br.com.tspaschoal.serenity;

import br.com.tspaschoal.serenity.models.UserRegistration;
import com.github.javafaker.Faker;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.*;
import static org.hamcrest.CoreMatchers.*;

import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class UserRegistrationTests {

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
    @Title("deve registrar um usuário com sucesso")
    public void shouldRegisterUserSuccessfully() {

        final String email = faker.bothify("????##@reqres.in");
        final String password = faker.number().digits(5);

        UserRegistration userRegistration = new UserRegistration(email, password);
        user.attemptsTo(Post.to("/register")
                .with(request -> request.header("Content-Type", "application/json")
                        .body(userRegistration)));

        user.should(seeThatResponse(response -> response.statusCode(201).body("id", notNullValue())));
    }

    @Test
    @Title("não deve registrar um usuário sem senha")
    public void shouldNotRegisterUserWithoutPassword() {

        final String email = faker.bothify("????##@reqres.in");

        UserRegistration userRegistration = new UserRegistration(email, null);
        user.attemptsTo(Post.to("/register")
                .with(request -> request.header("Content-Type", "application/json")
                        .body(userRegistration)));

        user.should(seeThatResponse(response -> response.statusCode(400).body("error", is("Missing password"))));

    }

    @Test
    @Title("não deve registrar um usuário sem email")
    public void shouldNotRegisterUserWithoutEmail() {

        final String password = faker.number().digits(5);

        UserRegistration userRegistration = new UserRegistration(null, password);
        user.attemptsTo(Post.to("/register")
                .with(request -> request.header("Content-Type", "application/json")
                        .body(userRegistration)));

        user.should(seeThatResponse(response -> response.statusCode(400).body("error", is("Missing email or username"))));

    }
}
