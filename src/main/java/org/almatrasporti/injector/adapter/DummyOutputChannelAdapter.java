package org.almatrasporti.injector.adapter;


import org.almatrasporti.common.models.CANBusMessage;

import java.util.Collection;
import java.util.logging.Logger;

public class DummyOutputChannelAdapter implements IOutputChannelAdapter {
    public boolean store(Collection<CANBusMessage> data) {
        Logger.getLogger("DummyOutput").info(data.toString());
        return true;
    }
}
