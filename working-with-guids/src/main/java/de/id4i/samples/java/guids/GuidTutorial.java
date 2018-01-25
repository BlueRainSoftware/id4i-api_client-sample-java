package de.id4i.samples.java.guids;

import de.id4i.ApiException;
import de.id4i.api.model.ApiError;

import static de.id4i.samples.java.guids.Id4iApiUtils.deserialize;

public class GuidTutorial {
    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        ResellerApp resellerApp = new ResellerApp();

        try {
            producerApp.createGuids(); // 2


        } catch (ApiException e) {
            ApiError apiError = deserialize(e);
            System.err.println(apiError);
            e.printStackTrace();
        }
    }
}
