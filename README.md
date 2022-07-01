
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
- [UML Finali](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/49a73809a90d28d94fbea5ab7cc43c92a3a6b58e/deliveries/Final_UML.svg)

### Protocollo di rete
Il seguente documento presenta una descrizione dettagliata del protocollo di rete da noi implementato:
- [Protocollo di rete](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/49a73809a90d28d94fbea5ab7cc43c92a3a6b58e/deliveries/Protocollo-di-comunicazione.doc)

### JavaDoc
La seguente documentazione include una descrizione per la maggior parte delle classi e dei metodi utilizzati, segue le tecniche di documentazione di Java.

### Testing coverage
La percentuale di testing del model del gioco è pari a:
- al 90% per le enum con una coverage dei metodi del 54.2% e delle righe di codice pari al 69.4%
- al 100% per il package model con una coverage dei metodi del 86.2% e delle righe di codice del 86.1%
- al 100% per il package riguardante la board con una coverage dei metodi del 77.8% e delle righe del 75.5%
- al 100% per il package riguardante la plancia giocatore con una coverage dei metodi del 83.3% e delle righe del 80.8%

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
```eriantys-client.jar``` e ```eriantys-server.jar```.
### Jars
I Jar del progetto possono essere scaricati al seguente link: [Jars](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/2b1d5a1cdb23fa100c06599b9d1fa4f4b787140a/deliveries/jar).
## Esecuzione
Questo progetto richiede una versione di Java 11 o superiore per essere eseguito correttamente.
### Eriantys Client
Le seguenti istruzioni descrivono come eseguire il client con interfaccia CLI o GUI.
#### CLI
Per lanciare Eriantys Client CLI digitare da terminale il comando:
```
java -jar eriantys-client.jar --cli
```
#### GUI
Per poter lanciare la modalità GUI sono disponibili due opzioni:
- effettuare doppio click sull'eseguibile ```eriantys-client.jar```
- digitare da terminale il comando:
```
java -jar eriantys-client.jar
```
### Erantys Server
Per lanciare Eriantys Server digitare da terminale il comando:
```
java -jar eriantys-server.jar [--port <port_number>]
```
#### Parametri
- `--port` `-p` : permette di specificare la porta del server. Se non specificato il valore di default è __16847__;

### Peer Review 
Le peer review a noi fatte:
- [__peer review riguardante l'uml iniziale__](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/613fda6515ad5eca7c056ae8b4626e1ac42a8235/deliveries/Peer-review-gruppo-68.docx)
- [__peer review riguardante il protocollo di rete](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/92ef29967cd1ee4c8e5b4db50afbbf4f8ed7e0d5/deliveries/Peer-Review-2_G68.mdS)

Le peer review da noi fatte:
-[__peer review riguardante l'uml iniziale__](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/dec7869420f0f3eb2a237b133de737054356c682/deliveries/Peer_Review_PSP_67.docx)
-[__peer review riguardante il protocollo di rete__](https://github.com/SaladSlayer00/ing-sw-2022-Insalata-Kimbi-Maccarini/blob/dec7869420f0f3eb2a237b133de737054356c682/deliveries/Peer_Review_PSP_67.docx)

## Componenti del gruppo
- [__Beatrice Insalata__](https://github.com/SaladSlayer00)
- [__Teka Kimbi__](https://github.com/ThetaKimbi)
- [__Maccarini Alice__](https://github.com/maccarismos)