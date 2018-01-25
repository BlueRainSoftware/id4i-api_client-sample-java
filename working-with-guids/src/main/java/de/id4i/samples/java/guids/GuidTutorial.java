package de.id4i.samples.java.guids;

import de.id4i.ApiException;
import de.id4i.api.model.ApiError;
import de.id4i.api.model.ListOfId4ns;

import static de.id4i.samples.java.guids.Id4iApiUtils.deserialize;

public class GuidTutorial {
    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        ResellerApp resellerApp = new ResellerApp();

        try {
            ListOfId4ns guids = producerApp.createGuids(); // 2
            producerApp.putGuidsIntoLabelledCollection(guids,"zU71YAG-epLaGdM-h4mPcbgwP6XeyFzucm5yCWK1B15imns4kr1QvTjiziWky0H5qpnCRxNLPkV5X1XWHzx2q0L4BZldvxp6PicHZVOCoKvjDHKuvKUo6OMZf5mlqRDq"); // 3

        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }
}
