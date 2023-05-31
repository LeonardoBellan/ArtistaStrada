import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Cliente extends Thread{
	private int ID;
	private long maxWaiting;
	private long tempoSedia;
	private long tempoArtista;
	private long tempoRitratto;
	private Semaphore artista;
	private Semaphore sedie;
	
	Cliente(int ID, Semaphore artista, Semaphore sedie, long maxWaiting){
		this.ID=ID;
		this.maxWaiting=maxWaiting;
		this.artista=artista;
		this.sedie=sedie;
	}
	
	public void run() {
		boolean sediaPresa = false;
		
		System.out.println("Il cliente " + ID + " è interessato a farsi un ritratto!\n");
		
		long auxTime = System.currentTimeMillis();
		try {																					//Il cliente prova ad acquisire una sedia, 
			sediaPresa=sedie.tryAcquire(maxWaiting, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tempoSedia=System.currentTimeMillis()-auxTime;
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			artista.release();
			tempoRitratto=System.currentTimeMillis()-auxTime;
			System.out.println("L'artista ha finito il ritratto del cliente " + ID +". Tempo per eseguire il ritratto: " + tempoRitratto + "\n");
		}else {																					//L'acquisizione non è andata a buon fine, termine thread
			System.out.println("Il cliente " + ID + " se ne è andato perchè ha aspettato troppo tempo! Orario di arrivo:" + auxTime + ", Tempo di attesa: " + tempoSedia +"\n");
		}
	}
}
