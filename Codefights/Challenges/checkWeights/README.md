<h1>Instructions</h1>
<p>You work for a company that makes weights that are used for balancing scales in high school science classes. The weights are shipped in boxes, and each box contains weights in a certain range. Each box is labeled with an array that represents the weights it contains. For example, a box of weights from <code>4</code> to <code>12</code> is labeled <code>[4, 12]</code>. The company never ships the same weight twice in a shipment.</p>
<p>A high school that needs a new set of weights orders a package that consists of several <code>boxes</code>. Once in a while, something goes wrong and either one weight falls off the line, or one weight is accidentally added to one of the boxes. Your job is to check each package before it goes out, and detect if this is the case.</p>
<p>Given the labels of the <code>boxes</code> about to be shipped and the actual <code>packageWeight</code>, you should return:</p>
<ul>
<li><code>0</code> if the provided actual <code>packageWeight</code> equals the package weight you would expect according to the label;</li>
<li><code>a</code> if weight <code>a</code> was accidentally added to the package;</li>
<li><code>-a</code> if weight <code>a</code> is missing from the package.</li>
</ul>
<p><strong>Example</strong></p>
<ul>
<li>
<p>For <code>boxes = [[1, 2], [3, 4]]</code> and <code>packageWeight = 10</code>, the output should be<br>
<code>checkWeights(boxes, packageWeight) = 0</code>.</p>
<p>The total expected weight of the package is <code>(1 + 2) + (3 + 4) = 10</code>, which is equal to the <code>packageWeight</code>.</p>
</li>
<li>
<p>For <code>boxes = [[1, 3], [8, 10]]</code> and <code>packageWeight = 35</code>, the output should be<br>
<code>checkWeights(boxes, packageWeight) = 2</code>.</p>
<p>The total expected weight of the package is <code>(1 + 2 + 3) + (8 + 9 + 10) = 33</code>, which means that a weight weighing <code>2</code> was accidentally added to the package.</p>
</li>
<li>
<p>For <code>boxes = [[1, 2], [3, 4]]</code> and <code>packageWeight = 6</code>, the output should be<br>
<code>checkWeights(boxes, packageWeight) = -4</code>.</p>
<p>The total expected weight of the weights is <code>(1 + 2) + (3 + 4) = 10</code>, which means that weight <code>4</code>  is missing from the package.</p>
</li>
</ul>
<p><strong>Input/Output</strong></p>
<ul>
<li><strong>[time limit] 4000ms (js)</strong></li>
</ul>
<ul>
<li>
<p><strong>[input] array.array.integer boxes</strong></p>
<p>A list of boxes, where each box is labeled as described above.</p>
<p><em>Guaranteed constraints:</em><br>
<code>1 ≤ boxes.length &lt; 10</code>,<br>
<code>boxes[i].length = 2</code>,<br>
<code>1 ≤ boxes[i][0] &lt; boxes[i][1] ≤ 1000</code>,<br>
<code>boxes[i][1] &lt; boxes[i + 1][0]</code>.</p>
</li>
<li>
<p><strong>[input] integer packageWeight</strong></p>
<p>The actual total weight of the package</p>
<p><em>Guaranteed constraints:</em><br>
<code>5 &lt; totalWeight &lt; 10<sup>6</sup></code>.</p>
</li>
<li>
<p><strong>[output] integer</strong></p>
<p>Return <code>0</code> if the weight of the set of weights in <code>boxes</code> is equal to <code>packageWeight</code>. If the weight of the set of weights in <code>boxes</code> is less than or greater than <code>packageWeight</code>, return the amount of weight that needs to be removed from or added to <code>boxes</code>.</p>
</li>
</ul>