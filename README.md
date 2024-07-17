# InitializerJSPMySQL

InitializerJSPMySQL è un'applicazione Java con interfaccia grafica che semplifica la creazione e l'inizializzazione di progetti web JSP con supporto per MySQL.

## Caratteristiche

- Interfaccia grafica intuitiva
- Generazione automatica di progetti Maven
- Configurazione automatica delle dipendenze per JSP e MySQL
- Aggiornamento automatico dei file `pom.xml` e `web.xml`
- Esecuzione integrata del comando `mvn clean install`

## Requisiti

- Java JDK 8 o superiore
- Maven installato e configurato nel PATH di sistema

## Utilizzo

1. Compilare e eseguire `InitializerJSPMySQL.java`
2. Inserire l'ID del gruppo e l'ID dell'artefatto
3. Cliccare su "Crea Progetto" per generare il progetto
4. Utilizzare "mvn clean install" per compilare il progetto generato

## Struttura del Progetto Generato

Il progetto generato avrà la seguente struttura:

```
nome-progetto/
├── src/
│   └── main/
│       ├── java/
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml
└── pom.xml
```

## Dipendenze Incluse

- JSTL
- MySQL Connector
- Servlet API

## Licenza

Questo progetto è distribuito sotto la licenza MIT. Vedere il file `LICENSE` per ulteriori dettagli.

## Autori

Creato con ❤ da [github.com/MindMelodies](https://github.com/MindMelodies) e Claude-3.5-Sonnet-200k
