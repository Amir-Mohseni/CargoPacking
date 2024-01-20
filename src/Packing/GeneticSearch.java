package Packing;

import Phase3.JFX3D.AlgoRequest;
import Phase3.JFX3D.AlgoResponse;
import Phase3.JFX3D.Renderable;
import Phase3.JFX3D.Updatable;

import java.util.Arrays;
import java.util.Comparator;

public class GeneticSearch implements Renderable {
    int populationSize = 1000;
    int maxGenerations = 100;
    int maxNumberOfEachBlock;
    Gene[] population;

    Grid geneticSearch(UnitDatabase database, Updatable updatable) {
        this.maxNumberOfEachBlock = 80;
        population = new Gene[populationSize];

        for (int i = 0; i < populationSize; i++)
            population[i] = new Gene(maxNumberOfEachBlock, database);

        sortPopulation();

        for (int i = 0; i < maxGenerations; i++) {
            Gene[] newPopulation = new Gene[populationSize];

            //Pick the top 10% of the population
            System.arraycopy(population, 0, newPopulation, 0, populationSize / 100);

            for (int j = populationSize / 100; j < populationSize; j++) {
                //Parent selection
                int parentIndex1 = Gene.randomInt(populationSize);
                int parentIndex2 = Gene.randomInt(populationSize);

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
//            updatable.update(getBestGene().grid.grid);
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
    public AlgoResponse getData(AlgoRequest algoRequest) {
        Grid result = geneticSearch(algoRequest.database, algoRequest.updatable);
        return new AlgoResponse(result.grid, result.score);
    }
}
