package de.id4i.samples.java.routing;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.GUIDsApi;
import de.id4i.api.RoutingApi;
import de.id4i.api.model.GuidAlias;

import java.io.IOException;

import static de.id4i.samples.java.routing.Id4iApiUtils.newBearerToken;

/**
 * Id4i routing tutorial.
 * <p>
 * Please refer to https://backend.id4i.de/docs/reference/en/reference.html#_how_to_set_up_routing_for_guids to
 * see what's going on here.
 */
public class RoutingTutorial {

    private static final String ENV_API_KEY = "ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "ID4I_API_KEY_SECRET";
    private static final String LANGUAGE = "en";

    private final ApiClient routingTutorialClient = new ApiClient();
    private final GUIDsApi guidsApi;
    private final RoutingApi routing;

    public RoutingTutorial() {
        routingTutorialClient.setUserAgent("id4i-sample-routing");
        routingTutorialClient.setBasePath(Id4iApiUtils.BASE_PATH);
        guidsApi = new GUIDsApi(routingTutorialClient);
        routing = new RoutingApi(routingTutorialClient);
    }

    public static void main(String[] args) throws IOException, ApiException {

        RoutingTutorial app = new RoutingTutorial();
        app.start();
    }


    private void start() throws IOException, ApiException {
        String subject = System.getenv(ENV_API_KEY);
        String secret = System.getenv(ENV_API_KEY_SECRET);

        String id4n = "8yhyErStsVpjKLQq"; // the GUID we work with in this tutorial

        GuidAlias gtinAlias = new GuidAlias();
        gtinAlias.setAlias("0345391802");
        guidsApi.addGuidAlias(id4n,"gtin", gtinAlias, newBearerToken(subject, secret), LANGUAGE);

    }


}























