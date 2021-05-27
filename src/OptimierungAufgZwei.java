import de.medieninf.ads.ADSTool;


public class OptimierungAufgZwei {

    static final int WAHR = 1;
    static final int FALSCH = -1;
    static final int UNBELEGT = 0;

    static int zuweisungsCount = 0;

    static final char[]letters = {'0','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};


    public static void main (String[] args){
        // Eingabe des Dateiname, wenn keine Eingabe dann klauseln1.dat

        int[][] klauseln = ADSTool.readInt2Array("erfuellbarkeit/klauseln9.dat");
        //printKlausel(klauseln);



        int[] belegung = new int[getHighestValue(klauseln)+1];

        boolean[]bereitsValid = new boolean[klauseln.length];


        long startTime = System.nanoTime();
        long duration = 0;
        if(erfuellbar(klauseln, belegung, bereitsValid)){
            duration = System.nanoTime() - startTime;
            System.out.println("Ist Erfüllbar");

        }else{
            duration = System.nanoTime() - startTime;
            System.out.println("Ist nicht Erfüllbar");

        }
        duration = duration/1000000;
        System.out.println("In: "+duration+" ms oder "+(duration/1000)+" s");
        System.out.println("Mit "+zuweisungsCount+" Zuweisungen.");

    }

    public static int getHighestValue(int[][] klauseln){
        int highestValue=0;
        for(int[] subklausel : klauseln){
            for(int proposition : subklausel){
                if(Math.abs(proposition) > highestValue){
                    highestValue = proposition;
                }
            }
        }
    return highestValue;
    }


    /**
     * 1 == Wahr
     * 0 == Unelegt
     * -1 == Falsch
     */

    public static boolean erfuellbar(int [][] klauseln, int []belegung, boolean[]bereitsValid){
        boolean erfuellt = true;

        for (int i = 0; i< klauseln.length;i++){
            //IST XY bereits wahr
            if(!bereitsValid[i]){
                if (!subKlausel(klauseln[i], belegung)){
                    erfuellt = false;
                    bereitsValid[i] = false;
                    break;
                }else{
                    bereitsValid[i] = true;
                }
            }

        }

        if(erfuellt){
            printBelegung(belegung);
            //printBelegungLetters(belegung);
            return true;
        }

        //?
        //Durchlauf der Belegung
        for(int i = 1; i< belegung.length;i++){
            //Wenn Stelle unbesetzt auf 1/WAHR setzten
            if(belegung[i]==UNBELEGT){
                belegung[i] = WAHR;
                bereitsValid = ueberPrufeValide(klauseln, belegung);
                zuweisungsCount++;

                //Rekursiver Aufruf -> Gibt True zurück, wenn irgendwann Abbruchbedinung erfüllt ist
                if(erfuellbar(klauseln, belegung, bereitsValid)){
                    return true;
                }else{
                    belegung[i] = FALSCH;
                    bereitsValid = ueberPrufeValide(klauseln, belegung);
                    zuweisungsCount++;
                    if(erfuellbar(klauseln, belegung, bereitsValid)){
                        return true;
                    }
                    belegung[i] = UNBELEGT;
                    bereitsValid = ueberPrufeValide(klauseln, belegung);
                    zuweisungsCount++;
                    //System.out.println("Backtrack");
                    //printBelegung(belegung);
                }
            }
        }
    return false;
    }

    static boolean[] ueberPrufeValide(int[][]klauseln, int[]belegung){
        boolean[]validCheck = new boolean[klauseln.length];

        for (int i = 0; i< klauseln.length;i++){
            if (!subKlausel(klauseln[i], belegung)){
                validCheck[i] = false;
            }else{
                validCheck[i] = true;
            }
        }
        return validCheck;
    }

// Geht Klauseln array durch
    // Wenn subklauseln false ist, return false sonst true
    static boolean erfuellt(int[][] klauseln, int[]belegung, int[][]bereitsvalid){
      for (int i = 0; i< klauseln.length;i++){
          //IST XY bereits wahr
              if (!subKlausel(klauseln[i], belegung)){
                  return false;
              }else{
                  // Subklausel XY == WAHR
              }

      }
      return true;
    }


//geht subklausel durch, also die drei Werte
    //Wenn der wert größer 0 ist UND belegung der stelle Wahr ist, dann  return WAHR
    // Wenn der wert kleiner 0 ist UND belegung der (echt) stelle falsch ist, dann return FALSCH
    // WENN beides nicht zutrifft, dann return false
    // Trifft nicht zu wenn:
    public static boolean subKlausel(int[]subklausel, int[] belegung){
      for(int i=0; i<subklausel.length; i++){
          if(subklausel[i]>0 && belegung[subklausel[i]]==WAHR){
             return true;
         }
          if(subklausel[i]<0 && belegung[Math.abs(subklausel[i])]==FALSCH) {
              return true;
          }
      }
      return false;
    }

    public static void printBelegung(int[] belegung){
        for(int index : belegung){
            System.err.print(index +" | ");
        }System.err.println("");
    }

    public static void printBelegungLetters(int[] belegung){
        System.out.println();
        for(int i = 0; i<belegung.length;i++){
            if(belegung[i] < 0){
                System.out.print(" -");
            }
            System.out.print(letters[i]+" | ");
        }System.err.println("");
    }

    public static void printKlausel(int[][]klauseln){
        for(int[]subklausel : klauseln){
            System.out.print("(");
            for(int iteral : subklausel){
                if(iteral<0){
                    System.out.print("-");
                }
                System.out.print(letters[Math.abs(iteral)]);
            }System.out.print(") ^ ");

        }

    }

}
