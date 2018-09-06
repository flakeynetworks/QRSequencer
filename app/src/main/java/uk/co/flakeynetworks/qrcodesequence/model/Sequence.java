package uk.co.flakeynetworks.qrcodesequence.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard Stokes on 9/6/2018.
 */

public class Sequence {

    /**
     * Default max number of characters each payload can contain.
     */
    public static final int DEFAULT_PAYLOAD_SIZE = 220;

    /**
     * Default interval between each payload
     */
    public static final int DEFAULT_SEQUENCE_INTERVAL = 500;


    private List<String> payloads = new ArrayList<>();

    private int maxPayloadSize = DEFAULT_PAYLOAD_SIZE;
    private int sequenceInterval = DEFAULT_SEQUENCE_INTERVAL;


    public Sequence(String message, int maxPayloadSize, int sequenceInterval) {

        if(maxPayloadSize < 1 || sequenceInterval < 1)
            throw new IllegalArgumentException();

        this.maxPayloadSize = maxPayloadSize;
        this.sequenceInterval = sequenceInterval;

        deconstructMessage(message);
    } // end of constructor


    public Sequence(String message) {

        deconstructMessage(message);
    } // end of constructor


    private void deconstructMessage(String message) {

        payloads.clear();

        while(!message.isEmpty()) {

            int maxOffset = maxPayloadSize;

            if(message.length() < maxPayloadSize)
                maxOffset = message.length();

            // Get the current payload
            String payload = message.substring(0, maxOffset);
            payloads.add(payload);

            message = message.substring(maxOffset);
        } // end of while
    } // end of deconstructMessage


    public int getNumberOfPayloads() { return payloads.size(); } // end of getNumberOfPayloads


    public String getPayload(int index) { return payloads.get(index); } // end of getPayload


    public String getMessage() {

        StringBuilder builder = new StringBuilder();

        for(String payload: payloads)
            builder.append(payload);

        return builder.toString();
    } // end of getMessage
} // end of Sequence
