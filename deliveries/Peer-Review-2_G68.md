# Peer-Review 2: Protocollo di Comunicazione

Matteo Matassoni, Vincenzo Lamonea, Rahman Khalaf, Alessandro Lugato

Gruppo 69

Valutazione della documentazione del protocollo di comunicazione del gruppo 68.

## Lati positivi

- Abbiamo apprezzato molto la parte introduttiva del documento che dettaglia le scelte architetturali effettuate a livello di protocolli di comunicazione, così come la spiegazione della suddivisione dei compiti tra client/server e i diversi oggetti coinvolti nello scambio di messaggi.
- Riteniamo positiva la scelta di utilizzare il GameController come dispatcher di messaggi che agisce sul model per via delle simplificazioni offerte dalla corrispondenza tra messaggio e azione.

## Lati negativi


- Nonostante i diagrammi siano presentati in maniera chiara, putroppo offrono solamente una vista ad alto livello. Nello specifico, avremmo voluto vedere alcuni dei tipi di messaggi scambiati tra le principali classi interessate.
- Non possiamo fare a meno di notare che nel documento mancano i diagrammi di alcuni dei flussi principali, come il diagramma relativo alle diverse fasi di gioco (pianificazione, azione, ...).
- Il documento risulta essere scritto in italiano nonostante fosse stato richiesto di produrre la documentazione in inglese.

## Confronto

Il gruppo 68 ha scelto di adottare  una virtual view per ogni giocatore in modo da simulare le azioni di ogni utente in un ambiente controllato sul server ed interfacciarsi singolarmente a ciascun client. Questo approccio ha il vantaggio di richiedere poco lavoro da parte del client.
Il nostro approccio è stato quello di adottare similmente un thin client, il cui principale lavoro è quello di stampare a schermo lo stato corrente del gioco (per intero, compreso delle informazioni relative alle planche avversarie) che riceve dal server. Volendo, a partire dallo stato completo di gioco, è anche possibile controllare preventivamente la validità di alcune mosse, bilanciando il lavoro svolto da client e server.

