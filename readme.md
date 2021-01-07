## Microservizio Injector

Il modulo Injector, realizzato in linguaggio Java, si occupa di caricare i dati da una sorgente dati per il successivo trasferimento su un'altra 
destinazione.

La dipendenza dai canali di input e di output è stata realizzata mediante applicazione del principio di __dependency inversion__, al fine di rendere il modulo agnostico rispetto ai dettagli implementativi ed ai servizi/fonti dati utilizzati.

Nella fattispecie, tuttavia, i requisiti non funzionali richiedono che i dati vengano caricati da un file csv e depositati su un topic Kafka corrispondente al numero di telaio del relativo veicolo.

I dati in input CSV, sono nel seguente formato:
`VinVehicle,Timestamp,Driver,Odometer,LifeConsumption,Position.lon,Position.lat,Position.altitude,Position.heading,Position.speed,Position.satellites`

Es:

`VIN00000000000010,1590971400,Driver10,1000016000,1333001000,12.366820845888245,45.23895419650073,-13.0,20.01,81,4`


### Configurazione
E' possibile configurare l'Injector mediante un file di properties, contenente i seguenti campi:

- **InputAdapter**: Classe usata per leggere i dati da una sorgente __(vedi IInputChannelAdapter)__
store- **OutputAdapter**: Classe usata per scrivere i dati sulla destinazione  __(vedi IOutputChannelAdapter)__
- **Kafka.servers**: elenco di coppie `host:port` separate da virgola ',', usato nel caso in cui `OuputAdapter` sia `KafkaOutputChannelAdapter`  
- **CSVFile**: Posizione del file csv con il dataset di test, usato nel caso in cui `InputAdapter` `CSVInputChannelAdapter`

## Funzionamento
La classe `Injector` viene istanziata passando due istanze delle interfacce `IInputChannelAdapter` e `IOnputChannelAdapter`.

Tramite il metodo `process`, uno o più eventi (di tipo CANBusMessage) vengono letti dall'inputChannel, per essere scritti sull'outputChannel immediatamente dopo. 

```
    private void process() throws IOException {
        Collection<CANBusMessage> data = inputChannel.load();
        if (data != null) {
            boolean result = outputChannel.store(data);
        }
    }
```

### IInputChannelAdapter
Interfaccia per realizzare l'acquisizione dati, secondo il seguente contratto:
```
public interface IInputChannelAdapter {
    public Collection<CANBusMessage> load() throws IOException;
}
```

Due diverse implementazioni sono state realizzate:
- `DummyInputChannelAdapter`: fornisce dati generati in modo casuale, per scopi di test;
- `CSVInputChannelAdapter`: fornisce dati generati a partire dal file csv definito in fase di configurazione.


### IOuputChannelAdapter
Interfaccia per realizzare la scrittura dati, secondo il seguente contratto:
```
public interface IOutputChannelAdapter {
    public boolean store(Collection<CANBusMessage> data);
}
```

Due diverse implementazioni sono state realizzate:
- `DummyOuputChannelAdapter`: Stampa i dati sulla console, per scopi di test;
- `KafkaOutputChannelAdapter`: Scrive il messaggio in formato CSV sul topic kafka corrispondente al VIN del veicolo.