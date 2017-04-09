int checkWeights(int[][] boxes, int packageWeight) {

    int s = 0; //sum

    for (int i = 0; i < boxes.length; i++) //For each box
        for (int k = boxes[i][0]; k <= boxes[i][1]; k++) //Get weights in range
            s += k; //Add to sum
    
    return packageWeight - s; 
}