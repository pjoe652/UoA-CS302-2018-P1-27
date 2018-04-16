package controller.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import controller.model.LevelData;
import controller.model.enemies;
import controller.model.level;
import controller.view.levelSelect;
import controller.view.upgradeControl;
import controller.view.MultiPlayerStage.Point;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LevelTwoStage extends level {

	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private ArrayList<Node> coins = new ArrayList<Node>();
	private ArrayList<Node> platforms = new ArrayList<Node>();
	private ArrayList<Node> hearts = new ArrayList<Node>();

	private Pane appRoot = new Pane();
	private Pane gameRoot = new Pane();
	private Pane uiRoot = new Pane();
	private Pane userRoot = new Pane();

	private double userX;
	private double userY;

	private int lives = player.getHp();
	private boolean createHearts = true;

	private static int Score = 0;
	private int CoinsCollected = 0;

	private int levelWidth;

	Node entityCreated;

	private String direction = "NONE";
	private String prevDirection = "Stationary";
	private int directionSet = 0;

	private boolean running = true;
	private boolean playerDead = false;
	private boolean playerHasDied = false;

	public Stage stage;

	// Count Down From 3 to 0
	private final Integer startTime = 3;
	private Integer currentTime = startTime;
	private StackPane display = new StackPane();
	Timeline timer = new Timeline();
	private StackPane displayTime = new StackPane();

	// Timer count from 120 to 0
	private final Integer playTime = 120;
	private Integer gameTime = playTime;
	private static int TIME = 0;
	private int count = 0;
	Timeline time = new Timeline();

	// Get the maze details from LevelData
	LevelData map = new LevelData();
	private int maze[][] = map.getLevel2();

	// Characters of the game
	private Node user;
	enemies red = new enemies();
	Node redEnemy = red.redEnemy();
	enemies blue = new enemies();
	Node blueEnemy = blue.blueEnemy();
	enemies purple = new enemies();
	Node purpleEnemy = purple.purpleEnemy();
	enemies green = new enemies();
	Node greenEnemy = green.greenEnemy();

	// AI movement
	private Point p; // RED
	private ArrayList<Point> store = new ArrayList<Point>(); // RED
	private int moveUp = 0; // RED
	private int moveRight = 0; // RED
	private Point p2; // PURPLE
	private ArrayList<Point> store2 = new ArrayList<Point>(); // PURPLE
	private int pmoveUp = 0; // PURPLE
	private int pmoveRight = 0; // PURPLE
	private int pcheck = 0; // PURPLE
	private Point p3; // BLUE
	private ArrayList<Point> store3 = new ArrayList<Point>(); // BLUE
	private int bmoveUp = 0; // PURPLE
	private int bmoveRight = 0; // PURPLE
	private int bcheck = 0; // PURPLE
	private Point p4;
	private ArrayList<Point> store4 = new ArrayList<Point>();
	private int gmoveUp = 0;
	private int gmoveRight = 0;

	private int checkMaze[][] = maze;
	private int redx, redy, purplex, purpley, bluex, bluey, greenx, greeny, playerx, playery;
	private int vertical, horizontal;

	// Time pauses upon user's input
	private boolean timePaused = false;

	// select next level depends on the result of the game
	levelSelect select = new levelSelect();

	// Power Pellets
	private boolean swordHeld = false;
	private int swordGet = 0;
	Timeline swordTime = new Timeline();
	private final Integer swordStart = 60;
	private Integer swordCount = swordStart;
	private ArrayList<Node> swords = new ArrayList<Node>();

	private int coinCount = coins.size();

	private Stage menuStage = new Stage();

	// Initializes the gameplay area
	private void initContent() {
		// Rectangle bg = new Rectangle(1020, 768);
		Image bg = new Image("img/floor.png");

		ImageView bgView = new ImageView(bg);
		bgView.setFitWidth(1080);
		bgView.setFitHeight(800);

		String scoreString = Integer.toString(getScore());

		StackPane scoreValue = createMenu(100, 30, 850, 15, scoreString, Color.WHITE);

		StackPane menuScore = createMenuRectangle(250, 40, 760, 10);

		StackPane menuScoreLabel = createMenuText(770, 17, "Score: ", Color.WHITE);

		StackPane Lives = createMenuRectangle(300, 40, 60, 10);

		StackPane LivesLabel = createMenuText(70, 17, "Lives: ", Color.WHITE);

		if (createHearts) {
			for (int i = 0; i < lives; i++) {
				Node heart = createImage(40, 40, 160 + i * 50, 12, "img/heart.png");
				hearts.add(heart);
			}
		}

		// coinSFX();

		playerDead = false;

		// Transfer matrices into 40*40 blocks
		levelWidth = maze[0].length * 40;
		if (coins.isEmpty()) {
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {
					switch (maze[i][j]) {
					case 0:
						break;
					case 1:
						Node Walls = createCharacter(j * 40, i * 40, 40, 40, Color.BLACK);
						Node Tile = createImage(40, 40, j * 40, i * 40, "img/tile2.png");
						platforms.add(Walls);
						break;
					case 2:
						Node coin = createImage(40, 40, j * 40, i * 40, "img/Coin.png");
						coins.add(coin);
						break;
					case 3:
						Node sword = createImage(40, 40, j * 40, i * 40, "img/sword.png");
						swords.add(sword);
					}
				}
			}
		}

		// Create characters in the game
		user = createKnight(522, 722, player.getModelDirection(direction));
		redEnemy.setTranslateX(520);
		redEnemy.setTranslateY(360);
		redEnemy.getProperties().put("alive", true);
		blueEnemy.setTranslateX(680);
		blueEnemy.setTranslateY(360);
		blueEnemy.getProperties().put("alive", true);
		purpleEnemy.setTranslateX(600);
		purpleEnemy.setTranslateY(360);
		purpleEnemy.getProperties().put("alive", true);
		greenEnemy.setTranslateX(400);
		greenEnemy.setTranslateY(360);
		greenEnemy.getProperties().put("alive", true);
		gameRoot.getChildren().addAll(redEnemy, blueEnemy, purpleEnemy, greenEnemy);

		user.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();

			if (offset > 640 && offset < levelWidth - 640) {
				gameRoot.setLayoutX(-(offset - 640));
			}
		});

		if (!playerHasDied) {
			setTime();
		}

		appRoot.getChildren().addAll(bgView, Lives, LivesLabel, gameRoot, userRoot, uiRoot, menuScore, menuScoreLabel,
				scoreValue);

	}

	// Reset game when user dies
	private void clearGame() {

		if (lives == 0) {
			levelSelect select = new levelSelect();
			Score = 0;
			select.levelFailed(stage);
		}

		userRoot.getChildren().clear();
		uiRoot.getChildren().clear();
		appRoot.getChildren().clear();
		gameRoot.getChildren().removeAll(user, redEnemy, blueEnemy, purpleEnemy, greenEnemy);
		gameRoot.getChildren().removeAll(hearts);

		currentTime = 3;
		count = 0;
		resetTime();
		resetGameTime();
	}

	// Continuously updates character interactions
	private void update() {
		if (currentTime < -1 && timePaused == false) {

			if (count == 0) {
				setGameTime();
				count++;
			}

			// Capture the position of the player
			// Update to checkMaze for BFS to find the player
			double x = user.getTranslateX();
			double y = user.getTranslateY();
			int newx = (int) (x / 40);
			int newy = (int) (y / 40);
			playerx = newx;
			playery = newy;

			redx = (int) (redEnemy.getTranslateX() / 40);
			redy = (int) (redEnemy.getTranslateY() / 40);

			// System.out.println();
			checkMaze[newy][newx] = 9;

			// Move red enemy
			redMovement();

			checkMaze[newy][newx] = 9;
			// Move purple enemy
			purpleMovement();

			//checkMaze[newy][newx] = 9;
			//blueMovement();

			checkMaze[newy][newx] = 9;
			greenMovement();
			// coinCount = coins.size();

			directionSelect();

			// Removes Coins when collected
			for (Node coin : coins) {
				if (user.getBoundsInParent().intersects(coin.getBoundsInParent())) {
					coin.getProperties().put("alive", false);

				}
			}

			// Removes swords when collected
			for (Node sword : swords) {
				if (user.getBoundsInParent().intersects(sword.getBoundsInParent())) {
					sword.getProperties().put("alive", false);
					swordGet = 1;
					stopSwordTime();

				}
			}
			for (Iterator<Node> it = swords.iterator(); it.hasNext();) {
				Node sword = it.next();
				if (!(Boolean) sword.getProperties().get("alive")) {
					it.remove();
					gameRoot.getChildren().remove(sword);
				}
			}

			// Start sword use time
			if (swordGet == 1) {
				swordGet = 0;
				swordHeld = true;
				setSwordTime();
			}

			// Sword usage
			if (swordHeld == true) {
				swordUse();
			}

			// Checks if player is killed
			if (!playerDead && (!swordHeld)) {
				if (!playerDead) {
					if (user.getBoundsInParent().intersects(redEnemy.getBoundsInParent())
							|| user.getBoundsInParent().intersects(blueEnemy.getBoundsInParent())
							|| user.getBoundsInParent().intersects(purpleEnemy.getBoundsInParent())
							|| user.getBoundsInParent().intersects(greenEnemy.getBoundsInParent())) {
						playerDead = true;
						clearGame();
						lives--;
						initContent();
					}
				}

				// Removes coin if collected
			}
			for (Iterator<Node> it = coins.iterator(); it.hasNext();) {
				Node coin = it.next();

				if (!(Boolean) coin.getProperties().get("alive")) {
					it.remove();
					CoinsCollected += 1;
					Score += 50 * player.getScoreMultiplier();
					String ScoreString = Integer.toString(Score);
					StackPane scoreValue = createMenu(100, 30, 850, 15, ScoreString, Color.WHITE);
					appRoot.getChildren().add(scoreValue);
					gameRoot.getChildren().remove(coin);

				}

			}

			// if (coinCount != coins.size()) {
			// coinSFX();
			// }

			// Fades away when all coins collected
			if (CoinsCollected == 150) {
				coinBagSFX();
				stopGameTime();
				levelSelect select = new levelSelect();
				try {
					select.levelClear(stage);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

	private void changeDirection(String direction) {
		userX = user.getTranslateX();
		userY = user.getTranslateY();
		userRoot.getChildren().clear();
		user = createKnight(userX, userY, player.getModelDirection(direction));

	}

	// Creates walls
	private Node createImage(int h, int w, int x, int y, String link) {

		Image img = new Image(link);
		ImageView imageView = new ImageView(img);
		imageView.setFitHeight(h);
		imageView.setFitWidth(w);
		imageView.setX(x);
		imageView.setY(y);
		imageView.getProperties().put("alive", true);

		gameRoot.getChildren().add(imageView);
		return imageView;

	}

	// Creates user
	private Node createCharacter(int x, int y, int w, int h, Color color) {

		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		entity.getProperties().put("alive", true);

		gameRoot.getChildren().add(entity);

		return entity;

	}

	// Create knight user
	private Node createKnight(double x, double y, String direction) {

		Image knight = new Image(direction);
		ImageView knightView = new ImageView(knight);
		knightView.setTranslateX(x);
		knightView.setTranslateY(y);
		knightView.setFitHeight(36);
		knightView.setFitWidth(36);

		knightView.getProperties().put("alive", true);

		userRoot.getChildren().add(knightView);

		return knightView;
	}

	// Creates menu label
	private StackPane createMenu(int w, int h, int x, int y, String string, Color color) {
		Rectangle rectangle = new Rectangle(w, h);
		rectangle.setFill(Color.BLACK);
		rectangle.setStroke(Color.WHITE);
		rectangle.setArcWidth(10.0);
		rectangle.setArcHeight(10.0);
		Text text = new Text();
		text.setFill(color);
		text.setText(string);
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		StackPane stack = new StackPane();
		stack.getChildren().addAll(rectangle, text);
		stack.setLayoutX(x);
		stack.setLayoutY(y);

		return stack;
	}

	// Creates menu box
	private StackPane createMenuRectangle(int w, int h, int x, int y) {

		Rectangle rectangle = new Rectangle(w, h);
		rectangle.setFill(Color.BLACK);
		rectangle.setStroke(Color.WHITE);
		rectangle.setArcWidth(10.0);
		rectangle.setArcHeight(10.0);
		StackPane stack = new StackPane();
		stack.getChildren().add(rectangle);
		stack.setLayoutX(x);
		stack.setLayoutY(y);

		return stack;
	}

	// Creates menu text
	private StackPane createMenuText(int x, int y, String string, Color color) {

		Text text = new Text();
		text.setFill(color);
		text.setText(string);
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		StackPane stack = new StackPane();
		stack.getChildren().add(text);
		stack.setLayoutX(x);
		stack.setLayoutY(y);

		return stack;
	}

	// Confirms key press
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	private void coinBagSFX() {
		URL path;
		AudioClip coinSFX;

		path = getClass().getResource("/sfx/coinBag.wav");
		coinSFX = new AudioClip(path.toString());
		coinSFX.setVolume(0.2);
		coinSFX.play();

	}

	public void mapGeneration(Stage map) throws Exception {

		menuStage.setTitle("PAUSE");
		menuStage.initStyle(StageStyle.TRANSPARENT);

		Scene menuScene = new Scene(menuSetUp());
		menuScene.getStylesheets().add("/controller/model/menu.css");

		menuStage.setOpacity(0.7);
		menuStage.initModality(Modality.APPLICATION_MODAL);
		menuStage.initOwner(stage);

		menuScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.P) {
				time.play();
				timePaused = false;
				if (currentTime != -2) {
					timer.play();
				}
				menuStage.close();
			}

		});

		initContent();
		this.stage = map;
		Scene scene = new Scene(appRoot);

		/*
		 * Detect keyboard input of P, ESCAPE and PgDn
		 * 
		 * The game will be paused if P is pressed until further instruction, The user
		 * will not have the control of the character nor the ghost will move.
		 * 
		 * A confirmation dialog will be displayed if Esc is pressed and if the result
		 * is yes, then return back to the main welcome stage
		 * 
		 * Time will be skipped to 0 if PgDn is pressed, in our game, if game time is 0,
		 * a Game Over page will be displayed. Therefore, pressing this button means
		 * ending the game with a loss.
		 */

		scene.setOnKeyPressed(event -> {

			keys.put(event.getCode(), true);
			if (event.getCode() == KeyCode.ESCAPE) {
				time.pause();
				timer.pause();
				timePaused = true;
				Alert warning = new Alert(AlertType.CONFIRMATION);
				warning.setTitle("Confirmation");
				warning.setHeaderText("Are you sure to exit?");

				ButtonType no = new ButtonType("No");
				ButtonType yes = new ButtonType("Yes");

				warning.getButtonTypes().setAll(yes, no);

				Optional<ButtonType> result = warning.showAndWait();
				if (result.get() == yes) {
					Parent parent;
					try {
						parent = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
						Scene scene2 = new Scene(parent);
						stage.setScene(scene2);
						stage.show();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					time.play();
					timePaused = false;
					if (currentTime != -2) {
						timer.play();
					}
				}
			}
			if (event.getCode() == KeyCode.P) {
				time.pause();
				timer.pause();
				timePaused = true;
				menuStage.setScene(menuScene);
				menuStage.show();
			}
			if (event.getCode() == KeyCode.PAGE_DOWN) {
				gameTime = 0;
			}
		});

		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

		map.setScene(scene);
		map.show();

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

	// ---------------------------------------Timer--------------------------------------------------
	/*
	 * Timers are paused if p or esc is pressed or when the player loses a life
	 */

	/*
	 * This is a count down timer from 3 to 0 which is displayed at the start of the
	 * game Character will not be able to move nor the enemies until the timer has
	 * reached 0
	 */

	public void setTime() {

		timer.setCycleCount(Timeline.INDEFINITE);
		if (timer != null) {
			timer.stop();
		}

		KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				display = createMenu(200, 68, 440, 350, currentTime.toString(), Color.WHITE);
				uiRoot.getChildren().add(display);
				currentTime--;
				if (currentTime == 0) {

					// display.setText("START");
				}
				if (currentTime == -1) {

					display = createMenu(200, 68, 440, 350, "Start!", Color.WHITE);
					uiRoot.getChildren().add(display);
				}
				if (currentTime == -2) {
					timer.stop();
					uiRoot.getChildren().clear();
				}

			}
		});

		timer.getKeyFrames().add(frame);

		timer.playFromStart();

	}

	public void resetTime() {

		timer.getKeyFrames().clear();

		timer.playFromStart();
	}

	/*
	 * This is a 2 minute game timer, counts from 120 to 0 This timer starts right
	 * after count down timer has reached 0 It stops when the time is 0 and displays
	 * TIMES UP, then a GAMEOVER scene will be displayed.
	 */
	public void setGameTime() {

		time.setCycleCount(Timeline.INDEFINITE);

		if (time != null) {
			time.stop();
		}

		KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				uiRoot.getChildren().clear();
				displayTime = createMenu(100, 40, 490, 10, gameTime.toString(), Color.WHITE);
				uiRoot.getChildren().add(displayTime);
				gameTime--;
				TIME++;
				if (gameTime == -1) {
					time.stop();
					displayTime = createMenu(100, 40, 490, 10, "Times up!", Color.RED);
					uiRoot.getChildren().add(displayTime);
					levelSelect select = new levelSelect();
					select.levelFailed(stage);
				}

			}
		});

		time.getKeyFrames().add(frame);

		time.playFromStart();
	}

	public void resetGameTime() {

		time.getKeyFrames().clear();

		time.play();
	}

	public void stopGameTime() {

		time.getKeyFrames().clear();

		time.stop();

	}

	// ------------------------------------SWORD----------------------------------
	// Check sword use
	private void swordUse() {
		if (user.getBoundsInParent().intersects(redEnemy.getBoundsInParent())) {
			gameRoot.getChildren().remove(redEnemy);
			redEnemy.setTranslateX(520);
			redEnemy.setTranslateY(360);
			gameRoot.getChildren().add(redEnemy);
		}
		if (user.getBoundsInParent().intersects(blueEnemy.getBoundsInParent())) {
			gameRoot.getChildren().remove(blueEnemy);
			blueEnemy.setTranslateX(520);
			blueEnemy.setTranslateY(360);
			gameRoot.getChildren().add(blueEnemy);
		}
		if (user.getBoundsInParent().intersects(purpleEnemy.getBoundsInParent())) {
			gameRoot.getChildren().remove(purpleEnemy);
			purpleEnemy.setTranslateX(600);
			purpleEnemy.setTranslateY(360);
			gameRoot.getChildren().add(purpleEnemy);
		}
	}

	public void setSwordTime() {

		swordTime.setCycleCount(Timeline.INDEFINITE);

		if (swordTime != null) {
			swordTime.stop();
		}

		KeyFrame swordFrame = new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				swordCount--;
				if ((swordCount <= 30) && (swordCount % 2 == 1)) {
					user.setOpacity(0.5);
				} else if ((swordCount <= 30) && (swordCount % 2 == 0)) {
					user.setOpacity(1);
				}
				if (swordCount == -1) {
					swordTime.getKeyFrames().clear();
					swordTime.stop();
					swordHeld = false;
					// System.out.println("Sword has broken");
					swordCount = 60;
				}
			}
		});

		swordTime.getKeyFrames().add(swordFrame);
		swordTime.playFromStart();
	}

	public void stopSwordTime() {
		swordTime.getKeyFrames().clear();
		swordTime.stop();
		swordCount = 60;
	}

	// --------------------------------------------------------------
	/*
	 * Getter methods that are useful for the game
	 */
	public int getScore() {
		return Score;
	}

	public void resetScore() {
		Score = 0;
	}

	public int getTime() {
		return TIME;
	}

	public void resetTIME() {
		TIME = 0;
	}

	private int[][] getCheckMaze() {

		return checkMaze;
	}

	// -------------------------------------Menu-------------------------------------------

	public VBox menuSetUp() {
		Button skip = new Button("skip");

		skip.setOnAction((ActionEvent event) -> {
			levelSelect select = new levelSelect();
			try {
				coinBagSFX();
				select.levelClear(stage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			menuStage.close();
		});

		Button resume = new Button("resume");
		// resume.setOpacity(1.0);
		resume.setOnAction((ActionEvent event) -> {
			time.play();
			timePaused = false;
			if (currentTime != -2) {
				timer.play();
			}
			menuStage.close();
		});

		Button retry = new Button("retry");

		retry.setOnAction((ActionEvent event) -> {
			try {
				menuStage.close();
				levelSelect select = new levelSelect();

				select.selectLevel(stage);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Button failTest = new Button("failTest");

		failTest.setOnAction((ActionEvent event) -> {
			try {
				menuStage.close();
				select.levelFailed(stage);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Label menuName = new Label();
		menuName.setText("Menu");
		HBox insertMenu = new HBox();
		insertMenu.setPadding(new Insets(10, 10, 10, 75));
		insertMenu.getChildren().add(menuName);

		VBox subMenu = new VBox();
		subMenu.setPrefHeight(300);
		subMenu.setPrefWidth(200);

		resume.setMinWidth(subMenu.getPrefWidth());
		resume.setMinHeight(40);

		skip.setMinWidth(subMenu.getPrefWidth());
		skip.setMinHeight(40);

		retry.setMinWidth(subMenu.getPrefWidth());
		retry.setMinHeight(40);

		failTest.setMinWidth(subMenu.getPrefWidth());
		failTest.setMinHeight(40);

		subMenu.getChildren().addAll(insertMenu, resume, retry, skip, failTest);
		return subMenu;
	}

	public void directionSelect() {
		int speed = player.getSpeed();
		int negativeSpeed = 0 - speed;

		prevDirection = direction;
		// Direction select

		if (isPressed(KeyCode.UP) && user.getTranslateY() >= 5) {
			direction = "UP";
		}

		if (isPressed(KeyCode.LEFT) && user.getTranslateX() >= 5) {
			direction = "LEFT";
		}

		if (isPressed(KeyCode.RIGHT) && user.getTranslateX() + 40 <= levelWidth - 5) {
			direction = "RIGHT";
		}

		if (isPressed(KeyCode.DOWN) && user.getTranslateY() + 40 >= 5) {
			direction = "DOWN";
		}
		if (isPressed(KeyCode.UP) && (isPressed(KeyCode.RIGHT))) {
			direction = "UP_RIGHT";
		}
		if (isPressed(KeyCode.UP) && (isPressed(KeyCode.LEFT))) {
			direction = "UP_LEFT";
		}
		if (isPressed(KeyCode.DOWN) && (isPressed(KeyCode.RIGHT))) {
			direction = "DOWN_RIGHT";
		}
		if (isPressed(KeyCode.DOWN) && (isPressed(KeyCode.LEFT))) {
			direction = "DOWN_LEFT";
		}
		if (isPressed(KeyCode.P)) {
			direction = "NONE";
		}

		if (!prevDirection.equals(direction)) {
			directionSet = 0;
		}

		// Direction Move
		if (direction.equals("UP")) {
			if (directionSet == 0) {
				changeDirection(player.getModelDirection(direction));
				directionSet = 1;
			}
			moveuserY(negativeSpeed);
		} else if (direction.equals("LEFT")) {
			if (directionSet == 0) {
				changeDirection(player.getModelDirection(direction));
				directionSet = 1;
			}
			moveuserX(negativeSpeed);
		} else if (direction.equals("RIGHT")) {
			if (directionSet == 0) {
				changeDirection(player.getModelDirection(direction));
				directionSet = 1;
			}
			moveuserX(speed);
		} else if (direction.equals("DOWN")) {
			if (directionSet == 0) {
				changeDirection(player.getModelDirection(direction));
				directionSet = 1;
			}
			moveuserY(speed);
		} else if (direction.equals("UP_RIGHT")) {
			moveuserY(negativeSpeed);
			moveuserX(speed);
		} else if (direction.equals("UP_LEFT")) {
			moveuserY(negativeSpeed);
			moveuserX(negativeSpeed);
		} else if (direction.equals("DOWN_RIGHT")) {
			moveuserY(speed);
			moveuserX(speed);
		} else if (direction.equals("DOWN_LEFT")) {
			moveuserY(speed);
			moveuserX(negativeSpeed);
		}
	}

	// Movement of user
	private void moveuserX(int value) {
		boolean movingRight = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (user.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingRight) {
						if (user.getTranslateX() + 36 == platform.getTranslateX()) {

							user.setTranslateX(user.getTranslateX() - 1);
							return;
						}
					} else {
						if (user.getTranslateX() == platform.getTranslateX() + 40) {

							user.setTranslateX(user.getTranslateX() + 1);
							return;
						}
					}
				}
			}
			user.setTranslateX(user.getTranslateX() + (movingRight ? 1 : -1));
			if (user.getTranslateX() + 36 == 1080.0) {
				user.setTranslateX(0);
			} else if (user.getTranslateX() == 0) {
				user.setTranslateX(1036.0);
			}

		}
	}

	private void moveuserY(int value) {
		boolean movingDown = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (user.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingDown) {
						if (user.getTranslateY() + 36 == platform.getTranslateY()) {

							user.setTranslateY(user.getTranslateY() - 1);
							return;
						}
					} else {
						if (user.getTranslateY() == platform.getTranslateY() + 40) {

							user.setTranslateY(user.getTranslateY() + 1);
							return;
						}
					}
				}
			}
			user.setTranslateY(user.getTranslateY() + (movingDown ? 1 : -1));
		}
	}
	// -----------
	// -----------------------------------ShortestPath--------------------------------
	/*
	 * We have used BFS as our path-finding algorithm to find the shortest path to
	 * get to the player. The following is similar to what we have done last year in
	 * 202 for dijkstra, except this maze is an Unweighted 2D graph, which we have
	 * to find the edges The purpose of the Point class is to store positions of x
	 * and y. Then we blindly search for possible edges and then enqueue them
	 * Dequeue a point to look for more possible edges until the destination is
	 * reached Point p will then contain all of the positions which then can be used
	 * by the ghosts
	 */

	public static class Point {
		int x;
		int y;
		Point parent;

		public Point(int x, int y, Point parent) {
			this.x = x;
			this.y = y;
			this.parent = parent;

		}

		public Point getParent() {
			return this.parent;
		}

		public String toString() {
			return "x = " + x + " y = " + y;
		}
	}

	public static Queue<Point> q = new LinkedList<Point>();

	public Point getPathBFS(int x, int y) {
		q.clear();
		int[][] ar = new int[20][27];

		ar = getCheckMaze();

		q.add(new Point(x, y, null));

		while (!q.isEmpty()) {
			Point p = q.remove();

			if (ar[p.x][p.y] == 9) {
				for (int i = 0; i < checkMaze.length; i++) {
					for (int j = 0; j < checkMaze[0].length; j++) {
						// System.out.print(checkMaze[i][j] + " ");
						if (ar[i][j] == -1 || ar[i][j] == 9) {
							ar[i][j] = 0;
							// System.out.print(checkMaze[i][j] + " ");
						}

					}
					// System.out.println();
				}

				// System.out.println();
				q.clear();
				return p;
			}

			if (isFree(p.x + 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x + 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x - 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x - 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y + 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y + 1, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y - 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y - 1, p);
				q.add(nextP);
			}

		}

		for (int i = 0; i < checkMaze.length; i++) {
			for (int j = 0; j < checkMaze[0].length; j++) {
				if (ar[i][j] == -1 || ar[i][j] == 9) {
					ar[i][j] = 0;
				}
			}
		}
		q.clear();
		return null;
	}

	public static boolean isFree(int x, int y, int lengthX, int lengthY, int[][] maze) {
		if ((x >= 0 && x < lengthX) && (y >= 0 && y < lengthY) && (maze[x][y] == 0 || maze[x][y] == 9 || maze[x][y] == 2
				|| maze[x][y] == 3 || maze[x][y] == 8 || maze[x][y] == 7 || maze[x][y] == 5 || maze[x][y] == 6)) {
			return true;
		}
		return false;
	}

	/*
	 * Movement for red enemy
	 */
	public void redMovement() {

		Point point;
		if (redEnemy.getTranslateX() % 40 == 0 && redEnemy.getTranslateY() % 40 == 0) {
			p = getPathBFS(redy, redx);
			if (p != null) {
				while (p.getParent() != null) {
					store.add(p);
					p = p.getParent();
				}

				point = null;
				for (int i = 0; i < store.size(); i++) {
					point = store.get(i);
					if (point.x == redy && point.y == redx && i != 0) {
						point = store.get(i - 1);
						break;
					}

				}

				p = point;
				if (p != null) {
					if (p.x > redy) {
						moveUp = 1;
					} else if (p.x < redy) {
						moveUp = 2;
					} else if (p.x == redEnemy.getTranslateY() / 40.0) {
						moveUp = 0;
					}
					if (p.y > redx) {
						moveRight = 1;
					} else if (p.y < redx) {
						moveRight = 2;
					} else if (p.y == redEnemy.getTranslateX() / 40.0) {
						moveRight = 0;
					}
				}
			}
		}
		if (moveUp == 1) {
			moveredY(1);
		} else if (moveUp == 2) {
			moveredY(-1);
		}
		if (moveRight == 1) {
			moveredX(1);
		} else if (moveRight == 2) {
			moveredX(-1);
		}

		// redx = 0;
		// redy = 0;
		store.clear();
	}

	private void moveredY(int value) {
		boolean movingDown = value > 0;
		int abs = Math.abs(value);
		for (int i = 0; i < abs; i++) {
			if (movingDown) {
				redEnemy.setTranslateY(redEnemy.getTranslateY() + 1);

			} else {
				redEnemy.setTranslateY(redEnemy.getTranslateY() - 1);
			}
		}
	}

	private void moveredX(int value) {
		boolean movingRight = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {
			if (movingRight) {
				redEnemy.setTranslateX(redEnemy.getTranslateX() + 1);

			} else {
				redEnemy.setTranslateX(redEnemy.getTranslateX() - 1);

			}
		}
	}

	public Point findOtherCorner(int x, int y) {

		int[][] ar = new int[20][27];

		ar = checkMaze;
		// for (int i = 0; i < checkMaze.length; i++) {
		// for (int j = 0; j < checkMaze[0].length; j++) {
		// System.out.print(checkMaze[i][j] + " ");
		// }
		// System.out.println();
		// }

		// System.out.println();
		q.add(new Point(x, y, null));

		while (!q.isEmpty()) {
			Point p = q.remove();

			if (ar[p.x][p.y] == 7) {
				for (int i = 0; i < checkMaze.length; i++) {
					for (int j = 0; j < checkMaze[0].length; j++) {
						if (ar[i][j] == -1 || ar[i][j] == 9) {
							ar[i][j] = 0;
						}
					}
				}
				q.clear();
				return p;
			}

			if (isFree(p.x + 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x + 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x - 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x - 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y + 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y + 1, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y - 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y - 1, p);
				q.add(nextP);
			}

		}

		for (int i = 0; i < checkMaze.length; i++) {
			for (int j = 0; j < checkMaze[0].length; j++) {
				if (ar[i][j] == -1 || ar[i][j] == 9) {
					ar[i][j] = 0;
				}
			}
		}
		return null;
	}

	public void purpleMovement() {
		purplex = (int) (purpleEnemy.getTranslateX() / 40);
		purpley = (int) (purpleEnemy.getTranslateY() / 40);

		// System.out.println(purpleEnemy.getTranslateY() + " " +
		// purpleEnemy.getTranslateX());

		// 3,1 -> 6,1 -> 6,5 -> 3,1

		if (purpleEnemy.getTranslateX() == 600 && purpleEnemy.getTranslateY() == 360) {
			checkMaze[3][1] = 7;
			pcheck = 0;
		}
		if (purpleEnemy.getTranslateX() == 40 && purpleEnemy.getTranslateY() == 120) {
			checkMaze[6][1] = 7;
			checkMaze[6][5] = 0;
			checkMaze[3][1] = 0;
			pcheck = 1;
		} else if (purpleEnemy.getTranslateX() == 40 && purpleEnemy.getTranslateY() == 240) {
			checkMaze[6][1] = 0;
			checkMaze[6][5] = 7;
			checkMaze[3][1] = 0;
			pcheck = 2;
		} else if (purpleEnemy.getTranslateX() == 200 && purpleEnemy.getTranslateY() == 240) {
			checkMaze[6][1] = 0;
			checkMaze[6][5] = 0;
			checkMaze[3][1] = 7;
			pcheck = 0;
		}
		// System.out.println(check);

		Point point;
		if (purpleEnemy.getTranslateX() % 40 == 0 && purpleEnemy.getTranslateY() % 40 == 0) {

			p2 = getPathBFS(purpley, purplex);
			if (pcheck == 0) {
				checkMaze[3][1] = 7;
				checkMaze[6][1] = 0;
				checkMaze[6][6] = 0;
			} else if (pcheck == 1) {
				checkMaze[6][1] = 7;
				checkMaze[3][1] = 0;
				checkMaze[6][5] = 0;
			} else if (pcheck == 2) {
				checkMaze[6][5] = 7;
				checkMaze[3][1] = 0;
				checkMaze[6][1] = 0;
			}

			if (p2 != null) {
				while (p2.getParent() != null) {
					store2.add(p2);
					p2 = p2.getParent();
				}

				point = null;
				if (store2.size() < 8) {
					store2.clear();
					// System.out.println("SOMETHING");
					// p = null;
					p2 = findOtherCorner(purpley, purplex);
					if (p2 != null) {
						while (p2.getParent() != null) {
							store2.add(p2);
							p2 = p2.getParent();
						}
						for (int i = 0; i < store2.size(); i++) {
							point = store2.get(i);
							// System.out.println("7: " + point);
							if (point.x == purpley && point.y == purplex && i != 0) {
								point = store2.get(i - 1);
								break;
							}
						}
						// System.out.println();
					}

				} else {
					// do something
					// store2.clear();
					// // System.out.println("SOMETHING");
					// // p = null;
					// p2 = findOtherCorner(purpley, purplex);
					// if (p2 != null) {
					// while (p2.getParent() != null) {
					// store2.add(p2);
					// p2 = p2.getParent();
					// }
					// for (int i = 0; i < store2.size(); i++) {
					// point = store2.get(i);
					// // System.out.println("7: " + point);
					// if (point.x == purpley && point.y == purplex && i != 0) {
					// point = store2.get(i - 1);
					// break;
					// }
					// }
					// // System.out.println();
					// }
					for (int i = 0; i < store2.size(); i++) {
						point = store2.get(i);
						// System.out.println(point);
						if (point.x == purpley && point.y == purplex && i != 0) {
							point = store2.get(i - 1);
							break;
						}

					}
				}

				p2 = point;

				if (p2 != null) {
					if (p2.x > purpley) {
						pmoveUp = 1;
					} else if (p2.x < purpley) {
						pmoveUp = 2;
					} else if (p2.x == purpleEnemy.getTranslateY() / 40.0) {
						pmoveUp = 0;
					}
					if (p2.y > purplex) {
						pmoveRight = 1;
					} else if (p2.y < purplex) {
						pmoveRight = 2;
					} else if (p2.y == purpleEnemy.getTranslateX() / 40.0) {
						pmoveRight = 0;
					}
				}
			}
		}
		if (pmoveUp == 1) {
			movepurpleY(1);
		} else if (pmoveUp == 2) {
			movepurpleY(-1);
		}
		if (pmoveRight == 1) {
			movepurpleX(1);
		} else if (pmoveRight == 2) {
			movepurpleX(-1);
		}

		purplex = 0;
		purpley = 0;
		store2.clear();
	}

	private void movepurpleY(int value) {
		boolean movingDown = value > 0;
		int abs = Math.abs(value);
		for (int i = 0; i < abs; i++) {
			if (movingDown) {
				purpleEnemy.setTranslateY(purpleEnemy.getTranslateY() + 1);

			} else {
				purpleEnemy.setTranslateY(purpleEnemy.getTranslateY() - 1);
			}
		}
	}

	private void movepurpleX(int value) {
		boolean movingRight = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {
			if (movingRight) {
				purpleEnemy.setTranslateX(purpleEnemy.getTranslateX() + 1);

			} else {
				purpleEnemy.setTranslateX(purpleEnemy.getTranslateX() - 1);

			}
		}
	}

	public void blueMovement() {

		bluey = (int) blueEnemy.getTranslateX() / 40;
		bluex = (int) blueEnemy.getTranslateY() / 40;
		int freey = 0;
		int freex = 0;
		boolean chasePlayer = false;

		if (direction.equals("UP") || direction.equals("UP_RIGHT") || direction.equals("UP_LEFT")
				|| direction.equals("NONE")) {
			if (isFree(playery - 1, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
				freey = -1;
				if (isFree(playery - 2, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
					freey = -2;
					if (isFree(playery - 3, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
						freey = -3;
						if (isFree(playery - 4, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
							freey = -4;
						}
					}
				}
			}
		}
		if (direction.equals("DOWN") || direction.equals("DOWN_RIGHT") || direction.equals("DOWN_LEFT")) {
			if (isFree(playery + 1, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
				freey = 1;
				if (isFree(playery + 2, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
					freey = 2;
					if (isFree(playery + 3, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
						freey = 3;
						if (isFree(playery + 4, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
							freey = 4;
						}
					}
				}
			}

		}
		if (direction.equals("RIGHT")) {
			if (isFree(playery, playerx + 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
				freex = 1;
				if (isFree(playery, playerx + 2, checkMaze.length, checkMaze[0].length, checkMaze)) {
					freex = 2;
					if (isFree(playery, playerx + 3, checkMaze.length, checkMaze[0].length, checkMaze)) {
						freex = 3;
						if (isFree(playery, playerx + 4, checkMaze.length, checkMaze[0].length, checkMaze)) {
							freex = 4;
						}
					}
				}
			}
		}
		if (direction.equals("LEFT")) {
			if (isFree(playery, playerx - 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
				freex = -1;
				if (isFree(playery, playerx - 2, checkMaze.length, checkMaze[0].length, checkMaze)) {
					freex = -2;
					if (isFree(playery, playerx - 3, checkMaze.length, checkMaze[0].length, checkMaze)) {
						freex = -3;
						if (isFree(playery, playerx - 4, checkMaze.length, checkMaze[0].length, checkMaze)) {
							freex = -4;
						}
					}
				}
			}
		}
	
		int posy = playery + freey;
		int posx = playerx + freex;

		Point point;
		if (blueEnemy.getTranslateX() % 40 == 0 && blueEnemy.getTranslateY() % 40 == 0) {
			p = shortestPath(bluey, bluex);
			if (p != null) {
				while (p.getParent() != null) {
					store.add(p);
					p = p.getParent();
				}

				point = null;
				for (int i = 0; i < store.size(); i++) {
					point = store.get(i);
					if (point.x == redy && point.y == redx && i != 0) {
						point = store.get(i - 1);
						break;
					}

				}

				p = point;
				if (p != null) {
					if (p.x > redy) {
						bmoveUp = 1;
					} else if (p.x < redy) {
						bmoveUp = 2;
					} else if (p.x == blueEnemy.getTranslateY() / 40.0) {
						bmoveUp = 0;
					}
					if (p.y > redx) {
						bmoveRight = 1;
					} else if (p.y < redx) {
						bmoveRight = 2;
					} else if (p.y == blueEnemy.getTranslateX() / 40.0) {
						bmoveRight = 0;
					}
				}
			}
		}
		if (bmoveUp == 1) {
			moveblueY(1);
		} else if (bmoveUp == 2) {
			moveblueY(-1);
		}
		if (bmoveRight == 1) {
			moveblueX(1);
		} else if (bmoveRight == 2) {
			moveblueX(-1);
		}

		// redx = 0;
		// redy = 0;
		store.clear();

	}

	public Point trapMode(int x, int y) {
		int[][] ar = new int[20][27];

		ar = getCheckMaze();

		q.add(new Point(x, y, null));

		while (!q.isEmpty()) {
			Point p = q.remove();

			if (ar[p.x][p.y] == 5) {
				for (int i = 0; i < checkMaze.length; i++) {
					for (int j = 0; j < checkMaze[0].length; j++) {
						if (ar[i][j] == -1 || ar[i][j] == 5) {
							ar[i][j] = 0;
						}
					}
				}
				q.clear();
				return p;
			}

			if (isFree(p.x + 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x + 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x - 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x - 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y + 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y + 1, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y - 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y - 1, p);
				q.add(nextP);
			}
		}

		for (int i = 0; i < checkMaze.length; i++) {
			for (int j = 0; j < checkMaze[0].length; j++) {
				if (ar[i][j] == -1 || ar[i][j] == 5) {
					ar[i][j] = 0;
				}
			}
		}

		return null;
	}

	private void moveblueY(int value) {
		boolean movingDown = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			if (movingDown) {
				blueEnemy.setTranslateY(blueEnemy.getTranslateY() + 1);

			} else {
				blueEnemy.setTranslateY(blueEnemy.getTranslateY() - 1);
			}
		}
	}

	private void moveblueX(int value) {
		boolean movingRight = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {
			if (movingRight) {
				blueEnemy.setTranslateX(blueEnemy.getTranslateX() + 1);

			} else {
				blueEnemy.setTranslateX(blueEnemy.getTranslateX() - 1);

			}
		}
	}

	public Point findVector(int x, int y) {

		int[][] ar = new int[20][27];

		ar = getCheckMaze();

		q.add(new Point(x, y, null));

		while (!q.isEmpty()) {
			Point p = q.remove();

			if (ar[p.x][p.y] == 6) {
				for (int i = 0; i < checkMaze.length; i++) {
					for (int j = 0; j < checkMaze[0].length; j++) {
						if (ar[i][j] == -1 || ar[i][j] == 6) { // may delete 6
							ar[i][j] = 0;
						}
					}
				}
				q.clear();
				return p;
			}

			if (isFree(p.x + 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x + 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x - 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x - 1, p.y, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y + 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y + 1, p);
				q.add(nextP);
			}

			if (isFree(p.x, p.y - 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y - 1, p);
				q.add(nextP);
			}

		}

		for (int i = 0; i < checkMaze.length; i++) {
			for (int j = 0; j < checkMaze[0].length; j++) {
				if (ar[i][j] == -1 || ar[i][j] == 6) { // may delete 6
					ar[i][j] = 0;
				}
			}
		}
		q.clear();
		return null;
	}

	public void greenMovement() {
		greenx = (int) (greenEnemy.getTranslateX() / 40);
		greeny = (int) (greenEnemy.getTranslateY() / 40);
		int offsetx = 0;
		int offsety = 0;
		int toy = 0;
		int tox = 0;
		int dy = 0;
		int dx = 0;
		boolean chasePlayer = false;
		if (greenEnemy.getTranslateX() % 40 == 0 && greenEnemy.getTranslateY() % 40 == 0) {
			if (direction.equals("UP") || direction.equals("UP_RIGHT") || direction.equals("UP_LEFT")
					|| direction.equals("NONE")) {
				if (isFree(playery - 1, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
					offsety = 1;
					if (isFree(playery - 2, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
						offsety = 2;
					}
				}
				dy = Math.abs(playery + offsety - redy);
				dx = Math.abs(playerx - redx);
			}
			if (direction.equals("DOWN") || direction.equals("DOWN_RIGHT") || direction.equals("DOWN_LEFT")) {
				if (isFree(playery + 1, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
					offsety = +1;
					if (isFree(playery + 2, playerx, checkMaze.length, checkMaze[0].length, checkMaze)) {
						offsety = +2;
					}
				}
				dy = Math.abs(playery + offsety - redy);
				dx = Math.abs(playerx - redx);
			}
			if (direction.equals("RIGHT")) {
				if (isFree(playery, playerx + 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
					offsetx = 1;
					if (isFree(playery, playerx + 2, checkMaze.length, checkMaze[0].length, checkMaze)) {
						offsetx = 2;
					}
				}
				dy = Math.abs(playery - redy);
				dx = Math.abs(playerx + offsetx - redx);
			}
			if (direction.equals("LEFT")) {
				if (isFree(playery, playerx - 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
					offsetx = 1;
					if (isFree(playery, playerx - 2, checkMaze.length, checkMaze[0].length, checkMaze)) {
						offsetx = 2;
					}
				}
				dy = Math.abs(playery - redy);
				dx = Math.abs(playerx + offsetx - redx);
			}

			// System.out.println("position: " + playery + " " + playerx + " " + redy + " "
			// + redx);
			// System.out.println("difference: " + dy + " " + dx);

			if (redy < playery) {
				vertical = 1; // TOP
			} else if (redy > playery) {
				vertical = 2; // BOTTOM
			} else if (redy == playery) {
				vertical = 0; // SAME
			}
			if (redx < playerx) {
				horizontal = 1; // LEFT
			} else if (redx > playerx) {
				horizontal = 2; // RIGHT
			} else if (redx == playerx) {
				horizontal = 0; // SAME
			}

			if (vertical == 1 && horizontal == 1) {
				toy = playery + dy;
				tox = playerx + dx;
			} else if (vertical == 1 && horizontal == 2) {
				toy = playery + dy;
				tox = playerx - dx;
			} else if (vertical == 1 && horizontal == 0) {
				toy = playery + dy;
				tox = playerx;
			} else if (vertical == 2 && horizontal == 1) {
				toy = playery - dy;
				tox = playerx + dx;
			} else if (vertical == 2 && horizontal == 2) {
				toy = playery - dy;
				tox = playerx - dx;
			} else if (vertical == 2 && horizontal == 0) {
				toy = playery - dy;
				tox = playerx;
			} else if (vertical == 0 && horizontal == 1) {
				toy = playery;
				tox = playerx + dx;
			} else if (vertical == 0 && horizontal == 2) {
				toy = playery;
				tox = playerx - dx;
			}
			// System.out.println("Before checking boundaries and validity: " + toy + " " +
			// tox);
			if (toy > 0 && toy < checkMaze.length && tox > 0 && tox < checkMaze[0].length) {
				if (checkMaze[toy][tox] != 1) {
					checkMaze[toy][tox] = 6;
				} else if (checkMaze[toy][tox] == 1) {
					if (isFree(toy - 1, tox, checkMaze.length, checkMaze[0].length, checkMaze)) {
						checkMaze[toy - 1][tox] = 6;
						toy--;
					} else if (isFree(toy + 1, tox, checkMaze.length, checkMaze[0].length, checkMaze)) {
						checkMaze[toy + 1][tox] = 6;
						toy++;
					} else if (isFree(toy, tox - 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
						checkMaze[toy][tox - 1] = 6;
						tox--;
					} else if (isFree(toy, tox + 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
						checkMaze[toy][tox + 1] = 6;
						tox++;
					}
				}
			} else {
				// Out of bounds prevention
				if (toy >= checkMaze.length) {
					while (toy >= checkMaze.length - 1) {
						toy--;
					}

				}
				if (toy <= 2) {
					while (toy <= 2) {
						toy++;
					}

				}
				if (tox >= checkMaze[0].length) {
					while (tox >= checkMaze[0].length - 1) {
						tox--;
					}

				}
				if (tox < 0) {
					while (tox <= 0) {
						tox++;
					}

				}
				if (checkMaze[toy][tox] == 1) {
					if (isFree(toy - 1, tox, checkMaze.length, checkMaze[0].length, checkMaze)) {
						toy--;
					} else if (isFree(toy + 1, tox, checkMaze.length, checkMaze[0].length, checkMaze)) {
						toy++;
					} else if (isFree(toy, tox - 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
						tox--;
					} else if (isFree(toy, tox + 1, checkMaze.length, checkMaze[0].length, checkMaze)) {
						tox++;
					}
				}
				if (checkMaze[toy][tox] == 9) {
					// do something
					// System.out.println("YES");
					chasePlayer = true;
				} else {
					checkMaze[toy][tox] = 6;
					// System.out.println("IN");
				}
			}

			// System.out.println(toy + " " + tox);
			// for (int i = 0; i < checkMaze.length; i++) {
			// for (int j = 0; j < checkMaze[0].length; j++) {
			// System.out.print(checkMaze[i][j] + " ");
			// }
			// System.out.println();
			// }
			// System.out.println();

			Point point;
			// if (greenEnemy.getTranslateX() % 40 == 0 && greenEnemy.getTranslateY() % 40
			// == 0) {
			p4 = getPathBFS(greeny, greenx);
			if (p4 != null) {
				while (p4.getParent() != null) {
					store4.add(p4);
					p4 = p4.getParent();
				}
				point = store4.get(store4.size() - 1);
				if (chasePlayer || store4.size() <= 2) {
					if (point != null) {
						// movePlayer
						if (point.x > greeny) {
							gmoveUp = 1;
						} else if (point.x < greeny) {
							gmoveUp = 2;
						} else if (point.x == greeny) {
							gmoveUp = 0;
						}
						if (point.y > greenx) {
							gmoveRight = 1;
						} else if (point.y < greenx) {
							gmoveRight = 2;
						} else if (point.y == greenx) {
							gmoveRight = 0;
						}
					}
				} else if (store4.size() > 2 || !chasePlayer) {
					store4.clear();

					// System.out.println();
					// System.out.println(toy + " " + tox);
					checkMaze[toy][tox] = 6;

					p4 = findVector(greeny, greenx);
					if (p4 != null) {
						while (p4.getParent() != null) {
							store4.add(p4);
							// System.out.println(p);
							p4 = p4.getParent();
						}
						point = store4.get(store4.size() - 1);
					}

					if (point != null) {
						// movePlayer
						if (point.x > greeny) {
							gmoveUp = 1;
						} else if (point.x < greeny) {
							gmoveUp = 2;
						} else if (point.x == greeny) {
							gmoveUp = 0;
						}
						if (point.y > greenx) {
							gmoveRight = 1;
						} else if (point.y < greenx) {
							gmoveRight = 2;
						} else if (point.y == greenx) {
							gmoveRight = 0;
						}
					}
				}
			}
		}

		if (gmoveUp == 1) {
			movegreenY(1);
		} else if (gmoveUp == 2) {
			movegreenY(-1);
		}
		if (gmoveRight == 1) {
			movegreenX(1);
		} else if (gmoveRight == 2) {
			movegreenX(-1);
		}

		greenx = 0;
		greeny = 0;
		store4.clear();
	}

	private void movegreenY(int value) {
		boolean movingDown = value > 0;
		int abs = Math.abs(value);
		for (int i = 0; i < abs; i++) {
			if (movingDown) {
				greenEnemy.setTranslateY(greenEnemy.getTranslateY() + 1);

			} else {
				greenEnemy.setTranslateY(greenEnemy.getTranslateY() - 1);
			}
		}
	}

	private void movegreenX(int value) {
		boolean movingRight = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {
			if (movingRight) {
				greenEnemy.setTranslateX(greenEnemy.getTranslateX() + 1);

			} else {
				greenEnemy.setTranslateX(greenEnemy.getTranslateX() - 1);

			}
		}
	}
	
	public static Queue<Point> Q = new LinkedList<Point>();
	
	public Point shortestPath(int x, int y) {
		Q.clear();
		int[][] ar = new int[20][27];

		ar = getCheckMaze();

		Q.add(new Point(x, y, null));

		while (!Q.isEmpty()) {
			Point p = Q.remove();

			if (ar[p.x][p.y] == 9) {
				for (int i = 0; i < checkMaze.length; i++) {
					for (int j = 0; j < checkMaze[0].length; j++) {
						// System.out.print(checkMaze[i][j] + " ");
						if (ar[i][j] == -1 || ar[i][j] == 9) {
							ar[i][j] = 0;
							// System.out.print(checkMaze[i][j] + " ");
						}

					}
					// System.out.println();
				}

				// System.out.println();
				Q.clear();
				return p;
			}

			if (isFree(p.x + 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x + 1, p.y, p);
				Q.add(nextP);
			}

			if (isFree(p.x - 1, p.y, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x - 1, p.y, p);
				Q.add(nextP);
			}

			if (isFree(p.x, p.y + 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y + 1, p);
				Q.add(nextP);
			}

			if (isFree(p.x, p.y - 1, ar.length, ar[0].length, ar)) {
				ar[p.x][p.y] = -1;
				Point nextP = new Point(p.x, p.y - 1, p);
				Q.add(nextP);
			}

		}

		for (int i = 0; i < checkMaze.length; i++) {
			for (int j = 0; j < checkMaze[0].length; j++) {
				if (ar[i][j] == -1 || ar[i][j] == 9) {
					ar[i][j] = 0;
				}
			}
		}
		Q.clear();
		return null;
	}

	// public static boolean canReach(int x, int y, int lengthX, int lengthY,
	// int[][] maze) {
	// if ((x >= 0 && x < lengthX) && (y >= 0 && y < lengthY) && (maze[x][y] == 0 ||
	// maze[x][y] == 9 || maze[x][y] == 2
	// || maze[x][y] == 3 || maze[x][y] == 8 || maze[x][y] == 7)) {
	// return true;
	// }
	// return false;
	// }
}
