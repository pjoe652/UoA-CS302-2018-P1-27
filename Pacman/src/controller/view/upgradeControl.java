package controller.view;

import controller.model.MusicPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class upgradeControl extends characterControl {
	/*
	 * This class controls upgrade.fxml
	 */

	// Character
	@FXML
	private ImageView characterView = new ImageView();

	private MusicPlayer music = new MusicPlayer();

	// Lives
	private int currentHp;
	@FXML
	private Label Hp;
	private String health;

	// Speed
	private int currentSpeed;
	@FXML
	private Label speed;
	private String quickness;

	// PowerDuration
	@FXML
	private Label powerDuration;
	private double time;
	private String duration;

	// Points
	private int currentPoints;
	@FXML
	private Label points;
	private String pt;

	// Retract Points
	private int spendHP = 0;
	private int spendSp = 0;
	private int spendPower = 0;

	// -------------------Attributes---------------------------
	@FXML
	public void handlePlusHpButton(ActionEvent event) {
		if (player.getPoints() >= 3) {
			player.addHp();
			spendHP += 3;
			displayHp();

			player.deductPoints();
			player.deductPoints();
			player.deductPoints();
			displayPoints();
		}
	}

	@FXML
	public void handlePlusHpKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (player.getPoints() >= 3) {
				player.addHp();
				spendHP += 3;
				displayHp();

				player.deductPoints();
				player.deductPoints();
				player.deductPoints();
				displayPoints();
			}
		}
	}

	@FXML
	public void handleMinusHpButton(ActionEvent event) {
		if (spendHP >= 3) {
			player.minusHp();
			spendHP -= 3;
			displayHp();

			player.returnPoints(3);
			displayPoints();
		}
	}

	@FXML
	public void handleMinusHpKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (spendHP >= 3) {
				player.minusHp();
				spendHP -= 3;
				displayHp();

				player.returnPoints(3);
				displayPoints();
			}
		}
	}

	@FXML
	public void handlePlusSpButton(ActionEvent event) {
		if (player.getPoints() >= 2) {
			player.addSpeed();
			spendSp += 2;
			displaySpeed();

			player.deductPoints();
			player.deductPoints();
			displayPoints();
		}
	}

	@FXML
	public void handlePlusSpKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (player.getPoints() >= 2) {
				player.addSpeed();
				spendSp += 2;
				displaySpeed();

				player.deductPoints();
				player.deductPoints();
				displayPoints();
			}
		}
	}

	@FXML
	public void handleMinusSpButton(ActionEvent event) {
		if (spendSp >= 2) {
			player.minusSpeed();
			spendSp -= 2;
			displaySpeed();

			player.returnPoints(2);
			displayPoints();
		}
	}

	@FXML
	public void handleMinusSpKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (spendSp >= 2) {
				player.minusSpeed();
				spendSp -= 2;
				displaySpeed();

				player.returnPoints(2);
				displayPoints();
			}
		}
	}

	@FXML
	public void handlePlusPowerButton(ActionEvent event) {
		if (player.getPoints() > 0) {
			player.addPowerDuration();
			spendPower += 1;
			displayPowerDuration();

			player.deductPoints();
			displayPoints();
		}
	}

	@FXML
	public void handlePlusPowerKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (player.getPoints() > 0) {
				player.addPowerDuration();
				spendPower += 1;
				displayPowerDuration();

				player.deductPoints();
				displayPoints();
			}
		}
	}

	@FXML
	public void handleMinusPowerButton(ActionEvent event) {
		if (spendPower > 0) {
			player.minusPowerDuration();
			spendPower -= 1;
			displayPowerDuration();

			player.returnPoints(1);
			displayPoints();
		}
	}

	@FXML
	public void handleMinusPowerKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (spendPower > 0) {
				player.minusPowerDuration();
				spendPower -= 1;
				displayPowerDuration();

				player.returnPoints(1);
				displayPoints();
			}
		}
	}

	// ----------------------------------------------------------------

	@FXML
	public void handleContinueButton(ActionEvent event) throws Exception {
		spendHP = 0;
		spendSp = 0;
		spendPower = 0;
		player.setLevel(player.getLevel() + 1);
		levelSelect select = new levelSelect();
		select.selectStory(event);

	}

	@FXML
	public void handleContinueKeyPressed(KeyEvent event) throws Exception {
		if (event.getCode() == KeyCode.ENTER) {
			spendHP = 0;
			spendSp = 0;
			spendPower = 0;
			player.setLevel(player.getLevel() + 1);
			levelSelect select = new levelSelect();
			select.selectStoryKeyPressed(event);
		}
	}

	// -------------------------------Initialize------------------------------------------------

	@FXML
	public void initialize() throws Exception {
		music.BGM();
		/*
		 * This initializes the character display
		 */
		displayCharacter();
		/*
		 * This initializes the amount of lives
		 */
		displayHp();
		/*
		 * This initializes the speed
		 */
		displaySpeed();
		/*
		 * This initializes the duration of power pellet
		 */
		displayPowerDuration();
		/*
		 * This initializes the points available for upgrade
		 */
		displayPoints();

	}
	// -------------------------------------------------------------------------------------------

	// ------------------------display-------------------------------------
	public void displayPoints() {
		currentPoints = player.getPoints();
		pt = Integer.toString(currentPoints);
		points.setText(pt);
	}

	public void displayHp() {
		currentHp = player.getHp();
		health = Integer.toString(currentHp);
		Hp.setText(health);
	}

	public void displaySpeed() {
		currentSpeed = player.getSpeed();
		quickness = Integer.toString(currentSpeed);
		speed.setText(quickness);
	}

	public void displayPowerDuration() {
		time = player.getPowerDuration();
		duration = Double.toString(time);
		powerDuration.setText(duration + "s");
	}

	public void displayCharacter() {
		Image character = new Image(player.getModelDirection("DOWN"));
		characterView.setImage(character);
		characterView.setFitHeight(200);
		characterView.setFitWidth(200);

	}
}
