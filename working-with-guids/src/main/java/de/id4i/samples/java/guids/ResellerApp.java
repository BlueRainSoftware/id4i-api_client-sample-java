package de.id4i.samples.java.guids;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.CollectionsApi;
import de.id4i.api.GuidsApi;
import de.id4i.api.TransferApi;
import de.id4i.api.model.GuidAlias;
import de.id4i.api.model.GuidCollection;
import de.id4i.api.model.TransferReceiveInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import static de.id4i.samples.java.common.Id4iApiUtils.BASE_PATH;
import static de.id4i.samples.java.common.Id4iApiUtils.refreshToken;

/**
 * Represents the ID4i client on the side of the reseller.
 * See the tutorial for details.
 */
public class ResellerApp {
    // we use a different language than in the Producer app here.
    // You'll see what happens if you get an API Error
    private static final String LANGUAGE = "de";

    // We retrieve configuration values from the environment as
    // - configuration may differ between test and production
    // - it contains secrets that may not be visible in your sources
    private static final String ENV_ORGA = "RESELLER_ID4I_ORGA";
    private static final String ENV_API_KEY = "RESELLER_ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "RESELLER_ID4I_API_KEY_SECRET";

    private String subject;
    private String secret;
    private long organizationId;

    private final ApiClient resellerAppClient = new ApiClient();
    private final GuidsApi guidsApi;
    private final CollectionsApi collectionsApi;
    private final TransferApi transferApi;


    public ResellerApp() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);
        organizationId = Long.parseLong(System.getenv(ENV_ORGA));

        resellerAppClient.setUserAgent("id4i-client-sample-reseller");
        resellerAppClient.setBasePath(BASE_PATH); // use the development system

        guidsApi = new GuidsApi(resellerAppClient);
        collectionsApi = new CollectionsApi(resellerAppClient);
        transferApi = new TransferApi(resellerAppClient);

    }

    public void takeOwnership(String guid) throws ApiException {
        refreshToken(resellerAppClient, subject, secret);
        TransferReceiveInfo tri = new TransferReceiveInfo();
        tri.setHolderOrganizationId(organizationId);
        transferApi.receive(guid, tri);

        GuidCollection guidCollectionRequest = new GuidCollection();
        guidCollectionRequest.setLabel("Incoming package - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        collectionsApi.updateCollection(guid, guidCollectionRequest);
    }

    public void setAlias(String id4n, String aliasType, String alias) throws ApiException {
        refreshToken(resellerAppClient, subject, secret);

        GuidAlias aliasObject = new GuidAlias();
        aliasObject.setAlias(alias);
        guidsApi.addGuidAlias(id4n, aliasType, aliasObject);
    }
}
