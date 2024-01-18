package Packing;

import Phase3.JFX3D.Renderable;
import Phase3.JFX3D.Updatable;

import java.util.Arrays;
import java.util.Comparator;

public class GeneticSearch implements Renderable {
    int populationSize = 5000;
    int maxGenerations = 50;
    int maxNumberOfEachBlock;
    Gene[] population;

    Grid geneticSearch(UnitDatabase database) {
        //If the data is pentominoes, the max number of each block is 264 and otherwise, it is 80
        if (database.getBlockArrayList().size() > 10)
            this.maxNumberOfEachBlock = 264;
        else
            this.maxNumberOfEachBlock = 80;

        population = new Gene[populationSize];

        for (int i = 0; i < populationSize; i++)
            population[i] = new Gene(maxNumberOfEachBlock, database);

        sortPopulation();

        for (int i = 0; i < maxGenerations; i++) {
            Gene[] newPopulation = new Gene[populationSize];

            //Pick the top 10% of the population
            System.arraycopy(population, 0, newPopulation, 0, populationSize / 100);

            double[] fitnessArr = new double[populationSize];
            double sum = 0;
            for (int j = 0; j < populationSize; j++) {
                fitnessArr[j] = population[j].fitness();
                sum += fitnessArr[j];
            }

            //Normalize fitness
            for (int j = 0; j < populationSize; j++) {
                fitnessArr[j] /= sum;
            }

            for (int j = populationSize / 100; j < populationSize; j++) {
                //Parent selection based on their fitness
                double random1 = Math.random();
                double random2 = Math.random();

                int parentIndex1 = populationSize - 1, parentIndex2 = populationSize - 1;

                for (int k = 0; k < populationSize; k++) {
                    if (random1 < fitnessArr[k]) {
                        parentIndex1 = k;
                        break;
                    }
                    random1 -= fitnessArr[k];
                }

                for (int k = 0; k < populationSize; k++) {
                    if (random2 < fitnessArr[k]) {
                        parentIndex2 = k;
                        break;
                    }
                    random2 -= fitnessArr[k];
                }

                Gene parent1 = population[parentIndex1];
                Gene parent2 = population[parentIndex2];

                //Create a child using the crossover method for the parents
                Gene child = parent1.crossover(parent2);

                //Add element to the new population
                newPopulation[j] = child;
            }
            population = newPopulation;
            sortPopulation();

            System.out.println(getBestGene().fitness());
        }
        return getBestGene().grid;
    }

    void sortPopulation() {
        //sort population based on their fitness
        Arrays.sort(population, Comparator.comparingInt(Gene::fitness).reversed());
    }

    Gene getBestGene() {
        return population[0];
    }

    @Override
    public int[][][] getData(UnitDatabase database) {
        return geneticSearch(database).grid;
    }

    @Override
    public int[][][] getData(UnitDatabase database, Updatable updatable) {
        return geneticSearch(database).grid;
    }

    @Override
    public int getScore() {
        return getBestGene().fitness();
    }
}
