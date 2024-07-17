import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class InitializerJSPMySQL extends JFrame {
    private JTextField campoGroupId;
    private JTextField campoArtifactId;
    private JButton pulsanteCrea;
    private JButton pulsanteInstall;
    private JTextArea areaOutput;

    public InitializerJSPMySQL() {
        setTitle("Creatore di Progetti JSP con dipendenze MYSQL");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel pannelloInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        pannelloInput.add(new JLabel("ID Gruppo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        campoGroupId = new JTextField("com.tuodominio", 20);
        pannelloInput.add(campoGroupId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pannelloInput.add(new JLabel("ID Artefatto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        campoArtifactId = new JTextField("nome-progetto", 20);
        pannelloInput.add(campoArtifactId, gbc);

        JPanel pannelloPulsanti = new JPanel(new GridLayout(1, 2, 5, 0));

        pulsanteCrea = new JButton("Crea Progetto");
        pulsanteCrea.addActionListener(e -> creaProgetto());
        pannelloPulsanti.add(pulsanteCrea);

        pulsanteInstall = new JButton("mvn clean install");
        pulsanteInstall.addActionListener(e -> eseguiMvnCleanInstall());
        pannelloPulsanti.add(pulsanteInstall);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        pannelloInput.add(pannelloPulsanti, gbc);

        add(pannelloInput, BorderLayout.NORTH);

        areaOutput = new JTextArea();
        areaOutput.setEditable(false);
        JScrollPane pannelloScorrimento = new JScrollPane(areaOutput);
        add(pannelloScorrimento, BorderLayout.CENTER);

        JLabel copyright = new JLabel("Creato con ❤ da github.com/MindMelodies e Claude-3.5-Sonnet-200k");
        copyright.setHorizontalAlignment(JLabel.CENTER);
        add(copyright, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void creaProgetto() {
        pulsanteCrea.setText("Attendi...");
        pulsanteCrea.setEnabled(false);
        areaOutput.setText("");

        SwingWorker<Void, String> lavoratore = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                String groupId = campoGroupId.getText();
                String artifactId = campoArtifactId.getText();

                String comando = String.format(
                        "mvn archetype:generate -DarchetypeArtifactId=maven-archetype-webapp -DgroupId=%s -DartifactId=%s -DinteractiveMode=false",
                        groupId, artifactId);

                try {
                    Process processo = Runtime.getRuntime().exec(new String[] { "cmd", "/c", comando });
                    BufferedReader lettore = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    String linea;
                    while ((linea = lettore.readLine()) != null) {
                        publish(linea);
                    }
                    int codiceUscita = processo.waitFor();
                    if (codiceUscita == 0) {
                        publish("\nProgetto iniziale creato");
                        aggiornaPomXml(artifactId);
                        aggiornaWebXml(artifactId);
                    } else {
                        publish("\nErrore nella creazione del progetto. Controlla l'output per i dettagli.");
                    }
                } catch (IOException | InterruptedException ex) {
                    publish("\nErrore: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> frammenti) {
                for (String frammento : frammenti) {
                    areaOutput.append(frammento + "\n");
                }
            }

            @Override
            protected void done() {
                pulsanteCrea.setText("Crea Progetto");
                pulsanteCrea.setEnabled(true);
            }

            private void aggiornaPomXml(String artifactId) {
                Path percorsoPom = Paths.get(artifactId, "pom.xml");
                try {
                    String contenuto = new String(Files.readAllBytes(percorsoPom));
                    String nuoveDipendenze = "  <dependencies>\n" +
                            "    <dependency>\n" +
                            "      <groupId>junit</groupId>\n" +
                            "      <artifactId>junit</artifactId>\n" +
                            "      <version>4.11</version>\n" +
                            "      <scope>test</scope>\n" +
                            "    </dependency>\n" +
                            "    <dependency>\n" +
                            "      <groupId>javax.servlet</groupId>\n" +
                            "      <artifactId>jstl</artifactId>\n" +
                            "      <version>1.2</version>\n" +
                            "    </dependency>\n" +
                            "\n" +
                            "    <!-- Dipendenza JSTL SQL -->\n" +
                            "    <dependency>\n" +
                            "      <groupId>taglibs</groupId>\n" +
                            "      <artifactId>standard</artifactId>\n" +
                            "      <version>1.1.2</version>\n" +
                            "    </dependency>\n" +
                            "\n" +
                            "    <!-- Dipendenza MySQL Connector -->\n" +
                            "    <dependency>\n" +
                            "      <groupId>mysql</groupId>\n" +
                            "      <artifactId>mysql-connector-java</artifactId>\n" +
                            "      <version>8.0.26</version>\n" +
                            "    </dependency>\n" +
                            "\n" +
                            "    <!-- Dipendenza Servlet API (fornita dal container) -->\n" +
                            "    <dependency>\n" +
                            "      <groupId>javax.servlet</groupId>\n" +
                            "      <artifactId>javax.servlet-api</artifactId>\n" +
                            "      <version>4.0.1</version>\n" +
                            "      <scope>provided</scope>\n" +
                            "    </dependency>\n" +
                            "  </dependencies>";

                    contenuto = contenuto.replaceFirst("<dependencies>[\\s\\S]*?</dependencies>", nuoveDipendenze);
                    Files.write(percorsoPom, contenuto.getBytes());
                    publish("\nFile pom.xml aggiornato con le nuove dipendenze.");
                } catch (IOException e) {
                    publish("\nErrore nell'aggiornamento di pom.xml: " + e.getMessage());
                }
            }

            private void aggiornaWebXml(String artifactId) {
                Path percorsoWebXml = Paths.get(artifactId, "src", "main", "webapp", "WEB-INF", "web.xml");
                try {
                    String nuovoContenuto = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" \n" +
                            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "         xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                            "         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd\"\n" +
                            "         version=\"3.1\">\n" +
                            "  <resource-ref>\n" +
                            "    <description>Connessione DB</description>\n" +
                            "    <res-ref-name>jdbc/MySQLDB</res-ref-name>\n" +
                            "    <res-type>javax.sql.DataSource</res-type>\n" +
                            "    <res-auth>Container</res-auth>\n" +
                            "  </resource-ref>\n" +
                            "</web-app>";

                    Files.write(percorsoWebXml, nuovoContenuto.getBytes());
                    publish("\nFile web.xml aggiornato con il nuovo contenuto.");
                } catch (IOException e) {
                    publish("\nErrore nell'aggiornamento di web.xml: " + e.getMessage());
                }
            }
        };

        lavoratore.execute();
    }

    private void eseguiMvnCleanInstall() {
        pulsanteInstall.setEnabled(false);
        areaOutput.setText("");

        SwingWorker<Void, String> lavoratore = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                String artifactId = campoArtifactId.getText();
                String comando = "mvn clean install";

                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", comando);
                    pb.directory(new File(artifactId));
                    Process processo = pb.start();
                    BufferedReader lettore = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    String linea;
                    while ((linea = lettore.readLine()) != null) {
                        publish(linea);
                    }
                    int codiceUscita = processo.waitFor();
                    if (codiceUscita == 0) {
                        publish("\nComando mvn clean install eseguito con successo");
                    } else {
                        publish("\nErrore nell'esecuzione di mvn clean install. Controlla l'output per i dettagli.");
                    }
                } catch (IOException | InterruptedException ex) {
                    publish("\nErrore: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> frammenti) {
                for (String frammento : frammenti) {
                    areaOutput.append(frammento + "\n");
                }
            }

            @Override
            protected void done() {
                pulsanteInstall.setEnabled(true);
            }
        };

        lavoratore.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new InitializerJSPMySQL();
        });
    }
}
