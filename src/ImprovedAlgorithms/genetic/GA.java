package ImprovedAlgorithms.genetic;
import ImprovedAlgorithms.Search;
import ImprovedAlgorithms.data.ParcelDatabase;
import ImprovedAlgorithms.data.PentominoesDatabase;
import Packing.Grid;
import Packing.UnitDatabase;
import Phase3.JFX3D.AlgoRequest;
import Phase3.JFX3D.AlgoResponse;
import Phase3.JFX3D.Renderable;

import java.util.Arrays;
import java.util.Random;

public class GA implements Renderable {
    static boolean isParcel;
    static int[] values;

    ParcelOrderIndividual[] population;
    int populationSize = 200;
    int convCounter = 0;
    Random rn = new Random();

    public static int[] numberOfEachParcel;
    static int totalNumberOfParcels;
    static int[] orderedParcelsArray;
    static boolean unlimited;
    
    

    /** 
     *
     * Total number of parcels
     * @return total number
     */

    public static int totalNumberOfParcels() { 

        int total = 0;

        for (int i = 0; i < numberOfEachParcel.length; i++) {
            total += numberOfEachParcel[i];
        }

        return total;
    }


    /** 
     * Generate ordered parcels array
     *
     * @return ordered parcels array
     */

    public static int[] generateOrderedParcelsArray() { 

        int[] orderedParcelsArray = new int[totalNumberOfParcels];
        int counter = 0;
        
        for (int i = 0; i < numberOfEachParcel.length; i++) {
            for (int j = 0; j < numberOfEachParcel[i]; j++) {
                orderedParcelsArray[counter] = i;
                counter++;
            }
        }

        return orderedParcelsArray;
    }


    /** 
     *
     * Initialize population
     *
     */
    public void initializePopulation() { 

        for (int i = 0; i < populationSize; i++) {
            population[i] = new ParcelOrderIndividual();
        }
    }


    /** 
     *
     * Generate next generation
     *
     */

    public void nextGeneration() { 


            ParcelOrderIndividual[] newGeneration = new ParcelOrderIndividual[populationSize];

            //add the top 10% of the old generation into the new one
            for (int i = 0; i < population.length * 0.1; i++) {
                newGeneration[i] = population[i];
            }

            double tenPercentile = population.length * 0.1;

            for (int i = (int) tenPercentile; i < population.length; i++) {
                //select 2 ParcelOrderIndividuals from the top 50% most fittest and make them mate
                ParcelOrderIndividual parent1 = population[rn.nextInt(population.length / 2)];
                ParcelOrderIndividual parent2 = population[rn.nextInt(population.length / 2)];

                ParcelOrderIndividual child = parent1.mate(parent2);
                newGeneration[i] = child;
            }

            population = newGeneration;
    }


    /** 
     *
     * Run the GA
     *
     */
    public void runGA() { 


        population = new ParcelOrderIndividual[populationSize];

        //populate the population with random ParcelOrderIndividuals
        initializePopulation();

        boolean converged = false;
        int counter = 0;

        while (!converged) {

            HeapSort.sort(population);

            nextGeneration();

            if (counter % 10 == 0 ) {
                System.out.println("Current best score: " + population[0].fitness);
            }

            counter++;
            
            if (popConverged()) {
                converged = true;
            }
        }
    }


    /** 
     *
     * check if population has converged
     *
     * @return true if yes, false if not
     */

    private boolean popConverged() { 

        double prevValue = 0;

        if(prevValue == population[0].getFitness() || prevValue == 0) convCounter++;
        else convCounter = 0;

        prevValue = population[0].getFitness();

        if(convCounter == 100) return true;
        return false;
    }

    public void run() { // ran when thread is started
        if(isParcel) numberOfEachParcel = new int[] {82,62,16};
        else numberOfEachParcel = new int[] {100,100,100};
        totalNumberOfParcels = totalNumberOfParcels();
        orderedParcelsArray = generateOrderedParcelsArray();
        runGA();
    }

