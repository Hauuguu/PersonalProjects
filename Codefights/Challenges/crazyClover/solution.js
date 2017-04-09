function crazyClover(leaves) {

    //Treat leaves as string and get only the last two characters in the string
    leaves=leaves.slice(leaves.length-2, leaves.length);
    var l = leaves;	//To make code shorter for challenge purposes
    
    //Treat leaves as number and get within two digits
    while (l > 100)
        l %= 100;
    
    //If last two digts are evenly divisible by 4, then the whole number is divisible by 4
    return l % 4 == 0;    

}
