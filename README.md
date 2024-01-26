# Cargo Packing



## A fully functional application that helps putting blocks of different sizes and dimensions in a container

This project helps with finding different ways to pack cargo in a container. It has two options:
1) Maximizing profits
2) Maximizing space utilization

The application gives the user the option to pick the approach and by default, the values for the profits of the blocks are 3, 4, and 5 for types A, B, and C respectively.

The application layer for this project was built using the JavaFX library and the algorithms were written in Java.

<img width="936" alt="Screenshot 2024-01-26 at 12 01 01 in the afternoon" src="https://github.com/Amir-Mohseni/CargoPacking/assets/51225853/29b1cc6f-83fa-41fe-a64b-a5ebc4e18784">


## Running the program
We used JavaFX 18 and Java 21 for this project.
For running this program you need to install JavaFX and add it to your project. You can find the instructions [here](https://openjfx.io/openjfx-docs/) and 
in order to download the Java 21 you can go to [this](https://www.oracle.com/java/technologies/javase-downloads.html) link.

After installing these libraries you can run the program by running the `JFX3D.java` file in `src/Phase3.JFX3D` directory or by running these two commands in the terminal:

```javac ./src/Phase3/JFX3D.java```

```java ./src/Phase3/JFX3D.java```


<img width="1335" alt="Screenshot 2024-01-26 at 11 58 38 in the morning" src="https://github.com/Amir-Mohseni/CargoPacking/assets/51225853/a8b0c69c-abab-4527-b398-05beaae6efe3">

## How to use the program
After running the program, you are able to pick the algorithm that you want to use and the types of blocks you have.

You can also change the values of the items by changing the numbers in the values section. For maximizing space utilization, the values of items should be `(-1, -1, -1)`.

<img width="1335" alt="Screenshot 2024-01-26 at 12 02 55 in the afternoon" src="https://github.com/Amir-Mohseni/CargoPacking/assets/51225853/6a6f79a0-7ddc-48f0-ade3-6fd7145238d8">


## Starting the program

After clicking on the `Start` button, the program will show you the result of the algorithm that you chose. You can reset the program by clicking on the `Reset` button.

You can use the sliders at the bottom of the app to render only parts of the container. 

Furthermore, you can select the container and rotate it by using the mouse.

## Results and reports
You can find the results and the full report about the algorithms in the `results` directory. The results and the report are in the form of `.pdf` and `.xlsx` files.
