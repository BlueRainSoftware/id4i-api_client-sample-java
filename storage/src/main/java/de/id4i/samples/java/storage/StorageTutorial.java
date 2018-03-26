package de.id4i.samples.java.storage;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.GuidsApi;
import de.id4i.api.StorageApi;
import de.id4i.api.model.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static de.id4i.samples.java.common.Id4iApiUtils.*;

/**
 * ID4i storage usage samples.
 * Please refer to the README and https://backend.id4i.de/docs/reference/en/reference.html#_how_to_exchange_data_on_id4i
 * for details.
 */
public class StorageTutorial {
    // We retrieve configuration values from the environment as
    // - configuration may differ between test and production
    // - it contains secrets that may not be visible in your sources
    private static final String ENV_ORGA = "ID4I_ORGA";
    private static final String ENV_API_KEY = "ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "ID4I_API_KEY_SECRET";

    public static Long organizationId;
    private final String subject;
    private final String secret;

    private final ApiClient myCustomApiClient = new ApiClient();
    private final GuidsApi guidsApi;
    private final StorageApi storageApi;

    public StorageTutorial() {
        subject = System.getenv(ENV_API_KEY);
        secret = System.getenv(ENV_API_KEY_SECRET);
        organizationId = System.getenv(ENV_ORGA) == null ? null : Long.parseLong(System.getenv(ENV_ORGA));

        if (subject == null || secret == null || organizationId == null) {
            throw new IllegalStateException(
                "API key cannot be created without applicationKey and and secret. Please set the environment variables "
                    + ENV_API_KEY + ", " + ENV_API_KEY_SECRET + " and " + ENV_ORGA);
        }

        myCustomApiClient.setUserAgent("id4i-sample-storage");
        myCustomApiClient.setBasePath(BASE_PATH);

        myCustomApiClient.setBasePath("http://localhost:8080/"); //FIXME

        guidsApi = new GuidsApi(myCustomApiClient);
        storageApi = new StorageApi(myCustomApiClient);
    }

    public static void main(String[] args) {
        StorageTutorial app = new StorageTutorial();

        try {
            String guid = app.createGuid();
            System.out.println("Created a GUID: " + guid);

            ClassLoader classLoader = StorageTutorial.class.getClassLoader();

            File file = new File(classLoader.getResource("lieferschein.pdf").getFile());
            app.attachFile(guid, file, false);

            file = new File(classLoader.getResource("history-lesson.jpg").getFile());
            app.attachFile(guid, file, true);

            app.useMircoStorage(guid, "my arbitrary character content. Could be xml, JSON or anything. Go wild.");

        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }


    public String createGuid() throws ApiException {
        CreateGuidRequest createGuidRequest = new CreateGuidRequest();
        createGuidRequest.setCount(1);
        createGuidRequest.setLength(6);
        createGuidRequest.setOrganizationId(organizationId);

        refreshToken(myCustomApiClient, subject, secret);

        ListOfId4ns createdGuids =
            guidsApi.createGuid(createGuidRequest);

        return createdGuids.getId4ns().get(0);
    }

    public void attachFile(String guid, File f, boolean publish) throws ApiException {
        refreshToken(myCustomApiClient, subject, secret);
        Document document = storageApi.createDocument(organizationId, guid, f);
        System.out.println(document);

        if (publish) {
            DocumentUpdate documentUpdate = new DocumentUpdate();
            VisibilityUpdate visibility = new VisibilityUpdate();
            visibility.setPublic(true);
            documentUpdate.setVisibility(visibility);
            storageApi.updateDocumentMetadata(organizationId, guid, f.getName(), documentUpdate);
        }
    }

    public void useMircoStorage(String guid, String content) throws ApiException {
        storageApi.writeToMicrostorage(
            organizationId,
            guid,
            content,
            "text/plain",
            Long.valueOf(content.getBytes(StandardCharsets.UTF_8).length)
        );

        // Normally, you would read the data from another API client in most scenarios
        System.out.println(new String(storageApi.readFromMicrostorage(organizationId, guid), StandardCharsets.UTF_8));
    }
}
