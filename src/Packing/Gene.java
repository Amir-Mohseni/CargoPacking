package Packing;


public class Gene {
    int[] chromosome;
    UnitDatabase database;
    int score = -1;
    Grid grid = new Grid(33, 5, 8);

    Gene(int maxSize, UnitDatabase database) {
        this.database = database;
        chromosome = new int[maxSize];
        for (int i = 0; i < maxSize; i++) {
            int blockType = randomInt(database.getBlockArrayList().size());
            chromosome[i] = blockType;
        }
    }

    static int randomInt(int upperBound) {
        return (int) (Math.random() * upperBound);
    }

    Gene crossover(Gene other) {
        int crossoverPoint = chromosome.length / 2;
        Gene child = new Gene(chromosome.length, database);
        for (int i = 0; i < chromosome.length; i++) {
            if (i < crossoverPoint) {
                child.chromosome[i] = chromosome[i];
            } else {
                child.chromosome[i] = other.chromosome[i];
            }
            //Mutation
            if (Math.random() < 0.001) {
                child.chromosome[i] = randomInt(database.getBlockArrayList().size());
            }
        }
        return child;
    }

    int fitness() {
        if(score != -1)
            return score;
        for (int blockIndex : chromosome) {
            while(!grid.emptyCells.isEmpty()) {
                Cell emptyCell = grid.emptyCells.getFirst();
                grid.emptyCells.remove(emptyCell);
                //Empty Cell axis
                int x = emptyCell.x;
                int y = emptyCell.y;
                int z = emptyCell.z;

                if (grid.validPlacement(x, y, z, database.getBlockArrayList().get(blockIndex))) {
                    grid.placeBlock(x, y, z, database.getBlockArrayList().get(blockIndex));
                    break;
                }
            }
        }
        score = grid.score;
        return score;
    }
}
