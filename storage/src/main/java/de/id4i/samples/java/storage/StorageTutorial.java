package de.id4i.samples.java.storage;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.GuidsApi;
import de.id4i.api.StorageApi;
import de.id4i.api.model.CreateGuidRequest;
import de.id4i.api.model.Document;
import de.id4i.api.model.ListOfId4ns;

import java.io.File;

import static de.id4i.samples.java.storage.Id4iApiUtils.refreshToken;

/**
 * Represents the ID4i client on the side of the producer.
 * See the tutorial for details.
 */
public class LocalApp1 {
    private static final String LANGUAGE = "en";

    // We retrieve configuration values from the environment as
    // - configuration may differ between test and production
    // - it contains secrets that may not be visible in your sources
    private static final String ENV_ORGA = "ID4I_ORGA";
    private static final String ENV_API_KEY = "ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "ID4I_API_KEY_SECRET";

    public static long organizationId;
    private final String subject;
    private final String secret;

    private final ApiClient myCustomApiClient = new ApiClient();
    private final GuidsApi guidsApi;
    private final StorageApi storageApi;


    public LocalApp1() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);
        organizationId = Long.parseLong(System.getenv(ENV_ORGA));

        if (subject == null || secret == null) {
            throw new IllegalStateException(
                "API key cannot be created without applicationKey and and secret. Please set the environment variables "
                    + ENV_API_KEY + " and " + ENV_API_KEY_SECRET);
        }

        myCustomApiClient.setUserAgent("id4i-sample-guids-producer");
        // myCustomApiClient.setBasePath(Id4iApiUtils.BASE_PATH);
        myCustomApiClient.setBasePath("http://localhost:8080/");
        guidsApi = new GuidsApi(myCustomApiClient);
        storageApi = new StorageApi(myCustomApiClient);
    }

    public ListOfId4ns createGuids() throws ApiException {
        CreateGuidRequest createGuidRequest = new CreateGuidRequest();
        createGuidRequest.setCount(1);
        createGuidRequest.setLength(6);
        createGuidRequest.setOrganizationId(organizationId);

        refreshToken(myCustomApiClient, subject, secret);

        ListOfId4ns createdGuids =
            guidsApi.createGuid(createGuidRequest);

        return createdGuids;
    }

    public void uploadPdf(String destination, File f, boolean published) throws ApiException {
        refreshToken(myCustomApiClient, subject, secret);
        Document document = storageApi.createDocument(organizationId, destination, f);
        System.out.println(document);
    }

}























