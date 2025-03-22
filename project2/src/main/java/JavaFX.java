import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class JavaFX extends Application {

	public static void main(String[] args) {

		launch(args);
	}

	ImageView forecastIconAssignment(String givenForecast) {
		InputStream imageStream;
		Image demoImage;

		return switch (givenForecast) {
			case "Sunny" -> {
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Any-Sunny-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
			case "Mostly Sunny", "Partly Cloudy", "Partly Sunny" -> {
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Day-PartlyCloudy-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
			case "Mostly Clear" -> {
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Night-Clear-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
			case "Mostly Cloudy", "Cloudy" -> {
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Day-Cloudy-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
			case "Showers And Thunderstorms", "Showers And Thunderstorms then Rain And Snow Likely" -> {
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Day-Thunderstorm-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
			case "Patchy Fog then Rain And Snow", "Rain And Snow", "Chance Rain and Show Showers" -> {
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Any-Snow-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
			default -> {
				//"Slight Chance Rain Showers", "Rain", "Rain And Snow", "Mostly Cloudy then Slight Chance Rain Showers", "Mostly Cloudy then Slight Chance Rain Showers", "Rain And Snow Showers Likely", "Chance Rain And Snow Showers", "Slight Chance Snow Showers then Partly Cloudy"
				imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Any-Rain-Icon.png"));
				demoImage = new Image(imageStream);
				yield new ImageView(demoImage);
			}
		};
	}

	ImageView windDirectionRotator(ImageView arrow, String direction) {
		switch (direction) {
			case "NE", "ENE", "NNE":
				arrow.setRotate(-45);
				return arrow;
			case "NW", "WNW", "NNW":
				arrow.setRotate(-135);
				return arrow;
			case "SW", "WSW", "SSW":
				arrow.setRotate(-225);
				return arrow;
			case "SE", "ESE", "SSE":
				arrow.setRotate(-315);
				return arrow;
			case  "N":
				arrow.setRotate(-90);
				return arrow;
			case  "W":
				arrow.setRotate(-180);
				return arrow;
			case  "S":
				arrow.setRotate(-270);
				return arrow;
			default:
				arrow.setRotate(0);
		}
		return arrow;
	}

	HBox scene3HBoxBuilder(int forecastIndex) {
		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
		if (forecast == null) {
			throw new RuntimeException("Forecast did not load");
		}

		InputStream imageStream;
		VBox leftVBox, rightVBox;
		HBox collectionBox, precipHBox, windDetailsHBox;
		Text dayName, temp, shortForecast, precipitation, windDetails;

		//Left Box
		dayName = new Text(forecast.get(forecastIndex).name);
		dayName.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		dayName.setFill(Color.RED);
		dayName.setWrappingWidth(150);
		temp = new Text(forecast.get(forecastIndex).temperature + " ℉");
		temp.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 50));
		temp.setFill(Color.RED);
		shortForecast = new Text(forecast.get(forecastIndex).shortForecast);
		shortForecast.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		shortForecast.setFill(Color.RED);
		shortForecast.setWrappingWidth(150);

		//Right Box
		precipitation = new Text(forecast.get(forecastIndex).probabilityOfPrecipitation.value + "%");
		precipitation.setWrappingWidth(75);

		windDetails = new Text(forecast.get(forecastIndex).windDirection + ", " + forecast.get(forecastIndex).windSpeed);
		windDetails.setWrappingWidth(75);

		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Raindrop-Precipitation-Icon.png"));
		Image raindropImg =  new Image(imageStream);
		ImageView raindropIcon = new ImageView(raindropImg);
		raindropIcon.setFitHeight(25);
		raindropIcon.setFitWidth(25);
		raindropIcon.setPreserveRatio(true);

		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Wind-Gust.png"));
		Image windImg = new Image(imageStream);
		ImageView windIcon = new ImageView(windImg);
		windIcon.setFitHeight(25);
		windIcon.setFitWidth(25);
		windIcon.setPreserveRatio(true);

		precipHBox = new HBox(10, raindropIcon, precipitation);
		precipHBox.setAlignment(Pos.CENTER);
		windDetailsHBox = new HBox(10, windIcon, windDetails);
		windDetailsHBox.setAlignment(Pos.CENTER);

		//Placing everything in boxes
		leftVBox = new VBox(20, dayName, temp, shortForecast);

		ImageView forecastIcon = forecastIconAssignment(forecast.get(forecastIndex).shortForecast);
		forecastIcon.setFitHeight(75);
		forecastIcon.setFitWidth(75);
		forecastIcon.setPreserveRatio(true);

		rightVBox = new VBox(5, forecastIcon, precipHBox, windDetailsHBox);
		rightVBox.setAlignment(Pos.CENTER);
		collectionBox = new HBox(75, leftVBox, rightVBox);
		collectionBox.setPadding(new Insets(20));
		collectionBox.setMaxWidth(50);
		if(forecast.get(forecastIndex).isDaytime) {

			collectionBox.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, new CornerRadii(50), Insets.EMPTY)));
		}
		else
		{
			collectionBox.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(50), Insets.EMPTY)));
		}
		return collectionBox;
	}

	VBox scene2VBoxBuilder(int forecastIndex) {
		InputStream imageStream;
		VBox scene2Content, detailBox1, detailBox2, detailBox3, detailBox4, infoBox;
		HBox extrasBox1, extrasBox2;
		Text infoDay, infoText, infoTempTitle, infoTemp, infoPrecipTitle, infoPrecip, infoDirectionTitle, infoWindDirection, infoSpeedTitle, infoWindSpeed;

		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
		if (forecast == null) {
			throw new RuntimeException("Forecast did not load");
		}

		//Temperature Box
		if(forecast.get(forecastIndex).temperature <= 32)
		{
			imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Low-Temp.png"));
		}
		else if (forecast.get(forecastIndex).temperature >= 70)
		{
			imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/High-Temp.png"));
		}
		else
		{
			imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Standard-Temp.png"));
		}
		Image temperatureImg = new Image(imageStream);
		ImageView temperatureIcon = new ImageView(temperatureImg);
		temperatureIcon.setFitHeight(100);
		temperatureIcon.setFitWidth(100);
		temperatureIcon.setPreserveRatio(true);

		//Precipitation Box
		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Much-Rain.png"));
		Image raindropImg = new Image(imageStream);
		ImageView precipitationIcon = new ImageView(raindropImg);
		precipitationIcon.setFitHeight(100);
		precipitationIcon.setFitWidth(100);
		precipitationIcon.setPreserveRatio(true);

		//Wind Direction Box, compass with rotated arrow
		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Compass-WindDirection-Icon.png"));
		Image compassImg = new Image(imageStream);
		ImageView compass = new ImageView(compassImg);
		compass.setFitHeight(100);
		compass.setFitWidth(100);
		compass.setPreserveRatio(true);

		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Compass-ArrowIcon.png"));
		Image arrowImg = new Image(imageStream);
		ImageView arrow = new ImageView(arrowImg);
		arrow.setFitHeight(60);
		arrow.setFitWidth(60);
		arrow.setPreserveRatio(true);
		arrow = windDirectionRotator(arrow, forecast.get(forecastIndex).windDirection);

		//Wind Speed Box
		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Low-Wind.png"));
		Image windSpeedImg = new Image(imageStream);
		ImageView windSpeedIcon = new ImageView(windSpeedImg);
		windSpeedIcon.setFitHeight(100);
		windSpeedIcon.setFitWidth(100);
		windSpeedIcon.setPreserveRatio(true);

		infoDay = new Text(forecast.get(forecastIndex).name + "'s Weather");
		infoDay.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
		infoText = new Text(forecast.get(forecastIndex).detailedForecast);
		infoText.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 13.5));
		infoText.setWrappingWidth(475);

		infoBox = new VBox(10, infoDay, infoText);
		infoBox.setAlignment(Pos.CENTER);

		infoTempTitle = new Text("Temperature");
		infoTempTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
		infoTemp = new Text(forecast.get(forecastIndex).temperature + " ℉");
		infoTemp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

		infoPrecipTitle = new Text("Precipitation");
		infoPrecipTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
		infoPrecip = new Text("Chance of Rain: " + forecast.get(forecastIndex).probabilityOfPrecipitation.value + "%");
		infoPrecip.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

		infoDirectionTitle = new Text("Wind Direction");
		infoDirectionTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
		infoWindDirection = new Text(forecast.get(forecastIndex).windDirection);
		infoWindDirection.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

		infoSpeedTitle = new Text("Wind Speed");
		infoSpeedTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
		infoWindSpeed = new Text(forecast.get(forecastIndex).windSpeed);
		infoWindSpeed.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

		StackPane compassStack = new StackPane();
		compassStack.getChildren().addAll(compass, arrow);

		detailBox1 = new VBox(25, infoTempTitle, temperatureIcon, infoTemp);
		detailBox1.setAlignment(Pos.CENTER);
		detailBox2 = new VBox(25, infoPrecipTitle, precipitationIcon, infoPrecip);
		detailBox2.setAlignment(Pos.CENTER);
		detailBox3 = new VBox(25, infoDirectionTitle, compassStack, infoWindDirection);
		detailBox3.setAlignment(Pos.CENTER);
		detailBox4 = new VBox(25, infoSpeedTitle, windSpeedIcon, infoWindSpeed);
		detailBox4.setAlignment(Pos.CENTER);

		extrasBox1 = new HBox(40, detailBox1, detailBox2);
		extrasBox1.setAlignment(Pos.CENTER);
		extrasBox1.setTranslateX(20);

		extrasBox2 = new HBox(60, detailBox3, detailBox4);
		extrasBox2.setAlignment(Pos.CENTER);

		scene2Content = new VBox(30, infoBox, extrasBox1, extrasBox2);
		scene2Content.setPadding(new Insets(10, 10, 10, 10));
		scene2Content.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(50), Insets.EMPTY)));

		return scene2Content;
	}

	HBox scene1HBoxBuilder() {
		InputStream imageStream;
		VBox leftVBox, rightVBox;
		HBox collectionBox, precipHBox, windDetailsHBox;
		Text dayName, temp, shortForecast, precipitation, windDetails;

		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
		if (forecast == null) {
			throw new RuntimeException("Forecast did not load");
		}

		//Left Box
		dayName = new Text(forecast.getFirst().name);
		dayName.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		dayName.setWrappingWidth(150);
		temp = new Text(forecast.getFirst().temperature + " ℉");
		temp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
		shortForecast = new Text(forecast.getFirst().shortForecast);
		shortForecast.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		shortForecast.setWrappingWidth(150);

		//Right Box
		precipitation = new Text(forecast.getFirst().probabilityOfPrecipitation.value + "%");
		precipitation.setWrappingWidth(75);

		windDetails = new Text(forecast.getFirst().windDirection + ", " + forecast.getFirst().windSpeed);
		windDetails.setWrappingWidth(75);

		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Raindrop-Precipitation-Icon.png"));
		Image raindropImg =  new Image(imageStream);
		ImageView raindropIcon = new ImageView(raindropImg);
		raindropIcon.setFitHeight(25);
		raindropIcon.setFitWidth(25);
		raindropIcon.setPreserveRatio(true);

		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Wind-Gust.png"));
		Image windImg =  new Image(imageStream);
		ImageView windIcon = new ImageView(windImg);
		windIcon.setFitHeight(25);
		windIcon.setFitWidth(25);
		windIcon.setPreserveRatio(true);

		ImageView forecastIcon = forecastIconAssignment(forecast.getFirst().shortForecast);
		forecastIcon.setFitHeight(100);
		forecastIcon.setFitWidth(100);
		forecastIcon.setPreserveRatio(true);

		precipHBox = new HBox(10, raindropIcon, precipitation);
		precipHBox.setAlignment(Pos.CENTER);
		windDetailsHBox = new HBox(10, windIcon, windDetails);
		windDetailsHBox.setAlignment(Pos.CENTER);

		//Placing everything in boxes
		leftVBox = new VBox(20, dayName, temp, shortForecast);
		rightVBox = new VBox(5, forecastIcon, precipHBox, windDetailsHBox);
		rightVBox.setAlignment(Pos.CENTER);
		collectionBox = new HBox(90, leftVBox, rightVBox);
		collectionBox.setPadding(new Insets(20));
		collectionBox.setMaxWidth(50);
		return collectionBox;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Welcome to the WeatherNaut app!");
		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
		if (forecast == null) {
			throw new RuntimeException("Forecast did not load");
		}

		InputStream imageStream;
		Image demoImage;

		//Code for Scene 3
		//Declarations
		VBox scene3VBox, forecastCollection;
		HBox forecastBox1, forecastBox2, forecastBox3, forecastBox4, forecastBox5, forecastBox6;
		Text scene3Title;
		Button backToToday;

		//Buttons
		backToToday = new Button("Back to Today");
		backToToday.setBackground(new Background(new BackgroundFill(Color.rgb(20,10, 12), new CornerRadii(50), Insets.EMPTY)));
		backToToday.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		backToToday.setTextFill(Color.WHITE);

		scene3Title = new Text("3-day Forecast");
		scene3Title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));

		//Placing everything in boxes
		forecastBox1 = scene3HBoxBuilder(0);
		forecastBox2 = scene3HBoxBuilder(1);
		forecastBox3 = scene3HBoxBuilder(2);
		forecastBox4 = scene3HBoxBuilder(3);
		forecastBox5 = scene3HBoxBuilder(4);
		forecastBox6 = scene3HBoxBuilder(5);

		forecastCollection = new VBox(20, forecastBox1, forecastBox2, forecastBox3, forecastBox4, forecastBox5, forecastBox6);
		forecastCollection.setAlignment(Pos.CENTER);
		ScrollPane forecastScrollable = new ScrollPane(forecastCollection);
		forecastScrollable.setFitToWidth(true);
		forecastScrollable.setFitToHeight(true);

		scene3VBox = new VBox(20, scene3Title, backToToday, forecastScrollable);
		scene3VBox.setAlignment(Pos.CENTER);

		//Background for Scene 3
		imageStream = getClass().getResourceAsStream("/Blue-Green-Background.jpg");
		Image scene3Back = new Image(imageStream);
		Background bg3 = new Background(new BackgroundImage(scene3Back, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
		scene3VBox.setBackground(bg3);

		//Code for Scene 2a, 2b, and 2c
		VBox scene2aVBox, scene2bVBox, scene2cVBox;
		Button todayButton2a, todayButton2b, todayButton2c;

		//Background for Scene 2
		imageStream = getClass().getResourceAsStream("/Blue-Green-Background.jpg");
		Image scene2Back = new Image(imageStream);
		Background bg2 = new Background(new BackgroundImage(scene2Back, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(700, 700, false, false, false, false)));

		todayButton2a = new Button("See Today's Weather");
		todayButton2a.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), Insets.EMPTY)));
		todayButton2a.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		todayButton2a.setTextFill(Color.WHITE);

		todayButton2b = new Button("See Today's Weather");
		todayButton2b.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), Insets.EMPTY)));
		todayButton2b.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		todayButton2b.setTextFill(Color.WHITE);

		todayButton2c = new Button("See Today's Weather");
		todayButton2c.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), Insets.EMPTY)));
		todayButton2c.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		todayButton2c.setTextFill(Color.WHITE);

		VBox moreInfo1 = scene2VBoxBuilder(0);
		VBox moreInfo2 = scene2VBoxBuilder(1);
		VBox moreInfo3 = scene2VBoxBuilder(2);

		scene2aVBox = new VBox(25, todayButton2a, moreInfo1);
		todayButton2a.setTranslateY(-25);
		moreInfo1.setTranslateY(-30);
		scene2aVBox.setAlignment(Pos.CENTER);
		scene2aVBox.setBackground(bg2);

		scene2bVBox = new VBox(25, todayButton2b, moreInfo2);
		todayButton2b.setTranslateY(-25);
		moreInfo2.setTranslateY(-30);
		scene2bVBox.setAlignment(Pos.CENTER);
		scene2bVBox.setBackground(bg2);

		scene2cVBox = new VBox(25, todayButton2c, moreInfo3);
		todayButton2c.setTranslateY(-25);
		moreInfo3.setTranslateY(-30);
		scene2cVBox.setAlignment(Pos.CENTER);
		scene2cVBox.setBackground(bg2);

		//All code for Scene 1
		//Today's Weather Scene
		VBox scene1VBox, todayCard, tonightCard, tomorrowCard;
		Button threeDayForecastButton, info1, info2, info3;
		HBox fullDayBox;
		Text todayPageTitle, t1, t2, t3, temp1, temp2, temp3;

		if(forecast.getFirst().isDaytime)
		{
			todayPageTitle = new Text("Today's Weather");
			t1 = new Text("Today");
			t2 = new Text("Tonight");
			t3 = new Text("Tomorrow");
		}
		else
		{
			todayPageTitle = new Text("Tonight's Weather");
			t1 = new Text("Tonight");
			t2 = new Text("Tomorrow");
			t3 = new Text("Tomorrow Night");
		}

		todayPageTitle.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));

		//Background for Scene 1
		imageStream = Objects.requireNonNull(getClass().getResourceAsStream("/Blue-Green-Background.jpg"));
		Image scene1Back =  new Image(imageStream);
		Background bg = new Background(new BackgroundImage(scene1Back, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

		threeDayForecastButton = new Button("See the 3-day Forecast");
		threeDayForecastButton.setBackground(new Background(new BackgroundFill(Color.rgb(20,10, 12), new CornerRadii(50), Insets.EMPTY)));
		threeDayForecastButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		threeDayForecastButton.setTextFill(Color.WHITE);

		info1 = new Button("More Info");
		info1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
		info1.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), Insets.EMPTY)));
		info1.setTextFill(Color.WHITE);
		info2 = new Button("More Info");
		info2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
		info2.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), Insets.EMPTY)));
		info2.setTextFill(Color.WHITE);
		info3 = new Button("More Info");
		info3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
		info3.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(50), Insets.EMPTY)));
		info3.setTextFill(Color.WHITE);

		t1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		t2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		t3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		t3.setWrappingWidth(125);
		t3.setTextAlignment(TextAlignment.CENTER);

		HBox frontPageBox = scene1HBoxBuilder();

		temp1 = new Text(forecast.get(0).temperature + " ℉");
		temp1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		temp2 = new Text(forecast.get(1).temperature + " ℉");
		temp2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		temp3 = new Text(forecast.get(2).temperature + " ℉");
		temp3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

		ImageView todayImg = forecastIconAssignment(forecast.get(0).shortForecast);
		todayImg.setFitHeight(75);
		todayImg.setFitWidth(75);
		todayImg.setPreserveRatio(true);

		ImageView tonightImg = forecastIconAssignment(forecast.get(1).shortForecast);
		tonightImg.setFitHeight(75);
		tonightImg.setFitWidth(75);
		tonightImg.setPreserveRatio(true);

		ImageView tomorrowImg = forecastIconAssignment(forecast.get(2).shortForecast);
		tomorrowImg.setFitHeight(75);
		tomorrowImg.setFitWidth(75);
		tomorrowImg.setPreserveRatio(true);

		todayCard = new VBox(50, t1, todayImg, temp1, info1);
		todayCard.setAlignment(Pos.CENTER);
		
		tonightCard = new VBox(50, t2, tonightImg, temp2, info2);
		tonightCard.setAlignment(Pos.CENTER);
		
		tomorrowCard = new VBox(50, t3, tomorrowImg, temp3, info3);
		tomorrowCard.setAlignment(Pos.CENTER);
		
		fullDayBox = new HBox(50, todayCard, tonightCard, tomorrowCard);
		fullDayBox.setAlignment(Pos.CENTER);
		fullDayBox.setTranslateX(10);

		VBox scene1InsideVBox = new VBox(30, frontPageBox, fullDayBox);
		scene1InsideVBox.setAlignment(Pos.CENTER);
		scene1VBox = new VBox(20, todayPageTitle, threeDayForecastButton, scene1InsideVBox);
		scene1VBox.setAlignment(Pos.CENTER);
		scene1VBox.setBackground(bg);

		//Code for Scene 0
		VBox scene0VBox;
		Text welcome, to, weatherNaut, slogan;
		Button seeWeather;

		welcome = new Text("Welcome");
		welcome.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
		to = new Text("to");
		to.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
		weatherNaut = new Text("WeatherNaut!");
		weatherNaut.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 30));
		slogan = new Text("Be prepared!");
		slogan.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.ITALIC, 25));
		
		//Creation and rectangular clip for logo
		imageStream = getClass().getResourceAsStream("/WeatherNaut Logo.png");
		demoImage = new Image(imageStream);
		ImageView weatherNautLogo = new ImageView(demoImage);
		weatherNautLogo.setFitHeight(200);
		weatherNautLogo.setFitWidth(200);

		Rectangle clip = new Rectangle(weatherNautLogo.getFitWidth(), weatherNautLogo.getFitHeight());
		weatherNautLogo.setClip(clip);
		clip.setArcWidth(40);
		clip.setArcHeight(40);
		weatherNautLogo.setPreserveRatio(true);

		seeWeather = new Button("Check Weather");
		seeWeather.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(50), Insets.EMPTY)));
		seeWeather.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		seeWeather.setTextFill(Color.WHITE);

		scene0VBox = new VBox(50, welcome, to, weatherNaut, slogan, weatherNautLogo, seeWeather);
		scene0VBox.setAlignment(Pos.CENTER);

		//Background for Scene 0
		imageStream = getClass().getResourceAsStream("/Blue-Green-Background.jpg");
		Image scene0BackImg = new Image(imageStream);
		Background scene0Back = new Background(new BackgroundImage(scene0BackImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(700, 700, false, false, false, false)));
		scene0VBox.setBackground(scene0Back);

		Scene scene3 = new Scene(scene3VBox, 500, 700);
		Scene scene2a = new Scene(scene2aVBox, 500, 700);
		Scene scene2b = new Scene(scene2bVBox, 500, 700);
		Scene scene2c = new Scene(scene2cVBox, 500, 700);
		Scene scene1 = new Scene(scene1VBox, 500, 700);
		Scene scene0 = new Scene(scene0VBox, 500, 700);

		//Button Functions for changing scenes
		//Button for Scene 0
		seeWeather.setOnAction(e -> {
			primaryStage.setScene(scene1);
			primaryStage.setTitle("Current Weather");
		});

		//Button for Scene 1
		threeDayForecastButton.setOnAction(e -> {
			primaryStage.setScene(scene3);
			primaryStage.setTitle("3-day Forecast");
		});
		info1.setOnAction(e -> {
			primaryStage.setScene(scene2a);
			primaryStage.setTitle("More Info on " + forecast.get(0).name + "'s Weather");
		});
		info2.setOnAction(e -> {
			primaryStage.setScene(scene2b);
			primaryStage.setTitle("More Info on " + forecast.get(1).name + "'s Weather");
		});
		info3.setOnAction(e -> {
			primaryStage.setScene(scene2c);
			primaryStage.setTitle("More Info on " + forecast.get(2).name + "'s Weather");
		});

		//Buttons for Scenes 2a-2c
		todayButton2a.setOnAction(e -> {
			primaryStage.setScene(scene1);
			primaryStage.setTitle("Current Weather");
		});
		todayButton2b.setOnAction(e -> {
			primaryStage.setScene(scene1);
			primaryStage.setTitle("Current Weather");
		});
		todayButton2c.setOnAction(e -> {
			primaryStage.setScene(scene1);
			primaryStage.setTitle("Current Weather");
		});

		//Buttons for Scene 3
		backToToday.setOnAction(e -> {
			primaryStage.setScene(scene1);
			primaryStage.setTitle("Current Weather");
		});

		primaryStage.setScene(scene0);
		primaryStage.show();
	}

}
