package ph.edu.dlsu.lbycpei.pokemoncardapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ph.edu.dlsu.lbycpei.pokemoncardapp.config.AppConfig;
import ph.edu.dlsu.lbycpei.pokemoncardapp.model.Pokemon;

import java.util.Objects;

/**
 * PokemonCardView - A JavaFX component that creates a visual Pokemon trading card.
 *
 * This class generates a complete Pokemon card UI with:
 * - Pokémon number and name
 * - Pokémon image (GIF) or placeholder
 * - Type information with color coding
 * - Basic stats (weight, height, power level)
 * - Battle stats with visual progress bars (attack, defense, stamina)
 *
 * The card automatically styles itself based on the Pokemon's type and includes
 * proper error handling for missing images.
 *
 */
public class PokemonCardView {

    /**
     * The main container for the Pokemon card.
     * This VBox holds all the card components in a vertical layout.
     * Think of this as a tray where you arrange your food
     */
    private final VBox card;

    /**
     * Creates a new Pokemon card view for the specified Pokemon.
     *
     * This constructor builds the entire card layout including:
     * - Card styling based on Pokemon type
     * - All visual components (image, labels, stats)
     * - Proper spacing and alignment
     *
     * @param pokemon The Pokemon object containing all the data to display
     * @throws NullPointerException if pokemon is null
     */
    public PokemonCardView(Pokemon pokemon) {
        // Initialize the main card container
        card = new VBox(15); // 15px spacing between child elements
        card.getStyleClass().add("pokemon-card"); // CSS class for styling
        card.setAlignment(Pos.CENTER); // Center all content
        card.setPadding(new Insets(25)); // 25px padding on all sides
        card.setMaxWidth(450); // Maximum card width (May vary accdg. to resolution)
        card.setMaxHeight(680); // Maximum card height (May vary accdg. to resolution)

        // Set dynamic background color based on Pokemon type
        // This creates a rounded card with drop shadow effect
        card.setStyle("-fx-background-color: " + pokemon.getTypeBackground() +
                "; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        // Create all card components in order from top to bottom
        Label pokemonNumber = createPokemonNumber(pokemon.getInstanceId());
        ImageView pokemonImage = createPokemonImage(pokemon);
        Label nameLabel = new Label(pokemon.getName());
        nameLabel.getStyleClass().add("pokemon-name");
        StackPane typeBox = createTypeBox(pokemon);
        VBox basicInfo = createBasicInfo(pokemon);
        VBox statsBox = createStatsBox(pokemon);

        // Add all components to the card with separators for visual organization
        card.getChildren().addAll(
                pokemonNumber,
                pokemonImage,
                new Separator(), // Visual divider
                nameLabel,
                new Separator(),
                basicInfo,
                typeBox,
                new Separator(),
                statsBox,
                new Separator()
        );
    }

    /**
     * Creates the Pokemon type display box.
     *
     * This method handles both single-type and dual-type Pokemon:
     * - Single type: Shows one colored rectangle with type name
     * - Dual type: Shows two colored rectangles side by side with both type names
     *
     * The colors are automatically determined by the Pokemon's type(s).
     *
     * @param pokemon The Pokemon whose type(s) to display
     * @return A StackPane containing the type display elements
     */
    private StackPane createTypeBox(Pokemon pokemon) {
        StackPane pane = new StackPane();
        double totalWidth = 500;
        pane.setPrefWidth(totalWidth);

        String[] types = pokemon.getType().split("-");

        if (types.length == 1) {
            Label typeLabel = new Label(types[0].toUpperCase());
            typeLabel.getStyleClass().add("pokemon-type");
            typeLabel.setAlignment(Pos.CENTER);
            typeLabel.setMaxWidth(Double.MAX_VALUE);
            typeLabel.setPadding(new Insets(10, 20, 10, 20));
            typeLabel.setStyle("-fx-background-color: " + pokemon.getTypeBackground() + ";");
            pane.getChildren().add(typeLabel);

        } else if (types.length == 2) {
            Label type1 = new Label(types[0]);
            Label type2 = new Label(types[1]);

            type1.getStyleClass().add("pokemon-type");
            type2.getStyleClass().add("pokemon-type");

            type1.setAlignment(Pos.CENTER);
            type2.setAlignment(Pos.CENTER);

            type1.setPrefWidth(totalWidth / 2);
            type2.setPrefWidth(totalWidth / 2);

            type1.setPadding(new Insets(10));
            type2.setPadding(new Insets(10));

            type1.setStyle("-fx-background-color: " + pokemon.getTypeBackground() + ";");
            type2.setStyle("-fx-background-color: " + pokemon.getSecondBackground() + ";");

            HBox hbox = new HBox(type1, type2);
            hbox.setMaxWidth(totalWidth);
            hbox.setSpacing(0);
            HBox.setHgrow(type1, Priority.ALWAYS);
            HBox.setHgrow(type2, Priority.ALWAYS);

            pane.getChildren().add(hbox);
        }
        return pane;
    }

    /**
     * Creates the basic information section of the Pokemon card.
     *
     * This section displays:
     * - Pokemon weight in kilograms
     * - Pokemon height in meters
     * - Calculated power level (computed from stats)
     *
     * @param pokemon The Pokemon whose basic info to display
     * @return A VBox containing the formatted basic information labels
     */
    private VBox createBasicInfo(Pokemon pokemon) {
        VBox basicInfo = new VBox(10);
        basicInfo.setAlignment(Pos.CENTER);

        Label weightLabel = new Label("Weight: " + pokemon.getWeight());
        weightLabel.getStyleClass().add("info-label");

        Label heightLabel = new Label("Height: " + pokemon.getHeight());
        heightLabel.getStyleClass().add("info-label");

        double attack = pokemon.getAttack();
        double defense = pokemon.getDefense();
        double stamina = pokemon.getStamina();
        double power = (attack + defense + stamina) * 100;

        Label powerLabel = new Label("Power Level: " + String.format("%.1f", power));
        powerLabel.getStyleClass().add("power-label");

        basicInfo.getChildren().addAll(weightLabel, heightLabel, powerLabel);
        return basicInfo;
    }

    /**
     * Creates the Pokemon number label (e.g., "#001").
     *
     * The number is formatted with a leading zero and hash symbol.
     * This label is left-aligned within the card layout.
     *
     * @param id The Pokemon's instance ID number
     * @return A formatted Label displaying the Pokemon number
     */
    private Label createPokemonNumber(int id) {
        Label pokemonNumber = new Label(String.format("#%03d", id));
        pokemonNumber.getStyleClass().add("pokemon-number");

        // Wrap in HBox for proper alignment
        HBox container = new HBox(pokemonNumber);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);

        // Use a placeholder label as this method must return a Label
        // You can store this HBox in a field and return it via getCard if needed
        pokemonNumber.setMaxWidth(Double.MAX_VALUE);
        return pokemonNumber;
    }

