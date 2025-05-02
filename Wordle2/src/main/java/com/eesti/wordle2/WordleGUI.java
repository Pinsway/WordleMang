package com.eesti.wordle2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleGUI extends Application {
    private Sõnad sõnad;
    private String õigeSõna;
    private List<HBox> ridad = new ArrayList<>();
    private int voor = 0; // hetkene voor

    public WordleGUI() throws FileNotFoundException {
    }

    public void start(Stage primaryStage) throws Exception {
        sõnad = new Sõnad("5 tähelised sõnad.txt");
        õigeSõna = sõnad.laeSuvalineSõna(); // õigeSõna ehk käiva mängu võidusõna txt failist

        VBox juur = new VBox(10);
        juur.setPadding(new Insets(10));

        Label pealkiri = new Label("EESTI KEELNE WORDLE!");
        juur.getChildren().add(pealkiri);

        Label juhised = new Label("Paku 5 tähelisi sõnu.\nTähe asukoht otsitavas sõnas õige = roheline\nTäht on otsitavas sõnas = kollane\nTäht ei ole otsitavas sõnas = valge");
        juur.getChildren().add(juhised);

        TextField pakkumiseVäli = new TextField();
        pakkumiseVäli.setPromptText("Sisesta 5-täheline eestikeelne nimisõna");
        juur.getChildren().add(pakkumiseVäli);

        FlowPane nupuRida = new FlowPane();
        nupuRida.setHgap(350);
        Button uueVooruNupp = Nupud.looUueVooruNupp();
        Button lõpetaProgrammNupp = Nupud.looLõpetaProgrammNupp();
        nupuRida.getChildren().addAll(uueVooruNupp, lõpetaProgrammNupp);

        juur.getChildren().add(nupuRida);


        uueVooruNupp.setOnAction(event -> {
            õigeSõna = sõnad.laeSuvalineSõna(); // Laeb uue sõna
            voor = 0;
            // tagasiside.setText("");

            for (int i = 0; i < ridad.size(); i++) {
                HBox rida = ridad.get(i);
                for (int j = 0; j < 5; j++) {
                    TextField kast = (TextField) rida.getChildren().get(j);
                    kast.clear();
                    kast.setStyle(null);
                }
            }
            pakkumiseVäli.requestFocus();
        });

        lõpetaProgrammNupp.setOnAction(event -> {
            primaryStage.close();
        });


        pakkumiseVäli.requestFocus(); // pakkumiste väli focus

        // looma kuus rida kus igas reas viis kasti, kuhu pakutud sõna tähed lähevad
        // kast muudab ka värvi või jääb valgeks - sõltub pakkumisest
        for (int i = 0; i < 6; i++) { // loome kuus rida
            HBox rida = new HBox(25); //horisontaalne kast ühe rea jaoks
            for (int j = 0; j < 5; j++) {
                TextField kast = new TextField(); // iga tähe jaoks eraldi kast
                kast.setPrefWidth(75);
                kast.setEditable(false);
                kast.setOnKeyPressed(event -> {
                    event.consume();
                });
                rida.getChildren().add(kast); // lisab tekstivälja reale
            }
            ridad.add(rida); // lisab rea mängulauale
            juur.getChildren().add(rida);
        }

        Label tagasiside = new Label();
        juur.getChildren().add(tagasiside);

        pakkumiseVäli.setOnAction(e -> {
            String pakkumine = pakkumiseVäli.getText().toUpperCase();
            if (!sõnad.onKehtivSõna(pakkumine)) { // kas sõna on txt failis või 5 tähte pikk
                tagasiside.setText("Sisestatud sõna ei ole olemas.\n VÕI \nSisestatud sõna ei ole 5 tähte pikk!");
                return;
            }
            // kontroll igale tähele reas ja värvi muutus kas kollaseks või roheliseks, kui tarvis
            for (int i = 0; i < 5; i++) {
                TextField kast = (TextField) ridad.get(voor).getChildren().get(i);
                if (pakkumine.charAt(i) == õigeSõna.charAt(i)) {
                    kast.setStyle("-fx-background-color: #00FF00;");
                } else if (õigeSõna.contains(String.valueOf(pakkumine.charAt(i)))) {
                    kast.setStyle("-fx-background-color: #FFFF00;");
                }
                kast.setText(String.valueOf(pakkumine.charAt(i)));
            }
            voor++; // next round boiii
            if (pakkumine.equals(õigeSõna)) {
                tagasiside.setText("Õige sõna! Sa võitsid!"); // big dub W

                try {
                    SalvestaArvatudSõnaFaili(õigeSõna);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            } else if (voor == 6) {
                tagasiside.setText("Sa kaotasid! Õige sõna oli " + õigeSõna + "."); // big L
            }
            pakkumiseVäli.clear();
        });

        Scene scene = new Scene(juur, 500, 500); //275, 450
        primaryStage.setScene(scene);
        primaryStage.setTitle("Wordle");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void SalvestaArvatudSõnaFaili(String sõna) throws FileNotFoundException {
        try (BufferedWriter fail = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("arvatud_sõnad.txt"), StandardCharsets.UTF_8))){
            fail.newLine();
            fail.write(sõna);
        } catch (IOException e) {
            System.out.println("Viga õige sõna salvestamisel faili: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}




