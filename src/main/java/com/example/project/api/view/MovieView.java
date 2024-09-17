package com.example.project.api.view;

import com.example.project.api.controller.MovieController;
import com.example.project.api.model.Movie;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MovieView extends JFrame {
    private static JPanel rootPanel, addPanel, viewPanel, settingsPanel, helpPanel, sidebarPanel;
    private static JTabbedPane tabbedPane;
    private static JLabel imageLabel;
    private static JList<String> movieList;
    private static Map<String, String> moviePosterMap;

    private static DefaultListModel<String> movieListModel = new DefaultListModel<>();

    private static JTextField nameField, yearField, posterField;
    private static JButton chooseFileButton, saveButton, cancelButton;

    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 300;

    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 300;

    private static final int MOVIE_LIST_WIDTH = 350;
    private static final int MOVIE_LIST_HEIGHT = 0;

    MovieController controller;

    public MovieView(MovieController controller) {
        this.controller = controller;
        createUI();
        registerListeners(chooseFileButton, saveButton, movieList, this, posterField);
    }

    private void createUI() {
        rootPanel = createRootPanel();
        createPanels();
        tabbedPane = createTabbedPane();
        rootPanel.add(tabbedPane, BorderLayout.CENTER);
        this.add(rootPanel);
        this.setTitle("Movie App");
        showFrame(this);
    }

    private JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    private JPanel createRootPanel() {
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        return rootPanel;
    }

    private void createPanels() {
        addPanel = createAddPanel();
        viewPanel = createViewPanel();
        settingsPanel = createSettingsPanel();
        helpPanel = createHelpPanel();
    }

    private static JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        nameField = new JTextField(12);
        yearField = new JTextField(12);
        posterField = new JTextField(12);
        chooseFileButton = new JButton("Choose a File");
        cancelButton = new JButton("Cancel");
        saveButton = new JButton("Save");

        addComponent(panel, new JLabel("Name :"), gbc, 0, 0);
        addComponent(panel, nameField, gbc, 1, 0);
        addComponent(panel, new JLabel("Year :"), gbc, 0, 1);
        addComponent(panel, yearField, gbc, 1, 1);
        addComponent(panel, new JLabel("Poster :"), gbc, 0, 2);
        addComponent(panel, posterField, gbc, 1, 2);
        addComponent(panel, chooseFileButton, gbc, 2, 2);
        addComponent(panel, cancelButton, gbc, 0, 3);
        addComponent(panel, saveButton, gbc, 1, 3);

        return panel;
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel movieListPanel = new JPanel(new BorderLayout());
        movieListPanel.setPreferredSize(new Dimension(MOVIE_LIST_WIDTH, MOVIE_LIST_HEIGHT));
        movieListPanel.add(new JLabel("My Movies", SwingConstants.CENTER), BorderLayout.NORTH);

        movieList = new JList<>(movieListModel);
        moviePosterMap = new HashMap<>();

        controller.displayJson(movieListModel, moviePosterMap);

        JScrollPane scrollPane = new JScrollPane(movieList);
        movieListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        panel.add(movieListPanel, BorderLayout.WEST);
        panel.add(imagePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Settings Content"));
        return panel;
    }

    private JPanel createHelpPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Help Content"));
        return panel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add", addPanel);
        tabbedPane.addTab("View", viewPanel);
        tabbedPane.addTab("Settings", settingsPanel);
        tabbedPane.addTab("Help", helpPanel);
        return tabbedPane;
    }

    private void registerListeners(JButton chooseFileButton, JButton saveButton, JList<String> movieList, JFrame frame, JTextField posterField) {
        chooseFileButton.addActionListener(e -> chooseFile(frame, posterField));
        saveButton.addActionListener(e -> saveMovie(nameField, yearField , posterField));
        movieList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    displaySelectedMovie();
                }
            }
        });
    }

    private static void chooseFile(JFrame frame, JTextField posterField) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            posterField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveMovie(JTextField nameField , JTextField yearField , JTextField posterField) {
        String movieName = nameField.getText();
        String movieYear = yearField.getText();
        String filePath = posterField.getText();

        Movie movie = new Movie(movieName, movieYear, filePath);

        if (!filePath.isEmpty() && new File(filePath).exists()) {
            try {
                Image image = ImageIO.read(new File(filePath));
                imageLabel.setIcon(new ImageIcon(image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH)));
                String movieTitle = movieName + " (" + movieYear + ")";
                movieListModel.addElement(movieTitle);
                moviePosterMap.put(movieTitle, filePath);

                controller.createJsonIfNotExist(movie);
                clearFields();

            } catch (IOException ex) {
                showError("Error loading image!");
            }
        } else {
            showError("File not found!");
        }
    }

    private void displaySelectedMovie() {
        String selectedMovie = movieList.getSelectedValue();
        String posterPath = moviePosterMap.get(selectedMovie);
        if (posterPath != null && new File(posterPath).exists()) {
            try {
                Image image = ImageIO.read(new File(posterPath));
                imageLabel.setIcon(new ImageIcon(image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH)));
            } catch (IOException ex) {
                showError("Error loading image!");
            }
        }
    }

    private static void addComponent(JPanel panel, Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    private static GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private static void clearFields() {
        nameField.setText("");
        yearField.setText("");
        posterField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private static void showFrame(JFrame frame) {
        frame.pack();
        frame.setVisible(true);
    }

}
