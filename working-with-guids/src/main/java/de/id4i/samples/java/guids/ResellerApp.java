package de.id4i.samples.java.guids;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.CollectionsApi;
import de.id4i.api.GUIDsApi;
import de.id4i.api.model.Guid;
import de.id4i.api.model.GuidCollection;

import java.text.SimpleDateFormat;
import java.util.Date;

import static de.id4i.samples.java.guids.Id4iApiUtils.newBearerToken;

public class ResellerApp {
    private static final String LANGUAGE = "de";

    private static final String ENV_ORGA = "RESELLER_ID4I_ORGA";
    private static final String ENV_API_KEY = "RESELLER_ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "RESELLER_ID4I_API_KEY_SECRET";

    private String subject;
    private String secret;
    private long organizationId;

    private ApiClient resellerAppClient = new ApiClient();
    private GUIDsApi guidsApi;
    private CollectionsApi collectionsApi;

    public ResellerApp() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);
        organizationId = Long.parseLong(System.getenv(ENV_ORGA));

        resellerAppClient.setUserAgent("id4i-client-sample-reseller");
        resellerAppClient.setBasePath("http://localhost:8080");

        guidsApi = new GUIDsApi(resellerAppClient);
        collectionsApi = new CollectionsApi(resellerAppClient);
    }

    public void takeOwnership(String id) throws ApiException {
        GuidCollection guidCollectionRequest = new GuidCollection();
        guidCollectionRequest.setOwnerOrganizationId(organizationId);

        // FIXME: Setting the label does not work
        //guidCollectionRequest.setLabel("Incoming package - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        collectionsApi.updateLogisticCollection(id, guidCollectionRequest,
            newBearerToken(subject, secret), LANGUAGE);
    }
}
