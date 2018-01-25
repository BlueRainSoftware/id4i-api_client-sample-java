package de.id4i.samples.java.guids;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.CollectionsApi;
import de.id4i.api.GUIDsApi;
import de.id4i.api.model.CreateGuidRequest;
import de.id4i.api.model.ListOfId4ns;

import static de.id4i.samples.java.guids.Id4iApiUtils.newBearerToken;

public class ProducerApp {
    public static final String LANGUAGE = "en";
    private static final String ENV_API_KEY = "PRODUCER_ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "PRODUCER_ID4I_API_KEY_SECRET";
    private final String subject;
    private final String secret;

    private final ApiClient myCustomApiClient = new ApiClient();
    private final GUIDsApi guidsApi;
    private final CollectionsApi collectionsApi;

    public ProducerApp() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);

        if (subject == null || secret == null) {
            throw new IllegalStateException(
                "API key cannot be created without applicationKey and and secret. Please set the environment variables "
                    + ENV_API_KEY + " and " + ENV_API_KEY_SECRET);
        }

        myCustomApiClient.setUserAgent("id4i-sample-guids-producer");
        guidsApi = new GUIDsApi(myCustomApiClient);
        collectionsApi = new CollectionsApi(myCustomApiClient);
    }

    public ListOfId4ns createGuids() throws ApiException {
        CreateGuidRequest createGuidRequest = new CreateGuidRequest();
        createGuidRequest.setCount(10);
        createGuidRequest.setLength(128);
        createGuidRequest.setOrganizationId(8L);
        ListOfId4ns createdGuids =
            guidsApi.createGuid(createGuidRequest,
                newBearerToken(subject, secret),
                LANGUAGE);

        return createdGuids;
    }

    public void putGuidsIntoLabelledCollection(ListOfId4ns guids, String collectionId) throws ApiException {
        collectionsApi.addElementsToLabelledCollection(
            collectionId,
            guids,
            newBearerToken(subject, secret),
            LANGUAGE);
    }

}























