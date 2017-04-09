function toBeContinued(f) {
    
    /* Reverse engineered the pattern as modified long division: 
     * Must build array of quotients where the next division is divisor / remainder
     * Until the remainder is 0
     */
    
    //a = dividend, b = divisor, r = result array to output
    var a = f[0], b = f[1], r = [];
    
    if (f[1] == 0) // If division by zero, return empty
        return r;
    
    else if (a % b == 0){ // If both f numbers are the same, return single element
        r.push(Math.floor(a / b)); //quotient
        return r;
    }
        
    else {
         while (a % b > 0){
        
            r.push(Math.floor(a / b)); //quotient

            var t = b; //save divisor into temp
            b = a % b; //new divisor = remainder
            a = t; //new dividend = old divisor
        }
        
        //Push last quotient
        r.push(Math.floor(a / b)); //quotient
        return r;
    }
}