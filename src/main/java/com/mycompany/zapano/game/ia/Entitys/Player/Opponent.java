package com.mycompany.zapano.game.ia.Entitys.Player;

import com.mycompany.zapano.game.ia.Entitys.Table.Card;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import java.io.*;
import java.util.*;

public class Opponent {

    private String name;
    private int difficulty;
    private int victorias;
    private int ultimaDecision;
    private int victoriasMayor;
    private int victoriasMenor;
    private static final String TRAINING_FILE = "src/main/resources/output/trainingData.csv";
    private static final String MODEL_FILE = "src/main/resources/output/trainedModel.nnet";
    private NeuralNetwork<?> neuralNet;

    public Opponent(String name, int difficulty) {
        this.name = name;
        this.difficulty = difficulty;
        this.victorias = 0;
        this.ultimaDecision = -1;
        this.victoriasMayor = 0;
        this.victoriasMenor = 0;
        cargarModelo();
    }

    private void cargarModelo() {
        File modelFile = new File(MODEL_FILE);
        if (!modelFile.exists()) {
            crearModeloVacio();
        } else {
            neuralNet = NeuralNetwork.createFromFile(MODEL_FILE);
        }
    }

    private void crearModeloVacio() {
        try {
            File outputDir = new File("src/main/resources/output");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            if (new File(MODEL_FILE).exists()) {
                return; // Si el modelo ya existe, no lo sobreescribimos
            }

            neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 7, 10, 6);

            if (neuralNet != null) {
                neuralNet.save(MODEL_FILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int decidirCondicionInicial(int sumaCartas, int victorias) {
        return (sumaCartas > 10) ? 1 : 0;
    }

    public String chooseTurnOption(int winCondition, List<Card> cartas, int sumaCartas, int turno,
            List<Card> cartasOponente, int decisionPrevia) {
        if (difficulty == 1) {
            return chooseRandomOption();
        }

        int sumaCartasOponente = cartasOponente.stream().mapToInt(Card::getValue).sum();
        int sumaPrevia = (cartas.size() > 1) ? (sumaCartas - cartas.get(cartas.size() - 1).getValue()) : sumaCartas;

        double[] input = {sumaCartas / 100.0, sumaCartasOponente / 100.0, winCondition, turno / 10.0,
            sumaPrevia / 100.0, decisionPrevia / 10.0, ultimaDecision / 10.0};

        neuralNet.setInput(input);
        neuralNet.calculate();
        double[] output = neuralNet.getOutput();

        int decision = obtenerDecision(output);
        ultimaDecision = decision;

        if (decision == 1 || decision == 4) { // "invertir" o "robar e invertir"
            winCondition = (winCondition == 1) ? 0 : 1;
        }

        return intToDecision(decision);
    }

    private String chooseRandomOption() {
        String[] options = {"robar", "invertir", "proponer finalizar ronda", "retirarse", "robar e invertir"};
        return options[new Random().nextInt(options.length)];
    }

    private int obtenerDecision(double[] output) {
        int decision = 0;
        double maxVal = output[0];
        for (int i = 1; i < output.length; i++) {
            if (output[i] > maxVal) {
                maxVal = output[i];
                decision = i;
            }
        }
        return decision;
    }

    private String intToDecision(int decision) {
        String[] options = {"robar", "invertir", "proponer finalizar ronda", "retirarse", "robar e invertir"};
        return options[Math.max(0, Math.min(decision, options.length - 1))];
    }

    public void registrarVictoria(int winCondition) {
        victorias++;
        if (winCondition == 1) {
            victoriasMayor++;
        } else {
            victoriasMenor++;
        }
    }

    public void writeTrainingData(int sumaCartas, int sumaCartasOponente, int winCondition, int turno, int sumaPrevia, int decisionPrevia, int decision, boolean gano) {
        File file = new File(TRAINING_FILE);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            int finalWinCondition = (decision == 1) ? (winCondition == 1 ? 0 : 1) : winCondition;

            // Asegurar que no haya saltos de línea extra
            String data = sumaCartas + "," + sumaCartasOponente + "," + finalWinCondition + "," + turno + ","
                    + sumaPrevia + "," + decisionPrevia + "," + decision + "," + (gano ? 1 : 0);

            writer.write(data);
            writer.newLine(); // Agregar solo una nueva línea
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void entrenarModelo() {
        DataSet trainingSet = new DataSet(7, 6);
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINING_FILE))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { // Saltar la cabecera
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                double[] input = new double[7];
                for (int j = 0; j < 7; j++) {
                    input[j] = Double.parseDouble(values[j]) / 100.0;
                }
                double[] output = new double[6];
                output[Integer.parseInt(values[6])] = 1.0;
                trainingSet.add(new DataSetRow(input, output));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((SupervisedLearning) neuralNet.getLearningRule()).setMaxIterations(1000);
        neuralNet.learn(trainingSet);
        neuralNet.save(MODEL_FILE);
    }

    public String getName() {
        return name;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getVictoriasMayor() {
        return victoriasMayor;
    }

    public int getVictoriasMenor() {
        return victoriasMenor;
    }

    public int getUltimaDecision() {
        return ultimaDecision;
    }
}
