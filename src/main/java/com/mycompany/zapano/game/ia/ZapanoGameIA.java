package com.mycompany.zapano.game.ia;

import java.io.*;
import java.util.*;

import com.mycompany.zapano.game.ia.Entitys.Player.Opponent;
import com.mycompany.zapano.game.ia.Entitys.Table.Card;

public class ZapanoGameIA {

    public static void main(String[] args) {
        System.out.println("El juego ha comenzado!");

        Opponent ia1 = new Opponent("IA1", 2);
        Opponent ia2 = new Opponent("IA2", 2);

        String trainingDataFilePath = "src/main/resources/output/trainingData.csv";
        String trainedModelPath = "src/main/resources/output/trainedModel.nnet";

        new File("src/main/resources/output").mkdirs(); // Asegurar que la carpeta exista

        int rondas = 1000;
        int victoriasIA1 = 0;
        int victoriasIA2 = 0;
        int contadorMayor = 0;
        int contadorMenor = 0;
        int turnosAntesDeRetirarseIA1 = 0;
        int turnosAntesDeRetirarseIA2 = 0;
        int retirosIA1 = 0;
        int retirosIA2 = 0;
        Map<String, Integer> estrategiasIA1 = new HashMap<>();
        Map<String, Integer> estrategiasIA2 = new HashMap<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(trainingDataFilePath, true))) {

            for (int ronda = 0; ronda < rondas; ronda++) {
                if (ronda % 100 == 0) {
                    System.out.println("Ronda: " + ronda);
                }

                ArrayList<Card> mazo = crearMazo();
                Collections.shuffle(mazo);

                ArrayList<Card> cartasIA1 = new ArrayList<>();
                ArrayList<Card> cartasIA2 = new ArrayList<>();
                int sumaCartasIA1 = 0;
                int sumaCartasIA2 = 0;

                Card cartaInicialIA1 = mazo.remove(0);
                cartasIA1.add(cartaInicialIA1);
                sumaCartasIA1 += cartaInicialIA1.getValue();

                Card cartaInicialIA2 = mazo.remove(0);
                cartasIA2.add(cartaInicialIA2);
                sumaCartasIA2 += cartaInicialIA2.getValue();

                Opponent iaQueDecide = (ronda % 2 == 0) ? ia1 : ia2;
                int[] winCondition = {iaQueDecide.decidirCondicionInicial(sumaCartasIA1, iaQueDecide.getVictorias())};
                
                if (winCondition[0] == 1) {
                    contadorMayor++;
                } else {
                    contadorMenor++;
                }

                int sumaPreviaIA1 = sumaCartasIA1;
                int sumaPreviaIA2 = sumaCartasIA2;
                int decisionPreviaIA1 = -1;
                int decisionPreviaIA2 = -1;

                for (int turno = 1; turno <= 6; turno++) {
                    String decisionIA1 = ia1.chooseTurnOption(winCondition[0], cartasIA1, sumaCartasIA1, turno, cartasIA2, ia1.getUltimaDecision());
                    if (!decisionIA1.equals("retirarse")) {
                        estrategiasIA1.put(decisionIA1, estrategiasIA1.getOrDefault(decisionIA1, 0) + 1);
                    } else {
                        turnosAntesDeRetirarseIA1 += turno;
                        retirosIA1++;
                        break;
                    }
                    
                    String decisionIA2 = ia2.chooseTurnOption(winCondition[0], cartasIA2, sumaCartasIA2, turno, cartasIA1, ia2.getUltimaDecision());
                    if (!decisionIA2.equals("retirarse")) {
                        estrategiasIA2.put(decisionIA2, estrategiasIA2.getOrDefault(decisionIA2, 0) + 1);
                    } else {
                        turnosAntesDeRetirarseIA2 += turno;
                        retirosIA2++;
                        break;
                    }
                }

                int ganador = (winCondition[0] == 1 && sumaCartasIA1 > sumaCartasIA2)
                        || (winCondition[0] == 0 && sumaCartasIA1 < sumaCartasIA2) ? 1 : 2;

                if (ganador == 1) {
                    victoriasIA1++;
                } else {
                    victoriasIA2++;
                }
            }

            ia1.entrenarModelo();

            System.out.println("\nResumen Final:");
            System.out.println("IA1 ganó: " + victoriasIA1 + " veces");
            System.out.println("IA2 ganó: " + victoriasIA2 + " veces");
            System.out.println("Condición de victoria más elegida: " + (contadorMayor > contadorMenor ? "Mayor" : "Menor"));
            System.out.println("Veces que se eligió Mayor: " + contadorMayor);
            System.out.println("Veces que se eligió Menor: " + contadorMenor);
            System.out.println("Estrategias IA1: " + estrategiasIA1);
            System.out.println("Estrategias IA2: " + estrategiasIA2);
            System.out.println("Promedio de turnos antes de retirarse IA1: " + (retirosIA1 == 0 ? 0 : (double) turnosAntesDeRetirarseIA1 / retirosIA1));
            System.out.println("Promedio de turnos antes de retirarse IA2: " + (retirosIA2 == 0 ? 0 : (double) turnosAntesDeRetirarseIA2 / retirosIA2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Card> crearMazo() {
        ArrayList<Card> mazo = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int valor = 1; valor <= 13; valor++) {
                String rank = Card.getPosibleRanks().get(valor - 1);
                for (String palo : Card.getPosibleSuits()) {
                    mazo.add(new Card(valor, rank, palo));
                }
            }
        }
        return mazo;
    }
}
