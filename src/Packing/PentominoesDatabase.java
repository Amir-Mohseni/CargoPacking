package Packing;

import java.util.ArrayList;
import java.util.List;

public class PentominoesDatabase {
    List<Integer[][][]> LRotations = new ArrayList<>();
    List<Integer[][][]> PRotations = new ArrayList<>();
    List<Integer[][][]> TRotations = new ArrayList<>();

    PentominoesDatabase(){

    }

    private void populateL(){
        // viewed from -x
        LRotations.add(new Integer[][][]{
                {
                        {1},
                        {1},
                        {1},
                        {1}
                },
                {
                        {0},
                        {0},
                        {0},
                        {1}
                }
        });

        // viewed from x
        LRotations.add(new Integer[][][]{
                {
                        {0},
                        {0},
                        {0},
                        {1}
                },
                {
                        {1},
                        {1},
                        {1},
                        {1}
                }
        });

        // viewed from y
        LRotations.add(new Integer[][][]{
                {
                        {1, 0},
                        {1, 0},
                        {1, 0},
                        {1, 1},

                }
        });

        // viewed from -y
        LRotations.add(new Integer[][][]{
                {
                        {0, 1},
                        {0, 1},
                        {0, 1},
                        {1, 1},

                }
        });

        // viewed from z
        LRotations.add(new Integer[][][]{
                {
                        {0, 1}
                },
                {
                        {0, 1}
                },
                {
                        {0, 1}
                },
                {
                        {1, 1}
                }
        });

        // viewed from -z
        LRotations.add(new Integer[][][]{
                {
                        {1, 1}
                },
                {
                        {0, 1}
                },
                {
                        {0, 1}
                },
                {
                        {0, 1}
                }
        });
    }

    private void populateR(){
        // viewed from -x
        PRotations.add(new Integer[][][]{
                {
                        {0},
                        {1},
                        {1}
                },
                {
                        {1},
                        {1},
                        {1}
                }
        });

        // viewed from x
        PRotations.add(new Integer[][][]{
                {
                        {1},
                        {1},
                        {1}
                },
                {
                        {0},
                        {1},
                        {1}
                }
        });

        // viewed from -y
        PRotations.add(new Integer[][][]{
                {
                        {0, 1},
                        {1, 1},
                        {1, 1}
                }
        });

        // viewed from y
        PRotations.add(new Integer[][][]{
                {
                        {1, 0},
                        {1, 1},
                        {1, 1}
                }
        });

        // viewed from z
        PRotations.add(new Integer[][][]{
                {
                        {0,1}
                },
                {
                        {1, 1}
                },
                {
                        {1, 1}
                }
        });

        // viewed from -z
        PRotations.add(new Integer[][][]{
                {
                        {1,1}
                },
                {
                        {1, 1}
                },
                {
                        {0, 1}
                }
        });
    }

    private void populateT(){
        // viewed from x
        TRotations.add(new Integer[][][]{
                {
                        {1},
                        {0},
                        {0}
                },
                {
                        {1},
                        {1},
                        {1}
                },
                {
                        {1},
                        {0},
                        {0}
                }
        });

        // viewed from x
        // rot 90
        TRotations.add(new Integer[][][]{
                {
                        {1, 0, 0}
                },
                {
                        {1, 1, 1}
                },
                {
                        {1, 0, 0}
                }
        });

        // viewed from -x
        TRotations.add(new Integer[][][]{
                {
                        {1},
                        {0},
                        {0}
                },
                {
                        {1},
                        {1},
                        {1}
                },
                {
                        {1},
                        {0},
                        {0}
                }
        });

        // viewed from -x
        // rot 90
        TRotations.add(new Integer[][][]{
                {
                        {1, 0, 0}
                },
                {
                        {1, 1, 1}
                },
                {
                        {1, 0, 0}
                }
        });

        // viewed from y
        TRotations.add(new Integer[][][]{
                {
                        {1, 1, 1},
                        {0, 1, 0},
                        {0, 1, 0}
                }
        });

        // viewed from -y
        TRotations.add(new Integer[][][]{
                {
                        {1, 1, 1},
                        {0, 1, 0},
                        {0, 1, 0}
                }
        });

        // viewed from z
        TRotations.add(new Integer[][][]{
                {
                        {1, 1, 1}
                },
                {
                        {0, 1, 0}
                },
                {
                        {0, 1, 0}
                }
        });

        // viewed from z
        // rot 90
        TRotations.add(new Integer[][][]{
                {
                        {1},
                        {1},
                        {1}
                },
                {
                        {0},
                        {1},
                        {0}
                },
                {
                        {0},
                        {1},
                        {0}
                }
        });

        // viewed from -z
        TRotations.add(new Integer[][][]{
                {
                        {0, 1, 0}
                },
                {
                        {0, 1, 0}
                },
                {
                        {1, 1, 1}
                }
        });

        // viewed from z
        // rot 90
        TRotations.add(new Integer[][][]{
                {
                        {0},
                        {1},
                        {0}
                },
                {
                        {0},
                        {1},
                        {0}
                },
                {
                        {1},
                        {1},
                        {1}
                }
        });
    }
}
