/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import java.util.Random;

/**
 *
 * @author lukak
 */
public class NitTestiranje implements Runnable {
    
     private int brojNiti;
	
    public NitTestiranje(final int brojNiti) {
        this.brojNiti = brojNiti;
    }

    @Override
    public void run() {
        
        while (true) {
            Random generator = new Random();

            try {
                Thread.sleep(generator
                        .nextInt(Configuration.MAX_VRIJEME_CEKANJA));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            testirajSlijedecuOsobu();
        }
    }
    
    
    public synchronized void testirajSlijedecuOsobu() {

        while (Configuration.isTestiranjeUTijeku == true) {
            try {
                System.out.println("Redni broj #" + brojNiti + " - čekanje na test!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Configuration.isTestiranjeUTijeku = true;

        if (Configuration.brLjudiURedu > 0) {
            Configuration.brLjudiURedu--;
        }
        if (Configuration.brMjestaUAmbulanti > 0) {
            Configuration.brMjestaUAmbulanti--;
        }
        
        Configuration.isTestiranjeUTijeku = false;
        notifyAll();
    }
    
}
