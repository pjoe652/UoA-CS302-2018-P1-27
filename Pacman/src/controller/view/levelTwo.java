package controller.view;

import java.util.HashMap;

import controller.model.Story;
import controller.model.level;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class levelTwo extends level {
	/*
	 * This class displays Level 2 Story
	 */

	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

	private int line = 0;
	private int x = 1;
	private int y = 1;

	private Pane appRoot = new Pane();
	private Pane gameRoot = new Pane();
	private Pane uiRoot = new Pane();

	Timeline timelineX = new Timeline();
	Timeline timelineY = new Timeline();

	private String section = "ScriptQuestion";

	Stage storyStage;

	private boolean keyPressed = false;
	private boolean running = true;
	private boolean choicesCreated = false;
	private boolean choiceMade = false;
	private boolean shake = false;

	// Initializes the gameplay area
	private void initContent() {
		// Background set
		Image bg = new Image("img/cave.png");
		ImageView bgView = new ImageView(bg);
		bgView.setFitWidth(1120);
		bgView.setFitHeight(840);
		// Gamebox Set
		appRoot.setPrefSize(1080, 800);
		// Speech box set
		Image Speech = new Image("lvlOneImg/SpeechBox.png");
		ImageView SpeechView = new ImageView(Speech);
		// Initialize text
		Text text = new Text();
		gameRoot.getChildren().add(text);

		appRoot.getChildren().addAll(bgView, SpeechView, gameRoot, uiRoot);

	}

	// Skip to next message on key press
	private void update() {

		// Next Level
		if ((line == maxLength("ScriptChoice1") - 1) && (choiceMade) && section.equals("ScriptChoice1")) {
			line++;
			if (player.getScore() >= 2000) {
				player.setScore(player.getScore() - 2000);
			} else {
				player.setScore(0);
			}

			if (player.getHp() >= 1) {
				player.minusHp();
			}
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

		if ((line == maxLength("ScriptChoice2") - 1) && (choiceMade) && section.equals("ScriptChoice2")) {
			line++;
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

		// Events
		if ((line == 2) && (section.equals("ScriptChoice1"))) {
			stopShake();
		}

		// Show dialogue
		if (isPressed(KeyCode.ENTER) && (keyPressed == false) && (line <= maxLength(section) - 1)) {

			setText(section);

			keyPressed = true;
		}
		if (isPressed(KeyCode.CONTROL) && (line <= maxLength(section) - 1)) {

			setText(section);

		}
		if (!isPressed(KeyCode.ENTER) && (keyPressed == true)) {
			keyPressed = false;
		}
		if (isPressed(KeyCode.ENTER) && (line == (Story.ScriptQuestion).length)) {
			if (choicesCreated == false) {
				createChoices("Open Chest", "Leave Chest");
				choicesCreated = true;
			}
		}

	}

	// Getter for max length of section
	private int maxLength(String section) {

		int maxScript = 0;

		if (section.equals("ScriptQuestion")) {
			maxScript = Story.ScriptQuestion.length;
		} else if (section.equals("ScriptChoice1")) {
			maxScript = Story.ScriptChoice1.length;
		} else if (section.equals("ScriptChoice2")) {
			maxScript = Story.ScriptChoice2.length;
		}

		return maxScript;
	}

	// Sets speech for character
	private void setText(String section) {

		Text scriptWords = new Text();
		Text characterWords = new Text();

		gameRoot.getChildren().clear();

		if (section.equals("ScriptQuestion")) {
			scriptWords = setDialogue(Story.ScriptQuestion[line]);
			characterWords = setCharacter(Story.ScriptQuestion[line]);
		} else if (section.equals("ScriptChoice1")) {
			scriptWords = setDialogue(Story.ScriptChoice1[line]);
			characterWords = setCharacter(Story.ScriptChoice1[line]);
		} else if (section.equals("ScriptChoice2")) {
			scriptWords = setDialogue(Story.ScriptChoice2[line]);
			characterWords = setCharacter(Story.ScriptChoice2[line]);
		}

		gameRoot.getChildren().add(scriptWords);
		gameRoot.getChildren().add(characterWords);
		line++;
	}

	// Sets the dialogue from script
	private Text setDialogue(String string) {

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
		name.setTranslateX(90);
		name.setTranslateY(493);

		return name;

	}

	// Creates box to pick choice
	private StackPane createChoiceBox(int x, int y, String string) {
		Image image = new Image("img/ChoiceBox.png");
		ImageView imageView = new ImageView(image);
		imageView.setX(x);
		imageView.setY(y);
		Text text = new Text();
		text.setFill(Color.WHITE);
		text.setText(string);
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		StackPane stack = new StackPane();
		stack.getChildren().addAll(imageView, text);
		stack.setLayoutX(x);
		stack.setLayoutY(y);

		return stack;
	}

	// Creates buttons to pick choice
	private void createChoices(String choice1, String choice2) {

		// Choice 1

		Button choice1Button = new Button(choice1);

		StackPane choice1Box = createChoiceBox(72, 200, choice1);

		choice1Button.setTranslateX(72);
		choice1Button.setTranslateY(200);
		// choice1Button.setPrefSize(883, 90);
		choice1Button.setMinWidth(883);
		choice1Button.setMinHeight(90);
		choice1Button.setOpacity(0);

		// Choice 2

		Button choice2Button = new Button(choice2);

		StackPane choice2Box = createChoiceBox(72, 300, choice2);

		choice2Button.setTranslateX(72);
		choice2Button.setTranslateY(300);
		choice2Button.setPrefSize(883, 90);
		choice2Button.setOpacity(0);

		uiRoot.getChildren().addAll(choice1Box, choice1Button, choice2Box, choice2Button);

		choice1Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				line = 0;
				choiceMade = true;
				section = "ScriptChoice1";
				appRoot.getChildren().remove(uiRoot);
				setText(section);
				shakeStage();

			}
		});

		choice2Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				line = 0;
				choiceMade = true;
				section = "ScriptChoice2";
				appRoot.getChildren().remove(uiRoot);
				setText(section);

			}
		});
	}

	// Confirms key press
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	// Shakes screen (OPTIONAL)
	public void shakeStage() {

		timelineX.setCycleCount(Timeline.INDEFINITE);

		KeyFrame frameX = new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (x == 0) {
					appRoot.setTranslateX(appRoot.getTranslateX() + 10);
					x = 1;
				} else {
					appRoot.setTranslateX(appRoot.getTranslateX() - 10);
					x = 0;
				}
			}
		});

		timelineX.getKeyFrames().add(frameX);
		timelineX.playFromStart();

		timelineY.setCycleCount(Timeline.INDEFINITE);

		KeyFrame frameY = new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (y == 0) {
					appRoot.setTranslateY(appRoot.getTranslateY() + 10);
					y = 1;
				} else {
					appRoot.setTranslateY(appRoot.getTranslateY() - 10);
					y = 0;
				}
			}
		});

		timelineY.getKeyFrames().add(frameY);
		timelineY.playFromStart();
	}

	private void stopShake() {

		timelineX.getKeyFrames().clear();
		timelineX.stop();
		timelineY.getKeyFrames().clear();
		timelineY.stop();
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
		if (event3.getCode() == KeyCode.ENTER) {
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
}