    @Override
    public AlgoResponse getData(AlgoRequest algoRequest) {
        values = algoRequest.values;
        UnitDatabase database = algoRequest.database;
        isParcel = true;
        if (database.getBlockArrayList().size() > 10)
            isParcel = false;
        run();
        Grid result = new Grid(33, 5, 8);
        if(isParcel) {
            result.grid = Search.generateFieldFromChromosomes(population[0].getChromosomes(), ParcelDatabase.getDatabase(), values);
            result.score = (int) population[0].getFitness();
        }
        else {
            result.grid = Search.generateFieldFromChromosomes(population[0].getChromosomes(), PentominoesDatabase.getDatabase(), values);
            if(values[0] == -1)
                values = new int[]{5, 5, 5};
            int[] count = new int[3];
            System.out.println(result.grid.length * result.grid[0].length * result.grid[0][0].length);
            for (int i = 0; i < result.grid.length; i++) {
                for (int j = 0; j < result.grid[i].length; j++) {
                    for (int k = 0; k < result.grid[i][j].length; k++) {
                        if(result.grid[i][j][k] > 0) {
                            count[result.grid[i][j][k] - 1]++;
                        }
                    }
                }
            }
            for (int i = 0; i < count.length; i++)
                count[i] /= 5;
            result.score = 0;
            for (int i = 0; i < count.length; i++)
                result.score += count[i] * values[i];
        }
        return new AlgoResponse(result.grid, result.score);
    }

    static class ParcelOrderIndividual implements Individual {

        private int[][] chromosomes;
        private Random rand = new Random();
        private final double reverseMutationProb = 0.2;
        private final double rotationMutationProb = 0.1;

        private double fitness;
        boolean[][][][][] database = ParcelDatabase.getDatabase();
        int[][][] field;



        /** 
         *
         * Sets the database to parcel/pentominoes
         *
         */

        private void setDatabase() {

            if(isParcel) {
                database = ParcelDatabase.getDatabase();
            }else {
                database = PentominoesDatabase.getDatabase();
            }
        }
        
        public ParcelOrderIndividual() { 

            setDatabase();
            this.chromosomes = generateChromosomes();
            this.field = Search.generateFieldFromChromosomes(chromosomes, database, values);

            this.fitness = Search.getValue();
        }

        public ParcelOrderIndividual(int[][] chromosomes) { 

            setDatabase();
            this.chromosomes = chromosomes;
            this.field = Search.generateFieldFromChromosomes(chromosomes, database, values);

            this.fitness = Search.getValue();
        }

        /** 
         *
         * Randomize array
         * @param array  the array to randomize
         * @return randomized array
         */

        private int[] randomizeArray(int[] array) { 

            int[] newArray = new int[array.length];

            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }

            for (int i = 0; i < newArray.length; i++) {
                int randomIndexToSwap = rand.nextInt(newArray.length);
                int temp = newArray[randomIndexToSwap];
                newArray[randomIndexToSwap] = newArray[i];
                newArray[i] = temp;
            }

            return newArray;
        }


        /** 
         *
         * Generate chromosomes
         * @return chromosomes
         */
        
        public int[][] generateChromosomes() { 

            int[] orderChromosome = randomizeArray(orderedParcelsArray);
            int[] rotationsChromosome = new int[orderChromosome.length];

            for (int i = 0; i < orderChromosome.length; i++) {
                int parcelId = orderChromosome[i];
                int numberOfRotations = database[parcelId].length;
                int randomRotation = rand.nextInt(numberOfRotations);
                rotationsChromosome[i] = randomRotation;
            }

            return new int[][] {
                orderChromosome,
                rotationsChromosome
            };
        }




        /** 
         *
         * Accessor for fitness
         * @return fitness
         */

        public double getFitness() { 

            return fitness;
        }

        /** 
         *
         * Accessor for chromosomes
         * @return chromosomes
         */

        public int[][] getChromosomes() { 

            return chromosomes;
        }


        /** 
         *
         * Geno to feno string
         *
         * @return fenotype
         */

        public String genoToFenoString() { 

            return Arrays.toString(chromosomes[0]) + "\n" + Arrays.toString(chromosomes[1]);
        }


        /** 
         *
         * MLet two individual mate
         *
         * @param partner mating partner
         * @return new individual
         */
        
