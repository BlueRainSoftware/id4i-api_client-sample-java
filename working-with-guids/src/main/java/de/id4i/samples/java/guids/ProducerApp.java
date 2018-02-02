package de.id4i.samples.java.guids;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.CollectionsApi;
import de.id4i.api.GUIDsApi;
import de.id4i.api.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static de.id4i.samples.java.guids.Id4iApiUtils.newBearerToken;

/**
 * Represents the ID4i client on the side of the producer.
 * See the tutorial for details.
 */
public class ProducerApp {
    private static final String LANGUAGE = "en";

    // We retrieve configuration values from the environment as
    // - configuration may differ between test and production
    // - it contains secrets that may not be visible in your sources
    private static final String ENV_ORGA = "PRODUCER_ID4I_ORGA";
    private static final String ENV_API_KEY = "PRODUCER_ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "PRODUCER_ID4I_API_KEY_SECRET";

    public static long organizationId;
    private final String subject;
    private final String secret;

    private final ApiClient myCustomApiClient = new ApiClient();
    private final GUIDsApi guidsApi;
    private final CollectionsApi collectionsApi;

    public ProducerApp() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);
        organizationId = Long.parseLong(System.getenv(ENV_ORGA));

        if (subject == null || secret == null) {
            throw new IllegalStateException(
                "API key cannot be created without applicationKey and and secret. Please set the environment variables "
                    + ENV_API_KEY + " and " + ENV_API_KEY_SECRET);
        }

        myCustomApiClient.setUserAgent("id4i-sample-guids-producer");
        myCustomApiClient.setBasePath(Id4iApiUtils.BASE_PATH);
        guidsApi = new GUIDsApi(myCustomApiClient);
        collectionsApi = new CollectionsApi(myCustomApiClient);
    }

    public ListOfId4ns createGuids() throws ApiException {
        CreateGuidRequest createGuidRequest = new CreateGuidRequest();
        createGuidRequest.setCount(10);
        createGuidRequest.setLength(128);
        createGuidRequest.setOrganizationId(organizationId);
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

    public void putGuidsIntoCollection(ListOfId4ns guids, String collectionId) throws ApiException {
        collectionsApi.addElementsToCollection(
            collectionId, guids,
            newBearerToken(subject, secret), LANGUAGE
        );
    }

    public Id4n createLogisticCollection() throws ApiException {
        CreateLogisticCollectionRequest request = new CreateLogisticCollectionRequest();
        request.setLabel("Shipment to Reseller - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        request.setOrganizationId(organizationId);
        request.setLength(128);
        return collectionsApi.createLogisticCollection(
            request,
            newBearerToken(subject, secret),
            LANGUAGE);
    }

    public void flagCollectionForTransfer(String collectionId) throws ApiException {
        GuidCollection guidCollection = new GuidCollection();
        guidCollection.setNextScanOwnership(true);
        collectionsApi.updateLogisticCollection(collectionId, guidCollection,
            newBearerToken(subject, secret),
            LANGUAGE);
    }

}























