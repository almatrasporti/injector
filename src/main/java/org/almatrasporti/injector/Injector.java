package org.almatrasporti.injector;

import org.almatrasporti.common.models.CANBusMessage;
import org.almatrasporti.injector.adapter.DataSourceDepletedException;
import org.almatrasporti.injector.adapter.IInputChannelAdapter;
import org.almatrasporti.injector.adapter.IOutputChannelAdapter;
import org.almatrasporti.common.utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;

public class Injector {

    IInputChannelAdapter inputChannel = null;
    IOutputChannelAdapter outputChannel = null;

    public Injector(IInputChannelAdapter inputChannel, IOutputChannelAdapter outputChannel) {
        this.inputChannel = inputChannel;
        this.outputChannel = outputChannel;
    }

    private void process() throws IOException {
        Collection<CANBusMessage> data = inputChannel.load();
        if (data != null) {
            boolean result = outputChannel.store(data);
        }
    }

    public static void main(String args[]) {
        Config config = Config.getInstance();

        IInputChannelAdapter inputChannel = null;
        IOutputChannelAdapter outputChannel = null;

        try {
            Class<?> className = Class.forName(config.get("InputAdapter"));
            Constructor<?> ctor = className.getConstructor();
            inputChannel = (IInputChannelAdapter) ctor.newInstance();

            className = Class.forName(config.get("OutputAdapter"));
            ctor = className.getConstructor();
            outputChannel = (IOutputChannelAdapter) ctor.newInstance();
        } catch (Exception e) {
            System.out.println("ERROR:" + e);
        }

        Injector injector = new Injector(inputChannel, outputChannel);

        while (true) {
            try {
                injector.process();
            } catch (DataSourceDepletedException e) {
              break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
