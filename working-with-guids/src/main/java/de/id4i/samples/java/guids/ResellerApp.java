package de.id4i.samples.java.guids;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.GUIDsApi;
import de.id4i.api.model.CreateGuidRequest;
import de.id4i.api.model.ListOfId4ns;

import static de.id4i.samples.java.guids.Id4iApiUtils.newBearerToken;

public class ResellerApp {
    private static final String ENV_API_KEY = "RESELLER_ID4I_API_KEY";
    private final String ENV_API_KEY_SECRET = "RESELLER_ID4I_API_KEY_SECRET";

    public void start() throws ApiException {
        String subject = System.getenv(ENV_API_KEY);
        String secret = System.getenv(ENV_API_KEY_SECRET);

        ApiClient myCustomApiClient = new ApiClient();
        myCustomApiClient.setUserAgent("id4i-client-sample-reseller");
        GUIDsApi apiInstance = new GUIDsApi();
        apiInstance.setApiClient(myCustomApiClient);
    }

}























