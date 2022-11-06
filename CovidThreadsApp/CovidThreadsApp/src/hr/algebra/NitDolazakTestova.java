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
public class NitDolazakTestova implements Runnable{
    
    private int brojNiti;
	
    
    public NitDolazakTestova(final int brojNiti) {
        this.brojNiti = brojNiti;
    }


    @Override
    public void run() {
        
        while (true) {
            Random generator = new Random();

            try {
                Thread.sleep(generator.nextInt(Configuration.MAX_VRIJEME_CEKANJA_TESTOVA));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dostaviTestove();
        }

    }
    
    
    public synchronized void dostaviTestove() {

          while (Configuration.isTestiranjeUTijeku == true) {
            try {
                System.out.println("Redni broj #" + brojNiti + " - čekanje na dolazak novih testova");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        Configuration.isTestiranjeUTijeku = true;
        if (Configuration.brMjestaUAmbulanti < 1) {
            Configuration.isDolazak = true;
            System.out.println("Dolaze novi testovi!");
          
            Random generator = new Random();
            Configuration.brMjestaUAmbulanti += generator
                    .nextInt(Configuration.KAPACITET_AMBULANTE);
            
        } else {
            System.out.println("Nema potrebe za dolaskom testova, ima dovoljno!");
        }
        Configuration.isTestiranjeUTijeku = false;
        notifyAll();
    }

}
