import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.*;
import database.*;

public class SmartFitnessAppGUI extends JFrame {
    // =================================================================================
    // FUTURISTIC THEME: COLORS & FONTS
    // =================================================================================
    private static final Color COLOR_BACKGROUND = new Color(21, 21, 21); // Deep black
    private static final Color COLOR_PANEL = new Color(42, 42, 42); // Dark gray
    private static final Color COLOR_PRIMARY_NEON = new Color(0, 221, 255); // Neon blue
    private static final Color COLOR_SECONDARY_ACCENT = new Color(0, 150, 255); // Slightly darker blue
    private static final Color COLOR_TEXT_LIGHT = new Color(220, 220, 220); // Off-white
    private static final Color COLOR_TEXT_MEDIUM = new Color(160, 160, 160); // Silver-gray
    private static final Color COLOR_SUCCESS = new Color(20, 180, 120); // Green for success
    private static final Color COLOR_WHITE = Color.WHITE;

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 36);
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_MONOSPACE = new Font("Consolas", Font.PLAIN, 14);

    // DAO instances (unchanged)
    private UserDAO userDAO;
    private WorkoutDAO workoutDAO;
    private NutritionDAO nutritionDAO;
    private GoalDAO goalDAO;
    private ProgressTrackerDAO progressDAO;

    // Current user (unchanged)
    private User currentUser;

    // Main components (unchanged)
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTabbedPane tabbedPane;

    // Dashboard components (unchanged)
    private JLabel welcomeLabel;
    private JTextArea dashboardStats;
    private JTextArea recommendationArea;
    private JButton recommendationButton;

    // Workout components (unchanged)
    private JTextField workoutTypeField;
    private JSpinner workoutDurationSpinner;
    private JSpinner workoutSetsSpinner;
    private JSpinner workoutRepsSpinner;
    private JSpinner workoutWeightSpinner;
    private JSpinner workoutCaloriesSpinner;
    private JTextArea workoutHistory;

    // Nutrition components (unchanged)
    private JTextField foodItemField;
    private JSpinner nutritionCaloriesSpinner;
    private JSpinner nutritionProteinSpinner;
    private JSpinner nutritionCarbsSpinner;
    private JSpinner nutritionFatsSpinner;
    private JComboBox<String> mealTimeCombo;
    private JTextArea nutritionHistory;

    // Goal components (unchanged)
    private JTextField goalDescriptionField;
    private JSpinner goalTargetSpinner;
    private JSpinner goalCurrentSpinner;
    private JComboBox<String> goalStatusCombo;
    private JTextArea goalHistory;

    // Progress components (unchanged)
    private JTextArea progressHistory;
    private JSpinner weightSpinner;

    public SmartFitnessAppGUI() {
        // Initialize DAOs
        userDAO = new UserDAO();
        workoutDAO = new WorkoutDAO();
        nutritionDAO = new NutritionDAO();
        goalDAO = new GoalDAO();
        progressDAO = new ProgressTrackerDAO();

        setGlobalUIProperties();

        // Setup frame
        setTitle("Smart Fitness System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);

        // Setup main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(COLOR_BACKGROUND);

        // Add login panel
        mainPanel.add(createLoginPanel(), "LOGIN");

        // Add main application panel (will be populated after login)
        mainPanel.add(createMainApplicationPanel(), "MAIN");

        add(mainPanel);

        // Start with login screen
        cardLayout.show(mainPanel, "LOGIN");
    }

    // =================================================================================
    // UI REDESIGN: LOGIN PANEL
    // =================================================================================
    private JPanel createLoginPanel() {
        // Gradient background panel for visual appeal
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_BACKGROUND, getWidth(), getHeight(), COLOR_PANEL.darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Rounded panel for the form itself
        RoundedPanel centerPanel = new RoundedPanel(30, COLOR_PANEL);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        // Title
        JLabel titleLabel = new JLabel("SMART FITNESS SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_PRIMARY_NEON);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);

        // Separator for visual structure
        gbc.gridy = 2; // Using gridy=2 to leave some space below the title
        gbc.insets = new Insets(10, 10, 20, 10);
        centerPanel.add(new JSeparator(), gbc);

        // Username
        JLabel userLabel = new JLabel("USERNAME");
        styleFormLabel(userLabel);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.insets = new Insets(10, 10, 2, 10);
        centerPanel.add(userLabel, gbc);

        JTextField usernameField = new JTextField(20);
        styleTextField(usernameField);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(0, 10, 10, 10);
        centerPanel.add(usernameField, gbc);

        // Password
        JLabel passLabel = new JLabel("PASSWORD");
        styleFormLabel(passLabel);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.insets = new Insets(10, 10, 2, 10);
        centerPanel.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.insets = new Insets(0, 10, 20, 10);
        centerPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        RoundedButton loginButton = new RoundedButton("Login", COLOR_PRIMARY_NEON, COLOR_SECONDARY_ACCENT);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                currentUser = user;
                welcomeLabel.setText("Welcome back, " + user.getName() + "!");
                refreshAllData();
                cardLayout.show(mainPanel, "MAIN");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        RoundedButton registerButton = new RoundedButton("Register", COLOR_TEXT_MEDIUM, COLOR_TEXT_LIGHT.darker());
        registerButton.addActionListener(e -> showRegistrationDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridy = 7; gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);
        
        loginPanel.add(centerPanel);
        return loginPanel;
    }
    
    // =================================================================================
    // UI REDESIGN: MAIN APPLICATION PANEL
    // =================================================================================
    private JPanel createMainApplicationPanel() {
        JPanel mainAppPanel = new JPanel(new BorderLayout());
        mainAppPanel.setBackground(COLOR_BACKGROUND);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_PANEL);
        headerPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, COLOR_PRIMARY_NEON.darker()),
                new EmptyBorder(10, 20, 10, 20)
        ));

        welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(FONT_HEADER);
        welcomeLabel.setForeground(COLOR_TEXT_LIGHT);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        RoundedButton logoutButton = new RoundedButton("Logout", COLOR_TEXT_MEDIUM, COLOR_TEXT_LIGHT.darker());
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainAppPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(FONT_BUTTON);
        
        tabbedPane.addTab("<html><body style='padding: 10px; text-align: center;'>DASHBOARD</body></html>", createDashboardTab());
        tabbedPane.addTab("<html><body style='padding: 10px; text-align: center;'>WORKOUTS</body></html>", createWorkoutsTab());
        tabbedPane.addTab("<html><body style='padding: 10px; text-align: center;'>NUTRITION</body></html>", createNutritionTab());
        tabbedPane.addTab("<html><body style='padding: 10px; text-align: center;'>GOALS</body></html>", createGoalsTab());
        tabbedPane.addTab("<html><body style='padding: 10px; text-align: center;'>PROGRESS</body></html>", createProgressTab());

        mainAppPanel.add(tabbedPane, BorderLayout.CENTER);

        return mainAppPanel;
    }

    // =================================================================================
    // UI REDESIGN: DASHBOARD TAB
    // =================================================================================
    private JPanel createDashboardTab() {
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(COLOR_BACKGROUND);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Left Panel for Stats
        RoundedPanel statsContainerPanel = new RoundedPanel(20, COLOR_PANEL);
        statsContainerPanel.setLayout(new BorderLayout(10, 10));
        
        JPanel statsHeader = createSectionHeader("Today's Statistics", COLOR_PRIMARY_NEON);
        dashboardStats = new JTextArea(10, 40);
        styleTextArea(dashboardStats, FONT_MONOSPACE);
        
        JScrollPane scrollPane = new JScrollPane(dashboardStats);
        styleScrollPane(scrollPane);

        RoundedButton refreshButton = new RoundedButton("Refresh Dashboard", COLOR_PRIMARY_NEON, COLOR_SECONDARY_ACCENT);
        refreshButton.addActionListener(e -> refreshDashboard());
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(refreshButton);
        
        statsContainerPanel.add(statsHeader, BorderLayout.NORTH);
        statsContainerPanel.add(scrollPane, BorderLayout.CENTER);
        statsContainerPanel.add(buttonWrapper, BorderLayout.SOUTH);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 1.0;
        dashboardPanel.add(statsContainerPanel, gbc);

        // Right Panel for Recommendations
        RoundedPanel recommendationContainerPanel = new RoundedPanel(20, COLOR_PANEL);
        recommendationContainerPanel.setLayout(new BorderLayout(10, 10));

        JPanel recHeader = createSectionHeader("Personalized Recommendations", COLOR_SUCCESS);
        recommendationArea = new JTextArea(8, 40);
        styleTextArea(recommendationArea, FONT_NORMAL);
        recommendationArea.setWrapStyleWord(true);
        recommendationArea.setLineWrap(true);
        recommendationArea.setText("Click the button to get a personalized recommendation based on your primary goal.");
        
        JScrollPane recScrollPane = new JScrollPane(recommendationArea);
        styleScrollPane(recScrollPane);

        recommendationButton = new RoundedButton("Get My Recommendation", COLOR_SUCCESS, COLOR_SUCCESS.darker());
        recommendationButton.addActionListener(e -> generateRecommendation());
        JPanel recButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        recButtonWrapper.setOpaque(false);
        recButtonWrapper.add(recommendationButton);
        
        recommendationContainerPanel.add(recHeader, BorderLayout.NORTH);
        recommendationContainerPanel.add(recScrollPane, BorderLayout.CENTER);
        recommendationContainerPanel.add(recButtonWrapper, BorderLayout.SOUTH);

        gbc.gridx = 1; gbc.gridy = 0;
        dashboardPanel.add(recommendationContainerPanel, gbc);
        
        return dashboardPanel;
    }
    
    private JPanel createDataTabPanel(JPanel formPanel, JPanel historyPanel) {
        JPanel mainTabPanel = new JPanel(new GridBagLayout());
        mainTabPanel.setBackground(COLOR_BACKGROUND);
        mainTabPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 0.0;
        mainTabPanel.add(formPanel, gbc);

        gbc.gridy = 1; gbc.weighty = 1.0; gbc.insets = new Insets(0, 0, 0, 0);
        mainTabPanel.add(historyPanel, gbc);

        return mainTabPanel;
    }

    private JPanel createWorkoutsTab() {
        // Form Panel
        RoundedPanel formPanel = new RoundedPanel(20, COLOR_PANEL);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = createGbc();
        formPanel.add(createSectionHeader("Log New Workout", COLOR_SUCCESS), gbc);
        gbc.gridwidth = 1;
        gbc.gridy++; formPanel.add(createFormLabel("Type:"), gbc);
        gbc.gridy++; workoutTypeField = new JTextField(15); styleTextField(workoutTypeField); formPanel.add(workoutTypeField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(createFormLabel("Duration (min):"), gbc);
        gbc.gridy++; workoutDurationSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 300, 5)); styleSpinner(workoutDurationSpinner); formPanel.add(workoutDurationSpinner, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(createFormLabel("Sets:"), gbc);
        gbc.gridy++; workoutSetsSpinner = new JSpinner(new SpinnerNumberModel(3, 0, 50, 1)); styleSpinner(workoutSetsSpinner); formPanel.add(workoutSetsSpinner, gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(createFormLabel("Reps:"), gbc);
        gbc.gridy++; workoutRepsSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1)); styleSpinner(workoutRepsSpinner); formPanel.add(workoutRepsSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createFormLabel("Calories Burned:"), gbc);
        gbc.gridy++; workoutCaloriesSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 2000.0, 10.0)); styleSpinner(workoutCaloriesSpinner); formPanel.add(workoutCaloriesSpinner, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(createFormLabel("Weight (kg):"), gbc);
        gbc.gridy++; workoutWeightSpinner = new JSpinner(new SpinnerNumberModel(50.0, 0.0, 500.0, 1.0)); styleSpinner(workoutWeightSpinner); formPanel.add(workoutWeightSpinner, gbc);
        gbc.gridx = 2; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        RoundedButton addButton = new RoundedButton("Add Workout", COLOR_SUCCESS, COLOR_SUCCESS.darker());
        addButton.addActionListener(e -> addWorkout());
        formPanel.add(addButton, gbc);
        
        // History Panel
        RoundedPanel historyPanel = new RoundedPanel(20, COLOR_PANEL);
        historyPanel.setLayout(new BorderLayout());
        historyPanel.add(createSectionHeader("Workout History", COLOR_PRIMARY_NEON), BorderLayout.NORTH);
        workoutHistory = new JTextArea(20, 50);
        styleTextArea(workoutHistory, FONT_MONOSPACE);
        JScrollPane scrollPane = new JScrollPane(workoutHistory);
        styleScrollPane(scrollPane);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        return createDataTabPanel(formPanel, historyPanel);
    }
    
    private JPanel createNutritionTab() {
        // Form Panel
        RoundedPanel formPanel = new RoundedPanel(20, COLOR_PANEL);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = createGbc();
        formPanel.add(createSectionHeader("Log New Meal", COLOR_SUCCESS), gbc);
        gbc.gridwidth = 1;
        gbc.gridy++; formPanel.add(createFormLabel("Food Item:"), gbc);
        gbc.gridy++; foodItemField = new JTextField(15); styleTextField(foodItemField); formPanel.add(foodItemField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(createFormLabel("Meal Time:"), gbc);
        gbc.gridy++; mealTimeCombo = new JComboBox<>(new String[]{"Breakfast", "Lunch", "Dinner", "Snack"}); styleComboBox(mealTimeCombo); formPanel.add(mealTimeCombo, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(createFormLabel("Calories:"), gbc);
        gbc.gridy++; nutritionCaloriesSpinner = new JSpinner(new SpinnerNumberModel(200.0, 0.0, 2000.0, 10.0)); styleSpinner(nutritionCaloriesSpinner); formPanel.add(nutritionCaloriesSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createFormLabel("Protein (g):"), gbc);
        gbc.gridy++; nutritionProteinSpinner = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 200.0, 0.5)); styleSpinner(nutritionProteinSpinner); formPanel.add(nutritionProteinSpinner, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(createFormLabel("Carbs (g):"), gbc);
        gbc.gridy++; nutritionCarbsSpinner = new JSpinner(new SpinnerNumberModel(30.0, 0.0, 500.0, 0.5)); styleSpinner(nutritionCarbsSpinner); formPanel.add(nutritionCarbsSpinner, gbc);
        gbc.gridx = 2; gbc.gridy = 3; formPanel.add(createFormLabel("Fats (g):"), gbc);
        gbc.gridy++; nutritionFatsSpinner = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 200.0, 0.5)); styleSpinner(nutritionFatsSpinner); formPanel.add(nutritionFatsSpinner, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        RoundedButton addButton = new RoundedButton("Add Meal", COLOR_SUCCESS, COLOR_SUCCESS.darker());
        addButton.addActionListener(e -> addMeal());
        formPanel.add(addButton, gbc);
        
        // History Panel
        RoundedPanel historyPanel = new RoundedPanel(20, COLOR_PANEL);
        historyPanel.setLayout(new BorderLayout());
        historyPanel.add(createSectionHeader("Nutrition History", COLOR_PRIMARY_NEON), BorderLayout.NORTH);
        nutritionHistory = new JTextArea(20, 50);
        styleTextArea(nutritionHistory, FONT_MONOSPACE);
        JScrollPane scrollPane = new JScrollPane(nutritionHistory);
        styleScrollPane(scrollPane);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        return createDataTabPanel(formPanel, historyPanel);
    }
    
    private JPanel createGoalsTab() {
        // Form Panel
        RoundedPanel formPanel = new RoundedPanel(20, COLOR_PANEL);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = createGbc();
        formPanel.add(createSectionHeader("Set New Goal", COLOR_SECONDARY_ACCENT), gbc);
        gbc.gridy++; gbc.gridwidth = 4; formPanel.add(createFormLabel("Description:"), gbc);
        gbc.gridy++; goalDescriptionField = new JTextField(30); styleTextField(goalDescriptionField); formPanel.add(goalDescriptionField, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createFormLabel("Target Value:"), gbc);
        gbc.gridy++; goalTargetSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 1.0)); styleSpinner(goalTargetSpinner); formPanel.add(goalTargetSpinner, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(createFormLabel("Current Value:"), gbc);
        gbc.gridy++; goalCurrentSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 1.0)); styleSpinner(goalCurrentSpinner); formPanel.add(goalCurrentSpinner, gbc);
        gbc.gridx = 2; gbc.gridy = 3; formPanel.add(createFormLabel("Status:"), gbc);
        gbc.gridy++; goalStatusCombo = new JComboBox<>(new String[]{"In Progress", "Completed", "Not Started"}); styleComboBox(goalStatusCombo); formPanel.add(goalStatusCombo, gbc);
        gbc.gridx = 3; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        RoundedButton addButton = new RoundedButton("Add Goal", COLOR_SECONDARY_ACCENT, COLOR_SECONDARY_ACCENT.darker());
        addButton.addActionListener(e -> addGoal());
        formPanel.add(addButton, gbc);

        // History Panel
        RoundedPanel historyPanel = new RoundedPanel(20, COLOR_PANEL);
        historyPanel.setLayout(new BorderLayout());
        historyPanel.add(createSectionHeader("Active Goals", COLOR_PRIMARY_NEON), BorderLayout.NORTH);
        goalHistory = new JTextArea(20, 50);
        styleTextArea(goalHistory, FONT_MONOSPACE);
        JScrollPane scrollPane = new JScrollPane(goalHistory);
        styleScrollPane(scrollPane);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        return createDataTabPanel(formPanel, historyPanel);
    }
    
    private JPanel createProgressTab() {
        JPanel progressPanel = new JPanel(new GridBagLayout());
        progressPanel.setBackground(COLOR_BACKGROUND);
        progressPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // History Panel
        RoundedPanel historyPanel = new RoundedPanel(20, COLOR_PANEL);
        historyPanel.setLayout(new BorderLayout());
        historyPanel.add(createSectionHeader("Progress History", COLOR_PRIMARY_NEON), BorderLayout.NORTH);
        progressHistory = new JTextArea(20, 50);
        styleTextArea(progressHistory, FONT_MONOSPACE);
        JScrollPane scrollPane = new JScrollPane(progressHistory);
        styleScrollPane(scrollPane);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 1.0;
        progressPanel.add(historyPanel, gbc);

        // Input Panel
        RoundedPanel inputPanel = new RoundedPanel(20, COLOR_PANEL);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        inputPanel.add(createSectionHeader("Log Today's Progress", COLOR_SUCCESS), gbc);
        JLabel weightLabel = createFormLabel("Today's Weight (kg):");
        inputPanel.add(weightLabel);
        weightSpinner = new JSpinner(new SpinnerNumberModel(70.0, 30.0, 300.0, 0.1));
        styleSpinner(weightSpinner);
        inputPanel.add(weightSpinner);
        RoundedButton saveButton = new RoundedButton("Save Progress", COLOR_SUCCESS, COLOR_SUCCESS.darker());
        saveButton.addActionListener(e -> saveProgress());
        inputPanel.add(saveButton);
        gbc.gridy = 1; gbc.weighty = 0; gbc.insets = new Insets(20, 0, 0, 0);
        progressPanel.add(inputPanel, gbc);

        return progressPanel;
    }
    
    // =================================================================================
    // CUSTOM UI HELPER CLASSES
    // =================================================================================
    
    /** A custom JPanel that paints with rounded corners. */
    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;
        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        }
    }

    /** A custom JButton that is rounded and has hover effects. */
    private static class RoundedButton extends JButton {
        private Color backgroundColor;
        private final Color hoverColor;
        private boolean hovered = false;

        public RoundedButton(String text, Color bg, Color hover) {
            super(text);
            this.backgroundColor = bg;
            this.hoverColor = hover;
            
            setFont(FONT_BUTTON);
            setForeground(COLOR_WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (hovered) {
                g2.setColor(hoverColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            } else {
                g2.setColor(backgroundColor);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
            }
            
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // =================================================================================
    // STYLING HELPERS AND UNCHANGED LOGIC
    // =================================================================================

    private JPanel createSectionHeader(String title, Color accentColor) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_TEXT_LIGHT);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JSeparator separator = new JSeparator();
        separator.setForeground(accentColor);
        separator.setBackground(accentColor.darker());
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private void styleFormLabel(JLabel label) {
        label.setFont(FONT_NORMAL);
        label.setForeground(COLOR_TEXT_MEDIUM);
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        styleFormLabel(label);
        return label;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(FONT_NORMAL);
        field.setBackground(COLOR_BACKGROUND);
        field.setForeground(COLOR_TEXT_LIGHT);
        field.setCaretColor(COLOR_PRIMARY_NEON);
        field.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, COLOR_TEXT_MEDIUM),
                new EmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_NORMAL);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setBackground(COLOR_PANEL);
            textField.setForeground(COLOR_TEXT_LIGHT);
            textField.setCaretColor(COLOR_PRIMARY_NEON);
            textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        spinner.setBorder(new LineBorder(COLOR_TEXT_MEDIUM));
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_NORMAL);
        combo.setBackground(COLOR_PANEL);
        combo.setForeground(COLOR_TEXT_LIGHT);
        combo.setBorder(new LineBorder(COLOR_TEXT_MEDIUM));
    }

    private void styleTextArea(JTextArea area, Font font) {
        area.setFont(font);
        area.setBackground(COLOR_BACKGROUND);
        area.setForeground(COLOR_TEXT_LIGHT);
        area.setEditable(false);
        area.setCaretColor(COLOR_PRIMARY_NEON);
        area.setMargin(new Insets(10, 10, 10, 10));
        area.setBorder(null);
    }
    
    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.weightx = 1.0;
        return gbc;
    }
    
    private void setGlobalUIProperties() {
        UIManager.put("Panel.background", COLOR_BACKGROUND);
        UIManager.put("TabbedPane.contentAreaColor", COLOR_PANEL);
        UIManager.put("TabbedPane.selected", COLOR_PRIMARY_NEON);
        UIManager.put("TabbedPane.foreground", COLOR_TEXT_MEDIUM);
        UIManager.put("TabbedPane.darkShadow", COLOR_PANEL);
        UIManager.put("TabbedPane.light", COLOR_PANEL);
        UIManager.put("TabbedPane.focus", COLOR_PRIMARY_NEON);
        UIManager.put("TabbedPane.unselectedTabForeground", COLOR_TEXT_MEDIUM);
        UIManager.put("TabbedPane.selectedTabForeground", COLOR_WHITE);
        
        UIManager.put("OptionPane.background", COLOR_PANEL);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXT_LIGHT);
        UIManager.put("OptionPane.messageFont", FONT_NORMAL);
        UIManager.put("Button.background", COLOR_PRIMARY_NEON);
        UIManager.put("Button.foreground", COLOR_WHITE);
        UIManager.put("Button.font", FONT_BUTTON);
    }

    private static class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = COLOR_PRIMARY_NEON;
            this.trackColor = COLOR_PANEL;
        }
        @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }

    // --- UNCHANGED LOGIC AND ACTION METHODS ---

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register New User", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_PANEL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(8, 15, 8, 15); gbc.weightx = 1.0;

        JTextField nameField = new JTextField(20); styleTextField(nameField);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(25, 10, 100, 1)); styleSpinner(ageSpinner);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"}); styleComboBox(genderCombo);
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(170.0, 100.0, 250.0, 0.1)); styleSpinner(heightSpinner);
        JSpinner weightSpinnerReg = new JSpinner(new SpinnerNumberModel(70.0, 30.0, 300.0, 0.1)); styleSpinner(weightSpinnerReg);
        JTextField usernameField = new JTextField(20); styleTextField(usernameField);
        JPasswordField passwordField = new JPasswordField(20); styleTextField(passwordField);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++; dialog.add(createFormLabel("Name:"), gbc);
        gbc.gridy = row++; dialog.add(nameField, gbc);
        gbc.gridy = row++; dialog.add(createFormLabel("Age:"), gbc);
        gbc.gridy = row++; dialog.add(ageSpinner, gbc);
        gbc.gridy = row++; dialog.add(createFormLabel("Gender:"), gbc);
        gbc.gridy = row++; dialog.add(genderCombo, gbc);
        gbc.gridy = row++; dialog.add(createFormLabel("Height (cm):"), gbc);
        gbc.gridy = row++; dialog.add(heightSpinner, gbc);
        gbc.gridy = row++; dialog.add(createFormLabel("Weight (kg):"), gbc);
        gbc.gridy = row++; dialog.add(weightSpinnerReg, gbc);
        gbc.gridy = row++; dialog.add(createFormLabel("Username:"), gbc);
        gbc.gridy = row++; dialog.add(usernameField, gbc);
        gbc.gridy = row++; dialog.add(createFormLabel("Password:"), gbc);
        gbc.gridy = row++; dialog.add(passwordField, gbc);

        gbc.insets = new Insets(20, 15, 8, 15); gbc.gridy = row;
        RoundedButton registerButton = new RoundedButton("Complete Registration", COLOR_SUCCESS, COLOR_SUCCESS.darker());
        registerButton.addActionListener(e -> {
            User newUser = new User(0, nameField.getText(), (int) ageSpinner.getValue(), (String) genderCombo.getSelectedItem(), (double) heightSpinner.getValue(), (double) weightSpinnerReg.getValue(), usernameField.getText(), new String(passwordField.getPassword()));
            if (userDAO.createUser(newUser)) {
                JOptionPane.showMessageDialog(dialog, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Registration failed. Username may already be taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(registerButton, gbc);
        dialog.setVisible(true);
    }
    
    private void addWorkout() {
        String type = workoutTypeField.getText();
        if (type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Workout type cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Workout workout = new Workout(0, type, (int) workoutDurationSpinner.getValue(), (int) workoutSetsSpinner.getValue(), (int) workoutRepsSpinner.getValue(), (double) workoutWeightSpinner.getValue(), (double) workoutCaloriesSpinner.getValue());
        if (workoutDAO.addWorkout(workout, currentUser.getUserID())) {
            JOptionPane.showMessageDialog(this, "Workout added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            workoutTypeField.setText("");
            refreshWorkouts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add workout.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addMeal() {
        String food = foodItemField.getText();
        if (food.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Food item cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Nutrition meal = new Nutrition(0, food, (double) nutritionCaloriesSpinner.getValue(), (double) nutritionProteinSpinner.getValue(), (double) nutritionCarbsSpinner.getValue(), (double) nutritionFatsSpinner.getValue());
        String mealTime = (String) mealTimeCombo.getSelectedItem();
        if (nutritionDAO.addMeal(meal, currentUser.getUserID(), mealTime)) {
            JOptionPane.showMessageDialog(this, "Meal added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            foodItemField.setText("");
            refreshNutrition();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add meal.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addGoal() {
        String description = goalDescriptionField.getText();
        if (description.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Goal description cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Goal goal = new Goal(0, description, (double) goalTargetSpinner.getValue(), (double) goalCurrentSpinner.getValue(), (String) goalStatusCombo.getSelectedItem());
        if (goalDAO.addGoal(goal, currentUser.getUserID())) {
            JOptionPane.showMessageDialog(this, "Goal added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            goalDescriptionField.setText("");
            refreshGoals();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add goal.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveProgress() {
        double weight = (double) weightSpinner.getValue();
        double heightInMeters = currentUser.getHeight() / 100.0;
        double bmi = (heightInMeters > 0) ? weight / (heightInMeters * heightInMeters) : 0;
        ProgressTracker progress = new ProgressTracker(0, LocalDate.now().toString(), weight, bmi);
        if (progressDAO.addProgressEntry(progress, currentUser.getUserID())) {
            JOptionPane.showMessageDialog(this, "Progress saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshProgress();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save progress.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateRecommendation() {
        if (currentUser == null) return;
        List<Goal> goals = goalDAO.getGoalsByUser(currentUser.getUserID());
        if (goals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please set at least one goal in the 'Goals' tab first.", "No Goal Set", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String goalDescription = goals.get(0).getDescription();
        RecommendationEngine engine = new RecommendationEngine(currentUser, goalDescription);
        String workoutPlan = engine.suggestWorkoutPlan();
        String dietPlan = engine.suggestDietPlan();
        String progressAnalysis = engine.analyzeProgress();
        StringBuilder sb = new StringBuilder();
        sb.append("Based on your goal: '").append(goalDescription).append("'\n\n");
        sb.append("--- WORKOUT PLAN ---\n").append(workoutPlan).append("\n\n");
        sb.append("--- DIET PLAN ---\n").append(dietPlan).append("\n\n");
        sb.append("--- PROGRESS ANALYSIS ---\n").append(progressAnalysis);
        recommendationArea.setText(sb.toString());
    }
    
    private void refreshAllData() {
        refreshDashboard();
        refreshWorkouts();
        refreshNutrition();
        refreshGoals();
        refreshProgress();
    }
    
    private void refreshDashboard() {
        if (currentUser == null) return;
        List<Workout> todaysWorkouts = workoutDAO.getTodaysWorkouts(currentUser.getUserID());
        double[] nutritionTotals = nutritionDAO.getDailyNutritionTotals(currentUser.getUserID());
        double caloriesBurned = todaysWorkouts.stream().mapToDouble(Workout::getCaloriesBurned).sum();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DATE: %s\n", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")).toUpperCase()));
        sb.append("=========================================\n");
        sb.append(String.format("Calories Consumed : %.0f kcal\n", nutritionTotals[0]));
        sb.append(String.format("Calories Burned   : %.0f kcal\n", caloriesBurned));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Protein           : %.1f g\n", nutritionTotals[1]));
        sb.append(String.format("Carbohydrates     : %.1f g\n", nutritionTotals[2]));
        sb.append(String.format("Fats              : %.1f g\n", nutritionTotals[3]));
        dashboardStats.setText(sb.toString());
    }

    private void refreshWorkouts() {
        if (currentUser == null) return;
        List<Workout> workouts = workoutDAO.getWorkoutsByUser(currentUser.getUserID());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-25s | %-10s | %-5s | %-5s | %-10s | %-10s\n", "Type", "Duration", "Sets", "Reps", "Weight", "Calories"));
        sb.append("----------------------------------------------------------------------------------\n");
        for (Workout w : workouts) {
            sb.append(String.format("%-25s | %-10d | %-5d | %-5d | %-10.1f | %-10.1f\n", w.getType(), w.getDuration(), w.getSets(), w.getReps(), w.getWeight(), w.getCaloriesBurned()));
        }
        workoutHistory.setText(sb.toString());
    }

    private void refreshNutrition() {
        if (currentUser == null) return;
        List<Nutrition> meals = nutritionDAO.getMealsByUser(currentUser.getUserID());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-25s | %-10s | %-10s | %-10s | %-10s\n", "Food Item", "Calories", "Protein", "Carbs", "Fats"));
        sb.append("-------------------------------------------------------------------------------\n");
        for (Nutrition m : meals) {
            sb.append(String.format("%-25s | %-10.1f | %-10.1f | %-10.1f | %-10.1f\n", m.getFoodItem(), m.getCalorieIntake(), m.getProtein(), m.getCarbs(), m.getFats()));
        }
        nutritionHistory.setText(sb.toString());
    }

    private void refreshGoals() {
        if (currentUser == null) return;
        List<Goal> goals = goalDAO.getGoalsByUser(currentUser.getUserID());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-35s | %-15s | %-15s | %-15s\n", "Description", "Current Value", "Target Value", "Status"));
        sb.append("-------------------------------------------------------------------------------------------\n");
        for (Goal g : goals) {
            sb.append(String.format("%-35s | %-15.1f | %-15.1f | %-15s\n", g.getDescription(), g.getCurrentValue(), g.getTargetValue(), g.getStatus()));
        }
        goalHistory.setText(sb.toString());
    }

    private void refreshProgress() {
        if (currentUser == null) return;
        List<ProgressTracker> progressList = progressDAO.getProgressHistory(currentUser.getUserID());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s | %-15s | %-15s\n", "Date", "Weight (kg)", "BMI"));
        sb.append("--------------------------------------------------\n");
        for (ProgressTracker p : progressList) {
            sb.append(String.format("%-15s | %-15.1f | %-15.2f\n", p.getDate(), p.getWeight(), p.getBmi()));
        }
        progressHistory.setText(sb.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SmartFitnessAppGUI().setVisible(true);
        });
    }
}