        public ParcelOrderIndividual mate(ParcelOrderIndividual partner) { 


            int[][] childChromosomes = new int[2][chromosomes[0].length];
            int[][] partnerChromosomes = partner.getChromosomes();

            //Select 2 random crossover points
            int crossOverPoint1 = rand.nextInt(chromosomes[0].length);
            int crossOverPoint2 = crossOverPoint1 + rand.nextInt(chromosomes[0].length - crossOverPoint1);

            

            int spaceBetweenPoints = crossOverPoint2 - crossOverPoint1;
            int allelesLeftToPopulate = chromosomes[0].length - spaceBetweenPoints;

            int[] usedParcels = new int[] {numberOfEachParcel[0],numberOfEachParcel[1],numberOfEachParcel[2]};

            for (int i = crossOverPoint1; i < crossOverPoint2; i++) {
                usedParcels[chromosomes[0][i]]--;
                childChromosomes[0][i] = chromosomes[0][i];
                childChromosomes[1][i] = chromosomes[1][i];
            }

            if(unlimited) {
                //faster
                for (int i = crossOverPoint1; i < crossOverPoint1 + allelesLeftToPopulate; i++) {
                    int indexOfPartner = i % partnerChromosomes[0].length;
                    int indexOfChild = (i + spaceBetweenPoints) % partnerChromosomes[0].length;
                    childChromosomes[0][indexOfChild] = partnerChromosomes[0][indexOfPartner];
                    childChromosomes[1][indexOfChild] = partnerChromosomes[1][indexOfPartner];
                }
            } else {
                int[][] allelesLeft = new int[2][allelesLeftToPopulate];
                int counter = 0;
                for (int i = crossOverPoint1; i < crossOverPoint1 + partnerChromosomes[0].length; i++) {
                    if(counter == allelesLeft[0].length) {
                        break;
                    }
    
                    int index = i % partnerChromosomes[0].length;
                    int allele = partnerChromosomes[0][index];
                    if(usedParcels[allele] > 0) {
                        usedParcels[allele]--;
                        allelesLeft[0][counter] = allele;
                        allelesLeft[1][counter] = partnerChromosomes[1][index];
                        counter++;
                        
                    }
                }
    
                for(int i = 0; i < allelesLeft[0].length; i++) {
                    int indexOfChild = (i + crossOverPoint2) % partnerChromosomes[0].length;
                    childChromosomes[0][indexOfChild] = allelesLeft[0][i];
                    childChromosomes[1][indexOfChild] = allelesLeft[1][i];
                }
            }

            if (rand.nextDouble() < reverseMutationProb) {
                int point1 = rand.nextInt(chromosomes[0].length);
                int point2 = point1 + rand.nextInt(chromosomes[0].length - point1);

                int length = point2 - point1;
                int temp1;
                int temp2;
                for (int i = 0; i < length / 2; i++) {
                    temp1 = childChromosomes[0][i + point1];
                    temp2 = childChromosomes[1][i + point1];
                    childChromosomes[0][i + point1] = childChromosomes[0][point2 - i - 1];
                    childChromosomes[1][i + point1] = childChromosomes[1][point2 - i - 1];
                    childChromosomes[0][point2 - i - 1] = temp1;
                    childChromosomes[1][point2 - i - 1] = temp2;
                }
            }

            for (int i = 0; i < childChromosomes[1].length; i++) {
                if (rand.nextDouble() < rotationMutationProb) {
                    int parcelId = childChromosomes[0][i];
                    int numberOfRotations = database[parcelId].length;
                    int randomRotation = rand.nextInt(numberOfRotations);
                    childChromosomes[1][i] = randomRotation;
                }
            }

            return new ParcelOrderIndividual(childChromosomes);
        }
    }

    public static void main(String[] args) { 

        ParcelOrderIndividual ind1 = new ParcelOrderIndividual();
        ParcelOrderIndividual ind2= new ParcelOrderIndividual();
        System.out.println(ind1.genoToFenoString());
        System.out.println();
        System.out.println(ind2.genoToFenoString());
        ParcelOrderIndividual ind3 = ind1.mate(ind2);
        System.out.println();
        System.out.println(ind3.genoToFenoString());
    
    }
}
