package fractal.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fractal.transformations.Bent;
import fractal.transformations.Diamond;
import fractal.transformations.Disc;
import fractal.transformations.Ex;
import fractal.transformations.Exponential;
import fractal.transformations.Fisheye;
import fractal.transformations.Handkerchief;
import fractal.transformations.Heart;
import fractal.transformations.Horseshoe;
import fractal.transformations.Julia;
import fractal.transformations.Polar;
import fractal.transformations.Popcorn;
import fractal.transformations.Sinusoidal;
import fractal.transformations.Spherical;
import fractal.transformations.Spiral;
import fractal.transformations.Swirl;
import fractal.transformations.Transformation;
import fractal.transformations.Waves;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import lombok.experimental.UtilityClass;

@SuppressWarnings({
    "MagicNumber",
    "ParameterNumber",
    "RegexpSinglelineJava",
    "MultipleStringLiterals",
    "CyclomaticComplexity"
})
@SuppressFBWarnings({
    "PATH_TRAVERSAL_IN",
    "IMC_IMMATURE_CLASS_PRINTSTACKTRACE",
    "CC_CYCLOMATIC_COMPLEXITY",
    "DMI_RANDOM_USED_ONLY_ONCE",
    "PREDICTABLE_RANDOM",
    "S508C_NON_TRANSLATABLE_STRING",
    "S508C_NO_SETLABELFOR",
    "S508C_SET_COMP_COLOR",
    "SACM_STATIC_ARRAY_CREATED_IN_METHOD"
})
@UtilityClass
public class Main {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel controlPanel;
    private static JPanel fractalPanel;
    private static JList<String> transformationsList;
    private static DefaultListModel<String> listModel;
    private static List<Transformation> variations;
    private static JTextField widthField;
    private static JTextField heightField;
    private static JTextField samplesField;
    private static JTextField iterPerSampleField;
    private static JTextField symmetryField;
    private static JComboBox<String> executionModeComboBox;
    private static JTextField numThreadsField;
    private static JComboBox<ImageFormat> imageFormatComboBox;
    private static String currentImagePath;

    public static void main(String[] args) {
        variations = new ArrayList<>();
        listModel = new DefaultListModel<>();

        frame = new JFrame("Fractal Flame Generator");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (currentImagePath != null) {
                    try {
                        Files.delete(Paths.get(currentImagePath));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);

        controlPanel = new JPanel();
        controlPanel.setBackground(Color.DARK_GRAY);
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Поля для ввода параметров
        widthField = createTextField("Image Width", gbc, controlPanel);
        heightField = createTextField("Image Height", gbc, controlPanel);
        samplesField = createTextField("Number of Samples", gbc, controlPanel);
        iterPerSampleField = createTextField("Iterations per Sample", gbc, controlPanel);
        symmetryField = createTextField("Symmetry", gbc, controlPanel);
        executionModeComboBox =
            createComboBox("Execution Mode", new String[] {"Single-threaded", "Multi-threaded"}, gbc, controlPanel);
        numThreadsField = createTextField("Number of Threads", gbc, controlPanel);
        imageFormatComboBox = createComboBox("Image Format", ImageFormat.values(), gbc, controlPanel);

        transformationsList = new JList<>(listModel);
        transformationsList.setBackground(Color.DARK_GRAY);
        transformationsList.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(transformationsList);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        controlPanel.add(scrollPane, gbc);

        JButton addButton = new JButton("Add Transformation");
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addTransformation());
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        controlPanel.add(addButton, gbc);

        JButton removeButton = new JButton("Remove Transformation");
        removeButton.setBackground(Color.DARK_GRAY);
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(e -> removeTransformation());
        gbc.gridy++;
        controlPanel.add(removeButton, gbc);

        JButton generateButton = new JButton("Generate Fractal");
        generateButton.setBackground(Color.DARK_GRAY);
        generateButton.setForeground(Color.WHITE);
        generateButton.addActionListener(e -> generateFractal());
        gbc.gridy++;
        controlPanel.add(generateButton, gbc);

        JButton saveButton = new JButton("Save Fractal");
        saveButton.setBackground(Color.DARK_GRAY);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveFractal());
        gbc.gridy++;
        controlPanel.add(saveButton, gbc);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            if (currentImagePath != null) {
                try {
                    Files.delete(Paths.get(currentImagePath));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.exit(0);
        });
        gbc.gridy++;
        controlPanel.add(exitButton, gbc);

        fractalPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Здесь будет рисоваться фрактал
            }
        };
        fractalPanel.setBackground(Color.BLACK);

        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(fractalPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JTextField createTextField(String labelText, GridBagConstraints gbc, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        panel.add(label, gbc);
        gbc.gridx++;
        JTextField textField = new JTextField(10);
        panel.add(textField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        return textField;
    }

    private static <T> JComboBox<T> createComboBox(
        String labelText,
        T[] options,
        GridBagConstraints gbc,
        JPanel panel
    ) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        panel.add(label, gbc);
        gbc.gridx++;
        JComboBox<T> comboBox = new JComboBox<>(options);
        panel.add(comboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        return comboBox;
    }

    private static void addTransformation() {
        String[] options =
            {"Sinusoidal", "Spherical", "Swirl", "Horseshoe", "Polar", "Handkerchief", "Heart", "Disc", "Spiral",
                "Diamond", "Ex", "Julia", "Bent", "Waves", "Fisheye", "Popcorn", "Exponential"};
        String choice =
            (String) JOptionPane.showInputDialog(frame, "Choose a transformation to add:", "Add Transformation",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice != null) {
            Transformation selectedTransformation = switch (choice) {
                case "Sinusoidal" -> new Sinusoidal();
                case "Spherical" -> new Spherical();
                case "Swirl" -> new Swirl();
                case "Horseshoe" -> new Horseshoe();
                case "Polar" -> new Polar();
                case "Handkerchief" -> new Handkerchief();
                case "Heart" -> new Heart();
                case "Disc" -> new Disc();
                case "Spiral" -> new Spiral();
                case "Diamond" -> new Diamond();
                case "Ex" -> new Ex();
                case "Julia" -> new Julia(Math.random() * 2 * Math.PI);
                case "Bent" -> new Bent();
                case "Waves" -> new Waves(Math.random(), Math.random(), Math.random(), Math.random());
                case "Fisheye" -> new Fisheye();
                case "Popcorn" -> new Popcorn(Math.random(), Math.random());
                case "Exponential" -> new Exponential();
                default -> throw new IllegalArgumentException("Invalid choice");
            };

            variations.add(selectedTransformation);
            listModel.addElement(choice);
        }
    }

    private static void removeTransformation() {
        int selectedIndex = transformationsList.getSelectedIndex();
        if (selectedIndex != -1) {
            variations.remove(selectedIndex);
            listModel.remove(selectedIndex);
        }
    }

    private static void generateFractal() {
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        int samples = Integer.parseInt(samplesField.getText());
        int iterPerSample = Integer.parseInt(iterPerSampleField.getText());
        int symmetry = Integer.parseInt(symmetryField.getText());
        int executionMode = executionModeComboBox.getSelectedIndex() + 1;
        int numThreads = executionMode == 2 ? Integer.parseInt(numThreadsField.getText()) : 1;

        FractalImage canvas = FractalImage.create(width, height);
        Rect world = new Rect(-2.0, -2.0, 4.0, 4.0);

        SecureRandom secureRandom = new SecureRandom();
        long seed = secureRandom.nextLong();
        Random random = new Random(seed);

        FractalImage image;
        if (executionMode == 1) {
            image =
                FractalFlameGenerator.render(canvas, world, variations, samples, iterPerSample, seed, symmetry, random);
        } else {
            image = FractalFlameGenerator.renderMultiThreaded(canvas, world, variations, samples, iterPerSample, seed,
                symmetry, numThreads, random);
        }

        ImageFormat selectedFormat = (ImageFormat) imageFormatComboBox.getSelectedItem();
        String uniqueFileName = "fractal_flame_" + UUID.randomUUID() + "." + selectedFormat.name().toLowerCase();
        currentImagePath = uniqueFileName;
        try {
            ImageUtils.save(image, Paths.get(uniqueFileName), selectedFormat);
            System.out.println("Image saved successfully as " + uniqueFileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to save image.");
        }

        // Отображение изображения на fractalPanel
        ImageIcon icon = new ImageIcon(uniqueFileName);
        JLabel label = new JLabel(icon);
        fractalPanel.removeAll();
        fractalPanel.add(label);
        fractalPanel.revalidate();
        fractalPanel.repaint();
    }

    private static void saveFractal() {
        if (currentImagePath != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Fractal Image");
            fileChooser.setSelectedFile(new File(currentImagePath));

            // Добавление фильтров для различных форматов изображений
            fileChooser.addChoosableFileFilter(new ImageFileFilter("PNG", "png"));
            fileChooser.addChoosableFileFilter(new ImageFileFilter("JPEG", "jpeg", "jpg"));
            fileChooser.addChoosableFileFilter(new ImageFileFilter("BMP", "bmp"));
            fileChooser.addChoosableFileFilter(new ImageFileFilter("GIF", "gif"));

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String format = ((ImageFileFilter) fileChooser.getFileFilter()).getExtension();

                // Ensure the file has the correct extension
                if (!fileToSave.getName().toLowerCase().endsWith("." + format)) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + "." + format);
                }

                try {
                    ImageUtils.save(FractalImage.load(currentImagePath), Paths.get(fileToSave.getAbsolutePath()),
                        ImageFormat.valueOf(format.toUpperCase()));
                    System.out.println("Image saved successfully to " + fileToSave.getAbsolutePath());
                    currentImagePath = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to save image.");
                }
            }
        }
    }

    private static class ImageFileFilter extends javax.swing.filechooser.FileFilter {
        private final String description;
        private final String[] extensions;

        ImageFileFilter(String description, String... extensions) {
            this.description = description;
            this.extensions = extensions;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                for (String ext : extensions) {
                    if (extension.equalsIgnoreCase(ext)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public String getExtension() {
            return extensions[0];
        }

        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
}
