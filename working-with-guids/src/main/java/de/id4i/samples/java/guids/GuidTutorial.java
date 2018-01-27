package de.id4i.samples.java.guids;

import de.id4i.ApiException;
import de.id4i.api.model.ApiError;
import de.id4i.api.model.Id4n;
import de.id4i.api.model.ListOfId4ns;

import java.util.List;

import static de.id4i.samples.java.guids.Id4iApiUtils.deserialize;

public class GuidTutorial {
    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        ResellerApp resellerApp = new ResellerApp();

        try {
            ListOfId4ns guids = producerApp.createGuids(); // 2
            System.out.println("[Producer] Created " + guids.getId4ns().size() + " GUIDs.");

            // production
            //producerApp.putGuidsIntoLabelledCollection(guids,"puFHxUKkv7KDFYYefsOuRrWkd1lG35mCvd4mdAzV8L5o1vg25dlp7VCZ39Nped--qQsDjwxBd9FEruHiFNmFDNBOpdSO9HLamQG4-RKLSwEZID8bEKcHtB16vtDwQoQT"); // 3

            // local
            producerApp.putGuidsIntoLabelledCollection(guids,"k2WtRYLbpoJyfv8riM5UVULMmNoHNYNBrYBE8qO0kClGHhLmOvupWT7d26liN0RZld4q0V8hAYhMUUF-tfFMVRAQDia5V24Q4dTbzzpzWrKlYf19UDsZS5TgtaEuRUuS"); // 3

            System.out.println("[Producer] Added GUIDS to labelled collection.");


            Id4n shipmentCollectionId = producerApp.createLogisticCollection(); // 4
            System.out.println("[Producer] Created logistic collection " + shipmentCollectionId.getId4n());

            //String tmpCollectionId = "aMwr2CvR3pPDqK6x0sSBYNreZ4yHjRtQcuabM6i8oLUU8BHu6TC0XkM-4SqjbatlLFSMHgY7dUACrYdQcpB8Q6Xb9iUdrilOrYd1pSGnRsbu-M4GoRQlS-KXL-JDQgjB";
            producerApp.putGuidsIntoCollection(guids, shipmentCollectionId.getId4n()); // 5
            //producerApp.putGuidsIntoCollection(guids, tmpCollectionId); // 5

            producerApp.flagCollectionForTransfer(shipmentCollectionId.getId4n()); // 6
            System.out.println("[Producer] Set next-scan-ownership flag on collection " + shipmentCollectionId.getId4n());

            // pack -> send -> unpack


            // We put in the shipment collection ID here
            // IRL, the package will contain a scanable code containing this ID
            resellerApp.takeOwnership(shipmentCollectionId.getId4n()); // 7
            System.out.println("[Reseller] Claimed ownership of collection " + shipmentCollectionId.getId4n());


            resellerApp.takeOwnership(shipmentCollectionId.getId4n()); // 9
            System.out.println("[Reseller] Claimed ownership of collection " + shipmentCollectionId.getId4n());

            // We add aliases to some of the GUIDs
            resellerApp.setAlias(guids.getId4ns().get(0), "gtin", "978-3200328587"); // 10
            resellerApp.setAlias(guids.getId4ns().get(2), "gtin", "978-3200328587");
            resellerApp.setAlias(guids.getId4ns().get(4), "gtin", "978-3200328587");

            resellerApp.setAlias(guids.getId4ns().get(1), "article", "internal-article-id");
            resellerApp.setAlias(guids.getId4ns().get(3), "article", "internal-article-id");
            resellerApp.setAlias(guids.getId4ns().get(5), "article", "internal-article-id");
            System.out.println("[Reseller] Set some aliases");

            // since we have nothing physical to scan, we just cheat and use the list of created items from the earlier steps
           // List<String> scannedItems = guids.getId4ns();
           // for (String guid : scannedItems) {
           //     resellerApp.aliasKRAM(guid);
           // }

        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }
}
