package de.id4i.samples.java.storage;

import de.id4i.ApiException;
import de.id4i.api.model.ApiError;
import de.id4i.api.model.Id4n;
import de.id4i.api.model.ListOfId4ns;

import java.io.File;

import static de.id4i.samples.java.storage.Id4iApiUtils.deserialize;

/**
 * This class implements the ID4i "How to exchange data on ID4i" tutorial.
 * Please refer to the README and https://backend.id4i.de/docs/reference/en/reference.html#_how_to_exchange_data_on_id4i
 * for details.
 */
public class StorageTutorial {

    public static void main(String[] args) {
        FirstLocalApp localApp = new FirstLocalApp();

        try {
            ListOfId4ns guids = localApp.createGuids(); // 2
            System.out.println("[Producer] Created " + guids.getId4ns().size() + " GUIDs.");

            // use the collection ID from the manually created labelled collection here.
            String labelledCollectionId = "k2WtR...snip...UuS";
            localApp.putGuidsIntoLabelledCollection(guids, labelledCollectionId); // 3
            System.out.println("[Producer] Added GUIDS to labelled collection " + labelledCollectionId);

            Id4n shipmentCollectionId = localApp.createLogisticCollection(); // 4
            System.out.println("[Producer] Created logistic collection " + shipmentCollectionId.getId4n());

            localApp.putGuidsIntoCollection(guids, shipmentCollectionId.getId4n()); // 5
            System.out.println("[Producer] Added GUIDs to logistic collection " + shipmentCollectionId.getId4n());

            localApp.flagCollectionForTransfer(shipmentCollectionId.getId4n()); // 6
            System.out.println("[Producer] Set next-scan-ownership flag on collection " + shipmentCollectionId.getId4n());

        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }
}
