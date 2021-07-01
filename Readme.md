# Masters of Renaissance

Prova Finale del corso di Ingegneria del Software, anno accademico 2020-2021.\
Prof. Gianpaolo Cugola

## Componenti del gruppo

- Targhini Enrico
- Tresoldi Valerio
- Zuccolotto Enrico

## Descrizione

Il progetto prevede lo sviluppo e l'implementazione del gioco da tavolo Maestri del Rinascimento. Viene data la possibilità di giocare in maniera distribuita (massimo 4 giocatori) tramite un sistema distribuito client-server.

## Funzionalità sviluppate

- Regole complete;
- Comunicazione in rete attraverso Socket;
- Interfaccia grafica:
  - CLI;
  - GUI con JavaFX;
- Funzionalità avanzate:
  - Partita in locale
  - Persistenza
  - Resilienza alle disconnessioni.

## Report coverage classi di test

Per tale report sono state evidenziate solo le classi effettivamente testabili, quindi vengono per ora escluse le classi di View e comunicazione via rete.
![coverage_report](/resources/coverage.png)

## Documentazione

### Diagrammi UML

I diagrammi UML sono disponibili a questi link:

- [UML INIZIALE - Comunicazione di rete](deliveries/UML/ClientServer.png);
- [UML INIZIALE - Controller](deliveries/UML/uml_Controller.png);
- [UML INIZIALE - Model](deliveries/UML/uml_Model.png)
- [UML INIZIALE - View](deliveries/UML/View.png)
  

- [UML FINALE dettagliato](deliveries/UML/ing-sw-2021-zuccolotto-targhini-tresoldi.png)

### JavaDOC
La documentazione JavaDOC relativa al progetto può essere consultata [qui](JavaDoc/index.html)

### Esecuzione

I file JAR sono disponibili a questi link:

- [Client](deliveries/JAR/GC45-client.jar);
- [Server](deliveries/JAR/GC45-server.jar);

Per avviare il JAR è necessario aprire un Prompt dei Comandi e digitare:
`java -jar [nomefile]`

#### Opzioni
- Server:
- Client:
  - `-g` avvia il client in modalità GUI, senza comandi viene avviato in modalità CLI.

