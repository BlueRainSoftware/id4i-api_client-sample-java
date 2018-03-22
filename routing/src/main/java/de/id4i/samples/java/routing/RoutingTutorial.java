package de.id4i.samples.java.routing;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.GUIDsApi;
import de.id4i.api.RoutingApi;
import de.id4i.api.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static de.id4i.samples.java.routing.Id4iApiUtils.deserialize;
import static de.id4i.samples.java.routing.Id4iApiUtils.refreshToken;

/**
 * Id4i routing tutorial.
 * <p>
 * Please refer to https://backend.id4i.de/docs/reference/en/reference.html#_how_to_set_up_routing_for_guids to
 * see what's going on here.
 */
public class RoutingTutorial {

    private static final String ENV_API_KEY = "ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "ID4I_API_KEY_SECRET";
    private static final String ENV_ORGA = "ID4I_ORGA";

    private final ApiClient routingTutorialClient = new ApiClient();
    private final GUIDsApi guidsApi;
    private final RoutingApi routingApi;

    public RoutingTutorial() {
        String subject = System.getenv(ENV_API_KEY);
        String secret = System.getenv(ENV_API_KEY_SECRET);
        Long organizationId = Long.parseLong(System.getenv(ENV_ORGA));

        if (subject == null || secret == null || organizationId == null) {
            throw new IllegalStateException(
                "Could not retrieve required environment. Are the environment variables "
                    + ENV_API_KEY + ", "
                    + ENV_ORGA + " and "
                    + ENV_API_KEY_SECRET + " set?");
        }

        routingTutorialClient.setUserAgent("id4i-sample-routing");
        routingTutorialClient.setBasePath(Id4iApiUtils.BASE_PATH);

        refreshToken(routingTutorialClient, subject, secret);
        guidsApi = new GUIDsApi(routingTutorialClient);
        routingApi = new RoutingApi(routingTutorialClient);
    }

    public static void main(String[] args) {
        RoutingTutorial app = new RoutingTutorial();

        try {
            app.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }

    private void start() throws IOException, ApiException {
        Long organizationId = Long.parseLong(System.getenv(ENV_ORGA));

        String guidId4n = "8yhyErStsVpjKLQq"; // the GUID we work with in this tutorial
        String routingCollectionId4n = "A-xFHBqq4kPKx4SEgfAJ9971x1tU2EnOStHHNFvjzSQrOrmIC93qlUQX1iGFLekEXwSGLapXv-tW7fDG6xYPozitfwPGIqG0RvANcCBEJUpn1km5CKIXImqfJJjZHHZ-"; // the ID of the routing collection we work with

        GuidAlias gtinAlias = new GuidAlias();
        gtinAlias.setAlias("0345391802");
        guidsApi.addGuidAlias(guidId4n, "gtin", gtinAlias);
        System.out.println("Added GTIN alias " + gtinAlias.getAlias() + " to " + guidId4n);

        RoutingFile routingFile = routingApi.getRoutingFile(routingCollectionId4n, organizationId);
        Route firstRoute = routingFile.getRoutes().get(0);
        System.out.println("First route of routing collection " + routingCollectionId4n);
        System.out.println(firstRoute);

        firstRoute.getParams().put("url", "https://www.amazon.de/s/&field-keywords={alias_gtin}");

        RoutingFileRequest routingFileRequest = new RoutingFileRequest();
        routingFileRequest.setRouting(routingFile);
        routingFileRequest.setOrganizationId(organizationId);
        routingApi.updateRoutingFile(routingFileRequest, routingCollectionId4n);
        System.out.println("Updated route");

        Route privateRoute = new Route();
        privateRoute.setPublic(false);
        privateRoute.setType("my-custom-route-type");
        privateRoute.setPriority(10);

        Map<String, String> routeParams = new HashMap<>();
        routeParams.put("host", "localhost");
        routeParams.put("port", "8080");
        routeParams.put("path", "/something/internal");
        routeParams.put("foo", "bar");
        privateRoute.setParams(routeParams);
        System.out.println("Created private route " + privateRoute);

        routingFile.getRoutes().add(privateRoute);
        System.out.println("Added new route to existing routing file");

        routingApi.updateRoutingFile(routingFileRequest, routingCollectionId4n);
        System.out.println("Updated routing file with a new private route");

        Route currentRoute = routingApi.getRoute(
            guidId4n,
            "my-custom-route-type",
            true,
            false,
            false);

        System.out.println("Retrieved current route for GUID " + guidId4n + ":");
        System.out.println(currentRoute);

    }


}























