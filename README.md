# ArtistaStrada

ArtistaStrada è un progetto in cui vengono utilizzati i thread per simulare un gruppo di persone che aspettano che un artista faccia loro un ritratto, di seguito è riportato il testo del problema:

> Un artista da strada esegue delle caricature e dei ritratti a
> carboncino. Chi vuole un ritratto si siede in una delle quattro sedie
> messe a disposizione e attende il suo turno per spostarsi nella zona
> di lavoro dove farsi fare il ritratto. Le persone intorno arrivano
> continuamente e guardano incuriosite, attendendo che una delle quattro
> sedie si liberi per potersi mettere in attesa del ritratto. Tuttavia,
> le persone che aspettano per troppo tempo una sedia libera (stabilire
> un tempo predefinito all’inizio del programma) rinunciano a farsi fare
> il ritratto. Simulare questa situazione utilizzando i semafori come
> meccanismo di sincronizzazione tra i processi. In particolare, tenere
> presente che: - all’inizio non c’è nessun cliente - i clienti arrivano
> in numero e a istanti di tempo casuali - l’artista non impiega sempre
> lo stesso tempo per eseguire un ritratto.

Per questo progetto viene utilizzata la classe Cliente che deriva dalla classe Thread, a cui viene fornito un codice identificativo univoco per ogni istanza, due semafori in comune a tutte le istanze: uno in mutua esclusione che rappresenta l'artista ed uno a conteggio che serve a gestire le quattro sedie, 

    Semaphore artista = new Semaphore(1);
    Semaphore sedie = new Semaphore(4);
ed un tempo di attesa massimo oltre il quale il cliente smette di aspettare che si liberi una sedia.

    public class Cliente extends Thread{
    	private int ID;
    	private long maxWaiting;
    	private Semaphore artista;
    	private Semaphore sedie;
    	
    	...
    }

## Main()

All'interno di un ciclo while viene istanziato un numero casuale compreso fra 0 e 4 di clienti ed ad ognuno viene assegnato un codice identificativo basato sul numero di istanze già presenti, per l'iterazione successiva occorrerà attendere un tempo indeterminato fra 6 e 11 secondi:

    int IDCliente=0;
    int maxClienti = 400;
    long maxWaiting=10000;
    while(IDCliente<maxClienti) {
	        int nClienti = (int) (Math.random()*4);
        	for(int j=0;j<=nClienti;j++) {
        		Cliente client=new Cliente(IDCliente, artista, sedie, maxWaiting);
        		IDCliente++;
        		client.start();
        	}
        	try {
        		Thread.sleep((long) (Math.random()*5000+6000));
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
    }
## Cliente
Una volta che viene fatto partire il thread esso verificherà la disponibilità di una sedia periodicamente utilizzando il comando tryAcquire dei semafori fino a che il tempo massimo di attesa non passerà, in tal caso la variabile sediaPresa otterrà il valore di false.

    boolean sediaPresa = false;
    try {
    	sediaPresa=sedie.tryAcquire(maxWaiting, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
    	e.printStackTrace();
    }

Se il thread fosse stato in grado di ottenere la sedia esso si metterà in coda per l'artista e, quando quest'ultimo sarà libero, si farà fare il ritratto per un tempo casuale compreso fra 5 e 10 secondi, grazie alla funzione System.currentTimeMillis() verrà visualizzato anche il tempo di attesa per ogni operazione.

    if(sediaPresa) {																		 
    	System.out.println("Il cliente " + ID + " è riuscito a sedersi! Tempo di attesa: " + tempoSedia +".\n");
    	auxTime=System.currentTimeMillis();
    	try {																				//L'acquisizione della sedia è andato a buon fine	
    		artista.acquire();																//Aspetta che si liberi l'artista
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	sedie.release();
    	tempoArtista = System.currentTimeMillis()-auxTime;
    		System.out.println("L'artista inizia il ritratto del cliente " + ID +". Tempo in cui il cliente è stato seduto: " + tempoArtista + "\n");
    	auxTime = System.currentTimeMillis();
    	try {													//Tempo ritratto
    		sleep((long) Math.random()*5000+5000);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	artista.release();
    	tempoRitratto=System.currentTimeMillis()-auxTime;
    	System.out.println("L'artista ha finito il ritratto del cliente " + ID +". Tempo per eseguire il ritratto: " + tempoRitratto + "\n");
    }else {																					//L'acquisizione non è andata a buon fine, termine thread
    	System.out.println("Il cliente " + ID + " se ne è andato perchè ha aspettato troppo tempo! Orario di arrivo:" + auxTime + ", Tempo di attesa: " + tempoSedia +"\n");
    }
