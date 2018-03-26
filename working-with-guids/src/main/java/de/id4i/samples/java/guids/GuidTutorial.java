package de.id4i.samples.java.guids;

import de.id4i.ApiException;
import de.id4i.api.model.ApiError;
import de.id4i.api.model.Id4n;
import de.id4i.api.model.ListOfId4ns;

import static de.id4i.samples.java.common.Id4iApiUtils.deserialize;

/**
 * This class implements the ID4i "Working with GUIDs" tutorial.
 * Please refer to the README and https://backend.id4i.de/docs/reference/en/reference.html#_how_to_work_with_guids_and_collections_using_the_java_api
 * for details.
 */
public class GuidTutorial {

    public static final String ALIAS_TYPE_GTIN = "gtin";
    public static final String ALIAS_TYPE_ARTICLE = "article";

    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        ResellerApp resellerApp = new ResellerApp();

        try {
            ListOfId4ns guids = producerApp.createGuids(); // 2
            System.out.println("[Producer] Created " + guids.getId4ns().size() + " GUIDs.");

            // TODO: add the collection ID from the manually created labelled collection here.
            String labelledCollectionId = null;//"k2WtR...snip...UuS";
            if (labelledCollectionId != null) {
                producerApp.putGuidsIntoLabelledCollection(guids, labelledCollectionId); // 3
                System.out.println("[Producer] Added GUIDS to labelled collection " + labelledCollectionId);
            }

            Id4n shipmentCollectionId = producerApp.createLogisticCollection(); // 4
            System.out.println("[Producer] Created logistic collection " + shipmentCollectionId.getId4n());

            producerApp.putGuidsIntoCollection(guids, shipmentCollectionId.getId4n()); // 5
            System.out.println("[Producer] Added GUIDs to logistic collection " + shipmentCollectionId.getId4n());

            producerApp.flagCollectionForTransfer(shipmentCollectionId.getId4n()); // 6
            System.out.println("[Producer] Set next-scan-ownership flag on collection " + shipmentCollectionId.getId4n());

            // pack -> send -> unpack, scan

            // We put in the shipment collection ID here
            // IRL, the package will contain a scannable code containing this ID
            resellerApp.takeOwnership(shipmentCollectionId.getId4n()); // 7
            System.out.println("[Reseller] Claimed ownership of collection " + shipmentCollectionId.getId4n());

            // We add aliases to some of the GUIDs
            resellerApp.setAlias(guids.getId4ns().get(0), ALIAS_TYPE_GTIN, "978-3200328587"); // 9
            resellerApp.setAlias(guids.getId4ns().get(2), ALIAS_TYPE_GTIN, "978-3200328587");
            resellerApp.setAlias(guids.getId4ns().get(4), ALIAS_TYPE_GTIN, "978-3200328587");

            resellerApp.setAlias(guids.getId4ns().get(1), ALIAS_TYPE_ARTICLE, "internal-article-id");
            resellerApp.setAlias(guids.getId4ns().get(3), ALIAS_TYPE_ARTICLE, "internal-article-id");
            resellerApp.setAlias(guids.getId4ns().get(5), ALIAS_TYPE_ARTICLE, "internal-article-id");
            System.out.println("[Reseller] Set some aliases");

        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }
}
