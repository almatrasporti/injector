package org.almatrasporti.injector.adapter;

import org.almatrasporti.common.models.CANBusMessage;
import org.almatrasporti.common.services.CANBusMessageFactory;
import org.almatrasporti.common.utils.Config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CSVInputChannelAdapter implements IInputChannelAdapter {

    BufferedReader reader = null;

    public CSVInputChannelAdapter() throws FileNotFoundException {
        String filename = Config.getInstance().get("CSVFile");

        this.reader = new BufferedReader(new FileReader(filename));

    }
    public Collection<CANBusMessage> load() throws IOException {
        String line = this.reader.readLine();
        if (line == null) {
            throw new DataSourceDepletedException();
        }
        if (!this.isValid(line)) {
            return null;
        }

        Collection<CANBusMessage> messages = new ArrayList<>();
        messages.add(CANBusMessageFactory.fromCSV(line));

        return messages;
    }

    private boolean isValid(String line) {
        return !(line.isEmpty() || line.chars().filter(ch -> ch == ',').count() != 10);
    }
}
