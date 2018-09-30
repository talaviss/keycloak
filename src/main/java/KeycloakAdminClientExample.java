import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.sql.Time;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class KeycloakAdminClientExample {

    public static void main(String[] args) {

        String serverUrl = "http://localhost:8080/auth";
        String realm = "apigw";
        String clientId = "account";
        String clientSecret = "33fdf3ab-1afe-47db-ac08-1b82d19c7846";

//		// Client "idm-client" needs service-account with at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
//		Keycloak keycloak = KeycloakBuilder.builder() //
//				.serverUrl(serverUrl) //
//				.realm(realm) //
//				.grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
//				.clientId(clientId) //
//				.clientSecret(clientSecret).build();

        // User "javaland" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
//        Keycloak keycloak = KeycloakBuilder.builder() //
//                .serverUrl(serverUrl) //
//                .realm(realm) //
//                .grantType(OAuth2Constants.PASSWORD) //
//                .clientId(clientId) //
//                .clientSecret(clientSecret) //
//                .username("admin") //
//                .password("Pa55w0rd") //
//                .build();

        Keycloak kc = Keycloak.getInstance("http://localhost:8080/auth", "master", "admin", "Pa55w0rd", "admin-cli");
        Random rnd = new Random(System.currentTimeMillis());
        int id = rnd.nextInt();
        String username ="tester" + id;
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName("First" + id);
        user.setLastName("Last" + id);
        user.setEmail("tom+tester" + id +"@tdlabs.local");
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

        // Get realm
        RealmResource realmResource = kc.realm(realm);
        UsersResource userRessource = realmResource.users();

        // Create user (requires manage-users role)
        int count = userRessource.count();
        Response response = userRessource.create(user);
        System.out.println("Repsonse: " + response.getStatusInfo());
        System.out.println(response.getLocation());
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        System.out.printf("User created with userId: %s%n", userId);

        // Get realm role "tester" (requires view-realm role)
        RoleRepresentation kc1RealmRole = realmResource.roles()//
                .get("kc-role1").toRepresentation();
        RoleRepresentation kc2RealmRole = realmResource.roles()//
                .get("kc-role2").toRepresentation();
        RoleRepresentation kc3RealmRole = realmResource.roles()//
                .get("kc-role3").toRepresentation();
        // Assign realm role tester to user
        userRessource.get(userId).roles().realmLevel() //
                .add(Arrays.asList(kc1RealmRole, kc2RealmRole, kc3RealmRole));

        // Get client
       /* ClientRepresentation app1Client = realmResource.clients() //
                .findByClientId("app-javaee-petclinic").get(0);

        // Get client level role (requires view-clients role)
        RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()) //
                .roles().get("user").toRepresentation();

        // Assign client level role to user
        userRessource.get(userId).roles() //
                .clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("test");

        // Set password credential
        userRessource.get(userId).resetPassword(passwordCred);*/

    }
}
