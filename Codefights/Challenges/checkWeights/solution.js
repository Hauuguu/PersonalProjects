checkWeights=(boxes,packageWeight)=>{

    var s = 0; //sum
    for (var b of boxes){ //For each box
        for (var i = b[0]; i <= b[1]; i++){ //Get numbers in range
            s += i; //Add to sum
        }
    }
    return packageWeight-s;
}