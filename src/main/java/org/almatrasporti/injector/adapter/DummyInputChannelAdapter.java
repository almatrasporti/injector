package org.almatrasporti.injector.adapter;


import org.almatrasporti.common.models.CANBusMessage;
import org.almatrasporti.common.services.CANBusMessageFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class DummyInputChannelAdapter implements IInputChannelAdapter {
    public Collection<CANBusMessage> load() {
        Collection<CANBusMessage> messages = new ArrayList<>();

        String line;

        int vin = Math.abs(((new Random()).nextInt() % 11)) + 1;
        line = "VIN000000000000" + vin + ",1590971400,Driver10,1000016000,1333001000,12.366820845888245,45.23895419650073,-13.0,20.01,81,4";

        messages.add(CANBusMessageFactory.fromCSV(line));

        return messages;
    }
}
