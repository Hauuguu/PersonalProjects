var Player = require('../models/user.js');
var College = require('../models/college.js');
var riotjs = require('./riot.js');


exports.checkForExistingProfile = function(username, ign, email, cb){
	console.log("\t Checking for existing profile...");
	
	Player.find( { $or:[ {'loginUser': username}, {'ign': ign}, {'email': email} ]}, function (err, found){	
		cb(err, found); //Did not find any of these, so it is safe to make a new one
		
	});
	
};


exports.removeProfile = function(loginUser, cb){
	console.log("\t Deleting player...");

	Player.remove({ loginUser: loginUser}, function(err){
		cb(err);
		console.log('Delete success!');
	});
};


exports.updateProfile = function(loginUser, userPassword, firstNameValue, lastNameValue, emailValue, collegeValue, statusValue, ignValue, favRoleValue, favChampionValue, cb) {
    console.log("\t Updating player...");
    Player.findOne({ loginUser: loginUser, password: userPassword }, function(err, u) {
        if (err) return res.send(500, 'Error occurred: database error');
		if(u) {
			if (emailValue !== null)
				u.email = emailValue; //ADD VERIFICATION
			if(statusValue !== null)
	        	u.status = statusValue;
			if (ignValue !== null)
				u.ign = ignValue;
				
	        u.favRole = favRoleValue;
	        u.favChampion = favChampionValue;
			
			u.save(function(err) {
				if(err) console.log('error');
				else console.log('success');

				cb(err,u);
			});
		}
    });

};

exports.formatPlayer = function(loginUser, userPassword, playerName, email, cb) {
    Player.findOne({
		loginUser: loginUser
    }, function(err, found) {
        if (found) cb("Error");
        else {
			riotjs.getSummonerID(playerName, function(id) {
				riotjs.getSummonerInfo(id, function(summInfo) {
				
					var emailStr = email.split('@');
					
					College.findOne({ emailDomain: emailStr[1] }, function(err, c) { //Find college from email
						if (err) console.log('Error');
						if (c) {
							var toAdd = new Player({
					
								//IRL Info
								firstName: '(Default) Joseph',
								lastName: 'Smith',
								email: email,
								college: c.collegeName,

								//Site profile info
								loginUser: loginUser,
								password: userPassword,
								status: 'sadface',

								//In-game info
								ign: playerName,
								userID: id,
								favRole: 'Feeder',
								favChampion: 'Teemo',

								//Rank info
								rankSoloQ: {
									tier: summInfo.tier,
									division: summInfo.division,
									lp: summInfo.lp,
									totalRank: summInfo.totalRank
								},

								//Admin info
								lastUpdate: new Date()

							});

							toAdd.save(function(err) {
								console.log("test");
								cb(err);
					});
						
						}
					});

				});
			});
        }
    });


};