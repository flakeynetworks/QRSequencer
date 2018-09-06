package uk.co.flakeynetworks.qrcodesequence.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uk.co.flakeynetworks.qrcodesequence.model.Sequence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Richard Stokes on 9/6/2018.
 */

public class TestSequence {

    private List<String> messages = new ArrayList<>();
    private List<Integer> payloadSizes = new ArrayList<>();


    public TestSequence() {

        // Add the messages
        messages.add("abcdefghijklmnopqrstuvwuxyz1234567890-=!@#$%^&*()_+][}{';abcdefghijklmnopqrstuvwuxyz1234567890-=!@#$%^&*()_+][}{'; abcdefghijklmnopqrstuvwuxyz1234567890-=!@#$%^&*()_+][}{';abcdefghijklmnopqrstuvwuxyz1234567890-=!@#$%^&*()_+][}{';");
        messages.add("a");
        messages.add("");

        // Add the payload sizes
        payloadSizes.add(-1);
        payloadSizes.add(0);
        payloadSizes.add(1);
        payloadSizes.add(100);
        payloadSizes.add(220);
        payloadSizes.add(1000000);
    } // end of constructor


    @Test
    /**
     * Test to see if the messages gets deconstructed into payloads correctly.
     */
    public void sequenceDeconstructTest() {

        for(String message: messages) {

            Sequence sequence = new Sequence(message);
            String response = sequence.getMessage();

            assertThat(message, is(response));
        } // end of for
    } // end of sequenceDeconstructTest


    @Test
    /**
     * Test to see if the payload sizes are correct.
     */
    public void testMaxPayloadSize() {

        for(String message: messages) {

            for(int maxPayloadSize: payloadSizes) {

                Sequence sequence;

                try {
                    sequence = new Sequence(message, maxPayloadSize, 0);
                } catch (IllegalArgumentException e) {

                    if(maxPayloadSize > 0) {
                        assert(false);
                        return;
                    } // end of if

                    continue;
                } // end of catch


                for (int i = 0; i < sequence.getNumberOfPayloads(); i++) {

                    String payload = sequence.getPayload(i);
                    if (payload == null || payload.isEmpty() || payload.length() > maxPayloadSize) {

                        assert (false);
                        return;
                    } // end of if
                } // end of for
            } // end of for
        } // end of for

        assert (true);
    } // end of testMaxPayloadSize
} // end of TestSequence
