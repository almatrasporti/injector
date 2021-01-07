package org.almatrasporti.injector.adapter;


import org.almatrasporti.common.models.CANBusMessage;

import java.io.IOException;
import java.util.Collection;

public interface IInputChannelAdapter {
    public Collection<CANBusMessage> load() throws IOException;
}
