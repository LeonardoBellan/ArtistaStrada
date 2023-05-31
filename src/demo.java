import java.util.concurrent.Semaphore;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Semaphore artista = new Semaphore(1);
		Semaphore sedie = new Semaphore(4);
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
