var mongoose = require('mongoose');
mongoose.connect('mongodb://unilol:unilol2015@ds045064.mongolab.com:45064/unilol');

var express = require('express');
var app = express();


var phjs = require('./lib/player_handler.js');

//=========================================================================
	
// configure app to use bodyParser()
// this will let us get the data from a POST

// sets port
app.set('port', process.env.PORT || 3000);
// sets directory for views
app.set('views', __dirname + '/views');
// sets view engine
app.set('view engine',"jade");


app.use(express.static(__dirname + '/public'));
// look @ routes.js
require('./routes.js')(app);

//ROUTES

var router = express.Router(); 


// test route to make sure everything is working (accessed at GET http://localhost:3000/api)
router.get('/', function(req, res) {
    res.json({ message: 'hooray! welcome to our api!' });   
});

// more routes for our API will happen here

// REGISTER OUR ROUTES -------------------------------
// all of our routes will be prefixed with /api
app.use('/rank', router);

// START THE SERVER
// =============================================================================
app.listen(app.get('port'));
console.log('Magic happens on port ' + app.get('port'));