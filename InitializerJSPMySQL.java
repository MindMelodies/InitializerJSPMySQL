import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class InitializerJSPMySQL extends JFrame {
    private JTextField campoGroupId;
    private JTextField campoArtifactId;
    private JButton pulsanteCrea;
    private JButton pulsanteInstall;
    private JTextArea areaOutput;

    private final Color COLOR_BACKGROUND = new Color(245, 245, 250);
    private final Color COLOR_PANEL = new Color(255, 255, 255);
    private final Color COLOR_TEXT = new Color(0, 0, 0);
    private final Color COLOR_ACCENT = new Color(33, 150, 243);
    private final Color COLOR_BUTTON = new Color(76, 175, 80);
    private final Color COLOR_BUTTON_PRESSED = new Color(56, 142, 60);

    public InitializerJSPMySQL() {
        setTitle("Creatore di Progetti JSP con dipendenze MYSQL");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BACKGROUND);

        JPanel pannelloInput = createInputPanel();
        add(pannelloInput, BorderLayout.NORTH);

        areaOutput = createOutputArea();
        JScrollPane pannelloScorrimento = new JScrollPane(areaOutput);
        pannelloScorrimento.setBorder(BorderFactory.createEmptyBorder());
        add(pannelloScorrimento, BorderLayout.CENTER);

        JLabel copyright = createCopyrightLabel();
        add(copyright, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel pannelloInput = new JPanel(new GridBagLayout());
        pannelloInput.setBackground(COLOR_PANEL);
        pannelloInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addLabelAndTextField(pannelloInput, gbc, "ID Gruppo:", "com.tuodominio");
        addLabelAndTextField(pannelloInput, gbc, "ID Artefatto:", "nome-progetto");
        addButtons(pannelloInput, gbc);

        return pannelloInput;
    }

    private void addLabelAndTextField(JPanel panel, GridBagConstraints gbc, String labelText, String defaultText) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        JLabel label = new JLabel("<html><b>" + labelText + "</b></html>");
        label.setForeground(COLOR_TEXT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField textField = new JTextField(defaultText, 20);
        textField.setBackground(COLOR_PANEL);
        textField.setForeground(COLOR_TEXT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_ACCENT),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(textField, gbc);

        if (labelText.contains("Gruppo")) {
            campoGroupId = textField;
        } else {
            campoArtifactId = textField;
        }
    }

    private void addButtons(JPanel panel, GridBagConstraints gbc) {
        JPanel pannelloPulsanti = new JPanel(new GridLayout(1, 2, 10, 0));
        pannelloPulsanti.setOpaque(false);

        pulsanteCrea = createStyledButton("Crea Progetto", COLOR_BUTTON);
        pulsanteCrea.addActionListener(e -> creaProgetto());
        pannelloPulsanti.add(pulsanteCrea);

        pulsanteInstall = createStyledButton("mvn clean install", COLOR_ACCENT);
        pulsanteInstall.addActionListener(e -> eseguiMvnCleanInstall());
        pannelloPulsanti.add(pulsanteInstall);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(pannelloPulsanti, gbc);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
            public void mousePressed(MouseEvent evt) {
                button.setBackground(COLOR_BUTTON_PRESSED);
                button.setForeground(new Color(220, 220, 220));
            }
            public void mouseReleased(MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    private JTextArea createOutputArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(COLOR_PANEL);
        area.setForeground(COLOR_TEXT);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }

    private JLabel createCopyrightLabel() {
        JLabel copyright = new JLabel("<html><b>Creato con <font color='red'>❤</font> da <font color='" + String.format("#%06x", COLOR_ACCENT.getRGB() & 0xFFFFFF) + "'>@MindMelodies</font> e Claude-3.5-Sonnet-200k</b></html>");
        copyright.setCursor(new Cursor(Cursor.HAND_CURSOR));
        copyright.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/MindMelodies"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        copyright.setHorizontalAlignment(JLabel.CENTER);
        copyright.setOpaque(true);
        copyright.setBackground(COLOR_PANEL);
        copyright.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return copyright;
    }

    private void disabilitaCampi() {
        campoGroupId.setEditable(false);
        campoArtifactId.setEditable(false);
        pulsanteCrea.setEnabled(false);
        pulsanteInstall.setEnabled(false);
    }

    private void abilitaCampi() {
        campoGroupId.setEditable(true);
        campoArtifactId.setEditable(true);
        pulsanteCrea.setEnabled(true);
        pulsanteInstall.setEnabled(true);
    }

    private void creaProgetto() {
        pulsanteCrea.setText("Attendi...");
        disabilitaCampi();
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
                abilitaCampi();
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
        pulsanteInstall.setText("Attendi...");
        disabilitaCampi();
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
                pulsanteInstall.setText("mvn clean install");
                abilitaCampi();
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
