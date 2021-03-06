package controller.view;

import java.util.HashMap;

import controller.model.Story;
import controller.model.level;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class levelOne extends level {
	/*
	 * This class displays intro story
	 */

	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

	private int line = 0;

	private Pane appRoot = new Pane();
	private Pane gameRoot = new Pane();
	private Pane uiRoot = new Pane();
	private Pane hintRoot = new Pane();

	Stage storyStage;

	private boolean keyPressed = false;
	private boolean running = true;
	private boolean hintShown = false;

	// Initializes the gameplay area
	private void initContent() {
		// appRoot size
		appRoot.setMinSize(1080, 800);
		// Background set
		Image bg = new Image("img/home.png");
		ImageView bgView = new ImageView(bg);
		bgView.setFitWidth(1080);
		bgView.setFitHeight(800);
		// Speech box set
		Image Speech = new Image("lvlOneImg/SpeechBox.png");
		ImageView SpeechView = new ImageView(Speech);
		// Initialize text
		Text text = new Text();
		gameRoot.getChildren().add(text);

		appRoot.getChildren().addAll(bgView, SpeechView, gameRoot, uiRoot, hintRoot);

	}

	// This function displays a hint to indicate the player on how to
	// interact with the story
	private void displayHint() {

		Image hintVN = new Image("img/hintVN.png");
		ImageView hintVNView = new ImageView(hintVN);
		hintVNView.setTranslateX(600);
		hintVNView.setTranslateY(120);
		hintRoot.getChildren().add(hintVNView);

		FadeTransition fadeOut = new FadeTransition(Duration.seconds(8), hintRoot);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setOnFinished((ActionEvent event) -> {
			hintRoot.getChildren().clear();
		});
	}

	// Skip to next message on key press
	private void update() {
		if (line == Story.ScriptData.length) {
			line++;

			Button continueChoice = new Button("Continue?");
			continueChoice.setMinWidth(200);
			continueChoice.setMinHeight(50);
			continueChoice.setTranslateX(440);
			continueChoice.setTranslateY(375);

			FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), appRoot);
			fadeOut.setToValue(0.0);

			fadeOut.setOnFinished((ActionEvent event) -> {

				levelSelect select = new levelSelect();
				try {
					running = false;

					select.selectLevel(storyStage);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
			fadeOut.play();
		}

		if (!hintShown) {
			displayHint();
			hintShown = true;
		}

		if (isPressed(KeyCode.ENTER) && (keyPressed == false) && (line <= Story.ScriptData.length - 1)) {

			gameRoot.getChildren().clear();
			Text scriptWords = setText(Story.ScriptData[line]);
			Text characterWords = setCharacter(Story.ScriptData[line]);
			gameRoot.getChildren().add(scriptWords);
			gameRoot.getChildren().add(characterWords);
			line++;
			keyPressed = true;

		}
		if (isPressed(KeyCode.CONTROL) && (line <= Story.ScriptData.length - 1)) {
			gameRoot.getChildren().clear();
			Text scriptWords = setText(Story.ScriptData[line]);
			Text characterWords = setCharacter(Story.ScriptData[line]);
			gameRoot.getChildren().add(scriptWords);
			gameRoot.getChildren().add(characterWords);
			line++;
		}
		// Debounce
		if (!isPressed(KeyCode.ENTER) && (keyPressed == true)) {
			keyPressed = false;
		}

	}

	// Sets speech for character
	private Text setText(String string) {

		// Set Character dialogue
		String script = string.substring(1);
		Text text = new Text();
		text.setFill(Color.WHITE);
		text.setText(script);
		text.setFont(Font.font("Verdana", 20));
		text.setTranslateX(50);
		text.setTranslateY(575);

		return text;

	}

	// Identifies and sets texts for character
	private Text setCharacter(String string) {
		// Set Character name
		String characterName = null;
		String character = string.substring(0, 1);
		if (character.equals("0")) {
			characterName = "";
		} else if (character.equals("1")) {
			characterName = player.getName();
		} else if (character.equals("2")) {
			characterName = "???";
		}
		Text name = new Text();
		name.setFill(Color.WHITE);

		name.setText(characterName);
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		name.setTranslateX(95);
		name.setTranslateY(493);

		return name;

	}

	// Confirms key press
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	public void displayDialogue(ActionEvent event2) throws Exception {
		initContent();

		Scene scene = new Scene(appRoot);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

		storyStage = (Stage) ((Node) event2.getSource()).getScene().getWindow();
		storyStage.setScene(scene);
		storyStage.show();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (running) {
					update();
				}
			}
		};
		timer.start();
	}

	public void displayDialogueKeyPressed(KeyEvent event3) throws Exception {
		initContent();

		Scene scene = new Scene(appRoot);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

		storyStage = (Stage) ((Node) event3.getSource()).getScene().getWindow();
		storyStage.setScene(scene);
		storyStage.show();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (running) {
					update();
				}
			}
		};
		timer.start();
	}
}
