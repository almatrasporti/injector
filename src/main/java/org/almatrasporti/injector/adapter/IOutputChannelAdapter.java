package org.almatrasporti.injector.adapter;


import org.almatrasporti.common.models.CANBusMessage;

import java.util.Collection;

public interface IOutputChannelAdapter {
    public boolean store(Collection<CANBusMessage> data);
}
