# Masters of Renaissance

Prova Finale del corso di Ingegneria del Software, anno accademico 2020-2021.\
Prof. Gianpaolo Cugola

## Componenti del gruppo

- Targhini Enrico
- Tresoldi Valerio
- Zuccolotto Enrico

## Descrizione

Il progetto prevede lo sviluppo e l'implementazione del gioco da tavolo Maestri del Rinascimento. Viene data la possibilità di giocare in maniera distribuita (massimo 4 giocatori) tramite un sistema distribuito client-server.

## Funzionalità finora sviluppate

- Regole complete;
- Comunicazione in rete attraverso Socket;
- Interfaccia grafica:
  - CLI;
  - GUI con JavaFX (WIP);
- Funzionalità avanzate:
  - Partita in locale
  - Persistenza (WIP)

## Report coverage classi di test

Per tale report sono state evidenziate solo le classi effettivamente testabili, quindi vengono per ora escluse le classi di View e comunicazione via rete.
![coverage_report](/resources/coverage.png)

## Documentazione

### Diagrammi UML

I diagrammi UML sono disponibili a questi link:

- [Comunicazione di rete](UML/ClientServer.png);
- [Controller](UML/uml_Controller.png);
- [Model](UML/uml_Model.png)
- [View](UML/View.png)
