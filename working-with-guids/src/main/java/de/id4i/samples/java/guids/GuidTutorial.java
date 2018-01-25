package de.id4i.samples.java.guids;

import de.id4i.ApiException;
import de.id4i.api.model.ApiError;
import de.id4i.api.model.Id4n;
import de.id4i.api.model.ListOfId4ns;

import static de.id4i.samples.java.guids.Id4iApiUtils.deserialize;

public class GuidTutorial {
    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        ResellerApp resellerApp = new ResellerApp();

        try {
            ListOfId4ns guids = producerApp.createGuids(); // 2
            producerApp.putGuidsIntoLabelledCollection(guids,"puFHxUKkv7KDFYYefsOuRrWkd1lG35mCvd4mdAzV8L5o1vg25dlp7VCZ39Nped--qQsDjwxBd9FEruHiFNmFDNBOpdSO9HLamQG4-RKLSwEZID8bEKcHtB16vtDwQoQT"); // 3

           // Id4n shipmentCollectionId = producerApp.createLogisticCollection(); // 4

            String tmpCollectionId = "aMwr2CvR3pPDqK6x0sSBYNreZ4yHjRtQcuabM6i8oLUU8BHu6TC0XkM-4SqjbatlLFSMHgY7dUACrYdQcpB8Q6Xb9iUdrilOrYd1pSGnRsbu-M4GoRQlS-KXL-JDQgjB";
           // producerApp.putGuidsIntoCollection(guids, shipmentCollectionId.getId4n()); // 6
            producerApp.putGuidsIntoCollection(guids, tmpCollectionId); // 6

            producerApp.flagCollectionForTransfer(tmpCollectionId); // 5


        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }
}
