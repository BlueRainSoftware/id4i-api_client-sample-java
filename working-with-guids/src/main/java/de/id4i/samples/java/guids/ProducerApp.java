package de.id4i.samples.java.guids;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.CollectionsApi;
import de.id4i.api.GuidsApi;
import de.id4i.api.TransferApi;
import de.id4i.api.model.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static de.id4i.samples.java.common.Id4iApiUtils.*;

/**
 * Represents the ID4i client on the side of the producer.
 * See the tutorial for details.
 */
public class ProducerApp {
    // We retrieve configuration values from the environment as
    // - configuration may differ between test and production
    // - it contains secrets that may not be visible in your sources
    private static final String ENV_ORGA = "PRODUCER_ID4I_ORGA";
    private static final String ENV_API_KEY = "PRODUCER_ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "PRODUCER_ID4I_API_KEY_SECRET";

    private final String organizationId;
    private final String subject;
    private final String secret;

    private final ApiClient myCustomApiClient = new ApiClient();
    private final GuidsApi guidsApi;
    private final CollectionsApi collectionsApi;
    private final TransferApi transferApi;

    public ProducerApp() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);
        organizationId = System.getenv(ENV_ORGA);

        if (subject == null || secret == null || organizationId == null) {
            throw new IllegalStateException(
                "Sample must be configured with API key and organization. Please set the environment variables "
                    + ENV_API_KEY + ", " + ENV_API_KEY_SECRET + " and " + ENV_ORGA);
        }

        myCustomApiClient.setUserAgent("id4i-sample-guids-producer");
        myCustomApiClient.setBasePath(BASE_PATH);

        guidsApi = new GuidsApi(myCustomApiClient);
        transferApi = new TransferApi(myCustomApiClient);
        collectionsApi = new CollectionsApi(myCustomApiClient);
    }

    public ListOfId4ns createGuids() throws ApiException {
        CreateGuidRequest createGuidRequest = new CreateGuidRequest();
        createGuidRequest.setCount(10);
        createGuidRequest.setLength(128);
        createGuidRequest.setOrganizationId(organizationId);

        refreshToken(myCustomApiClient, subject, secret);

        ListOfId4ns createdGuids =
            guidsApi.createGuid(createGuidRequest);

        return createdGuids;
    }

    public void putGuidsIntoCollection(ListOfId4ns guids, String collectionId) throws ApiException {
        refreshToken(myCustomApiClient, subject, secret);
        collectionsApi.addElementsToCollection(collectionId, guids);
    }

    public Id4n createLogisticCollection() throws ApiException {
        refreshToken(myCustomApiClient, subject, secret);

        CreateCollectionRequest request = new CreateCollectionRequest();
        request.setLabel("Shipment to Reseller - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        request.setType(CreateCollectionRequest.TypeEnum.LOGISTIC_COLLECTION);
        request.setOrganizationId(organizationId);
        request.setLength(128);
        return collectionsApi.createCollection(request);
    }

    public void flagCollectionForTransfer(String collectionId) throws ApiException {
        refreshToken(myCustomApiClient, subject, secret);

        TransferSendInfo tsi = new TransferSendInfo();
        tsi.setOpenForClaims(true);
        tsi.setRecipientOrganizationIds(Arrays.asList("de.id4i.sample.reseller"));
        tsi.setKeepOwnership(false);

        transferApi.prepare(collectionId,tsi);
    }
}























