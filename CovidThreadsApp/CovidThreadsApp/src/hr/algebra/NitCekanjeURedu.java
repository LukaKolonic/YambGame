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
public class NitCekanjeURedu implements Runnable {
    
     private int brojNiti;

    public NitCekanjeURedu(final int brojNiti) {
        this.brojNiti = brojNiti;
    }

    @Override
    public void run() {
        while (true) {
        Configuration.isDolazak = false;
        Random generator = new Random();

            try {
                Thread.sleep(generator.nextInt(Configuration.MAX_GUZVA));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zauzmiMjestoNaTestiranju();
        }
    }

    private synchronized void zauzmiMjestoNaTestiranju() {
        while (Configuration.isTestiranjeUTijeku == true) {
            try {
                System.out.println("Redni broj #" + brojNiti + " - ceka na testiranje.");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Configuration.isTestiranjeUTijeku = true;
        if (Configuration.brLjudiURedu < Configuration.MAX_BR_LJUDI_U_REDU) {
            Configuration.brLjudiURedu++;
        } else {
            System.out.println("Redni broj #" + brojNiti + " - nema dovoljno mjesta u ambulanti!");
        }
        
        
        Configuration.isTestiranjeUTijeku = false;
        notifyAll();
    }

    
    
}
