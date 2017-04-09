var riotjs = require('./lib/riot.js');
var UserJS = require('./models/user.js');
var nodemailer = require("nodemailer");
var phjs = require('./lib/player_handler.js');

module.exports = function(app) {

	app.use(require('cors')());

    // home page
    app.get('/', function(req, res) {
        res.sendFile('./index.html');
    });
   

    //====================== EMAIL AUTHENTICATION ======================

    var rand, mailOptions, host, link, username, password, ign, email;
	
	/*
	Here we are configuring our SMTP Server details.
	STMP is mail server which is responsible for sending and recieving email.
	*/
	var smtpTransport = nodemailer.createTransport("SMTP", {
		service: "Gmail",
		auth: { //COLLAPSED FOR SECURITY
			user: "hackunilol",
			pass: "unilol2015"
		}
	});

    /*------------------SMTP Over-----------------------------*/

    /*------------------Routing Started ------------------------*/

    app.get('/send/:email/:username/:password/:ign', function(req, res) {
		username = req.params.username;
		ign = req.params.ign;
		email = req.params.email;
		password = req.params.password;
		
		phjs.checkForExistingProfile(username, ign, email, function(err, found){
			if(err) console.log(err);
			if (found.length > 0) 
				res.end("conflict");	//not error means that profile (user || ign || email) already exists
			else{
				rand = Math.floor((Math.random() * 100) + 54);
				host = req.get('host');
				link = "http://" + req.get('host') + "/verify?id=" + rand;
				mailOptions = {
					to: email,
					subject: "Please confirm your Email account",
					html: "Hello,<br> Please Click on the link to verify your email.<br><a href=" + link + ">Click here to verify</a>"
				}
				console.log(mailOptions);
				smtpTransport.sendMail(mailOptions, function(error, response) {
					if (error) {
						console.log(error);
						res.end("error");
					} else {
						console.log("Message sent: " + response.message);
						res.end("sent");
					}
				});
				
			}
		});
    });

    app.get('/verify', function(req, res) {
        console.log(req.protocol + ":/" + req.get('host'));
        if ((req.protocol + "://" + req.get('host')) == ("http://" + host)) {
            console.log("Domain is matched. Information is from Authentic email");
            if (req.query.id == rand) {
                console.log("email is verified");
                res.end("<h1>Email " + mailOptions.to + " has been Successfully verified");
				
				//Create the user profile
				phjs.formatPlayer(username, password, ign, email, function(err) {
					console.log(err);
				});
				
            } else {
                console.log("email is not verified");
                res.end("<h1>Bad Request</h1>");
            }
        } else {
            res.end("<h1>Request is from unknown source");
        }
    });


    //====================== END OF EMAIL AUTHENTICATION ======================	

	//====================== MASTERY PAGE AUTHENTICATION ======================

    var correctMPName, host, link, ign, 
		isCorrect = false;

    /*------------------Routing Started ------------------------*/

    app.get('/masteryVerify', function(req, res) {
		correctMPName = 'unilol';
		ign = req.params.ign;
		summonerID = riotjs.getSummonerID(ign);
	
		var opts = {
			hostname: 'na.api.pvp.net',
			method: 'GET',
			path: '/api/lol/na/v1.4/summoner/' + summonerID + '/masteries',
			headers: {
				'X-Riot-Token': apiToken
			}
		}
		
		//This is JSON type (supposedly)
		var path = 'na.api.pvp.net/api/lol/na/v1.4/summoner/' + summonerID + '/masteries' + '?api_key=7d6620b3-bb57-48fc-8c86-25e607cb9e72';
		
		//Search for mastery page with correct name
		for (var i = 0; i < path.summonerID.pages.length; i++){
			if (summonerID.pages[i].name == correctMPName){		//WARNING: TWO EQUALS
				isCorrect = true;
				console.log("Mastery page name matched. League account is from owner.");
			}
		}

		if (isCorrect) {
			console.log("Ownership is verified");
			res.end("<h1>Ownership via mastery page has been successfully verified");
		} else {
			console.log("email is not verified");
			res.end("<h1>Bad Request</h1>");
		}
    });

    //====================== END OF MASTERY PAGE AUTHENTICATION ======================	
	

    //get, deletes, creates a new user, and edits a user

	app.get('/api/user/:college', function(req, res) {
        UserJS.find({ college: req.params.college}).sort({ 'rankSoloQ.totalRank': 'desc'}).exec(function(err, c) {
            if (err) return res.send(500, 'Error occurred: database error');
            res.json(c);
        });
    });
	
    app.get('/api/user/:username', function(req, res) {
        UserJS.findOne({ loginUser: req.params.username}, function(err, u) {
            if (err) return res.send(500, 'Error occurred: database error');
            res.json(u);
        });
    });
	
	app.delete('/api/user/:username', function(req, res) {
	
		phjs.removeProfile(req.params.username, function(err){
			if (err) return res.send(500, 'Error occurred: database error');
			else res.send('Delete success!');
		});
	});
	
	app.post('/api/user/:username/:ign', function(req, res) {
		phjs.formatPlayer(req.params.username, req.params.ign, function(err){
			if (err) res.send('Error');
			else res.send('Post success!');
		});
	});

    app.put('/api/user/:username/:password/:favRole/:favChampion/:status', function(req, res) {
		UserJS.findOne({ loginUser: req.params.username, password: req.params.password }, function(err, u) {
			if (err) return res.send(500, 'Error occurred: database error');
			if(!u) res.send('error');
						
			phjs.updateProfile(req.params.username, req.params.password, null, null, null, null, req.params.status, null, req.params.favRole, req.params.favChampion, function(err, found) {
				if(err) return res.send(500, 'Error occurred: database error');
				if(!u) res.send('error');
				if(u) res.send('success');
			});
		});
    });
	
	
	
	
	
	
	
	
	 // 404 page
    app.use(function(req, res) {
        res.type('text/plain');
        res.status(404);
        res.send('404 - Not Found');
    });

    // 500 page
    app.use(function(err, req, res, next) {
        console.error(err.stack);
        res.type('text/plain');
        res.status(500);
        res.send('500 - Server Error');
    });



};