    /**
     * Creates and loads the Pokemon image display.
     *
     * This method attempts to load a Pokemon GIF image from resources.
     * If the image cannot be loaded, it creates a styled placeholder instead.
     *
     * The image loading process:
     * 1. Try to load Pokemon-specific GIF from resources
     * 2. If loading fails, create a colored placeholder
     * 3. Apply drop shadow effect for visual appeal
     *
     * @param pokemon The Pokemon whose image to load
     * @return An ImageView containing either the Pokemon image or a placeholder
     */
    private ImageView createPokemonImage(Pokemon pokemon) {
        ImageView imageView = new ImageView();
        try {
            String imgPath = "/images/" + pokemon.getName().toLowerCase() + ".gif";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgPath)));

            if (image.isError()) {
                // Image failed to load, use placeholder
                imageView = createPlaceholderImage(pokemon);
            } else {
                // Image loaded successfully
                imageView.setImage(image);
                imageView.setFitWidth(180);
                imageView.setFitHeight(180);
            }
        } catch (Exception e) {
            // Exception occurred during loading, use placeholder
            imageView = createPlaceholderImage(pokemon);
        }

        // Add visual effect to the final image
        imageView.setEffect(new javafx.scene.effect.DropShadow(8, Color.BLACK));
        return imageView;
    }

    /**
     * Creates a placeholder image when the actual Pokemon image cannot be loaded.
     *
     * The placeholder is a colored circle that matches the Pokemon's type color,
     * with "No Image" text in the center. This provides a consistent visual
     * experience even when images are missing.
     *
     * @param pokemon The Pokemon for which to create a placeholder
     * @return An ImageView containing the generated placeholder image
     */
    private ImageView createPlaceholderImage(Pokemon pokemon) {
        ImageView placeholder = new ImageView();
        placeholder.setFitWidth(180);
        placeholder.setFitHeight(180);

        // Create a canvas to draw the placeholder
        Canvas canvas = new Canvas(180, 180);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw a colored circle background
        gc.setFill(Paint.valueOf(pokemon.getTypeBackground()));
        gc.fillOval(10, 10, 160, 160); // 10px margin from edges

        // Draw "No Image" text in white
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        String prompt = "No Image";
        gc.fillText(prompt, 45, 90); // Centered positioning

        // Convert the canvas drawing to an image
        javafx.scene.SnapshotParameters params = new javafx.scene.SnapshotParameters();
        params.setFill(Color.TRANSPARENT); // Transparent background
        Image placeholderImage = canvas.snapshot(params, null);

        placeholder.setImage(placeholderImage);
        return placeholder;
    }

    /**
     * Creates the statistics display section with visual progress bars.
     *
     * This section shows three key battle statistics:
     * - Attack (red bar)
     * - Defense (blue bar)
     * - Stamina (green bar)
     *
     * Each statistic is displayed with both numerical value and a colored
     * progress bar that visually represents the stat's strength.
     *
     * @param pokemon The Pokemon whose stats to display
     * @return A VBox containing all three stat bars
     */
    private VBox createStatsBox(Pokemon pokemon) {
        VBox statsBox = new VBox(12); // 12px spacing between stat bars
        statsBox.setAlignment(Pos.CENTER);

        VBox attackBox = new VBox(createStatBar("ATTACK", pokemon.getAttack(), Color.RED));
        VBox defenseBox = new VBox(createStatBar("DEFENSE", pokemon.getDefense(), Color.DODGERBLUE));
        VBox staminaBox = new VBox(createStatBar("STAMINA", pokemon.getStamina(), Color.LIMEGREEN));

        statsBox.getChildren().addAll(attackBox, defenseBox, staminaBox);
        return statsBox;
    }

    /**
     * Creates an individual statistic bar with label and visual progress indicator.
     *
     * Each stat bar consists of:
     * - A text label showing the stat name and numerical value
     * - A background bar (light gray)
     * - A colored progress bar showing the stat's relative strength
     *
     * The progress bar is proportional to the stat value, with a maximum of 100%.
     *
     * @param statName The name of the statistic (e.g., "ATTACK")
     * @param value The numerical value of the statistic (0.0 to 1.0)
     * @param color The color to use for the progress bar
     * @return A VBox containing the complete stat bar display
     */
    private VBox createStatBar(String statName, double value, Color color) {
        VBox statBox = new VBox(5);

        Label label = new Label(statName + ": " + (int) (value * 100));
        label.getStyleClass().add("stat-label");

        HBox labelContainer = new HBox(label);
        labelContainer.setAlignment(Pos.CENTER_LEFT); // Align label to left inside HBox
        labelContainer.setMaxWidth(Double.MAX_VALUE);

        StackPane barBackground = new StackPane();
        barBackground.setPrefSize(200, 18);
        barBackground.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 10;");

        Rectangle progress = new Rectangle();
        progress.setHeight(18);
        progress.setArcWidth(10);
        progress.setArcHeight(10);
        progress.setFill(color);
        progress.widthProperty().bind(barBackground.widthProperty().multiply(value));

        barBackground.getChildren().add(progress);
        barBackground.setAlignment(Pos.CENTER_LEFT);

        statBox.getChildren().addAll(labelContainer, barBackground);
        statBox.setAlignment(Pos.CENTER);

        return statBox;
    }

    /**
     * Gets the root card container.
     *
     * This method provides access to the main VBox that contains all
     * the Pokemon card components. Use this to add the card to your
     * scene graph or parent container.
     *
     * @return The VBox containing the complete Pokemon card
     */
    public VBox getCard() {
        return card;
    }
}