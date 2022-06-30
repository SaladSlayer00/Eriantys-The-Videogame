
# Prova Finale di Ingegneria del Software - AA 2021-2022
![alt text](src/main/resources/images/eriantys_banner_no_items.png)

Implementazione del gioco da tavolo [Eriantys](http://www.craniocreations.it/prodotto/eryantis/).

Il progetto consiste nell’implementazione di un sistema distribuito composto da un singolo server in grado di gestire una partita alla volta e multipli client (uno per giocatore) che possono partecipare ad una sola partita alla volta utilizzando il pattern MVC (Model-View-Controller).
La rete è stata gestita con l'utilizzo delle socket.

Interazione e gameplay: linea di comando (CLI) e grafica (GUI).

## Documentazione

### UML
I seguenti diagrammi delle classi rappresentano rispettivamente il modello iniziale sviluppato durante la fase di progettazione e i diagrammi del prodotto finale nelle parti critiche riscontrate.
- [UML Iniziali](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/19fd0f98f6f52bc3131149ba111d252107b3618b/deliveries/Initial_uml.pdf)
- [UML Finali]()

### JavaDoc
La seguente documentazione include una descrizione per la maggior parte delle classi e dei metodi utilizzati, segue le tecniche di documentazione di Java e può essere consultata al seguente indirizzo: [Javadoc]()

### Coverage report
Al seguente link è possibile consultare il report della coverage dei test effettuati con Junit: [Report]()


### Librerie e Plugins
|Libreria/Plugin|Descrizione|
|---------------|-----------|
|__Maven__|Strumento di automazione della compilazione utilizzato principalmente per progetti Java.|
|__JavaFx__|Libreria grafica per realizzare interfacce utente.|
|__JUnit__|Framework di unit testing.|

## Funzionalità
### Funzionalità Sviluppate
- Regole Complete
- CLI
- GUI
- Socket
- 2 FA (Funzionalità Avanzate):
    - __Persistenza:__ lo stato di una partita deve essere salvato su disco, in modo che la partita possa riprendere anche a seguito dell’interruzione dell’esecuzione del server.
    - __Carte Personaggio:__ il gioco può essere giocato in due diverse modalità: modalità facile e modalità esperto. Quest'ultima, oltre a mantenere le stesse regole e componenti della modalità facile, aggiunge la possibilità di utilizzare delle carte esperto, che influenzano la partita mediante specifici effetti.


## Compilazione e packaging
I jar sono stati realizzati con l'ausilio di Maven Shade Plugin.
Di seguito sono forniti i jar precompilati.
Per compilare i jar autonomamente, posizionarsi nella root del progetto e lanciare il comando:
```
mvn clean package
```
I jar compilati verranno posizionati all'interno della cartella ```target/``` con i nomi
```eryantis-client.jar``` e ```eryantis-server.jar```.
### Jars
I Jar del progetto possono essere scaricati al seguente link: [Jars]().
## Esecuzione
Questo progetto richiede una versione di Java 11 o superiore per essere eseguito correttamente.
### Santorini Client
Le seguenti istruzioni descrivono come eseguire il client con interfaccia CLI o GUI.
#### CLI
Per lanciare Eryantis Client CLI digitare da terminale il comando:
```
java -jar eryantis-client.jar --cli
```
#### GUI
Per poter lanciare la modalità GUI sono disponibili due opzioni:
- effettuare doppio click sull'eseguibile ```eryantis-client.jar```
- digitare da terminale il comando:
```
java -jar eryantis-client.jar
```
### Eryantis Server
Per lanciare Eryantis Server digitare da terminale il comando:
```
java -jar eryantis-server.jar [--port <port_number>]
```
#### Parametri
- `--port` `-p` : permette di specificare la porta del server. Se non specificato il valore di default è __16847__;

### Peer Review 
Le peer review a noi fatte:
- [__peer review riguardante l'uml iniziale__](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/613fda6515ad5eca7c056ae8b4626e1ac42a8235/deliveries/Peer-review-gruppo-68.docx)
- [__peer review riguardante il protocollo di rete](jetbrains://idea/navigate/reference?project=ing-sw-2022-Insalata-Kimbi-Maccarini&path=deliveries/Peer-Review-2_G68.md)

## Componenti del gruppo
- [__Beatrice Insalata__](https://github.com/SaladSlayer00)
- [__Teka Kimbi__](https://github.com/ThetaKimbi)
- [__Maccarini Alice__](https://github.com/maccarismos)